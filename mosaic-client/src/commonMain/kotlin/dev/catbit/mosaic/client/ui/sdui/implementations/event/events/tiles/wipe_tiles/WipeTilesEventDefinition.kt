package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.tiles.WipeTilesEventModel

object WipeTilesEventDefinition : EventDefinition<WipeTilesEventModel> {
    override val eventModelClass = WipeTilesEventModel::class
    override val eventRunner = WipeTilesEventRunner
}