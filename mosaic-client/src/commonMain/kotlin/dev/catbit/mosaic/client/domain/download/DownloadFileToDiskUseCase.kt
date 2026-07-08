package dev.catbit.mosaic.client.domain.download

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

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
