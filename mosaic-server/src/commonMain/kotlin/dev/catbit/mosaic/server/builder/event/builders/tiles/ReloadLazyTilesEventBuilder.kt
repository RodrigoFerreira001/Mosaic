package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ReloadLazyTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val lazyTileId: String
) : EventSchemaBuilder<ReloadLazyTilesEventSchema> {

    override fun build() = ReloadLazyTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        lazyTileId = lazyTileId
    )
}

fun EventSchemaBuilderScope.ReloadLazyTiles(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    lazyTileId: String
) {
    addBuilder(
        ReloadLazyTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            lazyTileId = lazyTileId
        )
    )
}
