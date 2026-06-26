package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.exceptions.GetRequestWithBodyException
import dev.catbit.mosaic.client.exceptions.MissingUploadUrlException
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import dev.catbit.mosaic.client.extensions.safeNetworkCall
import dev.catbit.mosaic.client.extensions.safeResult
import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import dev.catbit.mosaic.core.serialization.serializers.AnySerializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

class MosaicNetworkImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val networkParametersHolder: NetworkParametersHolder,
    private val mosaicSerializer: MosaicSerializer
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
        val screenUrl = "$baseUrl/screens/$screenId"
        httpClient.get(urlString = screenUrl) {
            method = httpMethod

            val networkParams = networkParametersHolder.consume()

            (networkParams.headers.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                header(key, value)
            }

            val strBody = (body ?: networkParams.body)?.let { anyBody ->
                mosaicSerializer.encodeToString(AnySerializer, anyBody)
            }

            if (httpMethod == HttpMethod.Get && strBody != null) {
                throw GetRequestWithBodyException(screenUrl)
            }

            strBody?.let { setBody(TextContent(it, ContentType.Application.Json)) }

            networkParams.queryParameters?.forEach { (key, value) ->
                value?.let { parameter(key, it.toString()) }
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
        httpClient.request(urlString = url) {
            method = httpMethod

            val networkParams = networkParametersHolder.consume()

            (networkParams.headers.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                header(key, value)
            }

            val strBody = (body ?: networkParams.body)?.let { anyBody ->
                mosaicSerializer.encodeToString(AnySerializer, anyBody)
            }

            if (httpMethod == HttpMethod.Get && strBody != null) {
                throw GetRequestWithBodyException(url)
            }

            strBody?.let { setBody(TextContent(it, ContentType.Application.Json)) }

            networkParams.queryParameters?.forEach { (key, value) ->
                value?.let { parameter(key, it.toString()) }
            }
        }
    }

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod,
        onProgress: suspend (Int) -> Unit,
        onBytesReceived: suspend (ByteArray) -> Unit,
        onDownloadFinished: suspend (ByteArray) -> Unit,
        onDownloadFailure: suspend (Throwable) -> Unit
    ) = safeResult {
        try {
            httpClient.prepareRequest(urlString = url) {
                method = httpMethod

                val networkParams = networkParametersHolder.consume()

                (networkParams.headers.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                    header(key, value)
                }

                val strBody = (body ?: networkParams.body)?.let { anyBody ->
                    mosaicSerializer.encodeToString(AnySerializer, anyBody)
                }

                if (httpMethod == HttpMethod.Get && strBody != null) {
                    throw GetRequestWithBodyException(url)
                }

                strBody?.let { setBody(TextContent(it, ContentType.Application.Json)) }

                networkParams.queryParameters?.forEach { (key, value) ->
                    value?.let { parameter(key, it.toString()) }
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

    override suspend fun uploadFile(
        url: String?,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        contentType: String?,
        bytes: ByteArray,
        onProgress: suspend (Int) -> Unit
    ) = runCatching {

        val (_, headersParams, urlParam) = networkParametersHolder.consume()

        val targetUrl = url ?: urlParam ?: throw MissingUploadUrlException()

        var lastPercent = -1

        if (httpMethod == HttpMethod.Get) {
            throw GetRequestWithBodyException(targetUrl)
        }

        httpClient.request(urlString = targetUrl) {
            method = httpMethod

            (headersParams.orEmpty() + headers.orEmpty()).forEach { (key, value) ->
                header(key, value)
            }

            setBody(
                ByteArrayContent(
                    bytes = bytes,
                    contentType = contentType
                        ?.let(ContentType::parse)
                        ?: ContentType.Application.OctetStream
                )
            )

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
    }
}