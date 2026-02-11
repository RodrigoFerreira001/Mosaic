package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.box

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.BoxTileSchema

object BoxTileRenderer : TileRenderer<BoxTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: BoxTileSchema,
    ) {
        println("Rendering Box")
    }
}
