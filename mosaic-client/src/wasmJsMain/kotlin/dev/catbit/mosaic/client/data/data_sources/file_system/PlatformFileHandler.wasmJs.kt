@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.data.data_sources.file_system

import kotlin.js.Promise
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

actual object PlatformFileHandler {

    private val client: JsAny = createOpfsClient()

    actual suspend fun saveFile(fileName: String, data: ByteArray) {
        runCatching {
            clientWrite(client, fileName, data.toUint8Array()).await<JsAny?>()
        }.onFailure {
            println("Failure when saving to OPFS: ${it.message}")
        }
    }

    actual suspend fun getFile(fileName: String): ByteArray? =
        runCatching {
            val result = clientRead(client, fileName).await<JsAny?>()
                ?: return@runCatching null
            getBufferFromResult(result)
                ?.unsafeCast<Uint8Array>()
                ?.toByteArray()
        }.getOrNull()

    actual suspend fun deleteFile(fileName: String) {
        runCatching {
            clientDelete(client, fileName).await<JsAny?>()
        }
    }

    private fun ByteArray.toUint8Array(): Uint8Array =
        Uint8Array(size).apply { forEachIndexed { index, byte -> this[index] = byte } }

    private fun Uint8Array.toByteArray(): ByteArray =
        ByteArray(length) { this[it] }
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

    return {
        write: (fileName, uint8Array) => new Promise((resolve, reject) => {
            const id = nextId++;
            pending.set(id, (data, err) => err ? reject(new Error(err)) : resolve(null));
            worker.postMessage(
                { id, data: { cmd: 'write', fileName, buffer: uint8Array } },
                [uint8Array.buffer]
            );
        }),
        read: (fileName) => new Promise((resolve, reject) => {
            const id = nextId++;
            pending.set(id, (data, err) => err ? reject(new Error(err)) : resolve(data));
            worker.postMessage({ id, data: { cmd: 'read', fileName } });
        }),
        delete: (fileName) => new Promise((resolve, reject) => {
            const id = nextId++;
            pending.set(id, (data, err) => err ? reject(new Error(err)) : resolve(null));
            worker.postMessage({ id, data: { cmd: 'delete', fileName } });
        })
    };
}""")
private external fun createOpfsClient(): JsAny

@JsFun("(client, fileName, uint8Array) => client.write(fileName, uint8Array)")
private external fun clientWrite(client: JsAny, fileName: String, buffer: Uint8Array): Promise<JsAny?>

@JsFun("(client, fileName) => client.read(fileName)")
private external fun clientRead(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(client, fileName) => client.delete(fileName)")
private external fun clientDelete(client: JsAny, fileName: String): Promise<JsAny?>

@JsFun("(result) => result?.buffer ?? null")
private external fun getBufferFromResult(result: JsAny): JsAny?
