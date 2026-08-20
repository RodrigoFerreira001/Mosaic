package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class FloatingActionButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val icon: IconSchema,
    private val size: FloatingActionButtonTileSchema.Size
) : TileSchemaBuilder<FloatingActionButtonTileSchema>() {

    override fun build() = FloatingActionButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        icon = when(size) {
            FloatingActionButtonTileSchema.Size.DEFAULT -> icon.copy(size = 24)
            FloatingActionButtonTileSchema.Size.MEDIUM -> icon.copy(size = 28)
            FloatingActionButtonTileSchema.Size.LARGE -> icon.copy(size = 36)
        },
        size = size
    )
}

/**
 * Renders a Material 3 floating action button. Its size is picked by [size] (default/small,
 * medium or large), which also scales [icon] accordingly (24dp, 28dp or 36dp). Dispatches
 * `onClick` when tapped.
 *
 * There's no `enabled`/`loading` here — Material 3 FABs don't take an `enabled` parameter by
 * design, since a disabled FAB fights the emphasis it's meant to carry. Hide the FAB via
 * [visibility] instead when its action isn't currently available.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param icon Icon rendered inside the FAB; its size is overridden based on [size].
 * @param size Size of the FAB — [defaultFloatingActionButon] (small), [mediumFloatingActionButon] or [largeFloatingActionButon]. Defaults to default.
 */
fun TileSchemaBuilderScope.FloatingActionButton(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = wrapHorizontally(),
            height = wrapVertically()
        )
    },
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    icon: IconSchema,
    size: FloatingActionButtonTileSchema.Size = defaultFloatingActionButon()
) {
    addBuilder(
        FloatingActionButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            icon = icon,
            size = size
        )
    )
}

/** Default (small) FAB size — icon scaled to 24dp. */
fun defaultFloatingActionButon() = FloatingActionButtonTileSchema.Size.DEFAULT

/** Medium FAB size — icon scaled to 28dp. */
fun mediumFloatingActionButon() = FloatingActionButtonTileSchema.Size.MEDIUM

/** Large FAB size — icon scaled to 36dp. */
fun largeFloatingActionButon() = FloatingActionButtonTileSchema.Size.LARGE