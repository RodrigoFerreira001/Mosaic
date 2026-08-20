package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Deletes a single entry within one namespace of the persistent segmented
 * (`segmentedDataBase(segmentId)`) local database — backs `RemoveData` targeting
 * `segmentedDataBase(segmentId)` with a single key. Reachable via `get<RemoveSegmentedDataUseCase>()`.
 */
class RemoveSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemoveSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deleteSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        )
    }

    /**
     * @property segmentKey namespace to delete from.
     * @property dataKey id of the entry to delete within [segmentKey].
     */
    data class Params(
        val segmentKey: String,
        val dataKey: String
    )
}
