package dev.catbit.mosaic.client.domain.data.segmented

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Writes a single entry within one namespace of the persistent segmented
 * (`segmentedDataBase(segmentId)`) local database — backs `UpdateData` targeting
 * `segmentedDataBase(segmentId)`. Reachable via `get<UpdateSegmentedDataUseCase>()`.
 */
class UpdateSegmentedDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, UpdateSegmentedDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.saveSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey,
            data = data
        )
    }

    /**
     * @property segmentKey namespace to write into.
     * @property dataKey id to write under, within [segmentKey].
     * @property data value to write.
     */
    data class Params(
        val segmentKey: String,
        val dataKey: String,
        val data: Any
    )
}
