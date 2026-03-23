package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.icon_button

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema

object IconButtonTileRenderer : TileRenderer<IconButtonTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: IconButtonTileSchema,
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(tileSchema.style)

            val onClick = { triggerEvent(EventTriggers.onClick()) }

            val content: @Composable () -> Unit = {
                Icon(icon)
            }

            when (buttonType) {
                IconButtonTileSchema.Type.DEFAULT ->
                    OutlinedIconButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )

                IconButtonTileSchema.Type.FILLED ->
                    FilledIconButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )

                IconButtonTileSchema.Type.FILLED_TONAL ->
                    FilledTonalIconButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )

                IconButtonTileSchema.Type.OUTLINED ->
                    OutlinedIconButton(
                        modifier = modifier,
                        onClick = onClick,
                        content = content
                    )
            }
        }
    }
}
