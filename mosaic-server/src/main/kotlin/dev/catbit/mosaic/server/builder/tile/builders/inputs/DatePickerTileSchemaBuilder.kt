package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import kotlinx.collections.immutable.toImmutableList

internal class DatePickerTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val selectedDate: String?,
    private val enabled: Boolean,
    private val kind: DatePickerTileSchema.Kind,
    private val confirmLabel: String,
    private val cancelLabel: String,
    private val supportingText: String?,
    private val state: DatePickerTileSchema.State
) : TileSchemaBuilder<DatePickerTileSchema>() {

    override fun build() = DatePickerTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        expanded = false,
        selectedDate = selectedDate,
        enabled = enabled,
        kind = kind,
        confirmLabel = confirmLabel,
        cancelLabel = cancelLabel,
        supportingText = supportingText,
        state = state
    )
}

/**
 * Renders a read-only text field that opens a Material 3 date picker dialog when tapped. [kind]
 * picks the field style — filled or outlined. Always shows a calendar leading icon, displays
 * [selectedDate] (empty when `null`, as an ISO `yyyy-MM-dd` string), forwards [enabled] and
 * [supportingText], and switches to Material's error styling when [state] is error. Typing is
 * impossible — the field is read-only. Opening, closing and date selection are handled entirely
 * on the client: pressing the field opens the dialog, confirming stores the picked date and
 * closes it, cancelling or dismissing just closes it — none of this needs a round trip to the
 * server. The dialog's buttons are labelled with [confirmLabel] and [cancelLabel], and the
 * confirm button stays disabled until a date is picked, so confirming always yields a date.
 * Dispatches `onDatePickerOpen` when pressed while closed, `onDateSelected` (carrying the ISO
 * date string) when confirmed, and `onDatePickerClose` when pressed while open, dismissed,
 * cancelled, or right after a confirm. The current [selectedDate] can be read from this tile by
 * its [id] via `GetData` — it produces no entry at all when no date is selected.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDatePickerOpen`, `onDatePickerClose`, `onDateSelected`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param selectedDate ISO (`yyyy-MM-dd`) date shown in the field. Defaults to none.
 * @param enabled Whether the field is interactive. Defaults to true.
 * @param kind Visual style of the field — filled or outlined. Defaults to outlined.
 * @param confirmLabel Label of the dialog's confirm button.
 * @param cancelLabel Label of the dialog's cancel button.
 * @param supportingText Helper text shown below the field. Defaults to none.
 * @param state Visual state of the field — normal or error. Defaults to normal.
 */
fun TileSchemaBuilderScope.DatePicker(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = visible(),
    selectedDate: String? = null,
    enabled: Boolean = true,
    kind: DatePickerTileSchema.Kind = outlinedDatePicker(),
    confirmLabel: String,
    cancelLabel: String,
    supportingText: String? = null,
    state: DatePickerTileSchema.State = normalDatePicker()
) {
    addBuilder(
        DatePickerTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            selectedDate = selectedDate,
            enabled = enabled,
            kind = kind,
            confirmLabel = confirmLabel,
            cancelLabel = cancelLabel,
            supportingText = supportingText,
            state = state
        )
    )
}

/** Filled date picker field variant. */
fun filledDatePicker() = DatePickerTileSchema.Kind.FILLED

/** Outlined date picker field variant. */
fun outlinedDatePicker() = DatePickerTileSchema.Kind.OUTLINED


/** Normal (non-error) visual state. */
fun normalDatePicker() = DatePickerTileSchema.State.NORMAL

/** Error visual state — switches the field to Material's error styling. */
fun errorDatePicker() = DatePickerTileSchema.State.ERROR