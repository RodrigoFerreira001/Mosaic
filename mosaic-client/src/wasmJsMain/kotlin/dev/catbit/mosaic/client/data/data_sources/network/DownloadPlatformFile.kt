@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

@JsFun("""(url, method, headersJson, body, onProgress) => {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url);
        const headers = JSON.parse(headersJson);
        for (const key in headers) xhr.setRequestHeader(key, headers[key]);
        xhr.responseType = 'arraybuffer';
        xhr.onprogress = (event) => {
            if (event.lengthComputable) onProgress(Math.round((event.loaded / event.total) * 100));
        };
        xhr.onload = () => resolve(xhr);
        xhr.onerror = () => reject(new Error('Download failed: network error'));
        xhr.send(body || null);
    });
}""")
private external fun xhrDownloadFile(
    url: String,
    method: String,
    headersJson: String,
    body: String?,
    onProgress: (Int) -> Unit
): Promise<JsAny>

@JsFun("(xhr) => xhr.status")
private external fun jsXhrDownloadStatus(xhr: JsAny): Int

@JsFun("(xhr) => xhr.statusText")
private external fun jsXhrDownloadStatusText(xhr: JsAny): String

@JsFun("(xhr) => xhr.response")
private external fun jsXhrDownloadResponse(xhr: JsAny): JsAny?

private suspend fun performXhrDownload(
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit
): ByteArray = coroutineScope {
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
        xhrDownloadFile(
            url = targetUrl,
            method = httpMethod.value,
            headersJson = headersJson,
            body = body,
            onProgress = { percent -> progressChannel.trySend(percent) }
        ).await()
    } finally {
        progressChannel.close()
        progressJob.join()
    }

    val status = jsXhrDownloadStatus(xhr)
    if (status !in 200..299) {
        throw NetworkResponseException(
            status = HttpStatusCode.fromValue(status),
            error = jsXhrDownloadStatusText(xhr)
        )
    }

    val arrayBuffer = jsXhrDownloadResponse(xhr)?.unsafeCast<ArrayBuffer>()
    val uint8 = arrayBuffer?.let(::Uint8Array)
    uint8?.let { u -> ByteArray(u.length) { u[it] } } ?: ByteArray(0)
}

internal actual suspend fun downloadPlatformFile(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend (ByteArray) -> Unit
) {
    val bytes = performXhrDownload(url, headers, body, httpMethod, queryParameters, onProgress)
    onDownloadFinished(bytes)
}

internal actual suspend fun downloadPlatformFileToDisk(
    httpClient: HttpClient,
    fileSystem: MosaicFileSystem,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    targetFileName: String,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend () -> Unit
) {
    // The XHR path always buffers the full response before onload fires, so there's
    // no incremental-write benefit here — it's already fully in memory either way.
    val bytes = performXhrDownload(url, headers, body, httpMethod, queryParameters, onProgress)
    fileSystem.saveFileStreaming(targetFileName, flowOf(bytes))
    onDownloadFinished()
}
