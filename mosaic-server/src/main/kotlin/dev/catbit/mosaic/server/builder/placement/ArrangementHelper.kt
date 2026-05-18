package dev.catbit.mosaic.server.builder.placement

import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema

fun arrangeVerticallyToTop() = ArrangementSchema.Vertical.Top
fun arrangeVerticallyToBottom() = ArrangementSchema.Vertical.Bottom
fun arrangeVerticallySpacedBy(
    space: Int,
    alignment: AlignmentSchema.Vertical = AlignmentSchema.Vertical.Top
) = ArrangementSchema.Vertical.SpacedBy(space, alignment)

fun arrangeHorizontallyToStart() = ArrangementSchema.Horizontal.Start
fun arrangeHorizontallyToEnd() = ArrangementSchema.Horizontal.End
fun arrangeHorizontallySpacedBy(
    space: Int,
    alignment: AlignmentSchema.Horizontal = AlignmentSchema.Horizontal.Start
) = ArrangementSchema.Horizontal.SpacedBy(space, alignment)

fun arrangeToCenter() = ArrangementSchema.HorizontalOrVertical.Center
fun arrangeSpaceEvenly() = ArrangementSchema.HorizontalOrVertical.SpaceEvenly
fun arrangeSpaceBetween() = ArrangementSchema.HorizontalOrVertical.SpaceBetween
fun arrangeSpaceAround() = ArrangementSchema.HorizontalOrVertical.SpaceAround