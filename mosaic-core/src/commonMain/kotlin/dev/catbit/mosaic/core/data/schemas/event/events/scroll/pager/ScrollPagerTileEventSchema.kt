package dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Moves the `Pager` or `Carousel` tile identified by [tileId] to another page, by broadcasting a
 * scroll command on the screen channel. [smoothly] chooses between an animated and an immediate
 * jump.
 *
 * [Where] selects the target: `Begin`, `End`, `NextPage` or `PreviousPage`. The receiving tile
 * clamps the result, so asking for the next page on the last one is a no-op.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — always, right after the command was broadcast. The broadcast is
 *   fire-and-forget, so this fires even when no tile carries [tileId]. No data is passed
 *   downstream.
 */
@Immutable
@Triggers([OnSuccessEventTrigger::class])
@Serializable
@SerialName("ScrollPager")
data class ScrollPagerTileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("tileId") val tileId: String,
    @SerialName("where") val where: Where,
    @SerialName("smoothly") val smoothly: Boolean
) : EventSchema {

    @Serializable
    sealed interface Where {
        @Serializable
        @SerialName("Begin")
        data object Begin : Where

        @Serializable
        @SerialName("PreviousPage")
        data object PreviousPage : Where

        @Serializable
        @SerialName("NextPage")
        data object NextPage : Where

        @Serializable
        @SerialName("End")
        data object End : Where
    }
}
