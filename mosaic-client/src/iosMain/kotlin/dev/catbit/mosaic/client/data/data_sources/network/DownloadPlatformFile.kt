package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.exceptions.DownloadCancelledException
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.flow.flow
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

internal actual suspend fun downloadPlatformFileToMemory(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend (ByteArray) -> Unit
) {
    httpClient.prepareRequest(urlString = url) {
        method = httpMethod

        headers.forEach { (k, v) -> header(k, v) }
        body?.let { setBody(TextContent(it, ContentType.Application.Json)) }
        queryParameters?.forEach { (key, value) -> value?.let { parameter(key, it.toString()) } }
    }.execute { response ->

        if (!response.status.isSuccess()) {
            throw NetworkResponseException(
                status = response.status,
                error = response.bodyAsText()
            )
        }

        val channel: ByteReadChannel = response.bodyAsChannel()
        val contentLength = response.contentLength()
        val buffer = ByteArray(8192)
        var bytesRead = 0L

        val completeArray = if (contentLength != null && contentLength > 0) {
            // Known size: write straight into a single pre-allocated array,
            // avoiding the extra full-file copy required by Buffer.readByteArray().
            val result = ByteArray(contentLength.toInt())
            var offset = 0

            while (!channel.isClosedForRead) {
                val read = channel.readAvailable(buffer)
                if (read <= 0) break

                buffer.copyInto(result, offset, 0, read)
                offset += read

                bytesRead += read
                onProgress((bytesRead.toDouble() / contentLength).toFloat())
            }

            result
        } else {
            // Unknown size (e.g. chunked encoding without length): fall back
            // to accumulating into a growable Buffer.
            val builder = Buffer()

            while (!channel.isClosedForRead) {
                val read = channel.readAvailable(buffer)
                if (read <= 0) break

                builder.write(buffer.copyOfRange(0, read))
            }

            builder.readByteArray()
        }

        onDownloadFinished(completeArray)
    }
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
    httpClient.prepareRequest(urlString = url) {
        method = httpMethod

        headers.forEach { (k, v) -> header(k, v) }
        body?.let { setBody(TextContent(it, ContentType.Application.Json)) }
        queryParameters?.forEach { (key, value) -> value?.let { parameter(key, it.toString()) } }
    }.execute { response ->

        if (!response.status.isSuccess()) {
            throw NetworkResponseException(
                status = response.status,
                error = response.bodyAsText()
            )
        }

        val channel: ByteReadChannel = response.bodyAsChannel()
        val contentLength = response.contentLength()
        val chunks = flow {
            val buffer = ByteArray(8192)
            var bytesRead = 0L

            while (!channel.isClosedForRead) {
                val read = channel.readAvailable(buffer)
                if (read <= 0) break
                emit(buffer.copyOfRange(0, read))

                if (contentLength != null && contentLength > 0) {
                    bytesRead += read
                    onProgress((bytesRead.toDouble() / contentLength).toFloat())
                }
            }
        }

        fileSystem.saveFileStreaming(targetFileName, chunks)
        onDownloadFinished()
    }
}

/**
 * Downloads [url] into memory (same Ktor streaming as [downloadPlatformFileToMemory]), then
 * presents `FileKit.openFileSaver()` — a `UIDocumentPickerViewController` export dialog — so the
 * user picks a Files-app destination (iCloud Drive, "On My iPhone", etc). There is no
 * app-writable public "Downloads" location on iOS; this is the closest sanctioned equivalent and
 * always requires one tap from the user. Cancelling the dialog throws [DownloadCancelledException]
 * rather than a generic failure.
 */
internal actual suspend fun downloadPlatformFileToPublicStorage(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    targetFileName: String,
    mimeType: String?,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend () -> Unit
) {
    val bytes = downloadToByteArray(
        httpClient = httpClient,
        url = url,
        headers = headers,
        body = body,
        httpMethod = httpMethod,
        queryParameters = queryParameters,
        onProgress = onProgress
    )

    val extension = targetFileName.substringAfterLast('.', "")
    val baseName = if (extension.isNotEmpty()) targetFileName.removeSuffix(".$extension") else targetFileName

    val savedFile = FileKit.openFileSaver(
        suggestedName = baseName,
        defaultExtension = extension.ifEmpty { "bin" }
    ) ?: throw DownloadCancelledException()

    savedFile.write(bytes)
    onDownloadFinished()
}

private suspend fun downloadToByteArray(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit
): ByteArray {
    var result: ByteArray = ByteArray(0)
    downloadPlatformFileToMemory(
        httpClient = httpClient,
        url = url,
        headers = headers,
        body = body,
        httpMethod = httpMethod,
        queryParameters = queryParameters,
        onProgress = onProgress,
        onDownloadFinished = { bytes -> result = bytes }
    )
    return result
}
