package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class SaveFileUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, SaveFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.saveFile(
            fileName = fileName,
            data = data
        )
    }

    data class Params(
        val fileName: String,
        val data: ByteArray
    )
}
