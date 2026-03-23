package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.carousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema

object CarouselTileRenderer : TileRenderer<CarouselTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CarouselTileSchema,
    ) {
        with(tileSchema) {
            BoxWithConstraints(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)

            ) {
                CompositionLocalProvider(
                    LocalSharedHorizontalArea provides SharedHorizontalArea.Defined(
                        columns = columns,
                        gutter = gutter,
                        totalWidth = maxWidth.value,
                        horizontalPadding = contentHorizontalPadding
                    )
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(gutter.dp),
                        contentPadding = PaddingValues(horizontal = contentHorizontalPadding.dp)
                    ) {
                        items(tiles, key = { it.id }) { tile ->
                            RenderChild(tile)
                        }
                    }
                }
            }
        }
    }
}
