package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads several entries at once within one namespace of the persistent segmented
 * (`segmentedDataBase(segmentId)`) local database — backs `GetData`'s `batchAccessMode(...)` over
 * `segmentedDataBase(segmentId)`. Reachable via `get<GetSegmentedDataByIdsUseCase>()`.
 */
class GetSegmentedDataByIdsUseCase(
    private val repository: MosaicRepository
) : UseCase<Map<String, Any>, GetSegmentedDataByIdsUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getSegmentedDataByIds(
            segmentKey = segmentKey,
            dataKeys = dataKeys
        )
    }

    /**
     * @property segmentKey namespace to read from.
     * @property dataKeys ids of the entries to read within [segmentKey].
     */
    data class Params(
        val segmentKey: String,
        val dataKeys: List<String>
    )
}
