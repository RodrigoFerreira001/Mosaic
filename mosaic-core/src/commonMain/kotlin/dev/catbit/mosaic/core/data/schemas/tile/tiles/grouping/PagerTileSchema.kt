package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `HorizontalPager` over [tiles], one page per child. [pageSize] chooses
 * between full-width pages ([PageSizeSchema.Fill]) and fixed-width pages
 * ([PageSizeSchema.Fixed], in dp); [pageSpacing] and [contentPadding] are dp values (the padding
 * is horizontal only), and [beyondViewportPageCount] controls how many off-screen pages stay
 * composed.
 *
 * **Page control:** the tile listens to the screen broadcast channel and reacts to
 * scroll-to-begin, scroll-to-end, next-page and previous-page commands addressed to its [id],
 * each optionally animated. The resulting page is clamped to the valid range, so asking for the
 * next page on the last one (or the previous page on the first) is a no-op rather than an error.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnPageChangedEventTrigger` — fired whenever the settled current page changes (the initial
 *   page is skipped). The trigger is dispatched once per direction the new page matches, and
 *   each dispatch only reaches the events wired to that exact direction: `Direction.Any` always,
 *   `Direction.Start` when landing on the first page, `Direction.End` when landing on the last
 *   page, and `Direction.Index(page)` for the new index. Wire events against whichever direction
 *   you care about — wiring several means several chains run for the same page change.
 *
 * **Notes:** the pager is horizontal only, and children are rendered by index without a scope
 * CompositionLocal.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnPageChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("Pager")
data class PagerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("pageSize") val pageSize: PageSizeSchema = PageSizeSchema.Fill,
    @SerialName("pageSpacing") val pageSpacing: Int = 0,
    @SerialName("contentPadding") val contentPadding: Int = 0,
    @SerialName("beyondViewportPageCount") val beyondViewportPageCount: Int = 0
) : TileSchema {

    @Serializable
    sealed interface PageSizeSchema {

        @Serializable
        @SerialName("fill")
        data object Fill : PageSizeSchema

        @Serializable
        @SerialName("fixed")
        data class Fixed(@SerialName("value") val value: Int) : PageSizeSchema
    }
}
