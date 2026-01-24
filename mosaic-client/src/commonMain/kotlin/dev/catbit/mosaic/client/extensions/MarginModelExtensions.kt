package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.tile.style.MarginModel

fun MarginModel.toPaddingValues() = PaddingValues(
    top = top.dp,
    end = end.dp,
    bottom = bottom.dp,
    start = start.dp,
)