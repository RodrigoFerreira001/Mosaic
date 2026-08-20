package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallyToTop
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class FlowRowTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val horizontalArrangement: ArrangementSchema.Horizontal,
    private val verticalArrangement: ArrangementSchema.Vertical,
    private val maxItemsInEachRow: Int
) : TileSchemaBuilder<FlowRowTileSchema>() {

    override fun build() = FlowRowTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        filterChildrenByTerm = filterChildrenByTerm,
        visibility = visibility,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    )
}

/**
 * Renders a Compose `FlowRow` laying [tiles] out horizontally and wrapping onto new lines when
 * they no longer fit. [horizontalArrangement] spaces items within a line, [verticalArrangement]
 * spaces the lines themselves, and [maxItemsInEachRow] caps how many children a single line may
 * hold (unlimited by default). Publishes its flow-row scope to children so they can use
 * modifiers such as `weight` and `fillMaxRowHeight`. When [filterChildrenByTerm] is set, only
 * children whose `searchableTerms` contain that term (case-insensitive substring match) are
 * rendered — children without `searchableTerms` are filtered out. Never scrollable — every child
 * is composed eagerly. Dispatches `onDisplay` once when composed, `onClick` only when declared.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param horizontalArrangement Spacing of items within a line. Defaults to arranged to the start.
 * @param verticalArrangement Spacing between wrapped lines. Defaults to arranged to the top.
 * @param maxItemsInEachRow Maximum number of children allowed on a single line. Defaults to unlimited.
 * @param tiles Child tiles laid out horizontally, wrapping onto new lines as needed.
 */
fun TileSchemaBuilderScope.FlowRow(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    horizontalArrangement: ArrangementSchema.Horizontal = arrangeHorizontallyToStart(),
    verticalArrangement: ArrangementSchema.Vertical = arrangeVerticallyToTop(),
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        FlowRowTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            maxItemsInEachRow = maxItemsInEachRow
        )
    )
}
