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
import dev.catbit.mosaic.server.builder.tile.visible
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

/**
 * Conditionally renders [tiles] based on the current window size class. Children are composed
 * only when **both** [widthVisibility] and [heightVisibility] are satisfied — otherwise nothing
 * is emitted (the children are not composed, not merely hidden). `VisibleFrom(breakpoint)` shows
 * from the rank above the given breakpoint upwards (exclusive); `VisibleUntil(breakpoint)` shows
 * at or below it (inclusive). Width and height are evaluated independently, so one may be
 * satisfied while the other is not — in that case the children still stay hidden. When both
 * conditions hold, the children are hosted in a `Box` carrying [style] and [visibility].
 * Dispatches `onDisplay` once when composed regardless of the breakpoints, and dispatches the
 * width/height satisfied/not-satisfied triggers on first composition and on every condition
 * change.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, width/height breakpoint satisfied/not-satisfied).
 * @param style Layout/appearance modifiers applied to the wrapping `Box` when children are shown.
 * @param visibility Whether the wrapping `Box` is shown, hidden but occupies space, or removed from layout, when children are shown. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param widthVisibility Width breakpoint condition that must hold for the children to render. Defaults to visible until extra large.
 * @param heightVisibility Height breakpoint condition that must hold for the children to render. Defaults to visible until expanded.
 * @param tiles Child tiles rendered only when both breakpoint conditions are satisfied.
 */
fun TileSchemaBuilderScope.AdaptiveVisibility(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
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

/** Visible from the Compact width breakpoint upwards (i.e. always, since Compact is the lowest rank). */
fun widthVisibleFromCompact() = WidthVisibility.VisibleFrom(WidthBreakpoint.Compact)

/** Visible from above the Medium width breakpoint (Expanded and up). */
fun widthVisibleFromMedium() = WidthVisibility.VisibleFrom(WidthBreakpoint.Medium)

/** Visible from above the Expanded width breakpoint (Large and up). */
fun widthVisibleFromExpanded() = WidthVisibility.VisibleFrom(WidthBreakpoint.Expanded)

/** Visible from above the Large width breakpoint (ExtraLarge only). */
fun widthVisibleFromLarge() = WidthVisibility.VisibleFrom(WidthBreakpoint.Large)

/** Visible from above the ExtraLarge width breakpoint (never satisfied — ExtraLarge is the highest rank). */
fun widthVisibleFromExtraLarge() = WidthVisibility.VisibleFrom(WidthBreakpoint.ExtraLarge)

/** Visible up to and including the Compact width breakpoint. */
fun widthVisibleUntilCompact() = WidthVisibility.VisibleUntil(WidthBreakpoint.Compact)

/** Visible up to and including the Medium width breakpoint. */
fun widthVisibleUntilMedium() = WidthVisibility.VisibleUntil(WidthBreakpoint.Medium)

/** Visible up to and including the Expanded width breakpoint. */
fun widthVisibleUntilExpanded() = WidthVisibility.VisibleUntil(WidthBreakpoint.Expanded)

/** Visible up to and including the Large width breakpoint. */
fun widthVisibleUntilLarge() = WidthVisibility.VisibleUntil(WidthBreakpoint.Large)

/** Visible up to and including the ExtraLarge width breakpoint (i.e. always, since ExtraLarge is the highest rank). */
fun widthVisibleUntilExtraLarge() = WidthVisibility.VisibleUntil(WidthBreakpoint.ExtraLarge)


/** Visible from the Compact height breakpoint upwards (i.e. always, since Compact is the lowest rank). */
fun heightVisibleFromCompact() = HeightVisibility.VisibleFrom(HeightBreakpoint.Compact)

/** Visible from above the Medium height breakpoint (Expanded only). */
fun heightVisibleFromMedium() = HeightVisibility.VisibleFrom(HeightBreakpoint.Medium)

/** Visible from above the Expanded height breakpoint (never satisfied — Expanded is the highest rank). */
fun heightVisibleFromExpanded() = HeightVisibility.VisibleFrom(HeightBreakpoint.Expanded)

/** Visible up to and including the Compact height breakpoint. */
fun heightVisibleUntilCompact() = HeightVisibility.VisibleUntil(HeightBreakpoint.Compact)

/** Visible up to and including the Medium height breakpoint. */
fun heightVisibleUntilMedium() = HeightVisibility.VisibleUntil(HeightBreakpoint.Medium)

/** Visible up to and including the Expanded height breakpoint (i.e. always, since Expanded is the highest rank). */
fun heightVisibleUntilExpanded() = HeightVisibility.VisibleUntil(HeightBreakpoint.Expanded)
