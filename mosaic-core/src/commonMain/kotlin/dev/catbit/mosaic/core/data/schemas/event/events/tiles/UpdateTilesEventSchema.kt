package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Patches tiles in place, the primary way to change what is on screen without refetching it.
 *
 * Each [Update] targets a tile by [Update.tileId] and merges a map of field names into it —
 * only the listed fields change, and `style` is merged rather than replaced. The map comes from
 * [Update.updateData]:
 * - `Incoming` — the event's incomingData, used as-is when it is a map. An update whose
 *   incomingData is not a map is skipped and counts as a failure.
 * - `Inline` — a literal map declared on the event.
 * - `Mapped` — one template per field, each applied to incomingData through the client's
 *   `TemplateProcessor`, so a payload can be reshaped per field on the way in.
 *
 * All updates are attempted; a failure in one does not stop the others.
 *
 * **incomingData consumed:** used by `Incoming` and `Mapped` updates.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every update was applied. No data is passed downstream.
 * - `OnFailureEventTrigger` — when at least one update failed: no tile carries that id, a
 *   `Mapped` template threw, or an `Incoming` update got non-map incomingData. Fired once at the
 *   end, after all updates were attempted, with no data attached; each failure is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("UpdateTiles")
data class UpdateTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("updates") val updates: SerializableImmutableList<Update>,
) : EventSchema {

    @Serializable
    data class Update(
        @SerialName("tileId") val tileId: String,
        @SerialName("updateData") val updateData: UpdateData
    ) {
        @Serializable
        sealed interface UpdateData {
            @Serializable
            @SerialName("Incoming")
            data object Incoming : UpdateData

            @Serializable
            @SerialName("Inline")
            data class Inline(
                @SerialName("data") val data: Map<String, AnySerializable?>
            ) : UpdateData

            @Serializable
            @SerialName("Mapped")
            data class Mapped(
                @SerialName("patterns") val patterns: Map<String, String>
            ) : UpdateData
        }
    }
}