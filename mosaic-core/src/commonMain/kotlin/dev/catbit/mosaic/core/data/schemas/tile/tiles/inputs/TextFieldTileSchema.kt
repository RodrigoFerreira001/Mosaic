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
data class TextFieldTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("value") val value: String,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("leadingIcon") val leadingIcon: IconSchema?,
    @SerialName("trailingIcon") val trailingIcon: IconSchema?,
    @SerialName("prefixText") val prefixText: String?,
    @SerialName("suffixText") val suffixText: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("label") val label: String?,
    @SerialName("supportingText") val supportingText: String?,
    @SerialName("maxLength") val maxLength: Int,
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
        data class Custom(val mask: String) : VisualTransformation
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
