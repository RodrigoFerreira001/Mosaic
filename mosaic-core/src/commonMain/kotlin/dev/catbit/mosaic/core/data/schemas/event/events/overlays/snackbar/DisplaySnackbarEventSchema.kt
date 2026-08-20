package dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarActionEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shows a snackbar with [message], by broadcasting a display command on the screen channel.
 * [duration] maps onto Material's `Short`, `Long` and `Indefinite`, and [actionLabel] adds an
 * action button when non-null.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — right after the command was broadcast, before the snackbar has
 *   resolved. The broadcast is fire-and-forget, so this fires regardless of what the snackbar does
 *   next. No data is passed downstream.
 * - `OnSnackbarActionEventTrigger` — later, when the user presses the action button. No data is
 *   passed downstream.
 * - `OnSnackbarDismissedEventTrigger` — later, when the snackbar goes away without its action
 *   being pressed. No data is passed downstream.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnSnackbarActionEventTrigger::class,
        OnSnackbarDismissedEventTrigger::class,
    ]
)
@Serializable
@SerialName("DisplaySnackbar")
data class DisplaySnackbarEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val actionLabel: String? = null
) : EventSchema {

    @Serializable
    enum class SnackbarDuration {
        @SerialName("Short")
        Short,
        @SerialName("Long")
        Long,
        @SerialName("Indefinite")
        Indefinite
    }
}
