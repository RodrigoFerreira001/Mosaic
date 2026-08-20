package dev.catbit.mosaic.client.domain.data.plain

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Writes a single entry into the persistent flat (`plainDataBase()`) local database — backs
 * `UpdateData` targeting `plainDataBase()`. Reachable via `get<UpdatePlainDataUseCase>()`.
 */
class UpdatePlainDataUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, UpdatePlainDataUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.savePlainData(
            dataKey = dataKey,
            data = data
        )
    }

    /**
     * @property dataKey id to write under.
     * @property data value to write.
     */
    data class Params(
        val dataKey: String,
        val data: Any
    )
}
