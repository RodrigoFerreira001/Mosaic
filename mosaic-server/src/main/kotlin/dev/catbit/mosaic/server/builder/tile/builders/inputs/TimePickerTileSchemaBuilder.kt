package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
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

fun TileSchemaBuilderScope.TimePicker(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
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

fun filledTimePicker() = TimePickerTileSchema.Kind.FILLED
fun outlinedTimePicker() = TimePickerTileSchema.Kind.OUTLINED
fun normalTimePicker() = TimePickerTileSchema.State.NORMAL
fun errorTimePicker() = TimePickerTileSchema.State.ERROR
