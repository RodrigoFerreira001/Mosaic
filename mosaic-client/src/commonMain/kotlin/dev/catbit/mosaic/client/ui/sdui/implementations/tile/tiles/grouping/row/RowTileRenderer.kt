package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.RowTileSchema

object RowTileRenderer : TileRenderer<RowTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: RowTileSchema
    ) {
        with(tileSchema) {
            if (!isGone()) {
                Row(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(style),
                    horizontalArrangement = arrangement.toArrangement(),
                    verticalAlignment = alignment.toAlignment(),
                ) {
                    CompositionLocalProvider(LocalRowScope provides this) {
                        RenderChildren(tiles)
                    }
                }
            }
        }
    }
}

