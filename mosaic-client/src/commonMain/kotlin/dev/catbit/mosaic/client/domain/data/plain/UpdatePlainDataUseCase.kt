package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class UpdatePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, UpdatePlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.savePlainData(
            dataKey = dataKey,
            data = data
        )
    }

    data class Params(
        val dataKey: String,
        val data: Any
    )
}
