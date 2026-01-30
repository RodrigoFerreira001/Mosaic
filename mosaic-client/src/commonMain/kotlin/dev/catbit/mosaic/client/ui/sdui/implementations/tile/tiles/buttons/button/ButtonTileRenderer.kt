package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema

object ButtonTileRenderer : TileRenderer<ButtonTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ButtonTileSchema,
    ) {
        with(tileSchema) {
            if (!isGone()) {
                Button(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(tileSchema.style),
                    onClick = {
                        triggerEvent(EventTriggers.onClick())
                    }
                ) {
                    Text(text)
                }
            }
        }
    }
}

