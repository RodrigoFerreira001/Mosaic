package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads every entry within one namespace of the persistent segmented (`segmentedDataBase(segmentId)`)
 * local database — backs `GetData`'s `fullAccessMode()` over `segmentedDataBase(segmentId)`.
 * Reachable via `get<GetAllSegmentedDataUseCase>()`.
 */
class GetAllSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetAllSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getAllSegmentedData(
            segmentKey = segmentKey
        )
    }

    /** @property segmentKey namespace to read every entry from. */
    data class Params(
        val segmentKey: String
    )
}
