package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.carousel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema.CarouselTypeSchema

object CarouselTileRenderer : TileRenderer<CarouselTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: CarouselTileSchema) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val padding = remember { PaddingValues(horizontal = contentPadding.dp) }

            when (val type = type) {
                is CarouselTypeSchema.MultiBrowse -> HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { tiles.size },
                    modifier = modifier,
                    preferredItemWidth = type.preferredItemWidth.dp,
                    itemSpacing = itemSpacing.dp,
                    userScrollEnabled = userScrollEnabled,
                    minSmallItemWidth = type.minSmallItemWidth?.dp ?: CarouselDefaults.MinSmallItemSize,
                    maxSmallItemWidth = type.maxSmallItemWidth?.dp ?: CarouselDefaults.MaxSmallItemSize,
                    contentPadding = padding
                ) { index -> RenderChild(tiles[index]) }

                is CarouselTypeSchema.Uncontained -> HorizontalUncontainedCarousel(
                    state = rememberCarouselState { tiles.size },
                    modifier = modifier,
                    itemWidth = type.itemWidth.dp,
                    itemSpacing = itemSpacing.dp,
                    userScrollEnabled = userScrollEnabled,
                    contentPadding = padding
                ) { index -> RenderChild(tiles[index]) }
            }
        }
    }
}
