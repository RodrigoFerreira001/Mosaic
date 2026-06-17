package dev.catbit.mosaic.core.data.schemas.event.events.tiles

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
 * Returns the current number of direct children of a container tile identified by
 * [groupingTileId]. The count is read synchronously from the live tile tree.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when [groupingTileId] is found; incomingData is the child count
 *   as an [Int].
 * - [OnFailureEventTrigger] — when [groupingTileId] does not match any tile in the current tree.
 *
 * **Notes:**
 * - The count reflects only direct children; nested descendants are not included.
 * - A container tile with no children fires [OnSuccessEventTrigger] with incomingData = `0`.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetTileChildrenCount")
data class GetTileChildrenCountEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("groupingTileId") val groupingTileId: String
) : EventSchema
