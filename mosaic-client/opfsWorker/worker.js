// OPFS Dedicated Worker
// Uses FileSystemSyncAccessHandle for direct byte-level I/O, chunked, so neither the
// worker nor the main thread ever needs the whole file in memory at once.
// Receives and returns Int8Array (matches ByteArray.toInt8Array()/Int8Array.toByteArray()
// on the Kotlin/Wasm side) — sem Base64, sem encoding overhead.

let nextHandleId = 0;
const openWrites = new Map(); // handleId -> { syncHandle, offset }
const openReads = new Map(); // handleId -> { syncHandle, offset, size }

// Resolves a "/"-separated relative path (e.g. "models/abc.glb") into its file handle,
// creating intermediate directories along the way when `create` is true.
async function resolveFileHandle(fileName, create) {
    const segments = fileName.split('/').filter((segment) => segment.length > 0);
    let dir = await navigator.storage.getDirectory();
    for (let i = 0; i < segments.length - 1; i++) {
        dir = await dir.getDirectoryHandle(segments[i], { create });
    }
    return dir.getFileHandle(segments[segments.length - 1], { create });
}

async function removeEntry(fileName) {
    const segments = fileName.split('/').filter((segment) => segment.length > 0);
    let dir = await navigator.storage.getDirectory();
    for (let i = 0; i < segments.length - 1; i++) {
        dir = await dir.getDirectoryHandle(segments[i]);
    }
    await dir.removeEntry(segments[segments.length - 1]);
}

// Keeps the error's name (e.g. "QuotaExceededError") — the Kotlin side classifies "disk full" by it,
// and `error.message` alone doesn't carry it.
function describeError(error) {
    return `${error.name}: ${error.message}`;
}

async function writeOpenRequest(id, data) {
    try {
        const fileHandle = await resolveFileHandle(data.fileName, true);
        const syncHandle = await fileHandle.createSyncAccessHandle();
        syncHandle.truncate(0);

        const handleId = nextHandleId++;
        openWrites.set(handleId, { syncHandle, offset: 0 });
        postMessage({ id, data: { handleId } });
    } catch (error) {
        postMessage({ id, error: describeError(error) });
    }
}

function writeChunkRequest(id, data) {
    const entry = openWrites.get(data.handleId);
    if (!entry) {
        postMessage({ id, error: `Unknown write handle: ${data.handleId}` });
        return;
    }
    try {
        const bytes = new Int8Array(data.buffer);
        entry.syncHandle.write(bytes, { at: entry.offset });
        entry.offset += bytes.length;
        postMessage({ id, data: {} });
    } catch (error) {
        postMessage({ id, error: describeError(error) });
    }
}

function writeCloseRequest(id, data) {
    const entry = openWrites.get(data.handleId);
    if (!entry) {
        postMessage({ id, data: {} });
        return;
    }
    try {
        entry.syncHandle.flush();
    } finally {
        entry.syncHandle.close();
        openWrites.delete(data.handleId);
    }
    postMessage({ id, data: {} });
}

async function readOpenRequest(id, data) {
    try {
        const fileHandle = await resolveFileHandle(data.fileName, false);
        const syncHandle = await fileHandle.createSyncAccessHandle();
        const size = syncHandle.getSize();

        const handleId = nextHandleId++;
        openReads.set(handleId, { syncHandle, offset: 0, size });
        postMessage({ id, data: { handleId, size } });
    } catch (error) {
        // Arquivo não encontrado ou ilegível — resolve como ausente, não como erro
        postMessage({ id, data: { notFound: true } });
    }
}

function readChunkRequest(id, data) {
    const entry = openReads.get(data.handleId);
    if (!entry) {
        postMessage({ id, error: `Unknown read handle: ${data.handleId}` });
        return;
    }
    try {
        const remaining = entry.size - entry.offset;
        const toRead = Math.min(data.chunkSize, remaining);
        const buffer = new Int8Array(Math.max(toRead, 0));
        if (toRead > 0) {
            entry.syncHandle.read(buffer, { at: entry.offset });
            entry.offset += toRead;
        }
        const done = entry.offset >= entry.size;
        // Transfere o ArrayBuffer de volta ao main thread (zero-copy worker→main)
        postMessage({ id, data: { buffer, done } }, [buffer.buffer]);
    } catch (error) {
        postMessage({ id, error: error.message });
    }
}

function readCloseRequest(id, data) {
    const entry = openReads.get(data.handleId);
    if (entry) {
        entry.syncHandle.close();
        openReads.delete(data.handleId);
    }
    postMessage({ id, data: {} });
}

async function getFileRequest(id, data) {
    try {
        const fileHandle = await resolveFileHandle(data.fileName, false);
        const file = await fileHandle.getFile();
        postMessage({ id, data: { file } });
    } catch (error) {
        // Arquivo não encontrado — resolve como ausente, não como erro
        postMessage({ id, data: { notFound: true } });
    }
}

async function existsRequest(id, data) {
    try {
        await resolveFileHandle(data.fileName, false);
        postMessage({ id, data: { exists: true } });
    } catch (error) {
        postMessage({ id, data: { exists: false } });
    }
}

async function deleteRequest(id, data) {
    try {
        await removeEntry(data.fileName);
        postMessage({ id, data: {} });
    } catch (error) {
        postMessage({ id, error: error.message });
    }
}

async function listRequest(id, data) {
    try {
        const segments = data.directory.split('/').filter((segment) => segment.length > 0);
        let dir = await navigator.storage.getDirectory();
        for (const segment of segments) {
            dir = await dir.getDirectoryHandle(segment);
        }
        const prefix = segments.length > 0 ? `${segments.join('/')}/` : '';
        const files = [];
        for await (const [name, handle] of dir.entries()) {
            if (handle.kind === 'file') {
                files.push(`${prefix}${name}`);
            }
        }
        postMessage({ id, data: { files } });
    } catch (error) {
        // Diretório não encontrado — resolve como ausente, não como erro
        postMessage({ id, data: { notFound: true } });
    }
}

const commandMap = {
    'writeOpen': writeOpenRequest,
    'writeChunk': writeChunkRequest,
    'writeClose': writeCloseRequest,
    'readOpen': readOpenRequest,
    'readChunk': readChunkRequest,
    'readClose': readCloseRequest,
    'getFile': getFileRequest,
    'exists': existsRequest,
    'delete': deleteRequest,
    'list': listRequest,
};

onmessage = (e) => {
    const { id, data } = e.data;
    const handler = commandMap[data?.cmd];
    if (handler) {
        handler(id, data);
    } else {
        postMessage({ id, error: `Unknown command: '${data?.cmd}'` });
    }
};
