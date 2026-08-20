package dev.catbit.mosaic.server.builder.placement

import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema

/** Packs children against the top of the main (vertical) axis, e.g. a `Column`'s `arrangement`. */
fun arrangeVerticallyToTop() = ArrangementSchema.Vertical.Top

/** Packs children against the bottom of the main (vertical) axis, e.g. a `Column`'s `arrangement`. */
fun arrangeVerticallyToBottom() = ArrangementSchema.Vertical.Bottom

/**
 * Spaces children [space] dp apart along the vertical axis, e.g. a `Column`'s `arrangement`.
 *
 * @param space Gap between consecutive children, in dp.
 * @param alignment Where the group of children sits when it doesn't fill the available height. Defaults to top.
 */
fun arrangeVerticallySpacedBy(
    space: Int,
    alignment: AlignmentSchema.Vertical = AlignmentSchema.Vertical.Top
) = ArrangementSchema.Vertical.SpacedBy(space, alignment)


/** Packs children against the start of the main (horizontal) axis (mirrored under RTL), e.g. a `Row`'s `arrangement`. */
fun arrangeHorizontallyToStart() = ArrangementSchema.Horizontal.Start

/** Packs children against the end of the main (horizontal) axis (mirrored under RTL), e.g. a `Row`'s `arrangement`. */
fun arrangeHorizontallyToEnd() = ArrangementSchema.Horizontal.End

/**
 * Spaces children [space] dp apart along the horizontal axis, e.g. a `Row`'s `arrangement`.
 *
 * @param space Gap between consecutive children, in dp.
 * @param alignment Where the group of children sits when it doesn't fill the available width. Defaults to start.
 */
fun arrangeHorizontallySpacedBy(
    space: Int,
    alignment: AlignmentSchema.Horizontal = AlignmentSchema.Horizontal.Start
) = ArrangementSchema.Horizontal.SpacedBy(space, alignment)


/** Packs children at the center of the main axis. Usable for both vertical and horizontal arrangements. */
fun arrangeToCenter() = ArrangementSchema.HorizontalOrVertical.Center

/** Distributes children with equal space between them and at both edges. Usable for both vertical and horizontal arrangements. */
fun arrangeSpaceEvenly() = ArrangementSchema.HorizontalOrVertical.SpaceEvenly

/** Distributes children with equal space between them, none at the edges. Usable for both vertical and horizontal arrangements. */
fun arrangeSpaceBetween() = ArrangementSchema.HorizontalOrVertical.SpaceBetween

/** Distributes children with equal space around each of them. Usable for both vertical and horizontal arrangements. */
fun arrangeSpaceAround() = ArrangementSchema.HorizontalOrVertical.SpaceAround