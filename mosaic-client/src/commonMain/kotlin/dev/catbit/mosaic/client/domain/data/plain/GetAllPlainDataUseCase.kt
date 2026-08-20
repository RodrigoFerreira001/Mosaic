package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads every entry from the persistent flat (`plainDataBase()`) local database — backs `GetData`'s
 * `fullAccessMode()` over `plainDataBase()`. Reachable via `get<GetAllPlainDataUseCase>()`.
 */
class GetAllPlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, Unit>() {

    override suspend fun execute(params: Unit) = repository.getAllPlainData()
}
