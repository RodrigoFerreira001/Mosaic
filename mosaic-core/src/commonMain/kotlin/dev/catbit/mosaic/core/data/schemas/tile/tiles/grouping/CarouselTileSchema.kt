package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 horizontal carousel that displays its child tiles as browsable
 * cards. Two carousel variants are supported via [type]:
 * - `MultiBrowse` — shows multiple items simultaneously with a preferred item width, allowing
 *   small peeking items at the edges. Item width bounds can be tuned via [minSmallItemWidth]
 *   and [maxSmallItemWidth].
 * - `Uncontained` — shows items at a fixed [itemWidth] without size constraints applied to
 *   partial items.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `type: CarouselTypeSchema`, `itemSpacing: Int`,
 * `contentPadding: Int`, `userScrollEnabled: Boolean`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** Item count is derived from the size of [tiles] at render time. The
 * [contentPadding] is applied as symmetric horizontal padding. User scrolling can be disabled
 * via [userScrollEnabled], which is useful when programmatic scrolling is the intended
 * interaction model.
 */
@Immutable
@Serializable
@SerialName("Carousel")
data class CarouselTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("type") val type: CarouselTypeSchema,
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
