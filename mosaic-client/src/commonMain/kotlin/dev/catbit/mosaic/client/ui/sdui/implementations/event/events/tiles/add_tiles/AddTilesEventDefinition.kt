package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel

object AddTilesEventDefinition : EventDefinition<AddTilesEventModel> {
    override val eventModelClass = AddTilesEventModel::class
    override val eventRunner = AddTilesEventRunner
}