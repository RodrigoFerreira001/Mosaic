package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.shimmer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ShimmerTileSchema

object ShimmerTileRenderer : TileRenderer<ShimmerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ShimmerTileSchema,
    ) {
        println("Rendering Shimmer")
    }
}
