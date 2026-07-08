package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class DeleteFileUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DeleteFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.deleteFile(fileName = fileName)
    }

    data class Params(
        val fileName: String
    )
}
