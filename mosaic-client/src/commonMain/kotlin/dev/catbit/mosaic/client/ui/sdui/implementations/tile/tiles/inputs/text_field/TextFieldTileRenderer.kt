package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.catbit.mosaic.client.extensions.hasErrorState
import dev.catbit.mosaic.client.extensions.iconButtonOrNull
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.extensions.keyboardOptions
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.extensions.visualTransformation
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema

object TextFieldTileRenderer : TileRenderer<TextFieldTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: TextFieldTileSchema) {
        with(tileSchema) {

            var inputState by remember {
                mutableStateOf(TextFieldValue(text = value))
            }

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            LaunchedEffect(value) {
                if (inputState.text != value) {
                    inputState = inputState.copy(
                        text = value,
                        selection = TextRange(value.length)
                    )
                }
            }

            val onValueChange: (TextFieldValue) -> Unit = remember(this) {
                { newValue ->
                    inputState = newValue
                    if (value != newValue.text) {
                        triggerEvent(
                            trigger = EventTriggers.onTextChanged(),
                            data = newValue.text
                        )
                        dispatchEvent(TextFieldTileEvents.OnTextChange(newValue.text))
                    }
                }
            }

            val keyboardActions = KeyboardActions(
                onDone = { triggerEvent(EventTriggers.onKeyboardDone()) },
                onGo = { triggerEvent(EventTriggers.onKeyboardGo()) },
                onNext = { triggerEvent(EventTriggers.onKeyboardNext()) },
                onPrevious = { triggerEvent(EventTriggers.onKeyboardPrevious()) },
                onSearch = { triggerEvent(EventTriggers.onKeyboardSearch()) },
                onSend = { triggerEvent(EventTriggers.onKeyboardSend()) },
            )

            val leadingIcon = if (clickableLeadingIcon)
                leadingIcon.iconButtonOrNull { triggerEvent(EventTriggers.onLeadingIconClick()) }
            else leadingIcon.iconOrNull()

            val trailingIcon = if (clickableTrailingIcon)
                trailingIcon.iconButtonOrNull { triggerEvent(EventTriggers.onTrailingIconClick()) }
            else trailingIcon.iconOrNull()

            when (kind) {
                TextFieldTileSchema.Kind.FILLED ->
                    TextField(
                        modifier = modifier,
                        value = inputState,
                        onValueChange = onValueChange,
                        enabled = enabled,
                        label = label.textOrNull(),
                        prefix = prefixText.textOrNull(),
                        suffix = suffixText.textOrNull(),
                        supportingText = supportingText.textOrNull(),
                        placeholder = placeholder.textOrNull(),
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        maxLines = maxLines,
                        isError = hasErrorState(),
                        keyboardActions = keyboardActions,
                        keyboardOptions = keyboardOptions(),
                        visualTransformation = visualTransformation()
                    )

                TextFieldTileSchema.Kind.OUTLINED ->
                    OutlinedTextField(
                        modifier = modifier,
                        value = inputState,
                        onValueChange = onValueChange,
                        enabled = enabled,
                        label = label.textOrNull(),
                        prefix = prefixText.textOrNull(),
                        suffix = suffixText.textOrNull(),
                        supportingText = supportingText.textOrNull(),
                        placeholder = placeholder.textOrNull(),
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        maxLines = maxLines,
                        isError = hasErrorState(),
                        keyboardActions = keyboardActions,
                        keyboardOptions = keyboardOptions(),
                        visualTransformation = visualTransformation()
                    )
            }
        }
    }
}