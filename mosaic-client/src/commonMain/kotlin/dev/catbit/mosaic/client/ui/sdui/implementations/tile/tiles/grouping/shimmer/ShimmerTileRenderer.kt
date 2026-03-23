package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ShimmerTileSchema

object ShimmerTileRenderer : TileRenderer<ShimmerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ShimmerTileSchema,
    ) {
        with(tileSchema) {
            Box(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
                    .shimmer()
            ) {
                RenderChildren(tiles)
            }
        }
    }
}
