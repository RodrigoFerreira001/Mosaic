package dev.catbit.mosaic.core.data.schemas.event.events.popup

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Flips the open/closed state of the `Popup` tile identified by [popupId], by dispatching a
 * `PopupTileEvents.OnTogglePopup` to it. Since the tile itself only closes on dismissal, this is
 * how a popup is opened from the server side.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the signal reached the tile. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no tile with [popupId] is currently mounted; the `Throwable` is
 *   passed as incomingData and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("TogglePopup")
data class TogglePopupEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val popupId: String
) : EventSchema
