package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import io.github.vinceglb.filekit.PlatformFile
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
        onProgress: suspend (Float) -> Unit = {},
        onDownloadFinished: suspend (ByteArray) -> Unit = {},
        onDownloadFailure: suspend (Throwable) -> Unit = {}
    ): Result<Unit>

    /** Streams the response body straight to [targetFileName], without ever holding the full file in memory. */
    suspend fun downloadFileToDisk(
        url: String,
        headers: Map<String, String>? = null,
        body: Any?,
        httpMethod: HttpMethod,
        targetFileName: String,
        onProgress: suspend (Float) -> Unit = {},
        onDownloadFinished: suspend () -> Unit = {},
        onDownloadFailure: suspend (Throwable) -> Unit = {}
    ): Result<Unit>

    suspend fun uploadFile(
        url: String?,
        headers: Map<String, String>? = null,
        httpMethod: HttpMethod,
        contentType: String?,
        platformFile: PlatformFile,
        onProgress: suspend (Float) -> Unit = {}
    ): Result<UploadResult>
}