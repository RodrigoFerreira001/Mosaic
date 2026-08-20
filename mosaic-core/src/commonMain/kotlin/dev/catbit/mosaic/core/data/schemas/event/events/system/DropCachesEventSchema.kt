package dev.catbit.mosaic.core.data.schemas.event.events.system

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
 * Clears the client's local caches. Each flag selects one store: [dropScreensCache] the cached
 * screen payloads, [dropInitialGraphCache] the cached initial navigation graph, and
 * [dropVersionCache] the cached version marker used to decide whether the cached content is still
 * valid.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the selected caches were dropped. No data is passed downstream.
 * - `OnFailureEventTrigger` — when dropping failed; the `Throwable` is passed as incomingData and
 *   the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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
