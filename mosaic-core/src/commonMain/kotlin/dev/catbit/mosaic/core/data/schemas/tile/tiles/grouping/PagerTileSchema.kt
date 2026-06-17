package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a horizontal swipeable pager where each child tile occupies one page, using
 * Compose's [HorizontalPager]. Page size is controlled by [pageSize] and spacing between
 * pages by [pageSpacing]. Extra pages beyond the visible viewport can be pre-composed via
 * [beyondViewportPageCount]. The pager state can be programmatically controlled via
 * [PagerTileBroadcastData] (ScrollToBegin, ScrollToEnd, ScrollToNextPage, ScrollToPreviousPage).
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `pageSize: PageSizeSchema`, `pageSpacing: Int`,
 * `contentPadding: Int`, `beyondViewportPageCount: Int`
 *
 * **Triggers dispatched:** `OnPageChangedEventTrigger` — fired on every page change (after the
 * first page, i.e., the initial render does not trigger it). Each change emits up to three
 * triggers: `Direction.Any` (always), `Direction.Start` (if the new page is page 0),
 * `Direction.End` (if the new page is the last page), and `Direction.Index(page)` (always,
 * carrying the specific zero-based page index).
 *
 * **Notes:** Page count is derived from the size of [tiles] at render time. The pager does not
 * expose a scoped CompositionLocal for children. The initial page change from page 0 is
 * suppressed via `snapshotFlow(...).drop(1)` to avoid spurious trigger firing on composition.
 */
@Immutable
@Triggers(
    [
        OnPageChangedEventTrigger::class
    ]
)
@Serializable
@SerialName("Pager")
data class PagerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
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
