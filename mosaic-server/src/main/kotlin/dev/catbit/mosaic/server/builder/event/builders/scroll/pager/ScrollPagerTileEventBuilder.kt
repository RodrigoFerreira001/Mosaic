package dev.catbit.mosaic.server.builder.event.builders.scroll.pager

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ScrollPagerTileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String,
    private val where: ScrollPagerTileEventSchema.Where,
    private val smoothly: Boolean
) : EventSchemaBuilder<ScrollPagerTileEventSchema>() {

    override fun build() = ScrollPagerTileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId,
        where = where,
        smoothly = smoothly
    )
}

fun EventSchemaBuilderScope.ScrollPager(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollPagerTileEventSchema.Where,
    smoothly: Boolean = true,
) {
    addBuilder(
        ScrollPagerTileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId,
            where = where,
            smoothly = smoothly
        )
    )
}

fun scrollPageToBegin() = ScrollPagerTileEventSchema.Where.Begin
fun scrollPageToPreviousPage() = ScrollPagerTileEventSchema.Where.PreviousPage
fun scrollPageToNextPage() = ScrollPagerTileEventSchema.Where.NextPage
fun scrollPageToEnd() = ScrollPagerTileEventSchema.Where.End
