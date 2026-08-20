package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Clears every entry from the persistent flat (`plainDataBase()`) local database — backs
 * `RemoveData`'s `fullAccessMode()` over `plainDataBase()`. Reachable via `get<WipePlainDataUseCase>()`.
 */
class WipePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) = repository.wipePlainData()
}
