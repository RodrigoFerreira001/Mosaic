package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.core.data.schemas.tile.style.SizeSchema

@Composable
fun Modifier.size(size: SizeSchema): Modifier {
    return this then then {
        when (val width = size.width) {
            SizeSchema.Behavior.Horizontal.Fill -> Modifier.fillMaxWidth()
            SizeSchema.Behavior.Horizontal.Wrap -> Modifier.wrapContentWidth()
            is SizeSchema.Behavior.Horizontal.Fixed -> Modifier.width(width.value.dp)
            is SizeSchema.Behavior.Horizontal.Weight -> {
                LocalRowScope.current?.let { rowScope ->
                    with(rowScope) {
                        Modifier.weight(width.value)
                    }
                } ?: Modifier
            }

            is SizeSchema.Behavior.Horizontal.Span -> {
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
                SizeSchema.Behavior.Vertical.Fill -> Modifier.fillMaxHeight()
                is SizeSchema.Behavior.Vertical.Fixed -> Modifier.height(height.value.dp)
                SizeSchema.Behavior.Vertical.Wrap -> Modifier.wrapContentHeight()
                is SizeSchema.Behavior.Vertical.Weight -> {
                    LocalColumnScope.current?.let { columnScope ->
                        with(columnScope) {
                            Modifier.weight(height.value)
                        }
                    } ?: Modifier
                }
            }
        }
}