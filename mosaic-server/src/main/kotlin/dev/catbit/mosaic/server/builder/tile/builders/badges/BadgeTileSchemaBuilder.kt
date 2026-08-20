package dev.catbit.mosaic.server.builder.tile.builders.badges

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class BadgeTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val content: String?
) : TileSchemaBuilder<BadgeTileSchema>() {

    override fun build() = BadgeTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        content = content
    )
}

/**
 * Renders a Material 3 badge. When [content] is non-null it is shown as the badge's text label
 * and the badge takes its larger, pill-shaped form; when `null` the badge is the small empty
 * dot. This is a standalone badge, not attached to a sibling tile — position it yourself, e.g.
 * inside a `Box` aligned over the tile it decorates. Dispatches no triggers and is not clickable.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the badge dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param content Text label shown inside the badge. When null, the badge renders as a small empty dot. Defaults to none.
 */
fun TileSchemaBuilderScope.Badge(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    content: String? = null
) {
    addBuilder(
        BadgeTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            content = content
        )
    )
}
