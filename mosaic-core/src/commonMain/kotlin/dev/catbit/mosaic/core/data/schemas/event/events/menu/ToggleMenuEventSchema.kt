package dev.catbit.mosaic.core.data.schemas.event.events.menu

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
 * Flips the open/closed state of the `Menu` tile identified by [menuId], by dispatching a
 * `MenuTileEvents.OnToggleMenu` to it. Since the tile itself only closes on dismissal, this is how
 * a menu is opened from the server side — and, wired onto a menu item's click, how it is closed
 * after acting on the selection.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the signal reached the tile. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no tile with [menuId] is currently mounted; the `Throwable` is
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
@SerialName("ToggleMenu")
data class ToggleMenuEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val menuId: String
) : EventSchema
