package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.get_tile_children_count

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.GetTileChildrenCountEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object GetTileChildrenCountEventRunner : EventRunner<GetTileChildrenCountEventSchema> {

    override suspend fun EventRunningScope.runEvent(
        event: GetTileChildrenCountEventSchema
    ) {
        with(event) {
            tilesEditor.getTileChildrenCount(groupingTileId)?.let { count ->
                onTrigger(
                    eventTrigger = EventTriggers.onSuccess(),
                    data = count
                )
            } ?: run {
                onTrigger(EventTriggers.onFailure())
            }
        }
    }
}
