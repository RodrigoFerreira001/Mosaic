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

/**
 * Moves the `Pager` or `Carousel` tile identified by [tileId] to [where], by broadcasting a
 * scroll command on the screen channel. [smoothly] chooses between an animated and an immediate
 * jump; the receiving tile clamps the result, so asking for the next page on the last one is a
 * no-op. Does not consume `incomingData`. Dispatches `onSuccess` (no data) always, right after
 * the command is broadcast — the broadcast is fire-and-forget, so this fires even when no tile
 * carries [tileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 * @param tileId Id of the `Pager`/`Carousel` tile to move.
 * @param where Page target — [scrollPageToBegin], [scrollPageToPreviousPage], [scrollPageToNextPage] or [scrollPageToEnd].
 * @param smoothly Whether the move animates instead of jumping immediately. Defaults to true.
 */
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

/** Moves to the first page. */
fun scrollPageToBegin() = ScrollPagerTileEventSchema.Where.Begin

/** Moves to the previous page — a no-op if already on the first page. */
fun scrollPageToPreviousPage() = ScrollPagerTileEventSchema.Where.PreviousPage

/** Moves to the next page — a no-op if already on the last page. */
fun scrollPageToNextPage() = ScrollPagerTileEventSchema.Where.NextPage

/** Moves to the last page. */
fun scrollPageToEnd() = ScrollPagerTileEventSchema.Where.End
