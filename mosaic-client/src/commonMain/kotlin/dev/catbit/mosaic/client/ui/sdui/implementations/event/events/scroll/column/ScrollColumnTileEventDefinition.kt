package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema

object ScrollColumnTileEventDefinition : EventDefinition<ScrollColumnTileEventSchema> {
    override val eventSchemaClass = ScrollColumnTileEventSchema::class
    override val eventRunner = ScrollColumnTileEventRunner
    override val eventHolderBuilder = ScrollColumnTileEventHolderBuilder
}