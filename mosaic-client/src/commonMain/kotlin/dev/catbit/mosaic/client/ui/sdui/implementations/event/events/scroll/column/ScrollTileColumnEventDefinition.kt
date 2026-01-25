package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollColumnTileEventModel

object ScrollTileColumnEventDefinition : EventDefinition<ScrollColumnTileEventModel> {
    override val eventModelClass = ScrollColumnTileEventModel::class
    override val eventRunner = ScrollTileColumnEventRunner
    override val eventHolderBuilder = ScrollTileColumnEventHolderBuilder
}