package dev.catbit.mosaic.client.domain.screen

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import dev.catbit.mosaic.core.domain.base.UseCase

class GetScreenUseCase(
    private val repository: MosaicRepository
) : UseCase<ScreenModel, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getScreen(
            screenId = screenId,
            headers = headers
        )
    }

    data class Params(
        val screenId: String,
        val headers: Map<String, String>?
    )
}