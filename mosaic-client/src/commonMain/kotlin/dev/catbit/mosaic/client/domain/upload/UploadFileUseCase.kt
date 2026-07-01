package dev.catbit.mosaic.client.domain.upload

import dev.catbit.mosaic.client.data.data_sources.network.UploadResult
import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.github.vinceglb.filekit.PlatformFile
import io.ktor.http.HttpMethod

class UploadFileUseCase(
    private val repository: MosaicRepository
) : UseCase<UploadResult, UploadFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.uploadFile(
            url = url,
            headers = headers,
            httpMethod = httpMethod,
            contentType = contentType,
            platformFile = platformFile,
            onProgress = onProgress
        )
    }

    class Params(
        val url: String?,
        val headers: Map<String, String>?,
        val httpMethod: HttpMethod,
        val contentType: String?,
        val platformFile: PlatformFile,
        val onProgress: suspend (Int) -> Unit = {}
    )
}
