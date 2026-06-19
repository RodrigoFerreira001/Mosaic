package dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 [BottomAppBar] that contains a row of [actions] tiles on the left
 * side and an optional [floatingActionButton] tile anchored to the right. The FAB slot
 * accepts any tile, but is typically used with a FAB tile schema.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `actions: SerializableImmutableList<TileSchema>`,
 * `floatingActionButton: TileSchema?`
 *
 * **Triggers dispatched:** None directly. Interaction triggers are dispatched by child tiles
 * placed in [actions] or [floatingActionButton].
 *
 * **Notes:** The [actions] tiles are rendered inside a [RowScope] provided by [BottomAppBar],
 * so children that need row-scoped modifiers (e.g. `weight`) will need the [LocalRowScope]
 * CompositionLocal. The [floatingActionButton] tile is rendered as an independent composable
 * lambda; if `null`, no FAB slot is shown.
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