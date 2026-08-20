package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class RadioButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val selected: Boolean,
    private val enabled: Boolean,
    private val groupId: String
) : TileSchemaBuilder<RadioButtonTileSchema>() {

    override fun build() = RadioButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        selected = selected,
        enabled = enabled,
        groupId = groupId
    )
}

/**
 * Renders a bare Material 3 radio button reflecting [selected]. Draws only the radio circle — no
 * label — so pair it with a `SimpleText` inside a `Row` when a caption is needed. Tapping the
 * button applies mutual exclusion entirely on the client: every radio tile sharing the same
 * [groupId] sets its own selected state to whether its id matches the tapped tile's id, with no
 * server round trip and no `UpdateTiles` needed to clear the previously selected button; radios
 * with a different [groupId] are unaffected. Dispatches `onSelect` only on the radio button that
 * becomes selected — tapping an already-selected radio fires nothing, and radios that become
 * deselected fire nothing either. The current [selected] value can be read from this tile by its
 * [id] via `GetData`.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onSelect`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param selected Whether the radio button starts selected. Defaults to false.
 * @param enabled Whether the radio button is interactive. Defaults to true.
 * @param groupId Identifier shared by every radio button that mutually excludes each other.
 */
fun TileSchemaBuilderScope.RadioButton(
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
    selected: Boolean = false,
    enabled: Boolean = true,
    groupId: String
) {
    addBuilder(
        RadioButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            selected = selected,
            enabled = enabled,
            groupId = groupId
        )
    )
}
