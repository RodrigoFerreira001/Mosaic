package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.ListFilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ListFilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val directory: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<ListFilesEventSchema>() {

    override fun build() = ListFilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        directory = directory
    )
}

/**
 * Lists the files directly inside [directory] in the client's own file storage; subdirectories and
 * the files inside them are skipped. Does not consume `incomingData`.
 * Dispatches `onSuccess` carrying the `List<String>` of file paths when the directory holds at
 * least one file — each path is relative to the storage root, so it can be passed straight to
 * `GetFile`/`DeleteFiles` as `fileName`; `onEmptyDir` (no data) when the directory holds no files;
 * `onFailure` (carrying the `Throwable`, logged) when the directory doesn't exist
 * (`DirNotFoundException`) or the listing failed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param directory Directory to list, relative to the client's file storage root.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onEmptyDir`, `onFailure`).
 */
fun EventSchemaBuilderScope.ListFiles(
    id: String = randomId(),
    trigger: EventTrigger,
    directory: String,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        ListFilesEventBuilder(
            id = id,
            trigger = trigger,
            directory = directory,
            events = events
        )
    )
}
