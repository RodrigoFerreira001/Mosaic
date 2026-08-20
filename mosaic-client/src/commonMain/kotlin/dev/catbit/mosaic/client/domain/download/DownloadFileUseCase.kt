package dev.catbit.mosaic.client.domain.download

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

/**
 * Downloads a file into the device's public/general storage (system Downloads location) — backs
 * `DownloadFile`, the only one of the 3 download events whose destination the user can interactively
 * cancel (reflected in `DownloadFile`'s own `OnCancelled` trigger, handled above this use case).
 * Reachable via `get<DownloadFileUseCase>()`.
 */
class DownloadFileUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DownloadFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.downloadFile(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
            targetFileName = targetFileName,
            mimeType = mimeType,
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
     * @property targetFileName name the downloaded file is saved under.
     * @property mimeType content type describing the saved file.
     * @property onProgress called repeatedly with download progress (0f–1f).
     * @property onDownloadFinished called once the file is fully written.
     * @property onDownloadFailure called if the download fails, with the causing throwable.
     */
    data class Params(
        val url: String,
        val headers: Map<String, String>?,
        val body: Any?,
        val httpMethod: HttpMethod,
        val targetFileName: String,
        val mimeType: String? = null,
        val onProgress: suspend (Float) -> Unit = {},
        val onDownloadFinished: suspend () -> Unit = {},
        val onDownloadFailure: suspend (Throwable) -> Unit = {}
    )
}
