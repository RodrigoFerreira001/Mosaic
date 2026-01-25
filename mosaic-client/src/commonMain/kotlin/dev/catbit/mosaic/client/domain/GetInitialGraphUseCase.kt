package dev.catbit.mosaic.client.domain

import dev.catbit.mosaic.client.domain.base.UseCase
import dev.catbit.mosaic.core.data.graph.GraphModel

class GetInitialGraphUseCase : UseCase<GraphModel, Unit>() {

    override suspend fun execute(params: Unit): Result<GraphModel> {
        return Result.success(
            GraphModel(
                id = "root",
                entries = listOf(
                    GraphModel.Entry(
                        screenId = "home",
                        loadingTiles = listOf(),
                        errorTiles = listOf()
                    ),
                    GraphModel.Entry(
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