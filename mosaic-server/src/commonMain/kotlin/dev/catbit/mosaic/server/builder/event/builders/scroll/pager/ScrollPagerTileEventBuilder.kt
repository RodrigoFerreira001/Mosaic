package dev.catbit.mosaic.server.builder.event.builders.scroll.pager

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ScrollPagerTileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String,
    private val where: ScrollPagerTileEventSchema.Where,
) : EventSchemaBuilder<ScrollPagerTileEventSchema>() {

    override fun build() = ScrollPagerTileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId,
        where = where
    )
}

fun EventSchemaBuilderScope.ScrollPager(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollPagerTileEventSchema.Where,
) {
    addBuilder(
        ScrollPagerTileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId,
            where = where
        )
    )
}
