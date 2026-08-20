package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema.Deletion
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RemoveDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val deletions: DeleteDataDeletionBuilderScope.() -> Unit
) : EventSchemaBuilder<RemoveDataEventSchema>() {

    override fun build() = RemoveDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        deletions = DeleteDataDeletionBuilderScope().apply(deletions).build()
    )
}

/**
 * Deletes values from data sources, on the IO dispatcher. Each [deletions] entry (built with
 * `addDeletion`) pairs a data source with an access mode — `Single` removes one id, `Batch`
 * removes a list of ids, `Full` wipes the source. Supports the application and screen data
 * holders (plain and segmented) and the plain and segmented local databases; deletions targeting
 * navigation data, a tile or an inline source are silently ignored. All deletions are attempted
 * even if one fails. Does not consume `incomingData`. Dispatches `onSuccess` (no data) when every
 * deletion completes without error; `onFailure` (no data), once at the end, when at least one
 * database deletion failed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param deletions Data source entries to delete, declared with `addDeletion`.
 */
fun EventSchemaBuilderScope.RemoveData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    deletions: DeleteDataDeletionBuilderScope.() -> Unit
) {
    addBuilder(
        RemoveDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            deletions = deletions
        )
    )
}

class DeleteDataDeletionBuilder(
    private val dataSource: DataSourceSchema,
    private val accessMode: AccessModeSchema
) : GenericBuilder<Deletion>() {

    override fun build() = Deletion(
        dataSource = dataSource,
        accessMode = accessMode
    )
}

class DeleteDataDeletionBuilderScope : GenericBuilderScope<Deletion, DeleteDataDeletionBuilder>() {

    /**
     * Declares one deletion inside a `RemoveData` event.
     *
     * @param dataSource Data source to delete from, built with the `data` package helpers (e.g. `plainDataBase()`, `segmentedDataBase(...)`).
     * @param accessMode How much of the source to delete, built with [dev.catbit.mosaic.server.builder.data.singleAccessMode], [dev.catbit.mosaic.server.builder.data.batchAccessMode] or [dev.catbit.mosaic.server.builder.data.fullAccessMode].
     */
    fun addDeletion(
        dataSource: DataSourceSchema,
        accessMode: AccessModeSchema
    ) {
        addBuilder(
            DeleteDataDeletionBuilder(
                dataSource = dataSource,
                accessMode = accessMode,
            )
        )
    }
}