package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel

object ScrollTileColumnEventDefinition : EventDefinition<ScrollTileColumnEventModel> {
    override val eventModelClass = ScrollTileColumnEventModel::class
    override val eventRunner = ScrollTileColumnEventRunner
}