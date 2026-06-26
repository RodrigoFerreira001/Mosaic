package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema

object ScrollTilePagerEventHolderBuilder :
    EventHolderBuilder<ScrollPagerTileEventSchema, ScrollTilePagerEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ScrollPagerTileEventSchema
    ) = with(eventSchema) {
        ScrollTilePagerEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
