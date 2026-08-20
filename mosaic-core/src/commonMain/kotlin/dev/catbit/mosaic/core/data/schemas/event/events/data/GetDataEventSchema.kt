package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reads one or more values from data sources and emits them downstream. Runs on the IO
 * dispatcher.
 *
 * Each [Reading] pairs a data source — inline map, plain or segmented local database,
 * application/screen/navigation data holders, or a tile's produced value — with an access mode:
 * `Single` (one id), `Batch` (a list of ids) or `Full` (everything the source holds). Readings are
 * processed in order into a single accumulator, so a later reading overwrites an earlier one on
 * key collision.
 *
 * **Emitted shape:** `Full`, and `Batch` without `unwrapValuesToList`, produce a map keyed by data
 * id. `Batch` with `unwrapValuesToList` produces a list of values. When neither applies — only
 * `Single` readings — a lone reading emits the bare value and several readings emit a list. A map
 * result wins over a list result when the two are mixed.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnStartEventTrigger` — before any reading is performed.
 * - `OnSuccessEventTrigger` — after every reading resolved; the assembled result is passed as
 *   incomingData.
 * - `OnFailureEventTrigger` — aborts the whole event on the first problem: a `Single` id that
 *   resolves to `null`, a `Batch` id missing while `allowMissingData` is `false` (a
 *   `DataNotFoundException` is passed as incomingData), or a database read that fails (the
 *   `Throwable` is passed).
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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