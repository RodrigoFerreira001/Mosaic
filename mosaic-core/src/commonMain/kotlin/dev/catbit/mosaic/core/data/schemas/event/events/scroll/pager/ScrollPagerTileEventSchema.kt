package dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Imperatively navigates a PagerTile to a target page. At runtime the runner converts the
 * [where] discriminator into a [PagerTileBroadcastData] message that is broadcast to the
 * PagerTile identified by [tileId]. The target tile acts on that broadcast using its internal
 * [PagerState].
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnScrolledEventTrigger] — declared via `@Triggers` but not fired by this runner; it is
 *   dispatched by the PagerTile renderer when the user swipes between pages.
 *
 * **Failure scenarios:** If no PagerTile with the given [tileId] is currently rendered the
 * broadcast is silently ignored. Navigating to [Where.NextPage] when already on the last page,
 * or [Where.PreviousPage] when on the first page, is clamped by the underlying [PagerState].
 *
 * **Notes:** [Where.Begin] jumps to page 0; [Where.End] jumps to the last page; [Where.NextPage]
 * and [Where.PreviousPage] move relative to the current page. [smoothly] controls whether the
 * page change is animated.
 */
@Triggers(
    [
        OnScrolledEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("ScrollPager")
data class ScrollPagerTileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
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
