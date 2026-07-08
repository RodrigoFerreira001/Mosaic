package dev.catbit.mosaic.core.data.schemas.event.events.popup

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Toggles the open/closed state of a PopupTile. At runtime the runner calls
 * [tilesEventDispatcher.onEvent] with [PopupTileEvents.OnTogglePopup] targeting the PopupTile
 * identified by [popupId]. If the popup is currently closed it opens; if open it closes.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the toggle signal is dispatched successfully.
 * - [OnFailureEventTrigger] — if no PopupTile with the given [popupId] is currently rendered
 *   (TileNotFoundException); incomingData is the exception.
 *
 * **Failure scenarios:** If no PopupTile with the given [popupId] is currently rendered, a
 * TileNotFoundException is thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:** The toggle is stateful inside the PopupTile's holder. Calling this event when the
 * popup is already open will close it, making it suitable as both an open and close action
 * depending on context.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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
