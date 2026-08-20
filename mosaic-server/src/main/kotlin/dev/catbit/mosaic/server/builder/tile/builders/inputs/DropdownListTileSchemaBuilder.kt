package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import kotlinx.collections.immutable.toImmutableList

internal class DropdownListTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val options: List<DropdownListTileSchema.SelectOption>,
    private val selectedOptionId: String,
    private val enabled: Boolean,
    private val kind: DropdownListTileSchema.Kind,
    private val supportingText: String?,
    private val state: DropdownListTileSchema.State,
) : TileSchemaBuilder<DropdownListTileSchema>() {

    override fun build(): DropdownListTileSchema {

        require(options.any {it.id == selectedOptionId}) {
          "The list of SelectOption must contains an option with id equals to $selectedOptionId"
        }

        return DropdownListTileSchema(
            id = id,
            events = EventSchemaBuilderScope().apply(events).build(),
            style = StyleSchemaBuilderScope().apply(style).buildStyle(),
            searchableTerms = searchableTerms?.toImmutableList(),
            visibility = visibility,
            options = options.toImmutableList(),
            expanded = false,
            selectedOptionId = selectedOptionId,
            enabled = enabled,
            kind = kind,
            supportingText = supportingText,
            state = state,
        )
    }
}

/**
 * Renders a Material 3 exposed dropdown menu: a read-only anchor field showing the label of the
 * currently selected option, plus a dropdown listing every entry in [options]. [kind] picks the
 * anchor style — filled or outlined. [enabled] and [supportingText] are forwarded, and [state]
 * set to error switches the field into Material's error styling. The anchor displays the label
 * of the option whose id equals [selectedOptionId] — [selectedOptionId] must match one of
 * [options]' ids, or building this tile throws. Opening, closing and item selection are handled
 * entirely on the client: tapping the anchor toggles the menu, tapping an item stores the new
 * selection and closes the menu, dismissing (tapping outside) just closes it — none of this needs
 * a round trip to the server. Dispatches `onDropdownListOpen` when the anchor is tapped while
 * closed; `onDropdownListItemSelected` (carrying the picked option's id) when an item is picked;
 * and `onDropdownListClose` whenever the menu closes — anchor tapped while open, dismissed by
 * tapping outside, or right after an item is picked. The current [selectedOptionId] can be read
 * from this tile by its [id] via `GetData`.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDropdownListOpen`, `onDropdownListClose`, `onDropdownListItemSelected`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param options Options listed in the dropdown menu, built with [selectOption].
 * @param selectedOptionId Id of the currently selected option. Must match one of [options]' ids.
 * @param enabled Whether the field is interactive. Defaults to true.
 * @param kind Visual style of the anchor field — [filledDropdownList] or [outlinedDropdownList]. Defaults to outlined.
 * @param supportingText Helper text shown below the field. Defaults to none.
 * @param state Visual state of the field — [normalDropdownList] or [errorDropdownList]. Defaults to normal.
 */
fun TileSchemaBuilderScope.DropdownList(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = visible(),
    options: List<DropdownListTileSchema.SelectOption>,
    selectedOptionId: String,
    enabled: Boolean = true,
    kind: DropdownListTileSchema.Kind = outlinedDropdownList(),
    supportingText: String? = null,
    state: DropdownListTileSchema.State = normalDropdownList(),
) {
    addBuilder(
        DropdownListTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            options = options,
            selectedOptionId = selectedOptionId,
            enabled = enabled,
            kind = kind,
            supportingText = supportingText,
            state = state,
        )
    )
}

/** One entry of a `DropdownList`'s menu — [id] is matched against `selectedOptionId`, [label] is what's displayed. */
fun selectOption(id: String, label: String) = DropdownListTileSchema.SelectOption(id = id, label = label)


/** Filled dropdown anchor field variant. */
fun filledDropdownList() = DropdownListTileSchema.Kind.FILLED

/** Outlined dropdown anchor field variant. */
fun outlinedDropdownList() = DropdownListTileSchema.Kind.OUTLINED


/** Normal (non-error) visual state. */
fun normalDropdownList() = DropdownListTileSchema.State.NORMAL

/** Error visual state — switches the field to Material's error styling. */
fun errorDropdownList() = DropdownListTileSchema.State.ERROR
