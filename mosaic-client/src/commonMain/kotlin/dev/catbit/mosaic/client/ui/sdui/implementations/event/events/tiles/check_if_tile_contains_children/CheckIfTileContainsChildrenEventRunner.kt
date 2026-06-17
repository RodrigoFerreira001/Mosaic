package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.check_if_tile_contains_children

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object CheckIfTileContainsChildrenEventRunner : EventRunner<CheckIfTileContainsChildrenEventSchema> {

    override suspend fun EventRunningScope.runEvent(
        event: CheckIfTileContainsChildrenEventSchema
    ) {
        with(event) {
            val hasChildren = tilesEditor.checkIfTileHasChildren(
                groupingTileId = groupingTileId,
                childrenIds = childrenIds
            )

            onTrigger(if (hasChildren) EventTriggers.onSuccess() else EventTriggers.onFailure())
        }
    }
}
