package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class CardTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val kind: CardTileSchema.Kind
) : TileSchemaBuilder<CardTileSchema>() {

    override fun build() = CardTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        kind = kind
    )
}

/**
 * Renders a Material 3 card hosting [tiles] stacked vertically in a column scope. [kind] picks
 * the visual style — default, elevated or outlined. Children can use column scope modifiers such
 * as `weight`. Dispatches `onDisplay` once when composed and `onClick` when the card is tapped.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param kind Visual style of the card — [defaultCard], [elevatedCard] or [outlinedCard]. Defaults to default.
 * @param tiles Child tiles stacked vertically inside the card.
 */
fun TileSchemaBuilderScope.Card(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    kind: CardTileSchema.Kind = defaultCard(),
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        CardTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            kind = kind
        )
    )
}

/** Default card variant — flat, no shadow or border. */
fun defaultCard() = CardTileSchema.Kind.DEFAULT

/** Elevated card variant — shadowed surface-colored background. */
fun elevatedCard() = CardTileSchema.Kind.ELEVATED

/** Outlined card variant — flat with an outline border. */
fun outlinedCard() = CardTileSchema.Kind.OUTLINED
