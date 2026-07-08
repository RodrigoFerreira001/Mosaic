package dev.catbit.mosaic.client.data.data_sources.network

import io.github.vinceglb.filekit.PlatformFile
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

class UploadResult(
    val statusCode: Int,
    val contentType: String?,
    val body: ByteArray
) {
    val isSuccess: Boolean get() = statusCode in 200..299
}

internal expect suspend fun uploadPlatformFile(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    httpMethod: HttpMethod,
    platformFile: PlatformFile,
    contentType: String?,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit
): UploadResult
