package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Deletes values from data sources. Runs on the IO dispatcher.
 *
 * Each [Deletion] pairs a data source with an access mode: `Single` removes one id, `Batch`
 * removes a list of ids, and `Full` wipes the source. Supported sources are the application and
 * screen data holders (plain and segmented) and the plain and segmented local databases.
 * Deletions targeting navigation data, a tile or an inline source are silently ignored.
 *
 * All deletions are attempted; a failure in one does not stop the others.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every deletion completed without error. No data is passed
 *   downstream.
 * - `OnFailureEventTrigger` — when at least one database deletion failed. Fired once at the end,
 *   after all deletions were attempted, with no data attached.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("RemoveData")
data class RemoveDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("deletions") val deletions: SerializableImmutableList<Deletion>
) : EventSchema {

    @Serializable
    data class Deletion(
        @SerialName("dataSource") val dataSource: DataSourceSchema,
        @SerialName("accessMode") val accessMode: AccessModeSchema
    )
}