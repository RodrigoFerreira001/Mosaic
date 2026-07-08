package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.FileOutputType
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val fileName: String,
    private val outputType: FileOutputType
) : EventSchemaBuilder<GetFileEventSchema>() {

    override fun build() = GetFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        fileName = fileName,
        outputType = outputType
    )
}

fun EventSchemaBuilderScope.GetFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    fileName: String,
    outputType: FileOutputType = arrayOfBytes()
) {
    addBuilder(
        GetFileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            fileName = fileName,
            outputType = outputType
        )
    )
}
