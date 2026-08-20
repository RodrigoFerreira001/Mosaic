package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardDoneEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardGoEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardNextEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardPreviousEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSendEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLeadingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTrailingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 text input field: [Kind.FILLED] → `TextField`, [Kind.OUTLINED] →
 * `OutlinedTextField`. [label], [prefixText], [suffixText], [supportingText] and [placeholder]
 * are rendered only when non-null, [enabled], [minLines] and [maxLines] are forwarded, and
 * [state] set to [State.ERROR] switches the field into Material's error styling.
 *
 * **Text state:** the renderer keeps a local `TextFieldValue` seeded from [value]. A
 * `LaunchedEffect` on [value] re-syncs that local state whenever the server pushes a new value
 * (e.g. via `UpdateTiles`), placing the caret at the end of the new text. On each keystroke the
 * renderer dispatches a local `TextFieldTileEvents.OnTextChange` that the holder applies to its
 * own state, so the typed text survives without a round trip to the server.
 *
 * **Icons:** [leadingIcon] / [trailingIcon] render as plain icons by default. Setting
 * [clickableLeadingIcon] / [clickableTrailingIcon] wraps them in an `IconButton` that fires its
 * own trigger.
 *
 * **Keyboard:** [keyboardOptions] maps to Compose `KeyboardOptions` (capitalization, keyboard
 * type, IME action, autocorrect, show-on-focus) and [visualTransformation] to the field's
 * visual transformation — [VisualTransformation.None], [VisualTransformation.Password] (dots)
 * or [VisualTransformation.Custom] with a [VisualTransformation.Custom.mask].
 *
 * **Triggers dispatched:**
 * - `OnTextChangedEventTrigger` — on every keystroke where the text actually differs from
 *   [value]; the new text is passed as the event's incoming data.
 * - `OnKeyboardDoneEventTrigger`, `OnKeyboardGoEventTrigger`, `OnKeyboardNextEventTrigger`,
 *   `OnKeyboardPreviousEventTrigger`, `OnKeyboardSearchEventTrigger`,
 *   `OnKeyboardSendEventTrigger` — when the matching IME action is pressed (the action shown
 *   depends on [keyboardOptions]`.imeAction`).
 * - `OnLeadingIconClickEventTrigger` — only when [clickableLeadingIcon] is `true`.
 * - `OnTrailingIconClickEventTrigger` — only when [clickableTrailingIcon] is `true`.
 *
 * **Value production:** the holder exposes the current [value] under a caller-chosen key so
 * `GetData` / `EvaluateData` events can read this field by its [id]. An empty value produces
 * no entry at all rather than an empty string.
 */
@Immutable
@Triggers(
    [
        OnTextChangedEventTrigger::class,
        OnKeyboardDoneEventTrigger::class,
        OnKeyboardGoEventTrigger::class,
        OnKeyboardNextEventTrigger::class,
        OnKeyboardPreviousEventTrigger::class,
        OnKeyboardSearchEventTrigger::class,
        OnKeyboardSendEventTrigger::class,
        OnLeadingIconClickEventTrigger::class,
        OnTrailingIconClickEventTrigger::class,
    ]
)
@Serializable
@SerialName("TextField")
data class TextFieldTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("value") val value: String,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("leadingIcon") val leadingIcon: IconSchema?,
    @SerialName("clickableLeadingIcon") val clickableLeadingIcon: Boolean,
    @SerialName("trailingIcon") val trailingIcon: IconSchema?,
    @SerialName("clickableTrailingIcon") val clickableTrailingIcon: Boolean,
    @SerialName("prefixText") val prefixText: String?,
    @SerialName("suffixText") val suffixText: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("label") val label: String?,
    @SerialName("supportingText") val supportingText: String?,
    @SerialName("minLines") val minLines: Int,
    @SerialName("maxLines") val maxLines: Int,
    @SerialName("kind") val kind: Kind,
    @SerialName("state") val state: State,
    @SerialName("keyboardOptions") val keyboardOptions: KeyboardOptions?,
    @SerialName("visualTransformation") val visualTransformation: VisualTransformation?
) : TileSchema {

    enum class Kind {
        FILLED, OUTLINED
    }

    enum class State {
        NORMAL, ERROR
    }

    @Serializable
    sealed interface VisualTransformation {
        @Serializable
        @SerialName("None")
        data object None : VisualTransformation

        @Serializable
        @SerialName("Password")
        data object Password : VisualTransformation

        @Serializable
        @SerialName("Custom")
        data class Custom(
            @SerialName("mask") val mask: String
        ) : VisualTransformation
    }

    @Serializable
    data class KeyboardOptions(
        @SerialName("autoCorrectEnabled") val autoCorrectEnabled: Boolean?,
        @SerialName("showKeyboardOnFocus") val showKeyboardOnFocus: Boolean?,
        @SerialName("capitalization") val capitalization: KeyboardCapitalization,
        @SerialName("keyboardType") val keyboardType: KeyboardType,
        @SerialName("imeAction") val imeAction: ImeAction
    ) {
        @Serializable
        sealed interface KeyboardCapitalization {
            @Serializable
            @SerialName("Unspecified")
            data object Unspecified : KeyboardCapitalization

            @Serializable
            @SerialName("None")
            data object None : KeyboardCapitalization

            @Serializable
            @SerialName("Characters")
            data object Characters : KeyboardCapitalization

            @Serializable
            @SerialName("Words")
            data object Words : KeyboardCapitalization

            @Serializable
            @SerialName("Sentences")
            data object Sentences : KeyboardCapitalization
        }

        @Serializable
        sealed interface KeyboardType {
            @Serializable
            @SerialName("Unspecified")
            data object Unspecified : KeyboardType

            @Serializable
            @SerialName("Text")
            data object Text : KeyboardType

            @Serializable
            @SerialName("Ascii")
            data object Ascii : KeyboardType

            @Serializable
            @SerialName("Number")
            data object Number : KeyboardType

            @Serializable
            @SerialName("Phone")
            data object Phone : KeyboardType

            @Serializable
            @SerialName("Uri")
            data object Uri : KeyboardType

            @Serializable
            @SerialName("Email")
            data object Email : KeyboardType

            @Serializable
            @SerialName("Password")
            data object Password : KeyboardType

            @Serializable
            @SerialName("NumberPassword")
            data object NumberPassword : KeyboardType

            @Serializable
            @SerialName("Decimal")
            data object Decimal : KeyboardType
        }

        @Serializable
        sealed interface ImeAction {
            @Serializable
            @SerialName("Unspecified")
            data object Unspecified : ImeAction

            @Serializable
            @SerialName("Default")
            data object Default : ImeAction

            @Serializable
            @SerialName("None")
            data object None : ImeAction

            @Serializable
            @SerialName("Go")
            data object Go : ImeAction

            @Serializable
            @SerialName("Search")
            data object Search : ImeAction

            @Serializable
            @SerialName("Send")
            data object Send : ImeAction

            @Serializable
            @SerialName("Previous")
            data object Previous : ImeAction

            @Serializable
            @SerialName("Next")
            data object Next : ImeAction

            @Serializable
            @SerialName("Done")
            data object Done : ImeAction
        }
    }
}
