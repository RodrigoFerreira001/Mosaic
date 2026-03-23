package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema

object NavigationBarTileRenderer : TileRenderer<NavigationBarTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NavigationBarTileSchema,
    ) {
        with(tileSchema) {
            if (!isGone()) {
                println("Render NavigationBar: https://developer.android.com/develop/ui/compose/components/navigation-bar")
                // Render placeholder
            }
        }
    }
}
