package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class RemoveSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemoveSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deleteSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        )
    }

    data class Params(
        val segmentKey: String,
        val dataKey: String
    )
}
