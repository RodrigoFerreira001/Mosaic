package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlexAlignSelf
import androidx.compose.foundation.layout.FlexBasis
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlexBoxScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlowRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalGridScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.core.data.schemas.tile.style.SizeSchema

@OptIn(ExperimentalFlexBoxApi::class, ExperimentalGridApi::class, ExperimentalLayoutApi::class)
@Composable
fun Modifier.size(size: SizeSchema): Modifier {

    return this
        .then {
            when (val width = size.width) {
                is SizeSchema.Behavior.Horizontal.Fill -> Modifier
                    .thenIfNotNull(width.max) { widthIn(it.dp) }.fillMaxWidth()

                SizeSchema.Behavior.Horizontal.Wrap -> Modifier.wrapContentWidth()

                is SizeSchema.Behavior.Horizontal.Fixed -> Modifier.width(width.value.dp)

                is SizeSchema.Behavior.Horizontal.Weight -> {
                    LocalRowScope.current?.let { rowScope ->
                        with(rowScope) { Modifier.weight(width.value) }
                    } ?: LocalFlowRowScope.current?.let { flowRowScope ->
                        with(flowRowScope) { Modifier.weight(width.value) }
                    } ?: Modifier
                }

                is SizeSchema.Behavior.Horizontal.Span -> {
                    LocalGridScope.current?.let { gridScope ->
                        with(gridScope) {
                            gridItem(
                                rowSpan = width.value
                            )
                        }
                    } ?: Modifier
                }

                is SizeSchema.Behavior.Horizontal.Flex -> {
                    LocalFlexBoxScope.current?.let { flexBoxScope ->
                        with(flexBoxScope) {
                            flex {
                                with(width) {
                                    grow?.let { grow(it) }
                                    shrink?.let { shrink(it) }
                                    basis?.let {
                                        when (it) {
                                            is SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Auto -> basis(
                                                FlexBasis.Auto
                                            )

                                            is SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fixed -> basis(
                                                it.value.dp
                                            )

                                            is SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fraction -> basis(
                                                it.value
                                            )
                                        }
                                    }
                                    alignSelf?.let {
                                        when (it) {
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Auto -> FlexAlignSelf.Auto
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Start -> FlexAlignSelf.Start
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Center -> FlexAlignSelf.Center
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.End -> FlexAlignSelf.End
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Stretch -> FlexAlignSelf.Stretch
                                            SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Baseline -> FlexAlignSelf.Baseline
                                        }
                                    }
                                    order?.let { order(it) }

                                }
                            }
                        }
                    } ?: Modifier
                }
            }
        }
        .then {
            when (val height = size.height) {
                is SizeSchema.Behavior.Vertical.Fill -> Modifier
                    .thenIfNotNull(height.max) { heightIn(max = it.dp) }.fillMaxHeight()

                is SizeSchema.Behavior.Vertical.Fixed -> Modifier.height(height.value.dp)

                SizeSchema.Behavior.Vertical.Wrap -> Modifier.wrapContentHeight()

                is SizeSchema.Behavior.Vertical.Weight -> {
                    LocalColumnScope.current?.let { columnScope ->
                        with(columnScope) { Modifier.weight(height.value) }
                    } ?: Modifier
                }

                is SizeSchema.Behavior.Vertical.Span -> {
                    LocalGridScope.current?.let { gridScope ->
                        with(gridScope) {
                            gridItem(
                                rowSpan = height.value
                            )
                        }
                    } ?: Modifier
                }

                is SizeSchema.Behavior.Vertical.FillRow -> {
                    LocalFlowRowScope.current?.let { flowRowScope ->
                        with(flowRowScope) { Modifier.fillMaxRowHeight(height.fraction) }
                    } ?: Modifier
                }
            }
        }
}
