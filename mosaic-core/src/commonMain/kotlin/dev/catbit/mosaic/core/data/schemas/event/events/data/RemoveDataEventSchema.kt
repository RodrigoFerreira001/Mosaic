package dev.catbit.mosaic.core.data.schemas.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Deletes data from one or more data stores. Each entry in [deletions] specifies a target
 * [Deletion.dataSource] and an [Deletion.accessMode] that controls the scope of deletion:
 * a single key ([AccessModeSchema.Single]), a batch of keys ([AccessModeSchema.Batch]), or
 * the entire store ([AccessModeSchema.Full]).
 *
 * **incomingData consumed:** Not used. This event only performs deletions.
 *
 * **Triggers fired:**
 * - [onDataRemoved()] – Declared in [@Triggers] but **not fired by the runner**. The runner
 *   performs all deletions and returns without calling any trigger.
 *
 * **Failure scenarios:**
 * - No explicit failure path. Use-case calls (database deletions) may throw, but those
 *   exceptions are not caught and will propagate unchecked.
 * - [DataSourceSchema.ScreenNavigationData] and [DataSourceSchema.Tile] are explicitly ignored
 *   (no-op) — deletions targeting these sources have no effect.
 *
 * **Notes:**
 * - Deletions are processed one [Deletion] at a time in list order; there is no grouping by
 *   data source (unlike [UpdateDataEventSchema]).
 * - [AccessModeSchema.Full] wipes the entire segment or plain store — use with caution.
 * - Screen-scoped data sources ([DataSourceSchema.ScreenPlainData],
 *   [DataSourceSchema.ScreenSegmentedData]) are deleted from the in-memory screen state holder,
 *   while database-backed sources ([DataSourceSchema.PlainDataBase],
 *   [DataSourceSchema.SegmentedDataBase]) are deleted from persistent storage.
 * - All I/O is dispatched on [Dispatchers.IO].
 */
@Triggers(
    [
        OnDataRemovedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("RemoveData")
data class RemoveDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("deletions") val deletions: List<Deletion>
) : EventSchema {

    @Serializable
    data class Deletion(
        @SerialName("dataSource") val dataSource: DataSourceSchema,
        @SerialName("accessMode") val accessMode: AccessModeSchema
    )
}