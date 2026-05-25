package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetScreenEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<GetScreenEventSchema>() {

    override fun build() = GetScreenEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
    )
}

fun EventSchemaBuilderScope.GetScreen(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        GetScreenEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
