package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flex_box

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignContent
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlexBoxScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema

@OptIn(ExperimentalFlexBoxApi::class)
object FlexBoxTileRenderer : TileRenderer<FlexBoxTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: FlexBoxTileSchema) {

        OnDisplayEffect()

        with(tileSchema) {
            FlexBox(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style = style, onClick = onClick(events)),
                config = {
                    direction(direction.toFlexDirection())
                    justifyContent(justifyContent.toFlexJustifyContent())
                    alignItems(alignItems.toFlexAlignItems())
                    alignContent(alignContent.toFlexAlignContent())
                    wrap(wrap.toFlexWrap())
                    columnGap(columnGap.dp)
                    rowGap(rowGap.dp)
                }
            ) {
                CompositionLocalProvider(LocalFlexBoxScope provides this) {
                    RenderChildren(tiles)
                }
            }
        }
    }

    private fun FlexBoxTileSchema.FlexDirectionSchema.toFlexDirection() = when (this) {
        FlexBoxTileSchema.FlexDirectionSchema.Row -> FlexDirection.Row
        FlexBoxTileSchema.FlexDirectionSchema.RowReverse -> FlexDirection.RowReverse
        FlexBoxTileSchema.FlexDirectionSchema.Column -> FlexDirection.Column
        FlexBoxTileSchema.FlexDirectionSchema.ColumnReverse -> FlexDirection.ColumnReverse
    }

    private fun FlexBoxTileSchema.FlexJustifyContentSchema.toFlexJustifyContent() = when (this) {
        FlexBoxTileSchema.FlexJustifyContentSchema.Start -> FlexJustifyContent.Start
        FlexBoxTileSchema.FlexJustifyContentSchema.Center -> FlexJustifyContent.Center
        FlexBoxTileSchema.FlexJustifyContentSchema.End -> FlexJustifyContent.End
        FlexBoxTileSchema.FlexJustifyContentSchema.SpaceBetween -> FlexJustifyContent.SpaceBetween
        FlexBoxTileSchema.FlexJustifyContentSchema.SpaceAround -> FlexJustifyContent.SpaceAround
        FlexBoxTileSchema.FlexJustifyContentSchema.SpaceEvenly -> FlexJustifyContent.SpaceEvenly
    }

    private fun FlexBoxTileSchema.FlexAlignItemsSchema.toFlexAlignItems() = when (this) {
        FlexBoxTileSchema.FlexAlignItemsSchema.Start -> FlexAlignItems.Start
        FlexBoxTileSchema.FlexAlignItemsSchema.End -> FlexAlignItems.End
        FlexBoxTileSchema.FlexAlignItemsSchema.Center -> FlexAlignItems.Center
        FlexBoxTileSchema.FlexAlignItemsSchema.Stretch -> FlexAlignItems.Stretch
        FlexBoxTileSchema.FlexAlignItemsSchema.Baseline -> FlexAlignItems.Baseline
    }

    private fun FlexBoxTileSchema.FlexAlignContentSchema.toFlexAlignContent() = when (this) {
        FlexBoxTileSchema.FlexAlignContentSchema.Start -> FlexAlignContent.Start
        FlexBoxTileSchema.FlexAlignContentSchema.End -> FlexAlignContent.End
        FlexBoxTileSchema.FlexAlignContentSchema.Center -> FlexAlignContent.Center
        FlexBoxTileSchema.FlexAlignContentSchema.Stretch -> FlexAlignContent.Stretch
        FlexBoxTileSchema.FlexAlignContentSchema.SpaceBetween -> FlexAlignContent.SpaceBetween
        FlexBoxTileSchema.FlexAlignContentSchema.SpaceAround -> FlexAlignContent.SpaceAround
    }

    private fun FlexBoxTileSchema.FlexWrapSchema.toFlexWrap() = when (this) {
        FlexBoxTileSchema.FlexWrapSchema.NoWrap -> FlexWrap.NoWrap
        FlexBoxTileSchema.FlexWrapSchema.Wrap -> FlexWrap.Wrap
        FlexBoxTileSchema.FlexWrapSchema.WrapReverse -> FlexWrap.WrapReverse
    }
}
