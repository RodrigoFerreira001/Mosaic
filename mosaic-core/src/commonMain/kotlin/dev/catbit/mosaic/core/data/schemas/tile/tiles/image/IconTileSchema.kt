package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a single icon using the shared Mosaic [Icon] composable, which resolves the icon
 * from the provided [IconSchema] (Material Symbols or other supported icon sources).
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `icon: IconSchema`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** The icon is always rendered without a filled/outlined toggle (the `filled`
 * parameter defaults to `false` in the renderer). To render a filled variant of a Material
 * Symbol icon, the [IconSchema] itself must specify the filled form. If the icon should be
 * interactive, wrap it in a tile that supports click triggers (e.g. [ButtonTileSchema]).
 */
@Immutable
@Serializable
@SerialName("Icon")
data class IconTileSchema(
    override val id: String,
    override val events: SerializableImmutableList<EventSchema>?,
    override val style: StyleSchema,
    override val searchableTerms: SerializableImmutableList<String>?,
    override val visibility: TileSchema.Visibility,
    val icon: IconSchema
) : TileSchema
