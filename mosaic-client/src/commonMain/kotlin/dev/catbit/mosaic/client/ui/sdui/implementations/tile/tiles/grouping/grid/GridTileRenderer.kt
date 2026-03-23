package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema

object GridTileRenderer : TileRenderer<GridTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: GridTileSchema,
    ) {
        with(tileSchema) {
            BoxWithConstraints(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(
                        style = style,
                        onClick = onClick(events)
                    )
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gutter.dp),
                    verticalArrangement = Arrangement.spacedBy(gutter.dp),
                    maxItemsInEachRow = columns
                ) {
                    CompositionLocalProvider(
                        LocalSharedHorizontalArea provides SharedHorizontalArea.Defined(
                            columns = columns,
                            gutter = gutter,
                            totalWidth = this@BoxWithConstraints.maxWidth.value,
                            horizontalPadding = 0
                        )
                    ) {
                        tiles.forEach { tile ->
                            RenderChild(tile)
                        }
                    }
                }
            }
        }
    }
}
