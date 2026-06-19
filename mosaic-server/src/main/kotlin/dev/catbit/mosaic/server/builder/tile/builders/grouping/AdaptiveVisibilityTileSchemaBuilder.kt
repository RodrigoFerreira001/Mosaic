package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.HeightBreakpoint
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.HeightVisibility
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.WidthBreakpoint
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.WidthVisibility
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class AdaptiveVisibilityTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val widthVisibility: WidthVisibility,
    private val heightVisibility: HeightVisibility,
) : TileSchemaBuilder<AdaptiveVisibilityTileSchema>() {

    override fun build() = AdaptiveVisibilityTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        widthVisibility = widthVisibility,
        heightVisibility = heightVisibility,
    )
}

fun TileSchemaBuilderScope.AdaptiveVisibility(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    searchableTerms: List<String>? = null,
    widthVisibility: WidthVisibility = widthVisibleUntilExtraLarge(),
    heightVisibility: HeightVisibility = heightVisibleUntilExpanded(),
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        AdaptiveVisibilityTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            widthVisibility = widthVisibility,
            heightVisibility = heightVisibility,
        )
    )
}

fun widthVisibleFromCompact() = WidthVisibility.VisibleFrom(WidthBreakpoint.Compact)
fun widthVisibleFromMedium() = WidthVisibility.VisibleFrom(WidthBreakpoint.Medium)
fun widthVisibleFromExpanded() = WidthVisibility.VisibleFrom(WidthBreakpoint.Expanded)
fun widthVisibleFromLarge() = WidthVisibility.VisibleFrom(WidthBreakpoint.Large)
fun widthVisibleFromExtraLarge() = WidthVisibility.VisibleFrom(WidthBreakpoint.ExtraLarge)

fun widthVisibleUntilCompact() = WidthVisibility.VisibleUntil(WidthBreakpoint.Compact)
fun widthVisibleUntilMedium() = WidthVisibility.VisibleUntil(WidthBreakpoint.Medium)
fun widthVisibleUntilExpanded() = WidthVisibility.VisibleUntil(WidthBreakpoint.Expanded)
fun widthVisibleUntilLarge() = WidthVisibility.VisibleUntil(WidthBreakpoint.Large)
fun widthVisibleUntilExtraLarge() = WidthVisibility.VisibleUntil(WidthBreakpoint.ExtraLarge)

fun heightVisibleFromCompact() = HeightVisibility.VisibleFrom(HeightBreakpoint.Compact)
fun heightVisibleFromMedium() = HeightVisibility.VisibleFrom(HeightBreakpoint.Medium)
fun heightVisibleFromExpanded() = HeightVisibility.VisibleFrom(HeightBreakpoint.Expanded)

fun heightVisibleUntilCompact() = HeightVisibility.VisibleUntil(HeightBreakpoint.Compact)
fun heightVisibleUntilMedium() = HeightVisibility.VisibleUntil(HeightBreakpoint.Medium)
fun heightVisibleUntilExpanded() = HeightVisibility.VisibleUntil(HeightBreakpoint.Expanded)
