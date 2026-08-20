package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Deletes several entries at once within one namespace of the persistent segmented
 * (`segmentedDataBase(segmentId)`) local database — backs `RemoveData` targeting
 * `segmentedDataBase(segmentId)` with a batch of keys. Reachable via
 * `get<RemoveSegmentedDataByIdsUseCase>()`.
 */
class RemoveSegmentedDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, RemoveSegmentedDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deleteSegmentedDataByIds(
            segmentKey = segmentKey,
            dataKeys = dataKeys
        )
    }

    /**
     * @property segmentKey namespace to delete from.
     * @property dataKeys ids of the entries to delete within [segmentKey].
     */
    data class Params(
        val segmentKey: String,
        val dataKeys: List<String>
    )
}
