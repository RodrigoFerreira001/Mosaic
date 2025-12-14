package dev.catbit.mosaic.client.ui.mappings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.BorderUIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.ClipUIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.RadiusUIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.SizeUIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.StyleUIState
import dev.catbit.mosaic.client.ui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.foundation.models.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.core.data.color.ColorModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.BorderModel
import dev.catbit.mosaic.core.data.tile.style.ClipModel
import dev.catbit.mosaic.core.data.tile.style.RadiusModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel
import dev.catbit.mosaic.core.mapping.mapTo
import dev.catbit.mosaic.core.mapping.mapping

object BaseUIMappings {
    val mappings by lazy {
        listOf(
            mapping<SizeModel.Behavior.Horizontal, SizeUIState.Behavior.Horizontal> {
                when (this) {
                    SizeModel.Behavior.Horizontal.Fill -> SizeUIState.Behavior.Horizontal.Fill
                    is SizeModel.Behavior.Horizontal.Fixed -> SizeUIState.Behavior.Horizontal.Fixed(value = value.dp)
                    is SizeModel.Behavior.Horizontal.Span -> SizeUIState.Behavior.Horizontal.Span(value = value)
                    is SizeModel.Behavior.Horizontal.Weight -> SizeUIState.Behavior.Horizontal.Weight(value = value)
                    SizeModel.Behavior.Horizontal.Wrap -> SizeUIState.Behavior.Horizontal.Wrap
                }
            },
            mapping<SizeModel.Behavior.Vertical, SizeUIState.Behavior.Vertical> {
                when (this) {
                    SizeModel.Behavior.Vertical.Fill -> SizeUIState.Behavior.Vertical.Fill
                    is SizeModel.Behavior.Vertical.Fixed -> SizeUIState.Behavior.Vertical.Fixed(value = value.dp)
                    is SizeModel.Behavior.Vertical.Weight -> SizeUIState.Behavior.Vertical.Weight(value = value)
                    SizeModel.Behavior.Vertical.Wrap -> SizeUIState.Behavior.Vertical.Wrap
                }
            },
            mapping<SizeModel, SizeUIState> { mapper ->
                SizeUIState(
                    width = width.mapTo(mapper),
                    height = height.mapTo(mapper),
                )
            },
            mapping<ClipModel, ClipUIState> { mapper ->
                ClipUIState(shape = shape.mapTo(mapper))
            },
            mapping<WindowInsetsModel, WindowInsetsUIModel> {
                when (this) {
                    WindowInsetsModel.CaptionBar -> WindowInsetsUIModel.CaptionBar
                    WindowInsetsModel.DisplayCutout -> WindowInsetsUIModel.DisplayCutout
                    WindowInsetsModel.Ime -> WindowInsetsUIModel.Ime
                    WindowInsetsModel.NavigationBar -> WindowInsetsUIModel.NavigationBar
                    WindowInsetsModel.StatusBar -> WindowInsetsUIModel.StatusBar
                    WindowInsetsModel.SystemBars -> WindowInsetsUIModel.SystemBars
                    WindowInsetsModel.Waterfall -> WindowInsetsUIModel.Waterfall
                }
            },
            mapping<RadiusModel, RadiusUIState> {
                RadiusUIState(
                    topStart = topStart.dp,
                    topEnd = topEnd.dp,
                    bottomStart = bottomStart.dp,
                    bottomEnd = bottomEnd.dp
                )
            },
            mapping<ColorModel, ColorUIModel> {
                when (this) {
                    is ColorModel.Hex -> ColorUIModel.Hex(
                        value = value
                    )

                    is ColorModel.Rgba -> ColorUIModel.Rgba(
                        r = r,
                        g = g,
                        b = b,
                        alpha = alpha
                    )

                    is ColorModel.Theme -> ColorUIModel.Theme(
                        value = ColorUIModel.Theme.Color.valueOf(value.name)
                    )
                }
            },
            mapping<BorderModel, BorderUIState> { mapper ->
                BorderUIState(
                    color = color.mapTo(mapper),
                    thickness = thickness.dp,
                    radius = radius?.mapTo(mapper)
                )
            },
            mapping<StyleModel, StyleUIState> { mapper ->
                StyleUIState(
                    size = size.mapTo(mapper),
                    margin = margin?.mapTo(mapper),
                    padding = padding?.mapTo(mapper),
                    background = background?.mapTo(mapper),
                    border = border?.mapTo(mapper),
                    clip = clip?.mapTo(mapper),
                    windowInsets = windowInsets?.mapTo(mapper),
                )
            },
            mapping<ArrangementModel.Vertical, Arrangement.Vertical> { mapper ->
                when (this) {
                    ArrangementModel.Vertical.Top -> Arrangement.Top
                    ArrangementModel.Vertical.Bottom -> Arrangement.Bottom
                    is ArrangementModel.Vertical.SpacedBy -> Arrangement.spacedBy(
                        space = space.dp,
                        alignment = alignment.mapTo<Alignment.Vertical>(mapper)
                    )

                    else -> throw IllegalArgumentException("$this is not a ArrangementModel.Vertical")
                }
            },
            mapping<ArrangementModel.Horizontal, Arrangement.Horizontal> { mapper ->
                when (this) {
                    ArrangementModel.Horizontal.Start -> Arrangement.Start
                    ArrangementModel.Horizontal.End -> Arrangement.End
                    is ArrangementModel.Horizontal.SpacedBy -> Arrangement.spacedBy(
                        space = space.dp,
                        alignment = alignment.mapTo<Alignment.Horizontal>(mapper)
                    )

                    else -> throw IllegalArgumentException("$this is not a ArrangementModel.Horizontal")
                }
            },
            mapping<ArrangementModel.HorizontalOrVertical, Arrangement.HorizontalOrVertical> {
                when (this) {
                    ArrangementModel.HorizontalOrVertical.Center -> Arrangement.Center
                    ArrangementModel.HorizontalOrVertical.SpaceAround -> Arrangement.SpaceAround
                    ArrangementModel.HorizontalOrVertical.SpaceBetween -> Arrangement.SpaceBetween
                    ArrangementModel.HorizontalOrVertical.SpaceEvenly -> Arrangement.SpaceEvenly
                }
            },
            mapping<AlignmentModel.Vertical, Alignment.Vertical> {
                when(this) {
                    AlignmentModel.Vertical.Top -> Alignment.Top
                    AlignmentModel.Vertical.Center -> Alignment.CenterVertically
                    AlignmentModel.Vertical.Bottom -> Alignment.Bottom
                }
            },
            mapping<AlignmentModel.Horizontal, Alignment.Horizontal> {
                when(this) {
                    AlignmentModel.Horizontal.Start -> Alignment.Start
                    AlignmentModel.Horizontal.Center -> Alignment.CenterHorizontally
                    AlignmentModel.Horizontal.End -> Alignment.End
                }
            },
            mapping<AlignmentModel.TwoDimensional, Alignment> {
                when(this) {
                    AlignmentModel.TwoDimensional.BottomCenter -> Alignment.BottomCenter
                    AlignmentModel.TwoDimensional.BottomEnd -> Alignment.BottomEnd
                    AlignmentModel.TwoDimensional.BottomStart -> Alignment.BottomStart
                    AlignmentModel.TwoDimensional.Center -> Alignment.Center
                    AlignmentModel.TwoDimensional.CenterEnd -> Alignment.CenterEnd
                    AlignmentModel.TwoDimensional.CenterStart -> Alignment.CenterStart
                    AlignmentModel.TwoDimensional.TopCenter -> Alignment.TopCenter
                    AlignmentModel.TwoDimensional.TopEnd -> Alignment.TopEnd
                    AlignmentModel.TwoDimensional.TopStart -> Alignment.TopStart
                }
            },
            mapping<TileModel.Visibility, TileUIState.Visibility> {
                TileUIState.Visibility.valueOf(name)
            }
        )
    }
}