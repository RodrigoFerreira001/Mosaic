package dev.catbit.mosaic.server.builder.tile.builders.chips

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class InputChipTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val selected: Boolean,
    private val leadingIcon: IconSchema?,
    private val trailingIcon: IconSchema?,
    private val enabled: Boolean,
) : TileSchemaBuilder<InputChipTileSchema>() {

    override fun build() = InputChipTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        text = text,
        selected = selected,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}

/**
 * Renders a Material 3 input chip with a toggleable [selected] state, [text] as its label and
 * optional [leadingIcon] / [trailingIcon]. Tapping the chip flips [selected] locally on the
 * client (no round trip needed for the new value to take effect), then dispatches `onCheck`
 * (when it becomes selected) or `onUncheck` (when it becomes unselected), always followed by
 * `onCheckChanged`. The trailing icon is decorative — tapping anywhere on the chip toggles it.
 * The current [selected] value can be read from this tile by its [id] via `GetData`.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onCheck`, `onUncheck`, `onCheckChanged`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param text Label displayed on the chip.
 * @param selected Whether the chip starts selected. Defaults to false.
 * @param leadingIcon Optional icon rendered before the text. Defaults to none.
 * @param trailingIcon Optional decorative icon rendered after the text. Defaults to none.
 * @param enabled Whether the chip is interactive. Defaults to true.
 */
fun TileSchemaBuilderScope.InputChip(
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
    text: String,
    selected: Boolean = false,
    leadingIcon: IconSchema? = null,
    trailingIcon: IconSchema? = null,
    enabled: Boolean = true,
) {
    addBuilder(
        InputChipTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            text = text,
            selected = selected,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
        )
    )
}
