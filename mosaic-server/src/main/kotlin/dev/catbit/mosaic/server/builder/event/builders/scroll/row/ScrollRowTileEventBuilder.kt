package dev.catbit.mosaic.server.builder.event.builders.scroll.row

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ScrollRowTileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String,
    private val where: ScrollRowTileEventSchema.Where,
    private val smoothly: Boolean
) : EventSchemaBuilder<ScrollRowTileEventSchema>() {

    override fun build() = ScrollRowTileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId,
        where = where,
        smoothly = smoothly
    )
}

/**
 * Scrolls the `Row` or `LazyRow` tile identified by [tileId] to [where], by broadcasting a
 * scroll command on the screen channel. [smoothly] chooses between an animated and an immediate
 * jump. Does not consume `incomingData`. Dispatches `onSuccess` (no data) always, right after the
 * command is broadcast — the broadcast is fire-and-forget, so this fires even when no tile
 * carries [tileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 * @param tileId Id of the `Row`/`LazyRow` tile to scroll.
 * @param where Scroll target — [scrollRowToStart], [scrollRowToEnd] or [scrollRowTo] (pixel offset in a `Row`, child index in a `LazyRow`).
 * @param smoothly Whether the scroll animates instead of jumping immediately. Defaults to true.
 */
fun EventSchemaBuilderScope.ScrollRow(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollRowTileEventSchema.Where,
    smoothly: Boolean = true
) {
    addBuilder(
        ScrollRowTileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId,
            where = where,
            smoothly = smoothly
        )
    )
}

/** Scrolls to the start (mirrored under RTL). */
fun scrollRowToStart() = ScrollRowTileEventSchema.Where.Start

/** Scrolls to an explicit position — a pixel offset in a `Row`, a child index in a `LazyRow`. */
fun scrollRowTo(index: Int) = ScrollRowTileEventSchema.Where.To(index)

/** Scrolls to the end (mirrored under RTL). */
fun scrollRowToEnd() = ScrollRowTileEventSchema.Where.End
