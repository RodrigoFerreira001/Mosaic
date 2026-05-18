package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.grid

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridFlow
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalGridScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema.GridFlowSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema.GridTrackSchema

@OptIn(ExperimentalLayoutApi::class)
object GridTileRenderer : TileRenderer<GridTileSchema> {

    @OptIn(ExperimentalGridApi::class)
    @Composable
    override fun TileRenderingScope.Render(tileSchema: GridTileSchema) {
        with(tileSchema) {
            Grid(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style = style, onClick = onClick(events)),
                config = {
                    flow = when (this@with.flow) {
                        GridFlowSchema.Row -> GridFlow.Row
                        GridFlowSchema.Column -> GridFlow.Column
                    }
                    columns.forEach { track ->
                        when (track) {
                            is GridTrackSchema.Fixed -> column(track.value.dp)
                            is GridTrackSchema.Fraction -> column(track.value)
                            is GridTrackSchema.Flexible -> column(track.value.fr)
                            GridTrackSchema.Auto -> column(GridTrackSize.Auto)
                            GridTrackSchema.MaxContent -> column(GridTrackSize.MaxContent)
                            GridTrackSchema.MinContent -> column(GridTrackSize.MinContent)
                        }
                    }
                    rows.forEach { track ->
                        when (track) {
                            is GridTrackSchema.Fixed -> row(track.value.dp)
                            is GridTrackSchema.Fraction -> row(track.value)
                            is GridTrackSchema.Flexible -> row(track.value.fr)
                            GridTrackSchema.Auto -> row(GridTrackSize.Auto)
                            GridTrackSchema.MaxContent -> row(GridTrackSize.MaxContent)
                            GridTrackSchema.MinContent -> row(GridTrackSize.MinContent)
                        }
                    }
                    columnGap(columnGap.dp)
                    rowGap(rowGap.dp)
                }
            ) {
                CompositionLocalProvider(LocalGridScope provides this) {
                    tiles.forEach { tile -> RenderChild(tile) }
                }
            }
        }
    }
}
