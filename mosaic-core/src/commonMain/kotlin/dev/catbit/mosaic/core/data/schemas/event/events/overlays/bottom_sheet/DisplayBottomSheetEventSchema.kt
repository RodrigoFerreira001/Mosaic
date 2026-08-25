package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shows a bottom sheet built from [tiles], registered under [bottomSheetId] so a later
 * `DismissBottomSheet` can close it. This is the non-modal variant: it does not dim or block the
 * content behind it.
 *
 * [isCancellable] decides whether the user can dismiss it by gesture, [fill] whether it takes the
 * full height, and [allowsPartialExpansion] whether it stops at a half-expanded state before
 * reaching full height.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the sheet was added. No data is passed downstream.
 * - `OnFailureEventTrigger` — when it could not be added, typically because [bottomSheetId] is
 *   already in use; the `Throwable` is passed as incomingData.
 * - `OnDisplayEventTrigger` — once, when the sheet actually enters composition on screen.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnDisplayEventTrigger::class,
    ]
)
@Serializable
@SerialName("DisplayBottomSheet")
data class DisplayBottomSheetEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("bottomSheetId") val bottomSheetId: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("isCancellable") val isCancellable: Boolean,
    @SerialName("fill") val fill: Boolean,
    @SerialName("allowsPartialExpansion") val allowsPartialExpansion: Boolean
) : EventSchema
