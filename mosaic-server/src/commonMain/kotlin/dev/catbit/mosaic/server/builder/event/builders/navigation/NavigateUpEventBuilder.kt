package dev.catbit.mosaic.server.builder.event.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class NavigateUpEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val navigatorId: String
) : EventSchemaBuilder<NavigateUpEventSchema>() {

    override fun build() = NavigateUpEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        navigatorId = navigatorId
    )
}

fun EventSchemaBuilderScope.NavigateUp(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    navigatorId: String
) {
    addBuilder(
        NavigateUpEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            navigatorId = navigatorId
        )
    )
}
