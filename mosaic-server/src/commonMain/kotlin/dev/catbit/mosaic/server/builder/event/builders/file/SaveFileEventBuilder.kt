package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SaveFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val fileName: String,
    private val overrideIfExists: Boolean
) : EventSchemaBuilder<SaveFileEventSchema>() {

    override fun build() = SaveFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        fileName = fileName,
        overrideIfExists = overrideIfExists
    )
}

fun EventSchemaBuilderScope.SaveFile(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    fileName: String,
    overrideIfExists: Boolean
) {
    addBuilder(
        SaveFileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            fileName = fileName,
            overrideIfExists = overrideIfExists
        )
    )
}
