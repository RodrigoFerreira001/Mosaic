package dev.catbit.mosaic.core.data.schemas.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Writes key-value data into one or more data stores. Each entry in [updates] specifies a
 * [Update.dataSource] target and an [Update.UpdateDate] describing the data to write — either
 * the current `incomingData` ([Update.UpdateDate.Incoming]) or a static inline map
 * ([Update.UpdateDate.Inline]). Updates are grouped by data source and applied in sequence.
 *
 * **incomingData consumed:** Used when [Update.UpdateDate] is [Update.UpdateDate.Incoming]. The
 * runner calls `incomingData.asMapAny()` to coerce it into `Map<String, Any>` before writing.
 * If `incomingData` cannot be coerced to a map, those entries are silently skipped (the helper
 * returns null, which is then treated as an empty entry set).
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
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("updates") val updates: List<Update>
) : EventSchema {

    @Serializable
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
                val data: Map<String, AnySerializable>
            ) : UpdateDate
        }
    }
}