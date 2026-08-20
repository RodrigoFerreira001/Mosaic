package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * Reads a file from the client's own private file storage as a chunked [Flow], without loading it
 * fully into memory — backs `GetFile`'s `flowOfBytes()` output type, meant for large files. Reachable
 * via `get<GetFileStreamingUseCase>()`.
 */
class GetFileStreamingUseCase(
    private val repository: MosaicRepository
) : UseCase<Flow<ByteArray>?, GetFileStreamingUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFileStreaming(fileName = fileName)
    }

    /** @property fileName file to read. */
    data class Params(
        val fileName: String
    )
}
