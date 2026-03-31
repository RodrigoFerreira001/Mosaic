package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetPlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Any, GetPlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getPlainData(
            dataKey = dataKey
        )
    }

    data class Params(
        val dataKey: String
    )
}
