package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.carousel

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CarouselTileSchema

object CarouselTileRenderer : TileRenderer<CarouselTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CarouselTileSchema,
    ) {
        println("Rendering Carousel")
    }
}
