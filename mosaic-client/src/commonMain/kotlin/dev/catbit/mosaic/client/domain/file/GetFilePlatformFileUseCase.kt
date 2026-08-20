package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.github.vinceglb.filekit.PlatformFile

/**
 * Reads a file from the client's own private file storage as a [PlatformFile] handle — backs
 * `GetFile`'s `platformFile()` output type. Reachable via `get<GetFilePlatformFileUseCase>()`.
 */
class GetFilePlatformFileUseCase(
    private val repository: MosaicRepository
) : UseCase<PlatformFile?, GetFilePlatformFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFilePlatformFile(fileName = fileName)
    }

    /** @property fileName file to read. */
    data class Params(
        val fileName: String
    )
}
