package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ProcessDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val processWith: String
) : EventSchemaBuilder<ProcessDataEventSchema>() {

    override fun build() = ProcessDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        processWith = processWith
    )
}

fun EventSchemaBuilderScope.ProcessData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    processWith: String
) {
    addBuilder(
        ProcessDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            processWith = processWith
        )
    )
}
