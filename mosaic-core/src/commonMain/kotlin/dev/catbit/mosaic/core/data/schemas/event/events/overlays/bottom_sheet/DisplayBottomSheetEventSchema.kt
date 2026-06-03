package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Displays a modal bottom sheet populated with a server-defined tile tree. The sheet is shown
 * immediately when this event runs; no network call is made.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:** None. This event fires no lifecycle triggers; it only pushes the
 * overlay state to the screen via [ScreenTileBroadcastData.OnDisplayBottomSheetRequested].
 *
 * **Failure scenarios:** None defined. The runner does not perform any fallible operation.
 *
 * **Notes:**
 * - [tiles] is loaded into the overlay editor before the broadcast is sent, so the sheet
 *   content is ready before the screen reacts to the broadcast.
 * - [isCancellable] controls whether the user can dismiss the sheet by swiping down or
 *   tapping the scrim; non-cancellable sheets can only be closed via [DismissBottomSheetEventSchema].
 * - [fill] determines whether the bottom sheet should expand to fill the full screen height.
 * - There is currently no real identifier for the bottom sheet instance, so dismiss events
 *   always target the single active sheet (see TODO in source).
 */
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DisplayBottomSheet")
data class DisplayBottomSheetEventSchema( // TODO ter aqui um identificador real para a BS, para fins de eventos de dismiss
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val tiles: List<TileSchema>,
    val isCancellable: Boolean,
    val fill: Boolean
) : EventSchema