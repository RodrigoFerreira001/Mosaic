package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Any, GetSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        )
    }

    data class Params(
        val segmentKey: String,
        val dataKey: String
    )
}
