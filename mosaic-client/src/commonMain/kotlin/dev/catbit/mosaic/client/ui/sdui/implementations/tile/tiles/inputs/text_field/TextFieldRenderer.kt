package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTextChangedEventTrigger

object TextFieldRenderer : TileRenderer<TextFieldTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(uiState: TextFieldTileUIState) {

        var inputState by remember {
            mutableStateOf(TextFieldValue(text = uiState.value))
        }

        LaunchedEffect(uiState.value) {
            if (inputState.text != uiState.value) {
                inputState = inputState.copy(
                    text = uiState.value,
                    selection = TextRange(uiState.value.length)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.styledWith(uiState.style),
            value = inputState,
            onValueChange = { newValue ->

                inputState = newValue

                if (uiState.value != newValue.text) {
                    dispatchEvent(TextFieldTileEvents.OnTextChange(newValue.text))
                    triggerEvent(
                        trigger = OnTextChangedEventTrigger,
                        data = newValue.text
                    )
                }
            }
        )
    }
}