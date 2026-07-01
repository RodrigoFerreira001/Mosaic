@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.extensions

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import io.github.vinceglb.filekit.exceptions.FileKitException
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

// File.size — retorna Double porque JS não distingue int/float
@JsFun("(file) => file.size")
private external fun jsFileSize(file: JsAny): Double

// File.slice(start, end) → Blob — cria uma view sem copiar bytes
@JsFun("(file, start, end) => file.slice(start, end)")
private external fun jsFileSlice(file: JsAny, start: Double, end: Double): JsAny

// Blob.arrayBuffer() → Promise<ArrayBuffer> — leitura async, suspende sem bloquear
@JsFun("(blob) => blob.arrayBuffer()")
private external fun blobArrayBuffer(blob: JsAny): Promise<JsAny>

actual fun PlatformFile.asChunkedFlow(chunkSize: Int): Flow<ByteArray> = flow {
    when (val wf = webFile) {
        is WebFile.FileWrapper -> {
            val jsFile: JsAny = wf.file.unsafeCast()
            val total = jsFileSize(jsFile).toLong()
            var offset = 0L

            while (offset < total) {
                val end = minOf(offset + chunkSize.toLong(), total)
                val blob = jsFileSlice(jsFile, offset.toDouble(), end.toDouble())
                val arrayBuffer = blobArrayBuffer(blob).await().unsafeCast<ArrayBuffer>()
                val uint8 = Uint8Array(arrayBuffer)
                emit(ByteArray(uint8.length) { uint8[it] })
                offset = end
            }
        }
        is WebFile.DirectoryWrapper -> throw FileKitException("Cannot read bytes from a directory")
    }
}
