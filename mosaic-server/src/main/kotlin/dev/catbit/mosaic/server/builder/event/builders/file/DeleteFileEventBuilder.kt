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

/**
 * Deletes the file stored under [fileName] in the client's own file storage. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the deletion completed; `onFailure`
 * (carrying the `Throwable`, logged) when it failed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param fileName Name of the file to delete.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
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
