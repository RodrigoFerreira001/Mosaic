package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema

fun ArrangementSchema.Vertical.toArrangement(): Arrangement.Vertical = when (this) {
    ArrangementSchema.Vertical.Bottom -> Arrangement.Bottom
    is ArrangementSchema.Vertical.SpacedBy -> Arrangement.spacedBy(
        space = space.dp, alignment = alignment.toAlignment()
    )

    else -> Arrangement.Top
}

fun ArrangementSchema.Horizontal.toArrangement(): Arrangement.Horizontal = when (this) {
    ArrangementSchema.Horizontal.End -> Arrangement.End
    is ArrangementSchema.Horizontal.SpacedBy -> Arrangement.spacedBy(
        space = space.dp,
        alignment = alignment.toAlignment()
    )

    else -> Arrangement.Start
}

fun ArrangementSchema.HorizontalOrVertical.toArrangement(): Arrangement.HorizontalOrVertical = when (this) {
    ArrangementSchema.HorizontalOrVertical.Center -> Arrangement.Center
    ArrangementSchema.HorizontalOrVertical.SpaceAround -> Arrangement.SpaceAround
    ArrangementSchema.HorizontalOrVertical.SpaceBetween -> Arrangement.SpaceBetween
    ArrangementSchema.HorizontalOrVertical.SpaceEvenly -> Arrangement.SpaceEvenly
}