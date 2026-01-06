package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.SizeUIState

@Composable
fun Modifier.size(size: SizeUIState): Modifier {
    return this then then {
        when (val width = size.width) {
            SizeUIState.Behavior.Horizontal.Fill -> Modifier.fillMaxWidth()
            SizeUIState.Behavior.Horizontal.Wrap -> Modifier.wrapContentWidth()
            is SizeUIState.Behavior.Horizontal.Fixed -> Modifier.width(width.value)
            is SizeUIState.Behavior.Horizontal.Weight -> {
                LocalRowScope.current?.let { rowScope ->
                    with(rowScope) {
                        Modifier.weight(width.value)
                    }
                } ?: Modifier
            }

            is SizeUIState.Behavior.Horizontal.Span -> {
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
                SizeUIState.Behavior.Vertical.Fill -> Modifier.fillMaxHeight()
                is SizeUIState.Behavior.Vertical.Fixed -> Modifier.height(height.value)
                SizeUIState.Behavior.Vertical.Wrap -> Modifier.wrapContentHeight()
                is SizeUIState.Behavior.Vertical.Weight -> {
                    LocalColumnScope.current?.let { columnScope ->
                        with(columnScope) {
                            Modifier.weight(height.value)
                        }
                    } ?: Modifier
                }
            }
        }
}