package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.bottom_app_bar

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema

object BottomAppBarTileRenderer : TileRenderer<BottomAppBarTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: BottomAppBarTileSchema,
    ) {
        with(tileSchema) {
            BottomAppBar(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(tileSchema.style),
                floatingActionButton = floatingActionButton?.let { fab ->
                    @Composable {
                        RenderChild(fab)
                    }
                },
                actions = {
                    RenderChildren(actions)
                }
            )
        }
    }
}
