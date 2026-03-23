package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toTextStyle
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema

object SimpleTextTileRenderer : TileRenderer<SimpleTextTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SimpleTextTileSchema,
    ) {
        with(tileSchema) {
            Text(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                text = text,
                color = color.toComposeColor(),
                style = typography.toTextStyle()
            )
        }
    }
}
