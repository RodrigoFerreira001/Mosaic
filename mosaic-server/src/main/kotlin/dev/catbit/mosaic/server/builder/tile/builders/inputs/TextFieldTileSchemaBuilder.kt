package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class TextFieldTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val value: String,
    private val enabled: Boolean,
    private val leadingIcon: IconSchema?,
    private val clickableLeadingIcon: Boolean,
    private val trailingIcon: IconSchema?,
    private val clickableTrailingIcon: Boolean,
    private val prefixText: String?,
    private val suffixText: String?,
    private val placeholder: String?,
    private val label: String?,
    private val supportingText: String?,
    private val minLines: Int,
    private val maxLines: Int,
    private val kind: TextFieldTileSchema.Kind,
    private val state: TextFieldTileSchema.State,
    private val keyboardOptions: TextFieldTileSchema.KeyboardOptions?,
    private val visualTransformation: TextFieldTileSchema.VisualTransformation?
) : TileSchemaBuilder<TextFieldTileSchema>() {

    override fun build() = TextFieldTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        value = value,
        enabled = enabled,
        leadingIcon = leadingIcon,
        clickableLeadingIcon = clickableLeadingIcon,
        trailingIcon = trailingIcon,
        clickableTrailingIcon = clickableTrailingIcon,
        prefixText = prefixText,
        suffixText = suffixText,
        placeholder = placeholder,
        label = label,
        supportingText = supportingText,
        minLines = minLines,
        maxLines = maxLines,
        kind = kind,
        state = state,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

fun TileSchemaBuilderScope.TextField(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    value: String = "",
    enabled: Boolean = true,
    leadingIcon: IconSchema? = null,
    clickableLeadingIcon: Boolean = false,
    trailingIcon: IconSchema? = null,
    clickableTrailingIcon: Boolean = true,
    prefixText: String? = null,
    suffixText: String? = null,
    placeholder: String? = null,
    label: String? = null,
    supportingText: String? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    kind: TextFieldTileSchema.Kind = TextFieldTileSchema.Kind.OUTLINED,
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
            clickableLeadingIcon = clickableLeadingIcon,
            trailingIcon = trailingIcon,
            clickableTrailingIcon = clickableTrailingIcon,
            prefixText = prefixText,
            suffixText = suffixText,
            placeholder = placeholder,
            label = label,
            supportingText = supportingText,
            minLines = minLines,
            maxLines = maxLines,
            kind = kind,
            state = state,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    )
}

fun filledTextField() = TextFieldTileSchema.Kind.FILLED
fun outlinedTextField() = TextFieldTileSchema.Kind.OUTLINED

fun keyboardOptions(
    autoCorrectEnabled: Boolean? = null,
    howKeyboardOnFocus: Boolean? = null,
    capitalization: TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization = keyboardCapitalizationUnspecified(),
    keyboardType: TextFieldTileSchema.KeyboardOptions.KeyboardType = keyboardTypeUnspecified(),
    imeAction: TextFieldTileSchema.KeyboardOptions.ImeAction = keyboardImeActionUnspecified()
) = TextFieldTileSchema.KeyboardOptions(
    autoCorrectEnabled = autoCorrectEnabled,
    showKeyboardOnFocus = howKeyboardOnFocus,
    capitalization = capitalization,
    keyboardType = keyboardType,
    imeAction = imeAction,
)

fun keyboardCapitalizationUnspecified() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Unspecified
fun keyboardCapitalizationNone() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.None
fun keyboardCapitalizationCharacters() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Characters
fun keyboardCapitalizationWords() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Words
fun keyboardCapitalizationSentences() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Sentences

fun keyboardTypeUnspecified() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Unspecified
fun keyboardTypeText() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Text
fun keyboardTypeAscii() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Ascii
fun keyboardTypeNumber() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Number
fun keyboardTypePhone() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Phone
fun keyboardTypeUri() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Uri
fun keyboardTypeEmail() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Email
fun keyboardTypePassword() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Password
fun keyboardTypeNumberPassword() = TextFieldTileSchema.KeyboardOptions.KeyboardType.NumberPassword
fun keyboardTypeDecimal() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Decimal

fun keyboardImeActionUnspecified() = TextFieldTileSchema.KeyboardOptions.ImeAction.Unspecified
fun keyboardImeActionDefault() = TextFieldTileSchema.KeyboardOptions.ImeAction.Default
fun keyboardImeActionNone() = TextFieldTileSchema.KeyboardOptions.ImeAction.None
fun keyboardImeActionGo() = TextFieldTileSchema.KeyboardOptions.ImeAction.Go
fun keyboardImeActionSearch() = TextFieldTileSchema.KeyboardOptions.ImeAction.Search
fun keyboardImeActionSend() = TextFieldTileSchema.KeyboardOptions.ImeAction.Send
fun keyboardImeActionPrevious() = TextFieldTileSchema.KeyboardOptions.ImeAction.Previous
fun keyboardImeActionNext() = TextFieldTileSchema.KeyboardOptions.ImeAction.Next
fun keyboardImeActionDone() = TextFieldTileSchema.KeyboardOptions.ImeAction.Done

fun keyboardVisualTransformationNone() = TextFieldTileSchema.VisualTransformation.None
fun keyboardVisualTransformationPassword() = TextFieldTileSchema.VisualTransformation.Password
fun keyboardVisualTransformationCustom(mask: String) = TextFieldTileSchema.VisualTransformation.Custom(mask)
