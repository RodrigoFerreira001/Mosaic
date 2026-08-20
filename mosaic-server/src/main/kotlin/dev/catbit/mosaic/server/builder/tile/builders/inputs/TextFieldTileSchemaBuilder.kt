package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class TextFieldTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
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
        searchableTerms = searchableTerms?.toImmutableList(),
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

/**
 * Renders a Material 3 text input field. [kind] picks the visual style — filled or outlined.
 * [label], [prefixText], [suffixText], [supportingText] and [placeholder] are shown only when
 * non-null; [enabled], [minLines] and [maxLines] are forwarded; [state] set to error switches the
 * field into Material's error styling. The client keeps its own local text state seeded from
 * [value], re-syncing (and moving the caret to the end) whenever the server pushes a new [value]
 * (e.g. via `UpdateTiles`); each keystroke updates that local state without a round trip to the
 * server. [leadingIcon] / [trailingIcon] render as plain icons by default; setting
 * [clickableLeadingIcon] / [clickableTrailingIcon] wraps them in a tappable `IconButton` that
 * fires its own trigger. [keyboardOptions] configures capitalization, keyboard type, IME action,
 * autocorrect and show-on-focus; [visualTransformation] masks the displayed text (e.g. for
 * passwords). Dispatches `onTextChanged` (carrying the new text) on every keystroke where the
 * text actually differs from [value]; the matching keyboard trigger
 * (`onKeyboardDone`/`onKeyboardGo`/`onKeyboardNext`/`onKeyboardPrevious`/`onKeyboardSearch`/`onKeyboardSend`)
 * when the IME action set by [keyboardOptions] is pressed; `onLeadingIconClick` only when
 * [clickableLeadingIcon] is true; and `onTrailingIconClick` only when [clickableTrailingIcon] is
 * true. The current [value] can be read from this tile by its [id] via `GetData` — an empty value
 * produces no entry at all rather than an empty string.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onTextChanged`, keyboard actions, icon clicks).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param value Current text shown in the field. Defaults to empty.
 * @param enabled Whether the field is interactive. Defaults to true.
 * @param leadingIcon Optional icon rendered before the text. Defaults to none.
 * @param clickableLeadingIcon Whether [leadingIcon] is wrapped in a tappable icon button that fires `onLeadingIconClick`. Defaults to false.
 * @param trailingIcon Optional icon rendered after the text. Defaults to none.
 * @param clickableTrailingIcon Whether [trailingIcon] is wrapped in a tappable icon button that fires `onTrailingIconClick`. Defaults to true.
 * @param prefixText Text shown fixed before the value inside the field. Defaults to none.
 * @param suffixText Text shown fixed after the value inside the field. Defaults to none.
 * @param placeholder Text shown when the field is empty. Defaults to none.
 * @param label Floating label shown above/inside the field. Defaults to none.
 * @param supportingText Helper text shown below the field. Defaults to none.
 * @param minLines Minimum number of visible lines. Defaults to 1.
 * @param maxLines Maximum number of visible lines. Defaults to unlimited.
 * @param kind Visual style of the field — [filledTextField] or [outlinedTextField]. Defaults to outlined.
 * @param state Visual state of the field — [normalTextField] or [errorTextField]. Defaults to normal.
 * @param keyboardOptions Keyboard configuration built with [keyboardOptions]. Defaults to none (platform defaults).
 * @param visualTransformation Text masking applied to the displayed value, built with [keyboardVisualTransformationNone], [keyboardVisualTransformationPassword] or [keyboardVisualTransformationCustom]. Defaults to none.
 */
fun TileSchemaBuilderScope.TextField(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
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
    kind: TextFieldTileSchema.Kind = outlinedTextField(),
    state: TextFieldTileSchema.State = normalTextField(),
    keyboardOptions: TextFieldTileSchema.KeyboardOptions? = null,
    visualTransformation: TextFieldTileSchema.VisualTransformation? = null
) {
    addBuilder(
        TextFieldTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
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

/** Filled text field variant. */
fun filledTextField() = TextFieldTileSchema.Kind.FILLED

/** Outlined text field variant. */
fun outlinedTextField() = TextFieldTileSchema.Kind.OUTLINED


/** Normal (non-error) visual state. */
fun normalTextField() = TextFieldTileSchema.State.NORMAL

/** Error visual state — switches the field to Material's error styling. */
fun errorTextField() = TextFieldTileSchema.State.ERROR


/**
 * Builds the keyboard configuration for a `TextField`.
 *
 * @param autoCorrectEnabled Whether autocorrect is enabled. Defaults to none (platform default).
 * @param howKeyboardOnFocus Whether the keyboard is shown automatically on focus. Defaults to none (platform default).
 * @param capitalization Auto-capitalization strategy. Defaults to unspecified.
 * @param keyboardType Keyboard layout shown (text, number, email, phone, etc). Defaults to unspecified.
 * @param imeAction IME action shown on the keyboard, which determines which keyboard trigger can fire. Defaults to unspecified.
 */
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


/** No forced capitalization strategy — platform default. */
fun keyboardCapitalizationUnspecified() =
    TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Unspecified

/** Does not auto-capitalize typed text. */
fun keyboardCapitalizationNone() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.None

/** Auto-capitalizes every character as it's typed. */
fun keyboardCapitalizationCharacters() =
    TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Characters

/** Auto-capitalizes the first letter of each word. */
fun keyboardCapitalizationWords() = TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Words

/** Auto-capitalizes the first letter of each sentence. */
fun keyboardCapitalizationSentences() =
    TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Sentences


/** No forced keyboard layout — platform default. */
fun keyboardTypeUnspecified() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Unspecified

/** General text keyboard layout. */
fun keyboardTypeText() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Text

/** ASCII-only text keyboard layout. */
fun keyboardTypeAscii() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Ascii

/** Numeric keyboard layout. */
fun keyboardTypeNumber() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Number

/** Phone-number keyboard layout. */
fun keyboardTypePhone() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Phone

/** URI keyboard layout, optimized for entering URLs. */
fun keyboardTypeUri() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Uri

/** Email keyboard layout, optimized for entering email addresses. */
fun keyboardTypeEmail() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Email

/** Password keyboard layout, disabling autocorrect/suggestions. */
fun keyboardTypePassword() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Password

/** Numeric password keyboard layout. */
fun keyboardTypeNumberPassword() = TextFieldTileSchema.KeyboardOptions.KeyboardType.NumberPassword

/** Decimal-number keyboard layout, allowing a decimal separator. */
fun keyboardTypeDecimal() = TextFieldTileSchema.KeyboardOptions.KeyboardType.Decimal


/** No forced IME action — platform default; no keyboard trigger can fire. */
fun keyboardImeActionUnspecified() = TextFieldTileSchema.KeyboardOptions.ImeAction.Unspecified

/** Default IME action for the current input; no keyboard trigger can fire. */
fun keyboardImeActionDefault() = TextFieldTileSchema.KeyboardOptions.ImeAction.Default

/** No IME action shown; no keyboard trigger can fire. */
fun keyboardImeActionNone() = TextFieldTileSchema.KeyboardOptions.ImeAction.None

/** "Go" IME action — pressing it fires `onKeyboardGo`. */
fun keyboardImeActionGo() = TextFieldTileSchema.KeyboardOptions.ImeAction.Go

/** "Search" IME action — pressing it fires `onKeyboardSearch`. */
fun keyboardImeActionSearch() = TextFieldTileSchema.KeyboardOptions.ImeAction.Search

/** "Send" IME action — pressing it fires `onKeyboardSend`. */
fun keyboardImeActionSend() = TextFieldTileSchema.KeyboardOptions.ImeAction.Send

/** "Previous" IME action — pressing it fires `onKeyboardPrevious`. */
fun keyboardImeActionPrevious() = TextFieldTileSchema.KeyboardOptions.ImeAction.Previous

/** "Next" IME action — pressing it fires `onKeyboardNext`. */
fun keyboardImeActionNext() = TextFieldTileSchema.KeyboardOptions.ImeAction.Next

/** "Done" IME action — pressing it fires `onKeyboardDone`. */
fun keyboardImeActionDone() = TextFieldTileSchema.KeyboardOptions.ImeAction.Done


/** No visual transformation — text is shown as typed. */
fun keyboardVisualTransformationNone() = TextFieldTileSchema.VisualTransformation.None

/** Masks every character as a dot, for password fields. */
fun keyboardVisualTransformationPassword() = TextFieldTileSchema.VisualTransformation.Password

/** Applies a custom [mask] pattern (e.g. `"###-###"`) to the displayed text. */
fun keyboardVisualTransformationCustom(mask: String) =
    TextFieldTileSchema.VisualTransformation.Custom(mask)
