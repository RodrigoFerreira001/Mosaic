package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.selection_container

import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.SelectionContainerTileSchema

object SelectionContainerTileRenderer : TileRenderer<SelectionContainerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SelectionContainerTileSchema,
    ) {
        with(tileSchema) {
            SelectionContainer(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                RenderChildren(tiles)
            }
        }
    }
}
