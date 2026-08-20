package dev.catbit.mosaic.client.domain.download

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

/**
 * Downloads a file entirely into memory, never touching the filesystem — backs
 * `DownloadFileToMemory`. Reachable via `get<DownloadFileToMemoryUseCase>()`.
 */
class DownloadFileToMemoryUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DownloadFileToMemoryUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.downloadFileToMemory(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
            onProgress = onProgress,
            onDownloadFinished = onDownloadFinished,
            onDownloadFailure = onDownloadFailure
        )
    }

    /**
     * @property url endpoint to download from.
     * @property headers request headers.
     * @property body request body.
     * @property httpMethod HTTP method.
     * @property onProgress called repeatedly with download progress (0f–1f).
     * @property onDownloadFinished called once the download completes, with the full content.
     * @property onDownloadFailure called if the download fails, with the causing throwable.
     */
    data class Params(
        val url: String,
        val headers: Map<String, String>?,
        val body: Any?,
        val httpMethod: HttpMethod,
        val onProgress: suspend (Float) -> Unit = {},
        val onDownloadFinished: suspend (ByteArray) -> Unit = {},
        val onDownloadFailure: suspend (Throwable) -> Unit = {}
    )
}
