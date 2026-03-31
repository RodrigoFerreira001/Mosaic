package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class WipeSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, WipeSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.wipeSegmentedData(
            segmentKey = segmentKey
        )
    }

    data class Params(
        val segmentKey: String
    )
}
