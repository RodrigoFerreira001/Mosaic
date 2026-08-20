package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Reads a single entry within one namespace of the persistent segmented
 * (`segmentedDataBase(segmentId)`) local database — backs `GetData`'s `singleAccessMode(dataKey)`
 * over `segmentedDataBase(segmentId)`. Reachable via `get<GetSegmentedDataUseCase>()`.
 */
class GetSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Any, GetSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        )
    }

    /**
     * @property segmentKey namespace to read from.
     * @property dataKey id of the entry to read within [segmentKey].
     */
    data class Params(
        val segmentKey: String,
        val dataKey: String
    )
}
