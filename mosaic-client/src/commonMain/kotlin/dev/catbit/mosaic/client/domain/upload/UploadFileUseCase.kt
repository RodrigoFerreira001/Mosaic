package dev.catbit.mosaic.client.domain.upload

import dev.catbit.mosaic.client.data.data_sources.network.UploadResult
import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.github.vinceglb.filekit.PlatformFile
import io.ktor.http.HttpMethod

/**
 * Uploads a [PlatformFile], reporting progress as it goes — backs the `UploadFile` event, meant to
 * be paired with `OpenFilePicker`/`GetFile`'s `PlatformFile` output. Reachable via
 * `get<UploadFileUseCase>()`.
 */
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

    /**
     * @property url endpoint — nullable so it can instead be staged beforehand via
     * `SetIncomingDataToNetworkParamsHolderUrl` and resolved from `NetworkParametersHolder`.
     * @property headers request headers.
     * @property httpMethod HTTP method.
     * @property contentType content type of the uploaded file.
     * @property platformFile the file to upload.
     * @property onProgress called repeatedly with upload progress (0f–1f).
     */
    class Params(
        val url: String?,
        val headers: Map<String, String>?,
        val httpMethod: HttpMethod,
        val contentType: String?,
        val platformFile: PlatformFile,
        val onProgress: suspend (Float) -> Unit = {}
    )
}
