package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema

object ScrollColumnTileEventHolderBuilder :
    EventHolderBuilder<ScrollColumnTileEventSchema, ScrollColumnTileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ScrollColumnTileEventSchema
    ) = with(eventSchema) {
        ScrollColumnTileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
