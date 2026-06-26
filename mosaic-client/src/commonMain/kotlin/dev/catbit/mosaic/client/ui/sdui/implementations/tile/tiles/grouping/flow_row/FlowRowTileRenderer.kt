package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flow_row

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlowRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema

object FlowRowTileRenderer : TileRenderer<FlowRowTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: FlowRowTileSchema) {

        OnDisplayEffect()

        with(tileSchema) {
            FlowRow(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style = style, onClick = onClick(events)),
                horizontalArrangement = horizontalArrangement.toArrangement(),
                verticalArrangement = verticalArrangement.toArrangement(),
                maxItemsInEachRow = maxItemsInEachRow
            ) {
                CompositionLocalProvider(
                    LocalFlowRowScope provides this,
                    LocalRowScope provides null,
                    LocalLazyItemScope provides null,
                ) {
                    RenderChildren(tiles)
                }
            }
        }
    }
}
