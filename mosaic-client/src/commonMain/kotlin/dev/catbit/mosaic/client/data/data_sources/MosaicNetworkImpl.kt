package dev.catbit.mosaic.client.data.data_sources

import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.header
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.build
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.readAvailable
import kotlinx.io.readByteArray

class MosaicNetworkImpl(
    private val httpClient: HttpClient
) : MosaicNetwork {

    override suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ) = httpClient.request(
        urlString = url
    ) {
        method = httpMethod
        body?.let {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(body)
        }
        headers?.forEach { (key, value) ->
            header(key, value)
        }
    }

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit,
        onBytesReceived: (ByteArray) -> Unit,
        onDownloadFinished: (ByteArray) -> Unit,
        onDownloadFailure: (Throwable) -> Unit
    ) {
        try {
            httpClient.prepareRequest(urlString = url) {
                method = httpMethod
                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                onDownload { bytesSentTotal, contentLength ->
                    if (contentLength != null && contentLength > 0) {
                        val percent = (bytesSentTotal.toDouble() / contentLength * 100).toInt()
                        onProgress(percent)
                    }
                }

            }.execute { response ->

                if (!response.status.isSuccess()) {
                    throw NetworkResponseException(
                        status = response.status,
                        error = response.bodyAsText()
                    )
                }

                val channel: ByteReadChannel = response.bodyAsChannel()
                val builder = BytePacketBuilder()
                val buffer = ByteArray(8192)

                while (!channel.isClosedForRead) {
                    val read = channel.readAvailable(buffer)
                    if (read <= 0) break

                    val chunk = buffer.copyOfRange(0, read)

                    onBytesReceived(chunk)

                    builder.writeFully(chunk)
                }

                val completeArray = builder.build().readByteArray()
                onDownloadFinished(completeArray)
            }
        } catch (e: Throwable) {
            onDownloadFailure(e)
        }
    }
}