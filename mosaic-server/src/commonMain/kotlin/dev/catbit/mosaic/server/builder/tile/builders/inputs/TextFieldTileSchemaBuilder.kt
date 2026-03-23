package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class TextFieldTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val value: String,
    private val enabled: Boolean,
    private val leadingIcon: IconSchema?,
    private val trailingIcon: IconSchema?,
    private val prefixText: String?,
    private val suffixText: String?,
    private val placeholder: String?,
    private val label: String?,
    private val supportingText: String?,
    private val maxLength: Int,
    private val minLines: Int,
    private val maxLines: Int,
    private val kind: TextFieldTileSchema.Kind,
    private val state: TextFieldTileSchema.State,
    private val keyboardOptions: TextFieldTileSchema.KeyboardOptions?,
    private val visualTransformation: TextFieldTileSchema.VisualTransformation?
) : TileSchemaBuilder<TextFieldTileSchema> {

    override fun build() = TextFieldTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        value = value,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefixText = prefixText,
        suffixText = suffixText,
        placeholder = placeholder,
        label = label,
        supportingText = supportingText,
        maxLength = maxLength,
        minLines = minLines,
        maxLines = maxLines,
        kind = kind,
        state = state,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

fun TileSchemaBuilderScope.TextField(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    value: String = "",
    enabled: Boolean = true,
    leadingIcon: IconSchema? = null,
    trailingIcon: IconSchema? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    placeholder: String? = null,
    label: String? = null,
    supportingText: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    kind: TextFieldTileSchema.Kind = TextFieldTileSchema.Kind.FILLED,
    state: TextFieldTileSchema.State = TextFieldTileSchema.State.NORMAL,
    keyboardOptions: TextFieldTileSchema.KeyboardOptions? = null,
    visualTransformation: TextFieldTileSchema.VisualTransformation? = null
) {
    addBuilder(
        TextFieldTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            value = value,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefixText = prefixText,
            suffixText = suffixText,
            placeholder = placeholder,
            label = label,
            supportingText = supportingText,
            maxLength = maxLength,
            minLines = minLines,
            maxLines = maxLines,
            kind = kind,
            state = state,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    )
}
