@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

@JsFun("""(url, method, headersJson, file, contentType, timeoutMillis, onProgress) => {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.timeout = timeoutMillis;
        const headers = JSON.parse(headersJson);
        for (const key in headers) xhr.setRequestHeader(key, headers[key]);
        if (contentType) xhr.setRequestHeader('Content-Type', contentType);
        xhr.responseType = 'arraybuffer';
        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) onProgress(Math.round((event.loaded / event.total) * 100));
        };
        xhr.onload = () => resolve(xhr);
        xhr.onerror = () => reject(new Error('Upload failed: network error'));
        xhr.ontimeout = () => reject(new Error('Upload failed: timed out after ' + timeoutMillis + 'ms'));
        xhr.send(file);
    });
}""")
private external fun xhrUploadFile(
    url: String,
    method: String,
    headersJson: String,
    file: JsAny,
    contentType: String?,
    timeoutMillis: Int,
    onProgress: (Int) -> Unit
): Promise<JsAny>

@JsFun("(xhr) => xhr.status")
private external fun jsXhrStatus(xhr: JsAny): Int

@JsFun("(xhr) => xhr.getResponseHeader('content-type')")
private external fun jsXhrContentType(xhr: JsAny): String?

@JsFun("(xhr) => xhr.response")
private external fun jsXhrResponse(xhr: JsAny): JsAny?

internal actual suspend fun uploadPlatformFile(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    httpMethod: HttpMethod,
    platformFile: PlatformFile,
    contentType: String?,
    queryParameters: Map<String, Any?>?,
    timeouts: TimeoutsSchema?,
    onProgress: suspend (Float) -> Unit
): UploadResult = coroutineScope {
    val jsFile: JsAny = (platformFile.webFile as WebFile.FileWrapper).file.unsafeCast()

    val targetUrl = URLBuilder(url).apply {
        queryParameters?.forEach { (key, value) -> value?.let { parameters.append(key, it.toString()) } }
    }.buildString()

    val headersJson = buildString {
        append("{")
        headers.entries.forEachIndexed { index, (k, v) ->
            if (index > 0) append(",")
            append("\"")
            append(k.replace("\\", "\\\\").replace("\"", "\\\""))
            append("\":\"")
            append(v.replace("\\", "\\\\").replace("\"", "\\\""))
            append("\"")
        }
        append("}")
    }

    val progressChannel = Channel<Int>(Channel.CONFLATED)
    val progressJob = launch {
        for (percent in progressChannel) onProgress(percent / 100f)
    }

    val xhr = try {
        xhrUploadFile(
            url = targetUrl,
            method = httpMethod.value,
            headersJson = headersJson,
            file = jsFile,
            contentType = contentType,
            timeoutMillis = timeouts.toXhrTimeoutMillis(),
            onProgress = { percent -> progressChannel.trySend(percent) }
        ).await()
    } finally {
        progressChannel.close()
        progressJob.join()
    }

    val status = jsXhrStatus(xhr)
    val ct = jsXhrContentType(xhr)
    val arrayBuffer = jsXhrResponse(xhr)?.unsafeCast<ArrayBuffer>()
    val uint8 = arrayBuffer?.let(::Uint8Array)

    UploadResult(
        statusCode = status,
        contentType = ct,
        body = uint8?.let { u -> ByteArray(u.length) { u[it] } } ?: ByteArray(0)
    )
}
