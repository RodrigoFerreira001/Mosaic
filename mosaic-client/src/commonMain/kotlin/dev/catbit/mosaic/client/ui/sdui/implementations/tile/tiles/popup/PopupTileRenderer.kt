package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema

object PopupTileRenderer : TileRenderer<PopupTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: PopupTileSchema) {
        with(tileSchema) {
            Box {
                RenderChildren(tiles)

                if (expanded) {
                    val density = LocalDensity.current

                    val pxOffset = remember(density, offsetX, offsetY) {
                        with(density) {
                            IntOffset(
                                x = offsetX.dp.roundToPx(),
                                y = offsetY.dp.roundToPx()
                            )
                        }
                    }

                    val positionProvider = remember(alignment, pxOffset) {
                        SafePopupPositionProvider(
                            alignment = alignment,
                            offset = pxOffset
                        )
                    }

                    Popup(
                        popupPositionProvider = positionProvider,
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

    /**
     * Positions the popup relative to the anchor's bounds on the side(s) indicated by
     * [alignment]. Vertically, `Top`/`Bottom` always place the popup fully above/below the
     * anchor (never overlapping it) plus [IntOffset.y] of gap, and `Center` centers it on the
     * anchor's vertical axis. Horizontally, behavior depends on whether the alignment is a true
     * corner (`TopStart`/`TopEnd`/`BottomStart`/`BottomEnd`) or a pure side flyout
     * (`CenterStart`/`CenterEnd`):
     * - Corners flush-align the popup's matching edge with the anchor's edge (e.g. `BottomEnd`
     *   hangs below the anchor with its right edge lined up with the anchor's right edge, like a
     *   typical dropdown menu), and [IntOffset.x] is a plain translation from that flush position
     *   — positive shifts right, negative shifts left. This lets a corner popup anchored near a
     *   screen edge be nudged back on-screen with a negative offset.
     * - `CenterStart`/`CenterEnd` render fully outside the anchor to the left/right (mirrored for
     *   RTL), with [IntOffset.x] as a positive gap from the anchor.
     * `TopCenter`/`Center`/`BottomCenter` center the popup horizontally on the anchor, with
     * [IntOffset.x] applied as a plain translation.
     */
    class SafePopupPositionProvider(
        private val alignment: AlignmentSchema.TwoDimensional,
        private val offset: IntOffset
    ) : PopupPositionProvider {

        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            val isLtr = layoutDirection == LayoutDirection.Ltr

            var x = when (alignment) {
                AlignmentSchema.TwoDimensional.TopStart,
                AlignmentSchema.TwoDimensional.BottomStart ->
                    if (isLtr) anchorBounds.left + offset.x
                    else anchorBounds.right - popupContentSize.width - offset.x

                AlignmentSchema.TwoDimensional.CenterStart ->
                    if (isLtr) anchorBounds.left - popupContentSize.width - offset.x
                    else anchorBounds.right + offset.x

                AlignmentSchema.TwoDimensional.TopEnd,
                AlignmentSchema.TwoDimensional.BottomEnd ->
                    if (isLtr) anchorBounds.right - popupContentSize.width + offset.x
                    else anchorBounds.left - offset.x

                AlignmentSchema.TwoDimensional.CenterEnd ->
                    if (isLtr) anchorBounds.right + offset.x
                    else anchorBounds.left - popupContentSize.width - offset.x

                AlignmentSchema.TwoDimensional.TopCenter,
                AlignmentSchema.TwoDimensional.Center,
                AlignmentSchema.TwoDimensional.BottomCenter ->
                    anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2 + offset.x
            }

            var y = when (alignment) {
                AlignmentSchema.TwoDimensional.TopStart,
                AlignmentSchema.TwoDimensional.TopCenter,
                AlignmentSchema.TwoDimensional.TopEnd ->
                    anchorBounds.top - popupContentSize.height - offset.y

                AlignmentSchema.TwoDimensional.BottomStart,
                AlignmentSchema.TwoDimensional.BottomCenter,
                AlignmentSchema.TwoDimensional.BottomEnd ->
                    anchorBounds.bottom + offset.y

                AlignmentSchema.TwoDimensional.CenterStart,
                AlignmentSchema.TwoDimensional.Center,
                AlignmentSchema.TwoDimensional.CenterEnd ->
                    anchorBounds.top + (anchorBounds.height - popupContentSize.height) / 2 + offset.y
            }

            val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
            val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)

            x = x.coerceIn(0, maxX)
            y = y.coerceIn(0, maxY)

            return IntOffset(x, y)
        }
    }
}
