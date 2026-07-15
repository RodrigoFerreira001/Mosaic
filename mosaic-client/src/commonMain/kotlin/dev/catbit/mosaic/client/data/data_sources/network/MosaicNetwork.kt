package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.responses.version.VersionResponse
import io.github.vinceglb.filekit.PlatformFile
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MosaicNetwork {

    suspend fun getInitialGraph(): Result<GraphResponse>

    /** Short-timeout version check meant to run before [getInitialGraph] at startup without blocking it. */
    suspend fun getVersion(): Result<VersionResponse>

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

    /**
     * Probes whether the device currently has an active internet connection by issuing a
     * lightweight GET to a captive-portal-style endpoint. Any exception (no connection, DNS
     * failure, timeout) is treated as "no connection" rather than propagated.
     */
    suspend fun hasInternetConnection(): Boolean
}