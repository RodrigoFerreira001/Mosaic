package dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Dismisses the currently displayed dialog overlay by broadcasting a dismiss signal to the
 * active screen. No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnDialogDismissedEventTrigger] — fired by the screen's overlay container after the
 *   dialog has been fully dismissed (the broadcast is handled by [ScreenTileBroadcastData.DismissDialog]).
 *
 * **Failure scenarios:** None defined. The runner unconditionally broadcasts the dismiss
 * signal regardless of whether a dialog is currently visible.
 *
 * **Notes:** If no dialog is currently shown, the broadcast is a no-op on the UI side.
 * The [OnDialogDismissedEventTrigger] is typically fired by the overlay container, not
 * directly by this runner.
 */
@Immutable
@Triggers(
    [
        OnDialogDismissedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("CloseBottomSheet")
data class DismissDialogEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema