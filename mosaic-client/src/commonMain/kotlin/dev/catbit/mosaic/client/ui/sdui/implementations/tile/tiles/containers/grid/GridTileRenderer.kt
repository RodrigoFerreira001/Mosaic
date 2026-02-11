package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.grid

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.GridTileSchema

object GridTileRenderer : TileRenderer<GridTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: GridTileSchema,
    ) {
        println("Rendering Grid")
    }
}
