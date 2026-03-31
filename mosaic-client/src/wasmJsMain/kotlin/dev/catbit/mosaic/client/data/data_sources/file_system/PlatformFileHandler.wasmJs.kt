@file:OptIn(ExperimentalWasmJsInterop::class, ExperimentalEncodingApi::class)

package dev.catbit.mosaic.client.data.data_sources.file_system

import kotlin.js.JsAny
import kotlin.js.JsString
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

actual object PlatformFileHandler {

    actual suspend fun saveFile(fileName: String, data: ByteArray) {
        runCatching {
            // Await agora inferirá JsAny? corretamente
            val root = getOPFSRoot().await<JsAny?>()
            val fileHandle = getOrCreateFileHandle(root, fileName).await<JsAny?>()

            val base64Data = Base64.encode(data)
            writeBase64ToFileHandle(fileHandle, base64Data).await<JsAny?>()
        }.onFailure {
            println("Falha ao salvar o arquivo no OPFS: ${it.message}")
        }
    }

    actual suspend fun getFile(fileName: String): ByteArray? =
        runCatching {
            val root = getOPFSRoot().await<JsAny?>()
            val fileHandle = getExistingFileHandle(root, fileName).await<JsAny?>()

            // Retorna um JsString? do JS. Chamamos toString() para virar String do Kotlin.
            val base64JsString = readBase64FromFileHandle(fileHandle).await<JsString?>()
            val base64Data = base64JsString?.toString() ?: return@runCatching null

            Base64.decode(base64Data)
        }.getOrNull()

    actual suspend fun deleteFile(fileName: String) {
        runCatching {
            val root = getOPFSRoot().await<JsAny?>()
            removeEntry(root, fileName).await<JsAny?>()
        }
    }
}

// =========================================================
// OPFS Interop Básico
// Note o uso de Promise<JsAny?> para evitar os erros do .await()
// =========================================================

@JsFun("() => navigator.storage.getDirectory()")
private external fun getOPFSRoot(): Promise<JsAny?>

@JsFun("(dir, name) => dir.getFileHandle(name, { create: true })")
private external fun getOrCreateFileHandle(dir: JsAny?, name: String): Promise<JsAny?>

@JsFun("(dir, name) => dir.getFileHandle(name)")
private external fun getExistingFileHandle(dir: JsAny?, name: String): Promise<JsAny?>

@JsFun("(dir, name) => dir.removeEntry(name)")
private external fun removeEntry(dir: JsAny?, name: String): Promise<JsAny?>

// =========================================================
// Pipeline de Leitura/Escrita de Alta Performance
// =========================================================

@JsFun("""async (handle, base64Data) => {
    const response = await fetch('data:application/octet-stream;base64,' + base64Data);
    const blob = await response.blob();
    const writable = await handle.createWritable();
    await writable.write(blob);
    await writable.close();
}""")
private external fun writeBase64ToFileHandle(handle: JsAny?, base64Data: String): Promise<JsAny?>

// Correção fundamental: Promise<JsString?> em vez de Promise<String>
@JsFun("""async (handle) => {
    const file = await handle.getFile();
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => {
            const dataUrl = reader.result;
            const base64 = dataUrl.substring(dataUrl.indexOf(',') + 1);
            resolve(base64);
        };
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}""")
private external fun readBase64FromFileHandle(handle: JsAny?): Promise<JsString?>