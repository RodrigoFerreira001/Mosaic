package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema

object PopupTileRenderer : TileRenderer<PopupTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: PopupTileSchema) {
        with(tileSchema) {
            Box(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                RenderChildren(tiles)

                if (expanded) {
                    val density = LocalDensity.current
                    Popup(
                        alignment = alignment.toAlignment(),
                        offset = with(density) {
                            IntOffset(
                                x = offsetX.dp.roundToPx(),
                                y = offsetY.dp.roundToPx()
                            )
                        },
                        onDismissRequest = { dispatchEvent(PopupTileEvents.OnTogglePopup) },
                        properties = PopupProperties(
                            focusable = focusable,
                            dismissOnBackPress = dismissOnBackPress,
                            dismissOnClickOutside = dismissOnClickOutside
                        )
                    ) {
                        RenderChildren(popupTiles)
                    }
                }
            }
        }
    }
}
