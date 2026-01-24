package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel

fun ArrangementModel.Vertical.toArrangement(): Arrangement.Vertical = when (this) {
    ArrangementModel.Vertical.Bottom -> Arrangement.Bottom
    is ArrangementModel.Vertical.SpacedBy -> Arrangement.spacedBy(
        space = space.dp, alignment = alignment.toAlignment()
    )

    else -> Arrangement.Top
}

fun ArrangementModel.Horizontal.toArrangement(): Arrangement.Horizontal = when (this) {
    ArrangementModel.Horizontal.End -> Arrangement.End
    is ArrangementModel.Horizontal.SpacedBy -> Arrangement.spacedBy(
        space = space.dp,
        alignment = alignment.toAlignment()
    )

    else -> Arrangement.Start
}

fun ArrangementModel.HorizontalOrVertical.toArrangement(): Arrangement.HorizontalOrVertical = when (this) {
    ArrangementModel.HorizontalOrVertical.Center -> Arrangement.Center
    ArrangementModel.HorizontalOrVertical.SpaceAround -> Arrangement.SpaceAround
    ArrangementModel.HorizontalOrVertical.SpaceBetween -> Arrangement.SpaceBetween
    ArrangementModel.HorizontalOrVertical.SpaceEvenly -> Arrangement.SpaceEvenly
}