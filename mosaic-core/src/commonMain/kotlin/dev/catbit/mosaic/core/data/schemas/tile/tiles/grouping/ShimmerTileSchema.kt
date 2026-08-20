package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a `Box` hosting [tiles] with a continuously animated shimmer applied over its whole
 * subtree (via the `compose-shimmer` library), the usual way to build a skeleton/loading
 * placeholder out of plain tiles.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 *
 * **Notes:** the shimmer is purely visual — children stay interactive, so build the placeholder
 * out of non-clickable tiles. The tile is never clickable itself, and children are laid out with
 * `Box` semantics (stacked, no scope CompositionLocal).
 */
@Immutable
@Triggers([OnDisplayEventTrigger::class])
@Serializable
@SerialName("Shimmer")
data class ShimmerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
) : TileSchema

//https://github.com/valentinilk/compose-shimmer