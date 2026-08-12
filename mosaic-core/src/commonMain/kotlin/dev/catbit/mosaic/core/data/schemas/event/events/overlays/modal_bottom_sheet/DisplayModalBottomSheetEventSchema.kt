package dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Displays a modal bottom sheet populated with a server-defined tile tree. The sheet is shown
 * immediately when this event runs; no network call is made.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired after the sheet is pushed onto the screen's overlay stack.
 *
 * **Failure scenarios:** Adding an overlay whose [modalBottomSheetId] is already on the stack
 * fails; the failure is reported through onFailure with the offending id.
 *
 * **Notes:**
 * - [modalBottomSheetId] identifies this sheet on the overlay stack, so a
 *   [DismissModalBottomSheetEventSchema] can close this specific sheet even with other overlays
 *   stacked above it.
 * - [isCancellable] controls whether the user can dismiss the sheet by swiping down, tapping the
 *   scrim or pressing back; non-cancellable sheets can only be closed via
 *   [DismissModalBottomSheetEventSchema].
 * - [fill] makes the sheet content take the full screen height, so the sheet opens flush with the
 *   top of the screen. With `false`, the sheet is exactly as tall as its content.
 * - [allowsPartialExpansion] adds a resting position at half the screen height: the sheet opens
 *   there and the user drags it up to reach the fully expanded position. With `false` (the
 *   default) the sheet only has open and closed states. **This is silently ignored when the
 *   content is shorter than half the screen** — the platform only creates the half-height anchor
 *   for sheets taller than that, which in practice means pairing it with [fill] or with
 *   content known to be long.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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