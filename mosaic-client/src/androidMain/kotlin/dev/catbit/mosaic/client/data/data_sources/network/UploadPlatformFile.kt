package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.extensions.asChunkedFlow
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.size
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully

internal actual suspend fun uploadPlatformFile(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    httpMethod: HttpMethod,
    platformFile: PlatformFile,
    contentType: String?,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Int) -> Unit
): UploadResult {

    var lastPercent = -1

    val response = httpClient.request(
        urlString = url
    ) {
        method = httpMethod

        headers.forEach { (k, v) -> header(k, v) }
        queryParameters?.forEach { (key, value) -> value?.let { parameter(key, it.toString()) } }

        setBody(object : OutgoingContent.WriteChannelContent() {
            override val contentType: ContentType = contentType?.let(ContentType::parse)
                ?: ContentType.Application.OctetStream
            override val contentLength: Long = platformFile.size()
            override suspend fun writeTo(channel: ByteWriteChannel) {
                platformFile.asChunkedFlow().collect { chunk -> channel.writeFully(chunk) }
            }
        })

        onUpload { bytesSentTotal, contentLength ->
            if (contentLength != null && contentLength > 0) {
                val percent = (bytesSentTotal.toDouble() / contentLength * 100).toInt()
                if (percent != lastPercent) {
                    lastPercent = percent
                    onProgress(percent)
                }
            }
        }
    }

    return UploadResult(
        statusCode = response.status.value,
        contentType = response.contentType()?.toString(),
        body = response.bodyAsBytes()
    )
}