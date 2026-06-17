package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Reads data from one or more sources and forwards the aggregated result to downstream events.
 * Each entry in [readings] is a (dataSource, accessMode) pair; all readings are executed
 * sequentially and their results are merged into a single accumulator map. The final shape
 * of [onSuccess] incomingData depends on the highest-precedence access mode used across all
 * readings (MAP > LIST > ANY — see Notes).
 *
 * **incomingData consumed:** Not used. This event produces new data rather than consuming it.
 *
 * **Triggers fired:**
 * - [onStart()] – Immediately before any reading is attempted, always.
 * - [onSuccess(data)] – After all readings succeed. The `data` value is:
 *     - `Map<String, Any>` if any reading used [AccessModeSchema.Full] or [AccessModeSchema.Batch]
 *       with `unwrapValuesToList = false`.
 *     - `List<Any>` if the highest reading used [AccessModeSchema.Batch] with
 *       `unwrapValuesToList = true` and no [AccessModeSchema.Full] reading was present.
 *     - The bare scalar/value of the single accumulated entry if all readings were
 *       [AccessModeSchema.Single] and there was exactly one reading; otherwise a `List<Any>`.
 * - [onFailure(data)] – As soon as any reading fails (see Failure scenarios). `data` is the
 *   exception that caused the failure. Execution stops; no further readings are processed.
 *
 * **Failure scenarios:**
 * - [AccessModeSchema.Full] on [DataSourceSchema.PlainDataBase] or [DataSourceSchema.SegmentedDataBase]
 *   fails if the underlying use case returns an error result.
 * - [AccessModeSchema.Batch] with `allowMissingData = false` fails if any requested key is absent
 *   in the source, firing [onFailure] with a [DataNotFoundException].
 * - [AccessModeSchema.Single] fails if the resolved value is null, firing [onFailure] with a
 *   [DataNotFoundException]. There is no `allowMissingData` option for single reads.
 *
 * **Notes:**
 * - When multiple readings are declared, later readings overwrite earlier ones on key collision
 *   inside the accumulator.
 * - [DataSourceSchema.ScreenNavigationData], [DataSourceSchema.ScreenPlainData],
 *   [DataSourceSchema.ScreenSegmentedData], and [DataSourceSchema.Tile] sources never throw on
 *   their own — failures only arise from database-backed sources or missing-key checks.
 * - The result-level precedence (MAP > LIST > ANY) is determined across all readings combined,
 *   not per-reading. A single [AccessModeSchema.Full] reading anywhere in the list will make the
 *   final output a Map regardless of other readings.
 * - All I/O is dispatched on [Dispatchers.IO].
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetData")
data class GetDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("readings") val readings: SerializableImmutableList<Reading>
) : EventSchema {

    @Serializable
    data class Reading(
        @SerialName("dataSource") val dataSource: DataSourceSchema,
        @SerialName("accessMode") val accessMode: AccessModeSchema
    )
}