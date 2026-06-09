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
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

class MosaicNetworkImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val networkParametersHolder: NetworkParametersHolder
) : MosaicNetwork {

    override suspend fun getInitialGraph(): Result<GraphResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/initialGraph")
    }.map { httpResponse ->
        httpResponse.body()
    }

    override suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<ScreenResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/screens/$screenId") {
            method = httpMethod

            val (bodyParam, headersParams) = networkParametersHolder.consume()

            (headersParams.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                header(key, value)
            }

            (body ?: bodyParam)?.let(::setBody)
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
        httpClient.request(urlString = url) {
            method = httpMethod

            val (bodyParam, headersParams) = networkParametersHolder.consume()

            (headersParams.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                header(key, value)
            }

            (body ?: bodyParam)?.let(::setBody)
        }
    }

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit,
        onBytesReceived: (ByteArray) -> Unit,
        onDownloadFinished: (ByteArray) -> Unit,
        onDownloadFailure: (Throwable) -> Unit
    ) = safeResult {
        try {
            httpClient.prepareRequest(urlString = url) {
                method = httpMethod

                val (bodyParam, headersParams) = networkParametersHolder.consume()

                (headersParams.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                    header(key, value)
                }

                (body ?: bodyParam)?.let(::setBody)

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
                val builder = Buffer()
                val buffer = ByteArray(8192)

                while (!channel.isClosedForRead) {
                    val read = channel.readAvailable(buffer)
                    if (read <= 0) break

                    val chunk = buffer.copyOfRange(0, read)

                    onBytesReceived(chunk)

                    builder.write(chunk)
                }

                val completeArray = builder.readByteArray()
                onDownloadFinished(completeArray)
            }
        } catch (e: Throwable) {
            onDownloadFailure(e)
        }
    }
}