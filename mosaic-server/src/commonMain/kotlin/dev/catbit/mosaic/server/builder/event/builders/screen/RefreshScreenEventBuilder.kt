package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RefreshScreenEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<RefreshScreenEventSchema>() {

    override fun build() = RefreshScreenEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
    )
}

fun EventSchemaBuilderScope.RefreshScreen(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        RefreshScreenEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
        )
    )
}
