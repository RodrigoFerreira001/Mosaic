package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardDoneEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardGoEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardNextEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardPreviousEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSendEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 text input field in either a filled or outlined visual style controlled
 * by [kind]. Supports rich decoration (icons, labels, prefix/suffix, supporting text) and
 * full keyboard configuration.
 *
 * **Updatable fields (via UpdateTiles):** `value`, `enabled`, `leadingIcon`, `clickableLeadingIcon`,
 * `trailingIcon`, `clickableTrailingIcon`, `prefixText`, `suffixText`, `placeholder`, `label`,
 * `supportingText`, `minLines`, `maxLines`, `kind`, `state`, `keyboardOptions`,
 * `visualTransformation`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnTextChangedEventTrigger] — fired on every keystroke when the text changes; the new text
 *   string is passed as incomingData to downstream events. The renderer also dispatches a local
 *   [TextFieldTileEvents.OnTextChange] so sibling tiles on the same screen can read the value.
 * - [OnKeyboardDoneEventTrigger] — fired when the user presses the IME "Done" action.
 * - [OnKeyboardGoEventTrigger] — fired when the user presses the IME "Go" action.
 * - [OnKeyboardNextEventTrigger] — fired when the user presses the IME "Next" action.
 * - [OnKeyboardPreviousEventTrigger] — fired when the user presses the IME "Previous" action.
 * - [OnKeyboardSearchEventTrigger] — fired when the user presses the IME "Search" action.
 * - [OnKeyboardSendEventTrigger] — fired when the user presses the IME "Send" action.
 * - `OnLeadingIconClickEventTrigger` — fired when [clickableLeadingIcon] is true and the user
 *   taps the leading icon (not listed in `@Triggers` but wired in the renderer).
 * - `OnTrailingIconClickEventTrigger` — fired when [clickableTrailingIcon] is true and the
 *   user taps the trailing icon (not listed in `@Triggers` but wired in the renderer).
 *
 * **Notes:** The renderer holds a local [TextFieldValue] state initialized from [value]. A
 * [LaunchedEffect] watches [value] and syncs the local state when the server pushes a new value
 * via UpdateTiles — the cursor is moved to the end of the new text on each sync. The trigger
 * for text changes is only fired when the new text differs from the previously known [value],
 * preventing feedback loops. [state] set to `ERROR` activates the Material error styling
 * (red border and supporting text color). [visualTransformation] supports `None`, `Password`
 * (dots), and `Custom` (character-level masking).
 */
@Triggers(
    [
        OnTextChangedEventTrigger::class,
        OnKeyboardDoneEventTrigger::class,
        OnKeyboardGoEventTrigger::class,
        OnKeyboardNextEventTrigger::class,
        OnKeyboardPreviousEventTrigger::class,
        OnKeyboardSearchEventTrigger::class,
        OnKeyboardSendEventTrigger::class,
    ]
)
@Serializable
@SerialName("TextField")
data class TextFieldTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
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
