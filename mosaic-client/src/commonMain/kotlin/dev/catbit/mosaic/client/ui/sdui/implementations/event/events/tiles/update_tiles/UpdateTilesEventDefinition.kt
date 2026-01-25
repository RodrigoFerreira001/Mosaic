package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.tiles.UpdateTilesEventModel

object UpdateTilesEventDefinition : EventDefinition<UpdateTilesEventModel> {
    override val eventModelClass = UpdateTilesEventModel::class
    override val eventRunner = UpdateTilesEventRunner
    override val eventHolderBuilder = UpdateTilesEventHolderBuilder
}
