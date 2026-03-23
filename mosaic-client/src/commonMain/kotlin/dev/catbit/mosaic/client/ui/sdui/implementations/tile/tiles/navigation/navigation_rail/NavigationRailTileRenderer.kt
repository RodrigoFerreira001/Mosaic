package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema

object NavigationRailTileRenderer : TileRenderer<NavigationRailTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NavigationRailTileSchema,
    ) {
        with(tileSchema) {
            if (!isGone()) {
                println("Render NavigationRail: https://developer.android.com/develop/ui/compose/components/navigation-rail")
                // Render placeholder
            }
        }
    }
}
