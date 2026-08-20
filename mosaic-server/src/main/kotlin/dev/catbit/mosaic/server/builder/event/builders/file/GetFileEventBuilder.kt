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

/**
 * Reads the file stored under [fileName] from the client's own file storage and emits its
 * content downstream, shaped by [outputType]. Does not consume `incomingData`. Dispatches
 * `onSuccess` (carrying the content, shaped by [outputType]) when the file was read; `onFailure`
 * (carrying the `Throwable`, logged) when the read failed, no file exists under [fileName], or
 * `mapObject()` decoding fails.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param fileName Name of the file to read.
 * @param outputType Shape of the content delivered as `incomingData` — [arrayOfBytes], [flowOfBytes], [platformFile], [mapObject] or [base64]. Defaults to raw bytes.
 */
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
