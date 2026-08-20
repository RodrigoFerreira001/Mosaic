package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.BoxTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class BoxTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val alignment: AlignmentSchema.TwoDimensional
) : TileSchemaBuilder<BoxTileSchema>() {

    override fun build() = BoxTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        alignment = alignment
    )
}

/**
 * Renders a Compose `Box` that stacks [tiles] on top of each other in declaration order (later
 * children paint above earlier ones), all positioned by [alignment]. Never scrollable, and
 * publishes no scope CompositionLocal, so children cannot use column/row scope modifiers such as
 * `weight`. Dispatches `onDisplay` once when composed; `onClick`/`onLongPress` are wired only
 * when the matching event is declared on this tile — with neither declared, the box is not made
 * interactive.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`, `onLongPress`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param alignment Alignment applied to every child. Defaults to aligned to the top start.
 * @param tiles Child tiles stacked on top of each other inside the box. Defaults to none.
 */
fun TileSchemaBuilderScope.Box(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    alignment: AlignmentSchema.TwoDimensional = alignToTopStart(),
    tiles: TileSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        BoxTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            alignment = alignment
        )
    )
}
