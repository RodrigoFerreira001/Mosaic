package dev.catbit.mosaic.client.domain.download

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

class DownloadFileUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DownloadFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.downloadFile(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
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
        val onProgress: suspend (Float) -> Unit = {},
        val onDownloadFinished: suspend (ByteArray) -> Unit = {},
        val onDownloadFailure: suspend (Throwable) -> Unit = {}
    )
}