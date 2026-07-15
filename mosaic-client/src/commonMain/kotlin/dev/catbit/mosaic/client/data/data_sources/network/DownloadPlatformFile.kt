package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

internal expect suspend fun downloadPlatformFile(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend (ByteArray) -> Unit
)

internal expect suspend fun downloadPlatformFileToDisk(
    httpClient: HttpClient,
    fileSystem: MosaicFileSystem,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    targetFileName: String,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend () -> Unit
)
