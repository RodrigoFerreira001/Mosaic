package dev.catbit.mosaic.core.data.schemas.event.events.navigation

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
 * Instructs the navigator identified by [navigatorId] to pop the current destination off its
 * back stack, equivalent to pressing the system back button within that navigator's scope.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the navigator successfully pops the back stack.
 * - [OnFailureEventTrigger] — if no navigator is registered under [navigatorId]; incomingData
 *   is null or the relevant exception.
 *
 * **Failure scenarios:**
 * - If no navigator is registered under [navigatorId], [OnFailureEventTrigger] fires.
 * - If the back stack is already empty, behavior depends on the underlying navigator; no error
 *   is surfaced to child events.
 *
 * **Notes:**
 * - This is the simplest navigation event: it carries no data and has no schema parameters beyond
 *   [navigatorId].
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("NavigateUp")
data class NavigateUpEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val navigatorId: String
) : EventSchema