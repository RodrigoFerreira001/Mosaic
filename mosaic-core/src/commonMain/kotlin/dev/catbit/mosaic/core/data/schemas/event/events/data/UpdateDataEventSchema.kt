package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Writes key-value data into one or more data stores. Each entry in [updates] specifies a
 * [Update.dataSource] target and an [Update.UpdateDate] describing the data to write.
 *
 * [Update.UpdateDate] has two families of behavior:
 * - [Update.UpdateDate.Incoming] / [Update.UpdateDate.Inline]: **legacy** modes that resolve to a
 *   `Map<String, Any>` and then explode it — **every key in that map becomes its own dataId**, and
 *   its value is written under that dataId. This is only correct when the map genuinely represents
 *   multiple independent dataId → value pairs. If the value you want to write is itself a map that
 *   represents a *single* record (e.g. a segmented-data row `{"action":"NONE","comment":null,...}`),
 *   these modes will incorrectly explode it into one write per top-level key instead of one write
 *   for the whole record. Kept only for backward compatibility with existing screens.
 * - [Update.UpdateDate.Explicit]: declares the target `dataId` and the value to write with no
 *   inference whatsoever — the value (whether it's a scalar, list, or map) is written as-is under
 *   that single `dataId`. This is the correct mode whenever the value being written is a record
 *   (typically a map) that must be stored intact under one key, e.g. writing a segmented-data row.
 *   It is also the correct (and only reliable) mode for writing an explicit `null` under a single
 *   `dataId`: use `Explicit(dataId, ExplicitValue.Inline(value = null))`. Because the dataId is
 *   already known ahead of time, the runner writes the value as-is — including `null` — instead of
 *   treating a null value as "nothing to write" the way the legacy modes below do.
 *
 * **incomingData consumed:** Used when [Update.UpdateDate] is [Update.UpdateDate.Incoming] (coerced
 * via `incomingData.asMapAny()`, silently skipped if the cast fails) or when
 * [Update.UpdateDate.Explicit] uses [Update.UpdateDate.Explicit.ExplicitValue.Incoming] (used
 * verbatim, not coerced to a map, silently skipped only if `incomingData` is null).
 *
 * **Triggers fired:**
 * - [onDataUpdated()] – Declared in [@Triggers] but **not fired by the runner**. The runner
 *   performs all writes and returns without calling any trigger.
 *
 * **Failure scenarios:**
 * - No explicit failure path. Individual use-case calls (database writes) may throw, but those
 *   exceptions are not caught and will propagate unchecked.
 * - [DataSourceSchema.ScreenNavigationData] and [DataSourceSchema.Tile] are explicitly ignored
 *   (no-op) — updates targeting these sources have no effect.
 *
 * **Notes:**
 * - Updates are grouped by data source before processing, so all updates for the same source
 *   are batched together in a single pass.
 * - When multiple [Update] entries target the same data source and key, later entries overwrite
 *   earlier ones (they are applied in iteration order).
 * - All I/O is dispatched on [Dispatchers.IO].
 */
@Immutable
@Triggers(
    [
        OnDataUpdatedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateData")
data class UpdateDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("updates") val updates: SerializableImmutableList<Update>
) : EventSchema {

    @Serializable
    @Immutable
    data class Update(
        @SerialName("dataSource") val dataSource: DataSourceSchema,
        @SerialName("updateData") val updateData: UpdateDate
    ) {
        @Serializable
        sealed interface UpdateDate {
            @Serializable
            @SerialName("Incoming")
            data object Incoming : UpdateDate

            @Serializable
            @SerialName("Inline")
            data class Inline(
                @SerialName("data")
                val data: Map<String, AnySerializable?>
            ) : UpdateDate

            @Serializable
            @SerialName("Explicit")
            data class Explicit(
                @SerialName("dataId") val dataId: String,
                @SerialName("value") val value: ExplicitValue
            ) : UpdateDate {
                @Serializable
                sealed interface ExplicitValue {
                    @Serializable
                    @SerialName("Incoming")
                    data object Incoming : ExplicitValue

                    @Serializable
                    @SerialName("Inline")
                    data class Inline(
                        @SerialName("value") val value: AnySerializable?
                    ) : ExplicitValue
                }
            }
        }
    }
}