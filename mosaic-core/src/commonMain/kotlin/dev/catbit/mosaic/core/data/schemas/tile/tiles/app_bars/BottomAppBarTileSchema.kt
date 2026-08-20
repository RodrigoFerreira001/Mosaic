package dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `BottomAppBar` with [actions] laid out in a `RowScope` on the leading
 * side and an optional [floatingActionButton] docked at the trailing edge (any tile, typically
 * a `FloatingActionButton`).
 *
 * **Triggers dispatched:** none. The bar itself emits no trigger and is not clickable, so any
 * `events` declared on it are never fired — wire events on the action tiles instead.
 */
@Immutable
@Serializable
@SerialName("BottomAppBar")
data class BottomAppBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("actions") val actions: SerializableImmutableList<TileSchema>,
    @SerialName("floatingActionButton") val floatingActionButton: TileSchema? = null
) : TileSchema