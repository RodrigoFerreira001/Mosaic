package dev.catbit.mosaic.client.data.repository

import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MosaicRepository {

    suspend fun getInitialGraph(): Result<GraphModel>

    suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?
    ): Result<ScreenModel>

    suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<HttpResponse>

    suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit = {},
        onBytesReceived: (ByteArray) -> Unit = {},
        onDownloadFinished: (ByteArray) -> Unit = {},
        onDownloadFailure: (Throwable) -> Unit = {}
    ): Result<Unit>
}