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
        onProgress: (Int) -> Unit = {},
        onBytesReceived: (ByteArray) -> Unit = {},
        onDownloadFinished: (ByteArray) -> Unit = {},
        onDownloadFailure: (Throwable) -> Unit = {}
    ): Result<Unit>
}