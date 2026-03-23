package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.icon

import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema

object IconTileRenderer : TileRenderer<IconTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: IconTileSchema,
    ) {
        with(tileSchema) {
            Icon(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                schema = icon
            )
        }
    }
}
