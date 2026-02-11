package dev.catbit.mosaic.server.builder.placement

import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema

fun alignVerticallyToTop() = AlignmentSchema.Vertical.Top
fun alignVerticallyToCenter() = AlignmentSchema.Vertical.Center
fun alignVerticallyToBottom() = AlignmentSchema.Vertical.Bottom

fun alignHorizontallyToStart() = AlignmentSchema.Horizontal.Start
fun alignHorizontallyToCenter() = AlignmentSchema.Horizontal.Center
fun alignHorizontallyToEnd() = AlignmentSchema.Horizontal.End

fun alignToTopStart() = AlignmentSchema.TwoDimensional.TopStart
fun alignToTopCenter() = AlignmentSchema.TwoDimensional.TopCenter
fun alignToTopEnd() = AlignmentSchema.TwoDimensional.TopEnd
fun alignToCenterStart() = AlignmentSchema.TwoDimensional.CenterStart
fun alignToCenter() = AlignmentSchema.TwoDimensional.Center
fun alignToCenterEnd() = AlignmentSchema.TwoDimensional.CenterEnd
fun alignToBottomStart() = AlignmentSchema.TwoDimensional.BottomStart
fun alignToBottomCenter() = AlignmentSchema.TwoDimensional.BottomCenter
fun alignToBottomEnd() = AlignmentSchema.TwoDimensional.BottomEnd