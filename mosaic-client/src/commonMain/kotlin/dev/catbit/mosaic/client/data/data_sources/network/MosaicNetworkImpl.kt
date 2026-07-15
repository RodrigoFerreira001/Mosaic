package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.exceptions.GetRequestWithBodyException
import dev.catbit.mosaic.client.exceptions.MissingUploadUrlException
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import dev.catbit.mosaic.client.extensions.safeNetworkCall
import dev.catbit.mosaic.client.extensions.safeResult
import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.responses.version.VersionResponse
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import dev.catbit.mosaic.core.serialization.serializers.AnySerializer
import io.github.vinceglb.filekit.PlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import io.ktor.http.isSuccess
import kotlin.time.Duration.Companion.seconds

class MosaicNetworkImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val networkParametersHolder: NetworkParametersHolder,
    private val mosaicSerializer: MosaicSerializer,
    private val fileSystem: MosaicFileSystem
) : MosaicNetwork {

    private val connectivityCheckTimeout = 5.seconds

    override suspend fun getInitialGraph(): Result<GraphResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/initialGraph")
    }.map { httpResponse ->
        httpResponse.body()
    }

    override suspend fun getVersion(): Result<VersionResponse> = safeNetworkCall {
        httpClient.get(urlString = "$baseUrl/version")
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
        onProgress: suspend (Float) -> Unit,
        onDownloadFinished: suspend (ByteArray) -> Unit,
        onDownloadFailure: suspend (Throwable) -> Unit
    ) = safeResult {
        try {
            val networkParams = networkParametersHolder.consume()

            val strBody = (body ?: networkParams.body)?.let { anyBody ->
                mosaicSerializer.encodeToString(AnySerializer, anyBody)
            }

            if (httpMethod == HttpMethod.Get && strBody != null) {
                throw GetRequestWithBodyException(url)
            }

            downloadPlatformFile(
                httpClient = httpClient,
                url = url,
                headers = networkParams.headers.orEmpty() + headers.orEmpty(),
                body = strBody,
                httpMethod = httpMethod,
                queryParameters = networkParams.queryParameters,
                onProgress = onProgress,
                onDownloadFinished = onDownloadFinished
            )
        } catch (e: Throwable) {
            onDownloadFailure(e)
        }
    }

    override suspend fun downloadFileToDisk(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod,
        targetFileName: String,
        onProgress: suspend (Float) -> Unit,
        onDownloadFinished: suspend () -> Unit,
        onDownloadFailure: suspend (Throwable) -> Unit
    ) = safeResult {
        try {
            val networkParams = networkParametersHolder.consume()

            val strBody = (body ?: networkParams.body)?.let { anyBody ->
                mosaicSerializer.encodeToString(AnySerializer, anyBody)
            }

            if (httpMethod == HttpMethod.Get && strBody != null) {
                throw GetRequestWithBodyException(url)
            }

            downloadPlatformFileToDisk(
                httpClient = httpClient,
                fileSystem = fileSystem,
                url = url,
                headers = networkParams.headers.orEmpty() + headers.orEmpty(),
                body = strBody,
                httpMethod = httpMethod,
                queryParameters = networkParams.queryParameters,
                targetFileName = targetFileName,
                onProgress = onProgress,
                onDownloadFinished = onDownloadFinished
            )
        } catch (e: Throwable) {
            onDownloadFailure(e)
        }
    }

    override suspend fun uploadFile(
        url: String?,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        contentType: String?,
        platformFile: PlatformFile,
        onProgress: suspend (Float) -> Unit
    ): Result<UploadResult> = runCatching {

        val networkParams = networkParametersHolder.consume()
        val targetUrl = url ?: networkParams.url ?: throw MissingUploadUrlException()

        if (httpMethod == HttpMethod.Get) throw GetRequestWithBodyException(targetUrl)

        uploadPlatformFile(
            httpClient = httpClient,
            url = targetUrl,
            headers = networkParams.headers.orEmpty() + headers.orEmpty(),
            httpMethod = httpMethod,
            platformFile = platformFile,
            contentType = contentType,
            queryParameters = networkParams.queryParameters,
            onProgress = onProgress
        )
    }

    override suspend fun hasInternetConnection(): Boolean = runCatching {
        httpClient.get(urlString = "$baseUrl/version") {
            timeout { requestTimeoutMillis = connectivityCheckTimeout.inWholeMilliseconds }
        }.status.isSuccess()
    }.getOrDefault(false)
}