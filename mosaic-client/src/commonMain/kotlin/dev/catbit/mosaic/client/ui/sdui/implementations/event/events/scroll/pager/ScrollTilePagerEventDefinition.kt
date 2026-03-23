package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema

object ScrollTilePagerEventDefinition : EventDefinition<ScrollPagerTileEventSchema> {
    override val eventSchemaClass = ScrollPagerTileEventSchema::class
    override val eventRunner = ScrollTilePagerEventRunner
    override val eventHolderBuilder = ScrollTilePagerEventHolderBuilder
}