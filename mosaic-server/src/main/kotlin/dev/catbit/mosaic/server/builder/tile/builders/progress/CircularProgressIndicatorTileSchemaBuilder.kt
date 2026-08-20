package dev.catbit.mosaic.server.builder.tile.builders.progress

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class CircularProgressIndicatorTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val progress: Float?
) : TileSchemaBuilder<CircularProgressIndicatorTileSchema>() {

    override fun build() = CircularProgressIndicatorTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        progress = progress
    )
}

/**
 * Renders a Material 3 circular progress indicator. When [progress] is `null` the indicator is
 * indeterminate (endless spinner); when set, it is determinate and shows that fraction, where
 * `0f` is empty and `1f` is complete. Colors and stroke are Material defaults — only [style] is
 * applied. Dispatches no triggers and is not clickable. Drive a determinate indicator by pushing
 * new [progress] values with `UpdateTiles`.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the tile dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to a fixed 48dp square.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param progress Determinate progress fraction, from 0f to 1f. Defaults to none (indeterminate).
 */
fun TileSchemaBuilderScope.CircularProgressIndicator(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = fixedHorizontally(48),
            height = fixedVertically(48)
        )
    },
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    progress: Float? = null
) {
    addBuilder(
        CircularProgressIndicatorTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            progress = progress
        )
    )
}
