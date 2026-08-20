package dev.catbit.mosaic.server.builder.placement

import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema

/** Aligns content to the top of the vertical axis. Used e.g. as a `Row`'s cross-axis alignment. */
fun alignVerticallyToTop() = AlignmentSchema.Vertical.Top

/** Aligns content to the center of the vertical axis. Used e.g. as a `Row`'s cross-axis alignment. */
fun alignVerticallyToCenter() = AlignmentSchema.Vertical.Center

/** Aligns content to the bottom of the vertical axis. Used e.g. as a `Row`'s cross-axis alignment. */
fun alignVerticallyToBottom() = AlignmentSchema.Vertical.Bottom


/** Aligns content to the start of the horizontal axis (mirrored under RTL). Used e.g. as a `Column`'s cross-axis alignment. */
fun alignHorizontallyToStart() = AlignmentSchema.Horizontal.Start

/** Aligns content to the center of the horizontal axis. Used e.g. as a `Column`'s cross-axis alignment. */
fun alignHorizontallyToCenter() = AlignmentSchema.Horizontal.Center

/** Aligns content to the end of the horizontal axis (mirrored under RTL). Used e.g. as a `Column`'s cross-axis alignment. */
fun alignHorizontallyToEnd() = AlignmentSchema.Horizontal.End


/** Aligns content to the top-start corner. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToTopStart() = AlignmentSchema.TwoDimensional.TopStart

/** Aligns content to the top edge, centered horizontally. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToTopCenter() = AlignmentSchema.TwoDimensional.TopCenter

/** Aligns content to the top-end corner. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToTopEnd() = AlignmentSchema.TwoDimensional.TopEnd

/** Aligns content to the start edge, centered vertically. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToCenterStart() = AlignmentSchema.TwoDimensional.CenterStart

/** Aligns content dead center, both axes. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToCenter() = AlignmentSchema.TwoDimensional.Center

/** Aligns content to the end edge, centered vertically. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToCenterEnd() = AlignmentSchema.TwoDimensional.CenterEnd

/** Aligns content to the bottom-start corner. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToBottomStart() = AlignmentSchema.TwoDimensional.BottomStart

/** Aligns content to the bottom edge, centered horizontally. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToBottomCenter() = AlignmentSchema.TwoDimensional.BottomCenter

/** Aligns content to the bottom-end corner. Used by two-dimensional containers such as `Box` and `AsyncImage`. */
fun alignToBottomEnd() = AlignmentSchema.TwoDimensional.BottomEnd