package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema

object TabsTileRenderer : TileRenderer<TabsTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: TabsTileSchema,
    ) {
        with(tileSchema) {

        }
    }
}
