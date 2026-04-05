package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CheckForReceivedDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dataKey: String
) : EventSchemaBuilder<CheckForReceivedDataEventSchema>() {

    override fun build() = CheckForReceivedDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dataKey = dataKey
    )
}

fun EventSchemaBuilderScope.CheckForReceivedData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dataKey: String
) {
    addBuilder(
        CheckForReceivedDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dataKey = dataKey
        )
    )
}
