package dev.catbit.mosaic.client.domain.version

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class CheckCacheVersionUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) = repository.getVersion()
}
