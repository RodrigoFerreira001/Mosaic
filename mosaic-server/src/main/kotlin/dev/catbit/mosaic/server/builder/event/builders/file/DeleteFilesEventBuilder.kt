package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema.DeleteTarget
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

// ── DeleteTarget helpers ──────────────────────────────────────────────────────

/** Deletes the one file stored under [fileName]. */
fun singleFile(fileName: String): DeleteTarget = DeleteTarget.Single(fileName = fileName)

/** Deletes every file in [fileNames]. */
fun multipleFiles(fileNames: List<String>): DeleteTarget =
    DeleteTarget.Multiple(fileNames = fileNames.toImmutableList() as SerializableImmutableList<String>)

// ── Builder ───────────────────────────────────────────────────────────────────

internal class DeleteFilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val target: DeleteTarget,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DeleteFilesEventSchema>() {

    override fun build() = DeleteFilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        target = target
    )
}

/**
 * Deletes the file or files selected by [target] from the client's own file storage. Does not
 * consume `incomingData`. With [multipleFiles] every file is attempted even after one fails.
 * Dispatches `onSuccess` (no data) when every deletion completed; `onFailure` once, after all were
 * attempted, when at least one failed (carrying the first `Throwable`; every error is logged).
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param target Which files to delete — [singleFile] or [multipleFiles].
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.DeleteFiles(
    id: String = randomId(),
    trigger: EventTrigger,
    target: DeleteTarget,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DeleteFilesEventBuilder(
            id = id,
            trigger = trigger,
            target = target,
            events = events
        )
    )
}
