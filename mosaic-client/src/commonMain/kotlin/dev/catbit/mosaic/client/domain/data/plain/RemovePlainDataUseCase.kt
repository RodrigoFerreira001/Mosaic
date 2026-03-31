package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class RemovePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemovePlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deletePlainData(
            dataKey = dataKey
        )
    }

    data class Params(
        val dataKey: String
    )
}
