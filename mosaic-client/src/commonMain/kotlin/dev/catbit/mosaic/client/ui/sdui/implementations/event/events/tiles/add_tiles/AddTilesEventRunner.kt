package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object AddTilesEventRunner : EventRunner<AddTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: AddTilesEventSchema) {
        with(event) {
            tilesEditor.addTiles(
                tileSchemas = tiles,
                groupingTileId = groupingTileId,
                where = position.toPosition()
            ).onFailure { throwable ->
                onTrigger(EventTriggers.onFailure(), data = throwable)
                logError(tag = "AddTilesEventRunner", throwable = throwable)
            }.onSuccess {
                onTrigger(EventTriggers.onSuccess())
            }
        }
    }

    private fun AddTilesEventSchema.InsertionPosition.toPosition() = when (this) {
        is AddTilesEventSchema.InsertionPosition.AfterTile -> InsertionPosition.AfterTile(tileId)
        is AddTilesEventSchema.InsertionPosition.AtIndex -> InsertionPosition.AtIndex(index)
        is AddTilesEventSchema.InsertionPosition.BeforeTile -> InsertionPosition.BeforeTile(tileId)
        AddTilesEventSchema.InsertionPosition.Start -> InsertionPosition.Start
        AddTilesEventSchema.InsertionPosition.End -> InsertionPosition.End
    }
}