package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class CheckboxTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val checked: Boolean,
    private val enabled: Boolean
) : TileSchemaBuilder<CheckboxTileSchema>() {

    override fun build() = CheckboxTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        checked = checked,
        enabled = enabled
    )
}

/**
 * Renders a bare Material 3 checkbox reflecting [checked]. Draws only the box — no label — so
 * pair it with a `SimpleText` inside a `Row` when a caption is needed. Toggling flips [checked]
 * locally on the client (no round trip needed for the new value to take effect), then dispatches
 * `onCheck` (when it becomes checked) or `onUncheck` (when it becomes unchecked), always followed
 * by `onCheckChanged`. The current [checked] value can be read from this tile by its [id] via
 * `GetData`.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onCheck`, `onUncheck`, `onCheckChanged`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param checked Whether the checkbox starts checked. Defaults to false.
 * @param enabled Whether the checkbox is interactive. Defaults to true.
 */
fun TileSchemaBuilderScope.Checkbox(
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
    checked: Boolean = false,
    enabled: Boolean = true
) {
    addBuilder(
        CheckboxTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            checked = checked,
            enabled = enabled
        )
    )
}
