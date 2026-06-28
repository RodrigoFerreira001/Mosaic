package dev.catbit.mosaic.client.domain.upload

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class UploadFileUseCase(
    private val repository: MosaicRepository
) : UseCase<HttpResponse, UploadFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.uploadFile(
            url = url,
            headers = headers,
            httpMethod = httpMethod,
            contentType = contentType,
            bytes = bytes,
            onProgress = onProgress
        )
    }

    class Params(
        val url: String?,
        val headers: Map<String, String>?,
        val httpMethod: HttpMethod,
        val contentType: String?,
        val bytes: ByteArray,
        val onProgress: suspend (Int) -> Unit = {}
    )
}
