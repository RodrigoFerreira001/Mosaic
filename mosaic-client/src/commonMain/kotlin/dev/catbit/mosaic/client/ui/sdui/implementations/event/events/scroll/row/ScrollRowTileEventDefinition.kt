package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema

object ScrollRowTileEventDefinition : EventDefinition<ScrollRowTileEventSchema> {
    override val eventSchemaClass = ScrollRowTileEventSchema::class
    override val eventRunner = ScrollRowTileEventRunner
    override val eventHolderBuilder = ScrollRowTileEventHolderBuilder
}
