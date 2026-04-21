// OPFS Dedicated Worker
// Uses FileSystemSyncAccessHandle for direct byte-level I/O.
// Receives and returns Uint8Array — sem Base64, sem encoding overhead.

async function writeRequest(id, data) {
    let syncHandle = null;
    try {
        const root = await navigator.storage.getDirectory();
        const fileHandle = await root.getFileHandle(data.fileName, { create: true });
        syncHandle = await fileHandle.createSyncAccessHandle();

        // data.buffer é o ArrayBuffer transferido do main thread (zero-copy)
        const bytes = new Uint8Array(data.buffer);
        syncHandle.truncate(0);
        syncHandle.write(bytes, { at: 0 });
        syncHandle.flush();

        postMessage({ id, data: {} });
    } catch (error) {
        postMessage({ id, error: error.message });
    } finally {
        syncHandle?.close();
    }
}

async function readRequest(id, data) {
    let syncHandle = null;
    try {
        const root = await navigator.storage.getDirectory();
        const fileHandle = await root.getFileHandle(data.fileName);
        syncHandle = await fileHandle.createSyncAccessHandle();

        const size = syncHandle.getSize();
        const buffer = new Uint8Array(size);
        syncHandle.read(buffer, { at: 0 });

        // Transfere o ArrayBuffer de volta ao main thread (zero-copy worker→main)
        postMessage({ id, data: { buffer } }, [buffer.buffer]);
    } catch (error) {
        // Arquivo não encontrado ou ilegível — resolve como ausente, não como erro
        postMessage({ id, data: { notFound: true } });
    } finally {
        syncHandle?.close();
    }
}

async function deleteRequest(id, data) {
    try {
        const root = await navigator.storage.getDirectory();
        await root.removeEntry(data.fileName);
        postMessage({ id, data: {} });
    } catch (error) {
        postMessage({ id, error: error.message });
    }
}

const commandMap = {
    'write': writeRequest,
    'read': readRequest,
    'delete': deleteRequest,
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
