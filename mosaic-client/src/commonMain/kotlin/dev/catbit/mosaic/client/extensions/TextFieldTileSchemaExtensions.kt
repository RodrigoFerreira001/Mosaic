package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.extensions.withNotNull

fun TextFieldTileSchema.hasErrorState() = state == TextFieldTileSchema.State.ERROR

fun TextFieldTileSchema.keyboardOptions(): KeyboardOptions = withNotNull(keyboardOptions) {
    KeyboardOptions(
        capitalization = when (capitalization) {
            TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Characters -> KeyboardCapitalization.Characters
            TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.None -> KeyboardCapitalization.None
            TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Sentences -> KeyboardCapitalization.Sentences
            TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Unspecified -> KeyboardCapitalization.Unspecified
            TextFieldTileSchema.KeyboardOptions.KeyboardCapitalization.Words -> KeyboardCapitalization.Words
        },
        autoCorrectEnabled = autoCorrectEnabled,
        keyboardType = when (keyboardType) {
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Ascii -> KeyboardType.Ascii
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Decimal -> KeyboardType.Decimal
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Email -> KeyboardType.Email
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Number -> KeyboardType.Number
            TextFieldTileSchema.KeyboardOptions.KeyboardType.NumberPassword -> KeyboardType.NumberPassword
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Password -> KeyboardType.Password
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Phone -> KeyboardType.Phone
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Text -> KeyboardType.Text
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Unspecified -> KeyboardType.Unspecified
            TextFieldTileSchema.KeyboardOptions.KeyboardType.Uri -> KeyboardType.Uri
        },
        imeAction = when (imeAction) {
            TextFieldTileSchema.KeyboardOptions.ImeAction.Default -> ImeAction.Default
            TextFieldTileSchema.KeyboardOptions.ImeAction.Done -> ImeAction.Done
            TextFieldTileSchema.KeyboardOptions.ImeAction.Go -> ImeAction.Go
            TextFieldTileSchema.KeyboardOptions.ImeAction.Next -> ImeAction.Next
            TextFieldTileSchema.KeyboardOptions.ImeAction.None -> ImeAction.None
            TextFieldTileSchema.KeyboardOptions.ImeAction.Previous -> ImeAction.Previous
            TextFieldTileSchema.KeyboardOptions.ImeAction.Search -> ImeAction.Search
            TextFieldTileSchema.KeyboardOptions.ImeAction.Send -> ImeAction.Send
            TextFieldTileSchema.KeyboardOptions.ImeAction.Unspecified -> ImeAction.Unspecified
        },
        showKeyboardOnFocus = showKeyboardOnFocus
    )
} ?: KeyboardOptions.Default

fun TextFieldTileSchema.visualTransformation(): VisualTransformation = when (val vt = visualTransformation) {
    is TextFieldTileSchema.VisualTransformation.Custom -> CustomVisualTransformation(vt.mask)
    TextFieldTileSchema.VisualTransformation.Password -> PasswordVisualTransformation()
    else -> VisualTransformation.None
}

private class CustomVisualTransformation(
    private val mask: String,
    private val maskSlotSignal: Char = '#'
) : VisualTransformation {

    private val offsetMapping = getOffsetMapping(mask)

    override fun filter(text: AnnotatedString): TransformedText {
        val unmasked = text.text.take(mask.count { it == maskSlotSignal })

        if (unmasked.isEmpty()) {
            return TransformedText(
                AnnotatedString(unmasked),
                OffsetMapping.Identity
            )
        }

        var textCharIndex = 0
        var maskedString = ""

        for (maskChar in mask) {
            if (maskChar != maskSlotSignal) {
                maskedString += maskChar
            } else {
                if (textCharIndex >= unmasked.length) break
                maskedString += unmasked[textCharIndex]
                textCharIndex++
            }
        }

        return TransformedText(AnnotatedString(maskedString), offsetMapping)
    }

    private fun getOffsetMapping(mask: String) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset + offsetMaskCount(offset)
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset - mask.take(offset).count { it != maskSlotSignal }
        }

        private fun offsetMaskCount(offset: Int): Int {
            var maskOffsetCount = 0
            var maskDataCount = 0

            for (maskChar in mask) {
                when {
                    maskChar != maskSlotSignal -> maskOffsetCount++
                    ++maskDataCount >= offset -> break
                }
            }

            val diffOffset = if (maskDataCount < offset) offset - maskDataCount else 0

            return maskOffsetCount - diffOffset
        }
    }
}