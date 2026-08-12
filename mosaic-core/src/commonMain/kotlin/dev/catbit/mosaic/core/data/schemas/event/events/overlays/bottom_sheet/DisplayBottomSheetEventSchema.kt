package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Displays a non-modal bottom sheet populated with a server-defined tile tree. The sheet is shown
 * immediately when this event runs; no network call is made.
 *
 * Unlike [dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema],
 * this sheet renders inline in the screen's layout instead of in its own window: there is no scrim
 * and the content behind it stays interactive. Use it for persistent panels — players, filters,
 * live summaries — that should coexist with the screen rather than interrupt it.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired after the sheet is pushed onto the screen's overlay stack.
 * - [OnFailureEventTrigger] — fired with the offending id when [bottomSheetId] is already on the
 *   stack.
 *
 * **Failure scenarios:** Adding an overlay whose [bottomSheetId] is already on the stack fails.
 *
 * **Notes:**
 * - [bottomSheetId] identifies this sheet on the overlay stack, so a
 *   [DismissBottomSheetEventSchema] can close this specific sheet even with other overlays
 *   stacked above it.
 * - [isCancellable] controls whether the user can dismiss the sheet by swiping down or pressing
 *   back; non-cancellable sheets can only be closed via [DismissBottomSheetEventSchema]. There is
 *   no scrim to tap, so this is the only gesture axis that applies.
 * - [fill] makes the sheet content take the full screen height, so the sheet opens flush with the
 *   top of the screen. With `false`, the sheet is exactly as tall as its content.
 * - [allowsPartialExpansion] adds a resting position at half the screen height: the sheet opens
 *   there and the user drags it up to reach the fully expanded position. With `false` the sheet
 *   only has open and closed states. **This is silently ignored when the content is shorter than
 *   half the screen** — the platform only creates the half-height anchor for sheets taller than
 *   that, which in practice means pairing it with [fill] or with content known to be long.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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
