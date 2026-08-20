package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads a file from the client's own private file storage as raw bytes — backs `GetFile`'s
 * `arrayOfBytes()` output type (its default). Reachable via `get<GetFileUseCase>()`.
 */
class GetFileUseCase(
    private val repository: MosaicRepository
) : UseCase<ByteArray?, GetFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFile(fileName = fileName)
    }

    /** @property fileName file to read. */
    data class Params(
        val fileName: String
    )
}
