package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.tiles.RemoveTilesEventModel

object RemoveTilesEventDefinition : EventDefinition<RemoveTilesEventModel> {
    override val eventModelClass = RemoveTilesEventModel::class
    override val eventRunner = RemoveTilesEventRunner
    override val eventHolderBuilder = RemoveTilesEventHolderBuilder
}