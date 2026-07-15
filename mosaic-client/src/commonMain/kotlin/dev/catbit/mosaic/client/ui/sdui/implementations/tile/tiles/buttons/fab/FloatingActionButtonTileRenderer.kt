package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.fab

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema

object FloatingActionButtonTileRenderer : TileRenderer<FloatingActionButtonTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: FloatingActionButtonTileSchema,
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(tileSchema.style)

            val onClick = { triggerEvent(EventTriggers.onClick()) }

            val content: @Composable () -> Unit = {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round,
                        color = LocalContentColor.current
                    )
                } else {
                    Icon(icon.name)
                }
            }

            when (size) {
                FloatingActionButtonTileSchema.Size.DEFAULT ->
                    SmallFloatingActionButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )

                FloatingActionButtonTileSchema.Size.MEDIUM ->
                    FloatingActionButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )

                FloatingActionButtonTileSchema.Size.LARGE ->
                    LargeFloatingActionButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )
            }
        }
    }
}
