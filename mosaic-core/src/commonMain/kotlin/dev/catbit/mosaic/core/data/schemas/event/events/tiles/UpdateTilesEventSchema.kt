package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList



/**
 * Applies a list of data-only updates to existing tiles without altering the tile tree
 * structure. Each [Update] entry targets a single tile by its ID and supplies an [Update.UpdateData]
 * describing which key-value properties to overwrite — either the current `incomingData`
 * ([Update.UpdateData.Incoming]) or a static inline map ([Update.UpdateData.Inline]). All updates
 * in [updates] are applied sequentially before the trigger fires.
 *
 * **incomingData consumed:** Used when [Update.UpdateData] is [Update.UpdateData.Incoming]. The
 * runner coerces `incomingData` into `Map<String, Any>` before applying the update. If
 * `incomingData` cannot be coerced to a map, that entry is silently skipped.
 *
 * **Triggers fired:**
 * - [OnTilesUpdatedEventTrigger] — fired after all updates have been applied; incomingData is
 *   not modified by this event.
 * - [OnSuccessEventTrigger] — fired after [OnTilesUpdatedEventTrigger], unconditionally.
 * - [OnFailureEventTrigger] — if a tile targeted by an [Update] is not found
 *   (TileNotFoundException); incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If an [Update.tileId] does not match any tile in the current tree, a TileNotFoundException
 *   is thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - Unlike [ReplaceTilesEventSchema], this event does not change tile types or the tree
 *   structure — it only patches mutable properties on tiles that already exist.
 * - [Update.UpdateData.Inline.data] is a `Map<String, AnySerializable?>` allowing arbitrary
 *   JSON-typed values. Keys that do not correspond to known tile properties are ignored by the
 *   renderer.
 * - Updates are applied in the order they appear in [updates]; if the same tile is listed
 *   twice, the second update overwrites keys set by the first.
 */
@Immutable
@Triggers(
    [
        OnTilesUpdatedEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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
        }
    }
}