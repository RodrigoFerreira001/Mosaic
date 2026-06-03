package dev.catbit.mosaic.core.data.schemas.event.events.menu

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Toggles the open/closed state of a MenuTile. At runtime the runner calls
 * [tilesEventDispatcher.onEvent] with [MenuTileEvents.OnToggleMenu] targeting the MenuTile
 * identified by [menuId]. If the menu is currently closed it opens; if open it closes.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the toggle signal is dispatched successfully.
 * - [OnFailureEventTrigger] — if no MenuTile with the given [menuId] is currently rendered
 *   (TileNotFoundException); incomingData is the exception.
 *
 * **Failure scenarios:** If no MenuTile with the given [menuId] is currently rendered, a
 * TileNotFoundException is thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:** The toggle is stateful inside the MenuTile's holder. Calling this event when the
 * menu is already open will close it, making it suitable as both an open and close action
 * depending on context.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("ToggleMenu")
data class ToggleMenuEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val menuId: String
) : EventSchema
