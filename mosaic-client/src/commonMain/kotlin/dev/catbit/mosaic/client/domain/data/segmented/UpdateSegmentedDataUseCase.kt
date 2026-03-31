package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class UpdateSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, UpdateSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.saveSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey,
            data = data
        )
    }

    data class Params(
        val segmentKey: String,
        val dataKey: String,
        val data: Any
    )
}
