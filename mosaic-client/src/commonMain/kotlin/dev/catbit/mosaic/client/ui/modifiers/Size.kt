package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.core.data.tile.style.SizeModel

@Composable
fun Modifier.size(size: SizeModel): Modifier {
    return this then then {
        when (val width = size.width) {
            SizeModel.Behavior.Horizontal.Fill -> Modifier.fillMaxWidth()
            SizeModel.Behavior.Horizontal.Wrap -> Modifier.wrapContentWidth()
            is SizeModel.Behavior.Horizontal.Fixed -> Modifier.width(width.value.dp)
            is SizeModel.Behavior.Horizontal.Weight -> {
                LocalRowScope.current?.let { rowScope ->
                    with(rowScope) {
                        Modifier.weight(width.value)
                    }
                } ?: Modifier
            }

            is SizeModel.Behavior.Horizontal.Span -> {
                when (val sharedArea = LocalSharedHorizontalArea.current) {
                    is SharedHorizontalArea.Defined -> {
                        val totalGutters = sharedArea.gutter * (sharedArea.columns - 1)
                        val internalGutters = (width.value - 1) * sharedArea.gutter

                        val totalPadding = sharedArea.horizontalPadding * 2
                        val remainingWidth = sharedArea.totalWidth - totalGutters - totalPadding
                        val columnWidth = remainingWidth / sharedArea.columns

                        Modifier.width((columnWidth * width.value + internalGutters).dp)
                    }

                    SharedHorizontalArea.Undefined -> Modifier.wrapContentWidth()
                }
            }
        }
    }
        .then {
            when (val height = size.height) {
                SizeModel.Behavior.Vertical.Fill -> Modifier.fillMaxHeight()
                is SizeModel.Behavior.Vertical.Fixed -> Modifier.height(height.value.dp)
                SizeModel.Behavior.Vertical.Wrap -> Modifier.wrapContentHeight()
                is SizeModel.Behavior.Vertical.Weight -> {
                    LocalColumnScope.current?.let { columnScope ->
                        with(columnScope) {
                            Modifier.weight(height.value)
                        }
                    } ?: Modifier
                }
            }
        }
}