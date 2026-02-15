package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import dev.catbit.mosaic.client.extensions.safeNetworkCall
import dev.catbit.mosaic.client.extensions.safeResult
import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
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
    private val baseUrl: String,
    private val httpClient: HttpClient
) : MosaicNetwork {

    override suspend fun getInitialGraph(): Result<GraphResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/initialGraph")
    }.map { httpResponse ->
        httpResponse.body()
    }

    override suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?,
    ): Result<ScreenResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/screen/$screenId") {
            headers?.forEach { (key, value) ->
                header(key, value)
            }
        }
    }.map { httpResponse ->
        httpResponse.body()
    }

    override suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ) = runCatching {
        httpClient.request(
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
    }

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit,
        onBytesReceived: (ByteArray) -> Unit,
        onDownloadFinished: (ByteArray) -> Unit,
        onDownloadFailure: (Throwable) -> Unit
    ) = safeResult {
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