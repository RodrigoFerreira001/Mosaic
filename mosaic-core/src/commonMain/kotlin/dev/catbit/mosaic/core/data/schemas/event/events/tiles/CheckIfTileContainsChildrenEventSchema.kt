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
 * Checks whether a container tile currently holds children with all of the specified
 * [childrenIds]. The check is performed synchronously against the live tile tree.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when every ID in [childrenIds] is found as a direct child of
 *   [groupingTileId].
 * - [OnFailureEventTrigger] — when one or more IDs in [childrenIds] are not present among the
 *   direct children of [groupingTileId].
 *
 * **Notes:**
 * - [groupingTileId] must reference an existing container tile in the current tile tree.
 * - The check evaluates only direct children, not descendants at deeper levels.
 * - An empty [childrenIds] list is considered a match and fires [OnSuccessEventTrigger].
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("CheckIfTileContainsChildren")
data class CheckIfTileContainsChildrenEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("groupingTileId") val groupingTileId: String,
    @SerialName("childrenIds") val childrenIds: SerializableImmutableList<String>
) : EventSchema
