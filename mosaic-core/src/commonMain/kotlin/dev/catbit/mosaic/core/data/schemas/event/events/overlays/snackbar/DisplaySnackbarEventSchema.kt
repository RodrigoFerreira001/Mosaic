package dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarActionEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Displays a Material 3 snackbar with a message, an optional action label, and a configurable
 * duration. The snackbar is shown immediately by broadcasting a display signal to the active
 * screen.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSnackbarActionEventTrigger] — fired when the user taps the action label button.
 *   Only relevant when [actionLabel] is non-null.
 * - [OnSnackbarDismissedEventTrigger] — fired when the snackbar is dismissed for any reason
 *   other than the action button (timeout, swipe, or programmatic dismiss).
 *
 * **Failure scenarios:** None defined. The runner does not perform any fallible operation.
 *
 * **Notes:**
 * - The [SnackbarDurationSchema] enum is mapped 1-to-1 to the Compose Material 3
 *   [SnackbarDuration] enum at runtime ([Short], [Long], [Indefinite]).
 * - [actionLabel] is optional; omitting it produces a snackbar with no action button, and
 *   [OnSnackbarActionEventTrigger] will never fire.
 * - Both trigger callbacks are wired at the time this event runs, before the snackbar is
 *   actually shown, so the hosting screen must keep this event's holder alive for the
 *   callbacks to be reachable.
 */
@Triggers(
    [
        OnSnackbarActionEventTrigger::class,
        OnSnackbarDismissedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DisplaySnackbar")
data class DisplaySnackbarEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val message: String,
    val duration: SnackbarDurationSchema = SnackbarDurationSchema.Short,
    val actionLabel: String? = null
) : EventSchema
