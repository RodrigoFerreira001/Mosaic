package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Carousel")
data class CarouselTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
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
