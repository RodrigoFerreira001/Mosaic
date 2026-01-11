package dev.catbit.mosaic.client.domain

import dev.catbit.mosaic.client.domain.base.UseCase
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel
import dev.catbit.mosaic.core.data.graph.GraphModel
import dev.catbit.mosaic.core.data.screen.ScreenModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.data.event_trigger.EventTriggers

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