package dev.catbit.mosaic.core.data.schemas.event.events.scroll.column

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
 * Imperatively scrolls a ColumnTile to a specified position. At runtime the runner converts
 * the [where] discriminator into a [ColumnTileBroadcastData] message that is broadcast to
 * the ColumnTile identified by [tileId]. The target tile listens for that broadcast and
 * performs the scroll on its internal [LazyListState] or [ScrollState].
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnScrolledEventTrigger] — declared via `@Triggers` but not fired by the runner; this
 *   trigger is dispatched by the ColumnTile renderer itself when the user physically scrolls,
 *   not by this event.
 *
 * **Failure scenarios:** If no ColumnTile with the given [tileId] is currently rendered the
 * broadcast is silently ignored — no error or fallback trigger is fired.
 *
 * **Notes:** The scroll is executed purely via the broadcast bus; no incomingData is produced
 * or consumed. [smoothly] controls whether the scroll uses animation (`animateScrollTo`) or
 * jumps instantly. [Where.To] requires a valid 0-based item [index]; out-of-bounds values
 * are handled by the underlying Compose scroll API (clamped to list bounds).
 */
@Immutable
@Triggers(
    [
        OnScrolledEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
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
