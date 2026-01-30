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
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema

object TextFieldTileRenderer : TileRenderer<TextFieldTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: TextFieldTileSchema) {
        with(tileSchema) {
            var inputState by remember {
                mutableStateOf(TextFieldValue(text = value))
            }

            LaunchedEffect(value) {
                if (inputState.text != value) {
                    inputState = inputState.copy(
                        text = value,
                        selection = TextRange(value.length)
                    )
                }
            }

            OutlinedTextField(
                modifier = Modifier.styledWith(style),
                value = inputState,
                onValueChange = { newValue ->

                    inputState = newValue

                    if (value != newValue.text) {
                        dispatchEvent(TextFieldTileEvents.OnTextChange(newValue.text))
                        triggerEvent(
                            trigger = dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger,
                            data = newValue.text
                        )
                    }
                }
            )
        }
    }
}