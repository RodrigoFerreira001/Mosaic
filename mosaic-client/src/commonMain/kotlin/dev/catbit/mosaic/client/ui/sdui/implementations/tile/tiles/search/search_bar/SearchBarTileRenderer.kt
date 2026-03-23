package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema

object SearchBarTileRenderer : TileRenderer<SearchBarTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SearchBarTileSchema,
    ) {
        with(tileSchema) {
            if (!isGone()) {
                println("Render SearchBar: https://developer.android.com/develop/ui/compose/components/search-bar")
                // Render placeholder
            }
        }
    }
}
