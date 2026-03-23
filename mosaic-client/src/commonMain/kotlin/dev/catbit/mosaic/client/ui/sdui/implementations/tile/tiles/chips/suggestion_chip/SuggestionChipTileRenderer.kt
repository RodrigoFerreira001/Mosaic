package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema
import io.ktor.websocket.Frame

object SuggestionChipTileRenderer : TileRenderer<SuggestionChipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SuggestionChipTileSchema,
    ) {
        with(tileSchema) {
            SuggestionChip(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                label = {
                    Text(text)
                },
                enabled = enabled,
                onClick = {
                    triggerEvent(EventTriggers.onClick())
                }
            )
        }
    }
}
