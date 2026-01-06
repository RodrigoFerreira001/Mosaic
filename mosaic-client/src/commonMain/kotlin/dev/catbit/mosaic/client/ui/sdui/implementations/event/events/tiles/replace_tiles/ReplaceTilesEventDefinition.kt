package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.tiles.ReplaceTilesEventModel

object ReplaceTilesEventDefinition : EventDefinition<ReplaceTilesEventModel> {
    override val eventModelClass = ReplaceTilesEventModel::class
    override val eventRunner = ReplaceTilesEventRunner
}