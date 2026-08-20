package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Writes values into data sources. Runs on the IO dispatcher.
 *
 * Updates are grouped by data source and each [Update] resolves to a set of key/value entries:
 * `Incoming` spreads the event's incomingData when it is a map (and contributes nothing when it is
 * not), `Inline` writes a literal map, and `Explicit` writes a single known [Update.UpdateDate.Explicit.dataId]
 * taking its value either from incomingData or from a literal.
 *
 * Supported sources are the application and screen data holders (plain and segmented) and the
 * plain and segmented local databases. Updates targeting navigation data, a tile or an inline
 * source are silently ignored.
 *
 * The data holders accept `null` values, so an explicit `null` clears the entry there. The
 * databases do not support `null` yet, so entries resolving to `null` are skipped instead of
 * written.
 *
 * **incomingData consumed:** used by `Incoming` updates (spread when it is a map) and by
 * `Explicit` updates whose value is `Incoming`.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every write completed without error. No data is passed
 *   downstream.
 * - `OnFailureEventTrigger` — when at least one database write failed. Fired once at the end,
 *   after all writes were attempted, with no data attached; each failure is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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