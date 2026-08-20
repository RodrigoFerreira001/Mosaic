package dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 top app bar. [barStyle] picks the composable:
 * [TopAppBarStyle.DEFAULT] → `TopAppBar`, [TopAppBarStyle.CENTER_ALIGNED] →
 * `CenterAlignedTopAppBar`, [TopAppBarStyle.MEDIUM] → `MediumTopAppBar`,
 * [TopAppBarStyle.LARGE] → `LargeTopAppBar`.
 *
 * **Slots:** [title] is rendered in the title slot (any tile, most often a `SimpleText`),
 * [navigationIcon] in the leading slot, and [actions] in the trailing slot, laid out inside a
 * `RowScope`. Both [navigationIcon] and [actions] are optional and render as empty slots when
 * omitted.
 *
 * **Triggers dispatched:** none. The bar itself emits no trigger and is not clickable, so any
 * `events` declared on it are never fired — wire events on the slot tiles (typically
 * `IconButton`s) instead.
 *
 * **Notes:** the bar has no scroll behavior, so the medium and large variants do not collapse
 * as the content scrolls.
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
