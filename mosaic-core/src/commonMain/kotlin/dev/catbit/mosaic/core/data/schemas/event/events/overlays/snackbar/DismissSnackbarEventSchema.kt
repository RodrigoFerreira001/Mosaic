package dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Programmatically dismisses the currently displayed snackbar by broadcasting a dismiss
 * signal to the active screen. No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:** None. This event fires no lifecycle triggers.
 *
 * **Failure scenarios:** None defined. The runner unconditionally broadcasts the dismiss
 * signal regardless of whether a snackbar is currently visible.
 *
 * **Notes:** If no snackbar is currently shown, the broadcast is a no-op on the UI side.
 * Dismissing via this event does NOT fire [OnSnackbarDismissedEventTrigger] — that trigger
 * is only fired by the snackbar's own dismiss callback (see [DisplaySnackbarEventSchema]).
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissSnackbar")
data class DismissSnackbarEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
