package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class RemoveSegmentedDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemoveSegmentedDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deleteSegmentedDataByIds(
            segmentKey = segmentKey,
            dataKeys = dataKeys
        )
    }

    data class Params(
        val segmentKey: String,
        val dataKeys: List<String>
    )
}
