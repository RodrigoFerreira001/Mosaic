package dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 top app bar in one of four styles controlled by [barStyle]:
 * - `DEFAULT` → [TopAppBar] (small, title left-aligned)
 * - `CENTER_ALIGNED` → [CenterAlignedTopAppBar] (small, title centered)
 * - `MEDIUM` → [MediumTopAppBar] (medium height, collapsible)
 * - `LARGE` → [LargeTopAppBar] (large height, collapsible)
 *
 * The [title] slot accepts any [TileSchema] as the title composable. The [navigationIcon]
 * slot accepts an optional tile rendered as the leading icon (e.g. a back button). The
 * [actions] slot accepts an optional list of tiles rendered as trailing action icons.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `title: TileSchema`, `navigationIcon: TileSchema?`,
 * `actions: SerializableImmutableList<TileSchema>?`, `barStyle: TopAppBarStyle`
 *
 * **Triggers dispatched:** None directly. Interaction triggers (clicks, etc.) are dispatched
 * by child tiles placed in [navigationIcon] or [actions].
 *
 * **Notes:** The title, navigation icon, and action tiles are rendered as Composable lambdas
 * passed to the Material 3 app bar components. This means those slots are not part of the
 * normal `RenderChildren` hierarchy — each slot tile is rendered independently via
 * `RenderChild`.
 */
@Immutable
@Serializable
@SerialName("TopAppBar")
data class TopAppBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("title") val title: TileSchema,
    @SerialName("navigationIcon") val navigationIcon: TileSchema? = null,
    @SerialName("actions") val actions: SerializableImmutableList<TileSchema>? = null,
    @SerialName("barStyle") val barStyle: TopAppBarStyle = TopAppBarStyle.DEFAULT
) : TileSchema {

    enum class TopAppBarStyle {
        DEFAULT, CENTER_ALIGNED, MEDIUM, LARGE
    }
}
