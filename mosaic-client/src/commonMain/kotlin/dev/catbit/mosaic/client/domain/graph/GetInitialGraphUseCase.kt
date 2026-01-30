package dev.catbit.mosaic.client.domain.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.domain.base.UseCase

class GetInitialGraphUseCase : UseCase<GraphResponse, Unit>() {

    override suspend fun execute(params: Unit): Result<GraphResponse> {
        return Result.success(
            GraphResponse(
                entries = listOf(
                    GraphResponse.Entry(
                        screenId = "home",
                        loadingTiles = listOf(),
                        errorTiles = listOf()
                    ),
                    GraphResponse.Entry(
                        screenId = "second",
                        loadingTiles = listOf(),
                        errorTiles = listOf()
                    )
                ),
                startEntryId = "home"
            )
        )
    }
}