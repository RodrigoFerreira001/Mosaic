package dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh

import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class StopRefreshingEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String
) : EventSchemaBuilder<StopRefreshingEventSchema> {

    override fun build() = StopRefreshingEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId
    )
}

fun EventSchemaBuilderScope.StopRefreshing(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String
) {
    addBuilder(
        StopRefreshingEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId
        )
    )
}
