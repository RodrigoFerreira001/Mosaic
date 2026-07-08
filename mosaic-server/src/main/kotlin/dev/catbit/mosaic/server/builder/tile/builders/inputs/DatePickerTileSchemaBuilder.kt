package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
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

fun TileSchemaBuilderScope.DatePicker(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
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

fun filledDatePicker() = DatePickerTileSchema.Kind.FILLED
fun outlinedDatePicker() = DatePickerTileSchema.Kind.OUTLINED
fun normalDatePicker() = DatePickerTileSchema.State.NORMAL
fun errorDatePicker() = DatePickerTileSchema.State.ERROR