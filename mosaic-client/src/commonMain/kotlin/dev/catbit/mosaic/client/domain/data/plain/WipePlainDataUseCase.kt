package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class WipePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) = repository.wipePlainData()
}
