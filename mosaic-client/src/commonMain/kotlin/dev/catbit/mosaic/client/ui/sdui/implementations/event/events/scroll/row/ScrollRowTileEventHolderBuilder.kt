package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema

object ScrollRowTileEventHolderBuilder : EventHolderBuilder<ScrollRowTileEventSchema, ScrollRowTileEventHolder> {
    override fun BuilderScope.build(eventSchema: ScrollRowTileEventSchema): ScrollRowTileEventHolder =
        with(eventSchema) {
            ScrollRowTileEventHolder(
                id = id,
                event = eventSchema,
                trigger = trigger,
                events = events?.map { eventModel -> buildEventHolder(eventModel) }
            )
        }
}
