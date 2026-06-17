package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema

object ButtonTileRenderer : TileRenderer<ButtonTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ButtonTileSchema,
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(tileSchema.style)

            val content: @Composable RowScope.() -> Unit = {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round,
                        color = LocalContentColor.current
                    )
                } else {
                    when (iconPosition) {
                        ButtonTileSchema.IconPosition.START -> {
                            icon?.let {
                                Icon(it)
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(text)
                        }
                        ButtonTileSchema.IconPosition.END -> {
                            Text(text)
                            icon?.let {
                                Spacer(Modifier.width(8.dp))
                                Icon(it)
                            }
                        }
                    }
                }
            }

            val onClick = { triggerEvent(EventTriggers.onClick()) }

            val shape = when (shape) {
                ButtonTileSchema.Shape.SQUARE -> MaterialTheme.shapes.medium
                ButtonTileSchema.Shape.ROUNDED -> CircleShape
            }

            when (buttonType) {
                ButtonTileSchema.Type.FILLED ->
                    Button(
                        modifier = modifier,
                        onClick = onClick,
                        enabled = enabled,
                        shape = shape,
                        content = content
                    )

                ButtonTileSchema.Type.ELEVATED ->
                    ElevatedButton(
                        modifier = modifier,
                        onClick = onClick,
                        enabled = enabled,
                        shape = shape,
                        content = content
                    )

                ButtonTileSchema.Type.FILLED_TONAL ->
                    FilledTonalButton(
                        modifier = modifier,
                        onClick = onClick,
                        enabled = enabled,
                        shape = shape,
                        content = content
                    )

                ButtonTileSchema.Type.OUTLINED ->
                    OutlinedButton(
                        modifier = modifier,
                        onClick = onClick,
                        enabled = enabled,
                        shape = shape,
                        content = content
                    )

                ButtonTileSchema.Type.TEXT ->
                    TextButton(
                        modifier = modifier,
                        onClick = onClick,
                        enabled = enabled,
                        shape = shape,
                        content = content
                    )
            }
        }
    }
}

