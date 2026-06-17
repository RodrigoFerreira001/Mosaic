package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MosaicNetwork {

    suspend fun getInitialGraph(): Result<GraphResponse>

    suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<ScreenResponse>

    suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>? = null,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<HttpResponse>

    suspend fun downloadFile(
        url: String,
        headers: Map<String, String>? = null,
        body: Any?,
        httpMethod: HttpMethod,
        onProgress: suspend (Int) -> Unit = {},
        onBytesReceived: suspend (ByteArray) -> Unit = {},
        onDownloadFinished: suspend (ByteArray) -> Unit = {},
        onDownloadFailure: suspend (Throwable) -> Unit = {}
    ): Result<Unit>

    suspend fun uploadFile(
        url: String?,
        headers: Map<String, String>? = null,
        httpMethod: HttpMethod,
        contentType: String?,
        bytes: ByteArray,
        onProgress: suspend (Int) -> Unit = {}
    ): Result<HttpResponse>
}