package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.badges.badge

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema

object BadgeTileRenderer : TileRenderer<BadgeTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: BadgeTileSchema,
    ) {
        with(tileSchema) {
            Badge(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                content?.let {
                    Text(it)
                }
            }
        }
    }
}
