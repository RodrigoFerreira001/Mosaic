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
 * Renders a Material 3 horizontal carousel over [tiles], one child per carousel item.
 * [type] picks the composable:
 * - [CarouselTypeSchema.MultiBrowse] → `HorizontalMultiBrowseCarousel`, where
 *   [CarouselTypeSchema.MultiBrowse.preferredItemWidth] sets the target item width and
 *   [CarouselTypeSchema.MultiBrowse.minSmallItemWidth] / [CarouselTypeSchema.MultiBrowse.maxSmallItemWidth]
 *   bound the shrunken edge items (falling back to `CarouselDefaults` when `null`).
 * - [CarouselTypeSchema.Uncontained] → `HorizontalUncontainedCarousel` with a fixed
 *   [CarouselTypeSchema.Uncontained.itemWidth].
 *
 * [itemSpacing] and [contentPadding] are interpreted as dp — the padding is applied
 * horizontally only — and [userScrollEnabled] toggles manual swiping.
 *
 * **Item control:** the tile listens to the screen broadcast channel and reacts to
 * scroll-to-begin, scroll-to-end, next-item and previous-item commands addressed to its [id],
 * each optionally animated. It shares the pager's broadcast, so the same scroll event drives
 * both tiles — just point it at this carousel's [id]. The resulting item is clamped to the valid
 * range, so asking for the next item on the last one (or the previous on the first) is a no-op.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnPageChangedEventTrigger` — fired whenever the current item changes (the initial item is
 *   skipped). The trigger is dispatched once per direction the new item matches, and each
 *   dispatch only reaches the events wired to that exact direction: `Direction.Any` always,
 *   `Direction.Start` when landing on the first item, `Direction.End` when landing on the last
 *   item, and `Direction.Index(item)` for the new index.
 *
 * **Notes:** children are rendered by index without a scope CompositionLocal.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnPageChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("Carousel")
data class CarouselTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    // SerialName differs from the Kotlin property name to avoid colliding with the
    // polymorphic TileSchema class discriminator, which also serializes under "type".
    @SerialName("carouselType") val type: CarouselTypeSchema,
    @SerialName("itemSpacing") val itemSpacing: Int = 0,
    @SerialName("contentPadding") val contentPadding: Int = 0,
    @SerialName("userScrollEnabled") val userScrollEnabled: Boolean = true
) : TileSchema {

    @Serializable
    sealed interface CarouselTypeSchema {

        @Serializable
        @SerialName("multiBrowse")
        data class MultiBrowse(
            @SerialName("preferredItemWidth") val preferredItemWidth: Int,
            @SerialName("minSmallItemWidth") val minSmallItemWidth: Int? = null,
            @SerialName("maxSmallItemWidth") val maxSmallItemWidth: Int? = null
        ) : CarouselTypeSchema

        @Serializable
        @SerialName("uncontained")
        data class Uncontained(
            @SerialName("itemWidth") val itemWidth: Int
        ) : CarouselTypeSchema
    }
}
