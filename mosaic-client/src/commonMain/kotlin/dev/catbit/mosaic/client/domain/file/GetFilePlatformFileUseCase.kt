package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.github.vinceglb.filekit.PlatformFile

class GetFilePlatformFileUseCase(
    private val repository: MosaicRepository
) : UseCase<PlatformFile?, GetFilePlatformFileUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFilePlatformFile(fileName = fileName)
    }

    data class Params(
        val fileName: String
    )
}
