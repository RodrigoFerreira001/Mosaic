package dev.catbit.mosaic.core.data.schemas.event.events.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Instructs the navigator identified by [navigatorId] to clear its entire back stack and push
 * [destination] as the sole entry, carrying data to the new destination.
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
 * - Unlike [NavigateEventSchema], there is no `popUpTo` option: the back stack is always cleared
 *   entirely before [destination] is pushed.
 * - When [launchSingleTop] is `true` (the default) and [destination] is already the top of the
 *   back stack, navigation is a no-op — the stack is left untouched.
 * - The merged navigation data map (`incomingData + schema data`) is what the destination screen
 *   receives as its initial `navigationData`.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("NavigateClearingStack")
data class NavigateClearingStackEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("destination") val destination: String,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("launchSingleTop") val launchSingleTop: Boolean = true,
    @SerialName("data") val data: Map<String, AnySerializable>?
) : EventSchema
