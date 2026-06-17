package dev.catbit.mosaic.core.data.schemas.event.events.scroll.row

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Imperatively scrolls a RowTile to a specified position. At runtime the runner converts the
 * [where] discriminator into a [RowTileBroadcastData] message that is broadcast to the RowTile
 * identified by [tileId]. The target tile listens for that broadcast and performs the scroll on
 * its internal scroll state.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnScrolledEventTrigger] — declared via `@Triggers` but not fired by this runner; it is
 *   dispatched by the RowTile renderer when the user physically scrolls.
 *
 * **Failure scenarios:** If no RowTile with the given [tileId] is currently rendered the
 * broadcast is silently ignored — no error or fallback trigger is fired.
 *
 * **Notes:** [Where.Start] scrolls to the first item, [Where.End] to the last item, and
 * [Where.To] scrolls to the given 0-based [index]. [smoothly] controls animation vs. instant
 * jump. Out-of-bounds index values are clamped by the underlying Compose scroll API.
 */
@Immutable
@Triggers(
    [
        OnScrolledEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("ScrollRow")
data class ScrollRowTileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("tileId") val tileId: String,
    @SerialName("where") val where: Where,
    @SerialName("smoothly") val smoothly: Boolean,
) : EventSchema {

    @Serializable
    sealed interface Where {
        @Serializable
        @SerialName("Start")
        data object Start : Where

        @Serializable
        @SerialName("To")
        data class To(val index: Int) : Where

        @Serializable
        @SerialName("End")
        data object End : Where
    }
}
