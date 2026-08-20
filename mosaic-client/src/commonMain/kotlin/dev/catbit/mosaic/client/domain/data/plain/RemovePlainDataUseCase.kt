package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Deletes a single entry from the persistent flat (`plainDataBase()`) local database — backs
 * `RemoveData` targeting `plainDataBase()` with a single key. Reachable via
 * `get<RemovePlainDataUseCase>()`.
 */
class RemovePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemovePlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deletePlainData(
            dataKey = dataKey
        )
    }

    /** @property dataKey id of the entry to delete. */
    data class Params(
        val dataKey: String
    )
}
