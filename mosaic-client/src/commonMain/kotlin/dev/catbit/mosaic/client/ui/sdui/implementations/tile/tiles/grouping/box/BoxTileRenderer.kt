package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.box

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.BoxTileSchema

object BoxTileRenderer : TileRenderer<BoxTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: BoxTileSchema,
    ) {
        with(tileSchema) {
            Box(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(
                        style = style,
                        onClick = onClick(events)
                    ),
                contentAlignment = alignment.toAlignment()
            ) {
                RenderChildren(tiles)
            }
        }
    }
}
