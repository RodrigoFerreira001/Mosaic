package dev.catbit.mosaic.core.data.schemas.event.events.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Instructs the navigator identified by [navigatorId] to push [destination] onto its back stack,
 * optionally popping entries before navigating and carrying data to the new destination.
 *
 * **incomingData consumed:** incomingData is cast to `Map<String, Any>` and merged with [data]
 * (schema-defined data takes precedence on key conflicts) to form the navigation data map passed
 * to the new destination. If incomingData is not a map, it is treated as an empty map.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the navigator successfully navigates to [destination].
 * - [OnFailureEventTrigger] — if no navigator is registered under [navigatorId]; incomingData
 *   is null or the relevant exception.
 *
 * **Failure scenarios:**
 * - If no navigator is registered under [navigatorId], [OnFailureEventTrigger] fires.
 *
 * **Notes:**
 * - [popUpTo] is optional. When provided, the back stack is popped up to [PopUpTo.destination]
 *   before navigating; [PopUpTo.inclusive] controls whether that destination itself is also removed.
 * - The merged navigation data map (`incomingData + schema data`) is what the destination screen
 *   receives as its initial `navigationData`.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("Navigate")
data class NavigateEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val destination: String,
    val navigatorId: String,
    val popUpTo: PopUpTo?,
    val data: Map<String, AnySerializable>?
) : EventSchema {

    @Serializable
    data class PopUpTo(
        val destination: String,
        val inclusive: Boolean
    )
}