package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Clears every entry within one namespace of the persistent segmented (`segmentedDataBase(segmentId)`)
 * local database — backs `RemoveData`'s `fullAccessMode()` over `segmentedDataBase(segmentId)`.
 * Reachable via `get<WipeSegmentedDataUseCase>()`.
 */
class WipeSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, WipeSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.wipeSegmentedData(
            segmentKey = segmentKey
        )
    }

    /** @property segmentKey namespace to clear entirely. */
    data class Params(
        val segmentKey: String
    )
}
