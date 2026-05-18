package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CheckIfHasInternetConnectionEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<CheckIfHasInternetConnectionEventSchema>() {

    override fun build() = CheckIfHasInternetConnectionEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

fun EventSchemaBuilderScope.CheckIfHasInternetConnection(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        CheckIfHasInternetConnectionEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
