package dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet

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
 * Shows a modal bottom sheet built from [tiles], registered under [modalBottomSheetId] so a later
 * `DismissModalBottomSheet` can close it. Unlike the plain bottom sheet, this one dims and blocks
 * the content behind it.
 *
 * [isCancellable] decides whether the user can dismiss it by gesture or scrim tap, [fill] whether
 * it takes the full height, and [allowsPartialExpansion] whether it stops at a half-expanded state
 * before reaching full height.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the sheet was added. No data is passed downstream.
 * - `OnFailureEventTrigger` — when it could not be added, typically because [modalBottomSheetId]
 *   is already in use; the `Throwable` is passed as incomingData.
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
@SerialName("DisplayModalBottomSheet")
data class DisplayModalBottomSheetEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("modalBottomSheetId") val modalBottomSheetId: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("isCancellable") val isCancellable: Boolean,
    @SerialName("fill") val fill: Boolean,
    @SerialName("allowsPartialExpansion") val allowsPartialExpansion: Boolean
) : EventSchema