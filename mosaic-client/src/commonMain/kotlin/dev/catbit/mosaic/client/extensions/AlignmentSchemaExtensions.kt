package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.Alignment
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema

/** Converts the wire-format [AlignmentSchema.Vertical] into its Compose [Alignment.Vertical]
 * counterpart — used by any renderer applying `alignVerticallyToX()`. */
fun AlignmentSchema.Vertical.toAlignment(): Alignment.Vertical = when (this) {
    AlignmentSchema.Vertical.Bottom -> Alignment.Bottom
    AlignmentSchema.Vertical.Center -> Alignment.CenterVertically
    AlignmentSchema.Vertical.Top -> Alignment.Top
}

/** Converts the wire-format [AlignmentSchema.Horizontal] into its Compose [Alignment.Horizontal]
 * counterpart — used by any renderer applying `alignHorizontallyToX()`. */
fun AlignmentSchema.Horizontal.toAlignment(): Alignment.Horizontal = when (this) {
    AlignmentSchema.Horizontal.Center -> Alignment.CenterHorizontally
    AlignmentSchema.Horizontal.End -> Alignment.End
    AlignmentSchema.Horizontal.Start -> Alignment.Start
}

/** Converts the wire-format [AlignmentSchema.TwoDimensional] into its Compose [Alignment]
 * counterpart — used by `Box`/`AsyncImage`/`Popup` and any other tile taking a 2D `alignment`. */
fun AlignmentSchema.TwoDimensional.toAlignment(): Alignment = when (this) {
    AlignmentSchema.TwoDimensional.BottomCenter -> Alignment.BottomCenter
    AlignmentSchema.TwoDimensional.BottomEnd -> Alignment.BottomEnd
    AlignmentSchema.TwoDimensional.BottomStart -> Alignment.BottomStart
    AlignmentSchema.TwoDimensional.Center -> Alignment.Center
    AlignmentSchema.TwoDimensional.CenterEnd -> Alignment.CenterEnd
    AlignmentSchema.TwoDimensional.CenterStart -> Alignment.CenterStart
    AlignmentSchema.TwoDimensional.TopCenter -> Alignment.TopCenter
    AlignmentSchema.TwoDimensional.TopEnd -> Alignment.TopEnd
    AlignmentSchema.TwoDimensional.TopStart -> Alignment.TopStart
}