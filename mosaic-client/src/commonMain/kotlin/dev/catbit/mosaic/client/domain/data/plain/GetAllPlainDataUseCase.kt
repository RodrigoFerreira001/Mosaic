package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetAllPlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, Unit>() {

    override suspend fun execute(params: Unit) = repository.getAllPlainData()
}
