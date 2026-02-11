package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pager

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PagerTileSchema

object PagerTileRenderer : TileRenderer<PagerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: PagerTileSchema,
    ) {
        println("Rendering Pager")
    }
}
