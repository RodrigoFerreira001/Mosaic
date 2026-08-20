package dev.catbit.mosaic.server.builder.tile.builders.app_bars

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class BottomAppBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val actions: TileSchemaBuilderScope.() -> Unit,
    private val floatingActionButton: (TileSchemaBuilderScope.() -> Unit)?
) : TileSchemaBuilder<BottomAppBarTileSchema>() {

    override fun build() = BottomAppBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        actions = TileSchemaBuilderScope().apply(actions).build(),
        floatingActionButton = floatingActionButton?.let {
            TileSchemaBuilderScope().apply(it).build().lastOrNull()
        }
    )
}

/**
 * Renders a Material 3 bottom app bar with [actions] laid out on the leading side and an
 * optional [floatingActionButton] docked at the trailing edge. The bar itself dispatches no
 * triggers and is not clickable — wire events on the action tiles instead.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the bar dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param floatingActionButton Tile (typically a `FloatingActionButton`) docked at the trailing edge. Defaults to none.
 * @param actions Tiles laid out on the leading side of the bar. Defaults to none.
 */
fun TileSchemaBuilderScope.BottomAppBar(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    floatingActionButton: (TileSchemaBuilderScope.() -> Unit)? = null,
    actions: TileSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        BottomAppBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            actions = actions,
            floatingActionButton = floatingActionButton
        )
    )
}
