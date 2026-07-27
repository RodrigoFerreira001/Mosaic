package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

internal expect suspend fun downloadPlatformFileToMemory(
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

/**
 * Downloads a file into the device's public/general storage (system Downloads location),
 * visible in the OS file manager — unlike [downloadPlatformFileToDisk], which targets the app's
 * private sandbox. Behavior is inherently platform-specific:
 * - Android: delegated entirely to [android.app.DownloadManager] (does not use [httpClient]).
 * - iOS: fetches [url] via [httpClient] into memory, then presents `FileKit.openFileSaver()`.
 * - JVM: streams via [httpClient] straight into the user's `~/Downloads` folder.
 * - wasmJs: fetches [url] into memory, then triggers the browser's native download via
 *   `FileKit.download(...)`.
 */
internal expect suspend fun downloadPlatformFileToPublicStorage(
    httpClient: HttpClient,
    url: String,
    headers: Map<String, String>,
    body: String?,
    httpMethod: HttpMethod,
    queryParameters: Map<String, Any?>?,
    targetFileName: String,
    mimeType: String?,
    onProgress: suspend (Float) -> Unit,
    onDownloadFinished: suspend () -> Unit
)
