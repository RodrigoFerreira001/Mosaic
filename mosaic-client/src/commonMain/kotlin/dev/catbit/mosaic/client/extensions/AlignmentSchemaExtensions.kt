package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.Alignment
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema

fun AlignmentSchema.Vertical.toAlignment(): Alignment.Vertical = when (this) {
    AlignmentSchema.Vertical.Bottom -> Alignment.Bottom
    AlignmentSchema.Vertical.Center -> Alignment.CenterVertically
    AlignmentSchema.Vertical.Top -> Alignment.Top
}

fun AlignmentSchema.Horizontal.toAlignment(): Alignment.Horizontal = when (this) {
    AlignmentSchema.Horizontal.Center -> Alignment.CenterHorizontally
    AlignmentSchema.Horizontal.End -> Alignment.End
    AlignmentSchema.Horizontal.Start -> Alignment.Start
}

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