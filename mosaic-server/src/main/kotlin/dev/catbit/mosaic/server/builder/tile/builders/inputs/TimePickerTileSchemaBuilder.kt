package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import kotlinx.collections.immutable.toImmutableList

internal class TimePickerTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val selectedTime: String?,
    private val enabled: Boolean,
    private val kind: TimePickerTileSchema.Kind,
    private val confirmLabel: String,
    private val cancelLabel: String,
    private val supportingText: String?,
    private val state: TimePickerTileSchema.State
) : TileSchemaBuilder<TimePickerTileSchema>() {

    override fun build() = TimePickerTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        expanded = false,
        selectedTime = selectedTime,
        enabled = enabled,
        kind = kind,
        confirmLabel = confirmLabel,
        cancelLabel = cancelLabel,
        supportingText = supportingText,
        state = state
    )
}

/**
 * Renders a read-only text field that opens a Material 3 time picker dialog when tapped. [kind]
 * picks the field style — filled or outlined. Always shows an alarm leading icon, displays
 * [selectedTime] (empty when `null`, as an ISO `HH:mm` string), forwards [enabled] and
 * [supportingText], and switches to Material's error styling when [state] is error. Typing is
 * impossible — the field is read-only. The dialog is always 24-hour, uses the vertical layout,
 * and opens at `00:00` when [selectedTime] is `null`. Opening, closing and time selection are
 * handled entirely on the client: pressing the field opens the dialog, confirming stores the
 * picked time and closes it, cancelling or dismissing just closes it — none of this needs a round
 * trip to the server. The dialog's buttons are labelled with [confirmLabel] and [cancelLabel].
 * Dispatches `onTimePickerOpen` when pressed while closed, `onTimeSelected` (carrying the ISO
 * time string) when confirmed, and `onTimePickerClose` when pressed while open, dismissed,
 * cancelled, or right after a confirm. The current [selectedTime] can be read from this tile by
 * its [id] via `GetData` — it produces no entry at all when no time is selected.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onTimePickerOpen`, `onTimePickerClose`, `onTimeSelected`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param selectedTime ISO (`HH:mm`) time shown in the field. Defaults to none.
 * @param enabled Whether the field is interactive. Defaults to true.
 * @param kind Visual style of the field — filled or outlined. Defaults to outlined.
 * @param confirmLabel Label of the dialog's confirm button.
 * @param cancelLabel Label of the dialog's cancel button.
 * @param supportingText Helper text shown below the field. Defaults to none.
 * @param state Visual state of the field — normal or error. Defaults to normal.
 */
fun TileSchemaBuilderScope.TimePicker(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = visible(),
    selectedTime: String? = null,
    enabled: Boolean = true,
    kind: TimePickerTileSchema.Kind = outlinedTimePicker(),
    confirmLabel: String,
    cancelLabel: String,
    supportingText: String? = null,
    state: TimePickerTileSchema.State = normalTimePicker()
) {
    addBuilder(
        TimePickerTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            selectedTime = selectedTime,
            enabled = enabled,
            kind = kind,
            confirmLabel = confirmLabel,
            cancelLabel = cancelLabel,
            supportingText = supportingText,
            state = state
        )
    )
}

/** Filled time picker field variant. */
fun filledTimePicker() = TimePickerTileSchema.Kind.FILLED

/** Outlined time picker field variant. */
fun outlinedTimePicker() = TimePickerTileSchema.Kind.OUTLINED


/** Normal (non-error) visual state. */
fun normalTimePicker() = TimePickerTileSchema.State.NORMAL

/** Error visual state — switches the field to Material's error styling. */
fun errorTimePicker() = TimePickerTileSchema.State.ERROR
