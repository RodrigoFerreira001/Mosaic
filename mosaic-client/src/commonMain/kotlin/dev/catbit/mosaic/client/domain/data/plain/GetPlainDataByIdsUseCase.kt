package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads several entries at once from the persistent flat (`plainDataBase()`) local database — backs
 * `GetData`'s `batchAccessMode(...)` over `plainDataBase()`. Reachable via
 * `get<GetPlainDataByIdsUseCase>()`.
 */
class GetPlainDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetPlainDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getPlainDataByIds(
            dataKeys = dataKeys
        )
    }

    /** @property dataKeys ids of the entries to read. */
    data class Params(
        val dataKeys: List<String>
    )
}
