package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Wraps its child tiles in Compose's [SelectionContainer], letting the user select and copy
 * text rendered by any descendant `SimpleText` (or other selectable content) as a single
 * contiguous selection spanning multiple children.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`
 *
 * **Notes:** Purely a selection boundary — it does not lay out its children (no arrangement or
 * alignment), unlike [ColumnTileSchema]/[RowTileSchema]/[BoxTileSchema]. Does not dispatch any
 * triggers — no click/long-press/display support, so the long-press gesture is left free for
 * initiating text selection.
 */
@Immutable
@Serializable
@SerialName("SelectionContainer")
data class SelectionContainerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility
) : TileSchema
