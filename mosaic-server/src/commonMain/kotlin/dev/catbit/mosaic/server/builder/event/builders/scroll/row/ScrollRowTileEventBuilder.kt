package dev.catbit.mosaic.server.builder.event.builders.scroll.row

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
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

fun EventSchemaBuilderScope.ScrollRow(
    id: String = randomUuid(),
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

fun scrollRowToStart() = ScrollRowTileEventSchema.Where.Start
fun scrollRowTo(index: Int) = ScrollRowTileEventSchema.Where.To(index)
fun scrollRowToEnd() = ScrollRowTileEventSchema.Where.End
