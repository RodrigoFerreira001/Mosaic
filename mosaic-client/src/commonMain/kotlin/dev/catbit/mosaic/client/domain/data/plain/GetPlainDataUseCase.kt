package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads a single entry from the persistent flat (`plainDataBase()`) local database — backs `GetData`'s
 * `singleAccessMode(dataKey)` over `plainDataBase()`. Reachable via `get<GetPlainDataUseCase>()`.
 */
class GetPlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Any, GetPlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getPlainData(
            dataKey = dataKey
        )
    }

    /** @property dataKey id of the entry to read. */
    data class Params(
        val dataKey: String
    )
}
