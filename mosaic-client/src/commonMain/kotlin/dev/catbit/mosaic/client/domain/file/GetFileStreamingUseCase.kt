package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetFileStreamingUseCase(
    private val repository: MosaicRepository
) : UseCase<Flow<ByteArray>?, GetFileStreamingUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getFileStreaming(fileName = fileName)
    }

    data class Params(
        val fileName: String
    )
}
