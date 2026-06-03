package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a shimmer loading effect over its child tiles using the
 * [compose-shimmer](https://github.com/valentinilk/compose-shimmer) library. The shimmer
 * animation is applied as a modifier on a [Box] that wraps all children, producing an
 * animated highlight sweep across the entire subtree simultaneously.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** The shimmer effect is always active while the tile is visible; there is no
 * on/off flag. To stop the shimmer, the server should replace or hide this tile via
 * UpdateTiles. Children are rendered normally inside the shimmer container, so placeholder
 * shapes should be provided as child tiles to produce a meaningful skeleton UI.
 */
@Serializable
@SerialName("Shimmer")
data class ShimmerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
) : TileSchema

//https://github.com/valentinilk/compose-shimmer