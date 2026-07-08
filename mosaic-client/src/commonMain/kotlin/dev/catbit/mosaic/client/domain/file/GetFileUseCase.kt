package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

class GetFileUseCase(
    private val repository: MosaicRepository
) : UseCase<ByteArray?, GetFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFile(fileName = fileName)
    }

    data class Params(
        val fileName: String
    )
}
