package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.card

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CardTileSchema

object CardTileRenderer : TileRenderer<CardTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CardTileSchema,
    ) {
        println("Rendering Card")
    }
}
