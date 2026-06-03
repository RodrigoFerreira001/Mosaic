package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a static image from a bundled drawable resource identified by [resourceName]. The
 * resource is resolved at runtime via [DrawableResourcesHolder], which maps string names to
 * Compose [DrawableResource] references registered by the host application. If the resource
 * name is not found in the holder, nothing is rendered.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `resourceName: String`, `contentDescription: String?`,
 * `contentScale: ContentScale`, `alpha: Float`, `alignment: AlignmentSchema.TwoDimensional`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** Unlike [AsyncImageTileSchema], this tile renders only images that are bundled
 * with the client application and registered in [DrawableResourcesHolder]. The server can
 * only reference resources that the client has previously registered; requesting an unknown
 * [resourceName] silently renders nothing. Content scale options are CROP, FIT, FILL_HEIGHT,
 * FILL_WIDTH, INSIDE, and FILL_BOUNDS.
 */
@Serializable
@SerialName("Image")
data class ImageTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("resourceName") val resourceName: String,
    @SerialName("contentDescription") val contentDescription: String?,
    @SerialName("contentScale") val contentScale: ContentScale,
    @SerialName("alpha") val alpha: Float,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional
) : TileSchema {

    enum class ContentScale {
        CROP,
        FIT,
        FILL_HEIGHT,
        FILL_WIDTH,
        INSIDE,
        FILL_BOUNDS,
    }
}
