package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.nested_navigation_graph

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema

object NestedNavigationGraphTileRenderer : TileRenderer<NestedNavigationGraphTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NestedNavigationGraphTileSchema,
    ) {
        with(tileSchema) {
            if (!isGone()) {
                println("Render NestedNavigationGraph")
                // Render placeholder
            }
        }
    }
}
