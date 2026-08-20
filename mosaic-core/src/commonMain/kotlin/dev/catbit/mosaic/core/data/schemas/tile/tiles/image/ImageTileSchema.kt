package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `Image` from a drawable bundled with the client application. [resourceName]
 * is looked up in the app's `DrawableResourcesHolder`; when no resource is registered under that
 * name **nothing is rendered at all** — there is no fallback and no error trigger.
 * [contentScale] maps onto the Compose `ContentScale` of the same name, and
 * [contentDescription], [alpha] and [alignment] are forwarded as-is.
 *
 * **Triggers dispatched:** none. The tile emits no trigger of its own and is not clickable, so
 * any `events` declared on it are never fired.
 *
 * **Notes:** use [AsyncImageTileSchema] for images that come from the network or from raw bytes;
 * this tile can only show assets that ship with the client.
 */
@Immutable
@Serializable
@SerialName("Image")
data class ImageTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
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
