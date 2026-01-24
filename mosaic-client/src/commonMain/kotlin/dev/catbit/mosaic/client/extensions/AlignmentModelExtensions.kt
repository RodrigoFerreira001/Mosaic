package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.Alignment
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel

fun AlignmentModel.Vertical.toAlignment(): Alignment.Vertical = when (this) {
    AlignmentModel.Vertical.Bottom -> Alignment.Bottom
    AlignmentModel.Vertical.Center -> Alignment.CenterVertically
    AlignmentModel.Vertical.Top -> Alignment.Top
}

fun AlignmentModel.Horizontal.toAlignment(): Alignment.Horizontal = when (this) {
    AlignmentModel.Horizontal.Center -> Alignment.CenterHorizontally
    AlignmentModel.Horizontal.End -> Alignment.End
    AlignmentModel.Horizontal.Start -> Alignment.Start
}

fun AlignmentModel.TwoDimensional.toAlignment(): Alignment = when (this) {
    AlignmentModel.TwoDimensional.BottomCenter -> Alignment.BottomCenter
    AlignmentModel.TwoDimensional.BottomEnd -> Alignment.BottomEnd
    AlignmentModel.TwoDimensional.BottomStart -> Alignment.BottomStart
    AlignmentModel.TwoDimensional.Center -> Alignment.Center
    AlignmentModel.TwoDimensional.CenterEnd -> Alignment.CenterEnd
    AlignmentModel.TwoDimensional.CenterStart -> Alignment.CenterStart
    AlignmentModel.TwoDimensional.TopCenter -> Alignment.TopCenter
    AlignmentModel.TwoDimensional.TopEnd -> Alignment.TopEnd
    AlignmentModel.TwoDimensional.TopStart -> Alignment.TopStart
}