package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnBottomSheetDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Dismisses the currently displayed modal bottom sheet by broadcasting a dismiss signal to
 * the active screen. No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnBottomSheetDismissedEventTrigger] — fired by the screen's overlay container after the
 *   sheet has been fully dismissed (the broadcast is handled by [ScreenTileBroadcastData.DismissBottomSheet]).
 *
 * **Failure scenarios:** None defined. The runner unconditionally broadcasts the dismiss
 * signal regardless of whether a bottom sheet is currently visible.
 *
 * **Notes:** If no bottom sheet is currently shown, the broadcast is a no-op on the UI side.
 * The [OnBottomSheetDismissedEventTrigger] is typically fired by the overlay container, not
 * directly by this runner.
 */
@Triggers(
    [
        OnBottomSheetDismissedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissBottomSheet")
data class DismissBottomSheetEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?
) : EventSchema