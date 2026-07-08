@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.BrowserFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.khronos.webgl.Int8Array
import org.khronos.webgl.toByteArray
import org.khronos.webgl.toInt8Array

actual object PlatformFileHandler {

    private const val STREAM_CHUNK_SIZE = 64 * 1024

    private val client: JsAny = createOpfsClient()

    actual suspend fun saveFile(fileName: String, data: ByteArray) {
        val handleId = clientWriteOpen(client, fileName).await<JsAny?>()?.let(::getHandleId) ?: return
        try {
            clientWriteChunk(client, handleId, data.toInt8Array()).await<JsAny?>()
        } finally {
            clientWriteClose(client, handleId).await<JsAny?>()
        }
    }

    actual suspend fun getFile(fileName: String): ByteArray? =
        runCatching {
            val chunks = mutableListOf<ByteArray>()
            getFileStreaming(fileName)?.collectInto(chunks) ?: return@runCatching null
            concat(chunks)
        }.getOrNull()

    actual suspend fun getFilePlatformFile(fileName: String): PlatformFile? =
        runCatching {
            val result = clientGetFile(client, fileName).await<JsAny?>() ?: return@runCatching null
            if (isNotFound(result)) return@runCatching null
            val browserFile = getBrowserFileFromResult(result)?.unsafeCast<BrowserFile>() ?: return@runCatching null
            PlatformFile(WebFile.FileWrapper(file = browserFile, path = null, parent = null))
        }.getOrNull()

    actual suspend fun deleteFile(fileName: String) {
        runCatching {
            clientDelete(client, fileName).await<JsAny?>()
        }
    }

    actual suspend fun fileExists(fileName: String): Boolean =
        runCatching {
            val result = clientExists(client, fileName).await<JsAny?>() ?: return@runCatching false
            isExists(result)
        }.getOrDefault(false)

    actual suspend fun saveFileStreaming(fileName: String, data: Flow<ByteArray>) {
        val openResult = clientWriteOpen(client, fileName).await<JsAny?>()
            ?: error("Failed to open OPFS file for writing: $fileName")
        val handleId = getHandleId(openResult)
        try {
            data.collect { chunk ->
                clientWriteChunk(client, handleId, chunk.toInt8Array()).await<JsAny?>()
            }
        } finally {
            clientWriteClose(client, handleId).await<JsAny?>()
        }
    }

    actual suspend fun getFileStreaming(fileName: String): Flow<ByteArray>? {
        val openResult = clientReadOpen(client, fileName).await<JsAny?>() ?: return null
        if (isNotFound(openResult)) return null
        val handleId = getHandleId(openResult)

        return flow {
            try {
                var done = false
                while (!done) {
                    val chunkResult = clientReadChunk(client, handleId, STREAM_CHUNK_SIZE).await<JsAny?>()
                        ?: break
                    val buffer = getBufferFromResult(chunkResult)
                    val bytes = buffer?.unsafeCast<Int8Array>()?.toByteArray()
                    if (bytes != null && bytes.isNotEmpty()) emit(bytes)
                    done = isDone(chunkResult)
                }
            } finally {
                clientReadClose(client, handleId).await<JsAny?>()
            }
        }
    }

    private suspend fun Flow<ByteArray>.collectInto(destination: MutableList<ByteArray>) {
        collect { chunk -> destination.add(chunk) }
    }

    private fun concat(chunks: List<ByteArray>): ByteArray {
        val result = ByteArray(chunks.sumOf { it.size })
        var offset = 0
        for (chunk in chunks) {
            chunk.copyInto(result, offset)
            offset += chunk.size
        }
        return result
    }
}

@JsFun("""() => {
    const pending = new Map();
    let nextId = 0;
    const worker = new Worker(new URL("opfs-wasm-worker/worker.js", import.meta.url));

    worker.onmessage = (e) => {
        const { id, data, error } = e.data;
        const cb = pending.get(id);
        if (cb) { pending.delete(id); cb(data, error); }
    };

    function call(cmd, extra, transfer) {
        return new Promise((resolve, reject) => {
            const id = nextId++;
            pending.set(id, (data, err) => err ? reject(new Error(err)) : resolve(data));
            worker.postMessage({ id, data: { cmd, ...extra } }, transfer ?? []);
        });
    }

    return {
        writeOpen: (fileName) => call('writeOpen', { fileName }),
        writeChunk: (handleId, int8Array) => call(
            'writeChunk',
            { handleId, buffer: int8Array },
            [int8Array.buffer]
        ),
        writeClose: (handleId) => call('writeClose', { handleId }),
        readOpen: (fileName) => call('readOpen', { fileName }),
        readChunk: (handleId, chunkSize) => call('readChunk', { handleId, chunkSize }),
        readClose: (handleId) => call('readClose', { handleId }),
        getFile: (fileName) => call('getFile', { fileName }),
        exists: (fileName) => call('exists', { fileName }),
        delete: (fileName) => call('delete', { fileName }),
    };
}""")
private external fun createOpfsClient(): JsAny

@JsFun("(client, fileName) => client.writeOpen(fileName)")
private external fun clientWriteOpen(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(client, handleId, int8Array) => client.writeChunk(handleId, int8Array)")
private external fun clientWriteChunk(client: JsAny, handleId: Int, buffer: Int8Array): Promise<JsAny?>

@JsFun("(client, handleId) => client.writeClose(handleId)")
private external fun clientWriteClose(client: JsAny, handleId: Int): Promise<JsAny?>

@JsFun("(client, fileName) => client.readOpen(fileName)")
private external fun clientReadOpen(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(client, handleId, chunkSize) => client.readChunk(handleId, chunkSize)")
private external fun clientReadChunk(client: JsAny, handleId: Int, chunkSize: Int): Promise<JsAny?>

@JsFun("(client, handleId) => client.readClose(handleId)")
private external fun clientReadClose(client: JsAny, handleId: Int): Promise<JsAny?>

@JsFun("(client, fileName) => client.getFile(fileName)")
private external fun clientGetFile(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(client, fileName) => client.exists(fileName)")
private external fun clientExists(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(client, fileName) => client.delete(fileName)")
private external fun clientDelete(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(result) => result?.buffer ?? null")
private external fun getBufferFromResult(result: JsAny): JsAny?

@JsFun("(result) => result?.file ?? null")
private external fun getBrowserFileFromResult(result: JsAny): JsAny?

@JsFun("(result) => result?.handleId ?? -1")
private external fun getHandleId(result: JsAny): Int

@JsFun("(result) => result?.notFound === true")
private external fun isNotFound(result: JsAny): Boolean

@JsFun("(result) => result?.done === true")
private external fun isDone(result: JsAny): Boolean

@JsFun("(result) => result?.exists === true")
private external fun isExists(result: JsAny): Boolean
