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
            httpMethod = httpMethod,
            onProgress = onProgress,
            onBytesReceived = onBytesReceived,
            onDownloadFinished = onDownloadFinished,
            onDownloadFailure = onDownloadFailure,
        )
    }

    data class Params(
        val url: String,
        val headers: Map<String, String>? = null,
        val httpMethod: HttpMethod,
        val onProgress: (Int) -> Unit = {},
        val onBytesReceived: (ByteArray) -> Unit = {},
        val onDownloadFinished: (ByteArray) -> Unit = {},
        val onDownloadFailure: (Throwable) -> Unit = {}
    )
}