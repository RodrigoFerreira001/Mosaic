package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetSegmentedDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetSegmentedDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getSegmentedDataByIds(
            segmentKey = segmentKey,
            dataKeys = dataKeys
        )
    }

    data class Params(
        val segmentKey: String,
        val dataKeys: List<String>
    )
}
