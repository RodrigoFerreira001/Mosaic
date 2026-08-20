package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Deletes several entries at once from the persistent flat (`plainDataBase()`) local database — backs
 * `RemoveData` targeting `plainDataBase()` with a batch of keys. Reachable via
 * `get<RemovePlainDataByIdsUseCase>()`.
 */
class RemovePlainDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemovePlainDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deletePlainDataByIds(
            dataKeys = dataKeys
        )
    }

    /** @property dataKeys ids of the entries to delete. */
    data class Params(
        val dataKeys: List<String>
    )
}
