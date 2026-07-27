package dev.catbit.mosaic.client.data.data_sources.network

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import dev.catbit.mosaic.client.application.ActivityHolder
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
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
import kotlinx.coroutines.delay
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

private const val DOWNLOAD_POLL_INTERVAL_MS = 500L

/**
 * Delegated entirely to [DownloadManager] — the system service does its own networking and
 * writes straight into the public Downloads folder, silently, with no runtime permission needed
 * on any supported API level (26+). [httpClient] is unused here since the request never goes
 * through Ktor. Only plain `GET` requests with no body are supported, matching `DownloadManager`'s
 * own capabilities.
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
    if (httpMethod != HttpMethod.Get || body != null) {
        throw UnsupportedOperationException(
            "DownloadFile to public storage only supports GET requests without a body on Android (DownloadManager limitation)"
        )
    }

    val requestUrl = Uri.parse(url).buildUpon().apply {
        queryParameters?.forEach { (key, value) -> value?.let { appendQueryParameter(key, it.toString()) } }
    }.build()

    val resolvedMimeType = mimeType
        ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(targetFileName.substringAfterLast('.', ""))

    val activity = ActivityHolder.getActivity()
    val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val request = DownloadManager.Request(requestUrl).apply {
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, targetFileName)
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        resolvedMimeType?.let { setMimeType(it) }
        headers.forEach { (key, value) -> addRequestHeader(key, value) }
    }

    val downloadId = downloadManager.enqueue(request)
    val query = DownloadManager.Query().setFilterById(downloadId)

    while (true) {
        val cursor = downloadManager.query(query)
        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
        val totalBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
        val reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)

        if (!cursor.moveToFirst()) {
            cursor.close()
            throw IllegalStateException("DownloadManager lost track of download id $downloadId")
        }

        val status = cursor.getInt(statusIndex)
        val bytesDownloaded = cursor.getLong(bytesDownloadedIndex)
        val totalBytes = cursor.getLong(totalBytesIndex)
        val reason = cursor.getInt(reasonIndex)
        cursor.close()

        if (totalBytes > 0) {
            onProgress(bytesDownloaded.toFloat() / totalBytes)
        }

        when (status) {
            DownloadManager.STATUS_SUCCESSFUL -> {
                onDownloadFinished()
                return
            }
            DownloadManager.STATUS_FAILED -> {
                throw IllegalStateException("DownloadManager failed for '$targetFileName' (reason=$reason)")
            }
            else -> delay(DOWNLOAD_POLL_INTERVAL_MS)
        }
    }
}
