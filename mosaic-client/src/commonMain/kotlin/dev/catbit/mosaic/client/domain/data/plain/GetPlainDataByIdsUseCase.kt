package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetPlainDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetPlainDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getPlainDataByIds(
            dataKeys = dataKeys
        )
    }

    data class Params(
        val dataKeys: List<String>
    )
}
