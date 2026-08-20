package dev.catbit.mosaic.core.data.schemas.event.events.scroll.column

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Scrolls the `Column` or `LazyColumn` tile identified by [tileId], by broadcasting a scroll
 * command on the screen channel. [smoothly] chooses between an animated and an immediate jump.
 *
 * [Where] selects the target: `Top`, `Bottom`, or `To` with an explicit position — which the
 * receiving tile reads as a pixel offset in a `Column` and as a child index in a `LazyColumn`.
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
@SerialName("ScrollColumn")
data class ScrollColumnTileEventSchema(
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
        @SerialName("Top")
        data object Top : Where

        @Serializable
        @SerialName("To")
        data class To(val index: Int) : Where

        @Serializable
        @SerialName("Bottom")
        data object Bottom : Where
    }
}
