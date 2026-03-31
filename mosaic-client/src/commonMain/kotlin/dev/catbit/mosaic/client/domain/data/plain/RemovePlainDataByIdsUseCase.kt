package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class RemovePlainDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemovePlainDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deletePlainDataByIds(
            dataKeys = dataKeys
        )
    }

    data class Params(
        val dataKeys: List<String>
    )
}
