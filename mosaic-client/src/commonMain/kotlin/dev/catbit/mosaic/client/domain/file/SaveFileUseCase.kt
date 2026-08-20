package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Writes bytes to the client's own private file storage under a given name — backs the `SaveFile`
 * event. Reachable via `get<SaveFileUseCase>()`.
 */
class SaveFileUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, SaveFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.saveFile(
            fileName = fileName,
            data = data
        )
    }

    /**
     * @property fileName file to write.
     * @property data content to write.
     */
    data class Params(
        val fileName: String,
        val data: ByteArray
    )
}
