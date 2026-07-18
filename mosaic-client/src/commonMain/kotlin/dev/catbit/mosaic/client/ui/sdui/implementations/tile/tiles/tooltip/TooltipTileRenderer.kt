package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.tooltip

import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toShape
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip.TooltipTileSchema

object TooltipTileRenderer : TileRenderer<TooltipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: TooltipTileSchema) {
        with(tileSchema) {
            val tooltipState = rememberTooltipState()

            val positionProvider = if (spacing != null) {
                TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = position.toTooltipAnchorPosition(),
                    spacingBetweenTooltipAndAnchor = spacing?.dp ?: 4.dp
                )
            } else {
                TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = position.toTooltipAnchorPosition()
                )
            }

            TooltipBox(
                positionProvider = positionProvider,
                state = tooltipState,
                tooltip = {
                    PlainTooltip(
                        caretShape = if (showCaret) TooltipDefaults.caretShape() else null,
                        maxWidth = maxWidth?.dp ?: TooltipDefaults.plainTooltipMaxWidth,
                        shape = shape?.toShape() ?: TooltipDefaults.plainTooltipContainerShape,
                        contentColor = contentColor?.toComposeColor() ?: TooltipDefaults.plainTooltipContentColor,
                        containerColor = containerColor?.toComposeColor() ?: TooltipDefaults.plainTooltipContainerColor
                    ) {
                        Text(text)
                    }
                },
                content = {
                    RenderChildren(tiles)
                }
            )
        }
    }

    private fun TooltipTileSchema.Position.toTooltipAnchorPosition() = when (this) {
        TooltipTileSchema.Position.ABOVE -> TooltipAnchorPosition.Above
        TooltipTileSchema.Position.BELOW -> TooltipAnchorPosition.Below
        TooltipTileSchema.Position.LEFT -> TooltipAnchorPosition.Left
        TooltipTileSchema.Position.RIGHT -> TooltipAnchorPosition.Right
        TooltipTileSchema.Position.START -> TooltipAnchorPosition.Start
        TooltipTileSchema.Position.END -> TooltipAnchorPosition.End
    }
}
