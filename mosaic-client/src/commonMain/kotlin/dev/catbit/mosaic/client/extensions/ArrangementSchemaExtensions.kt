package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema

/** Converts a vertical [ArrangementSchema] — including the axis-agnostic
 * [ArrangementSchema.HorizontalOrVertical] cases — into its Compose [Arrangement.Vertical]
 * counterpart. Used by `Column`/`LazyColumn`'s `arrangement`. */
fun ArrangementSchema.Vertical.toArrangement(): Arrangement.Vertical = when (this) {
    is ArrangementSchema.HorizontalOrVertical -> toArrangement()
    ArrangementSchema.Vertical.Bottom -> Arrangement.Bottom
    is ArrangementSchema.Vertical.SpacedBy -> Arrangement.spacedBy(
        space = space.dp, alignment = alignment.toAlignment()
    )

    else -> Arrangement.Top
}

/** Converts a horizontal [ArrangementSchema] — including the axis-agnostic
 * [ArrangementSchema.HorizontalOrVertical] cases — into its Compose [Arrangement.Horizontal]
 * counterpart. Used by `Row`/`LazyRow`/`FlowRow`'s `arrangement`. */
fun ArrangementSchema.Horizontal.toArrangement(): Arrangement.Horizontal = when (this) {
    is ArrangementSchema.HorizontalOrVertical -> toArrangement()
    ArrangementSchema.Horizontal.End -> Arrangement.End
    is ArrangementSchema.Horizontal.SpacedBy -> Arrangement.spacedBy(
        space = space.dp,
        alignment = alignment.toAlignment()
    )

    else -> Arrangement.Start
}

/** Converts the axis-agnostic [ArrangementSchema.HorizontalOrVertical] cases (`Center`,
 * `SpaceAround`, `SpaceBetween`, `SpaceEvenly`) into their Compose counterpart, valid on either
 * axis. */
fun ArrangementSchema.HorizontalOrVertical.toArrangement(): Arrangement.HorizontalOrVertical = when (this) {
    ArrangementSchema.HorizontalOrVertical.Center -> Arrangement.Center
    ArrangementSchema.HorizontalOrVertical.SpaceAround -> Arrangement.SpaceAround
    ArrangementSchema.HorizontalOrVertical.SpaceBetween -> Arrangement.SpaceBetween
    ArrangementSchema.HorizontalOrVertical.SpaceEvenly -> Arrangement.SpaceEvenly
}