package dev.catbit.mosaic.core.data.schemas.event.events.system

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
 * Drops locally persisted Mosaic caches, selectively controlled by [dropScreensCache],
 * [dropInitialGraphCache] and [dropVersionCache].
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the requested caches are dropped successfully.
 * - [OnFailureEventTrigger] — if dropping any of the requested caches throws; incomingData is
 *   the exception.
 *
 * **Notes:**
 * - The screens cache holds every cached [dev.catbit.mosaic.core.data.responses.screen.ScreenResponse],
 *   regardless of screen ID or request differentiators.
 * - Dropping the initial graph or version cache forces the next app start (or version check) to
 *   re-fetch them from the backend.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("DropCaches")
data class DropCachesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("dropScreensCache") val dropScreensCache: Boolean,
    @SerialName("dropInitialGraphCache") val dropInitialGraphCache: Boolean,
    @SerialName("dropVersionCache") val dropVersionCache: Boolean
) : EventSchema
