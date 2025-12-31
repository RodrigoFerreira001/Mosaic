package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope
import dev.catbit.mosaic.core.trigger.triggers.OnTextChangedTrigger

object TextFieldRenderer : TileRenderer<TextFieldTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(uiState: TextFieldTileUIState) {
        OutlinedTextField(
            modifier = Modifier.styledWith(uiState.style),
            value = uiState.value,
            onValueChange = {
                dispatchEvent(TextFieldTileEvents.OnTextChange(it))
                triggerEvent(
                    trigger = OnTextChangedTrigger,
                    data = it
                )
            }
        )
    }
}