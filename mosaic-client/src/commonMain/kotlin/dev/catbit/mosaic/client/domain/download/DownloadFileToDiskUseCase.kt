package dev.catbit.mosaic.client.domain.download

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

/**
 * Downloads a file into the client's own private file storage — backs `DownloadFileToDisk`, whose
 * result is afterward reachable via [GetFileUseCase]/[GetFilePlatformFileUseCase]/[DeleteFileUseCase].
 * Reachable via `get<DownloadFileToDiskUseCase>()`.
 */
class DownloadFileToDiskUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DownloadFileToDiskUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.downloadFileToDisk(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
            targetFileName = targetFileName,
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
     * @property targetFileName name the file is saved under, in the app's private storage.
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
        val onProgress: suspend (Float) -> Unit = {},
        val onDownloadFinished: suspend () -> Unit = {},
        val onDownloadFailure: suspend (Throwable) -> Unit = {}
    )
}
