package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DeleteFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val fileName: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DeleteFileEventSchema>() {

    override fun build() = DeleteFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        fileName = fileName
    )
}

fun EventSchemaBuilderScope.DeleteFile(
    id: String = randomId(),
    trigger: EventTrigger,
    fileName: String,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DeleteFileEventBuilder(
            id = id,
            trigger = trigger,
            fileName = fileName,
            events = events
        )
    )
}
