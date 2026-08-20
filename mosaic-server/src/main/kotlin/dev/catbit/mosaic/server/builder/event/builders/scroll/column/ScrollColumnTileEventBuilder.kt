package dev.catbit.mosaic.server.builder.event.builders.scroll.column

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ScrollColumnTileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String,
    private val where: ScrollColumnTileEventSchema.Where,
    private val smoothly: Boolean
) : EventSchemaBuilder<ScrollColumnTileEventSchema>() {

    override fun build() = ScrollColumnTileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId,
        where = where,
        smoothly = smoothly
    )
}

/**
 * Scrolls the `Column` or `LazyColumn` tile identified by [tileId] to [where], by broadcasting a
 * scroll command on the screen channel. [smoothly] chooses between an animated and an immediate
 * jump. Does not consume `incomingData`. Dispatches `onSuccess` (no data) always, right after the
 * command is broadcast — the broadcast is fire-and-forget, so this fires even when no tile
 * carries [tileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 * @param tileId Id of the `Column`/`LazyColumn` tile to scroll.
 * @param where Scroll target — [scrollColumnToTop], [scrollColumnToBottom] or [scrollColumnTo] (pixel offset in a `Column`, child index in a `LazyColumn`).
 * @param smoothly Whether the scroll animates instead of jumping immediately. Defaults to true.
 */
fun EventSchemaBuilderScope.ScrollColumn(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollColumnTileEventSchema.Where,
    smoothly: Boolean = true
) {
    addBuilder(
        ScrollColumnTileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId,
            where = where,
            smoothly = smoothly
        )
    )
}

/** Scrolls to the top. */
fun scrollColumnToTop() = ScrollColumnTileEventSchema.Where.Top

/** Scrolls to an explicit position — a pixel offset in a `Column`, a child index in a `LazyColumn`. */
fun scrollColumnTo(index: Int) = ScrollColumnTileEventSchema.Where.To(index)

/** Scrolls to the bottom. */
fun scrollColumnToBottom() = ScrollColumnTileEventSchema.Where.Bottom