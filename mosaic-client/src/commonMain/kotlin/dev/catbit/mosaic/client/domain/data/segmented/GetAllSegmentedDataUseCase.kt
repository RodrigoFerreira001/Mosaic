package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetAllSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetAllSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getAllSegmentedData(
            segmentKey = segmentKey
        )
    }

    data class Params(
        val segmentKey: String
    )
}
