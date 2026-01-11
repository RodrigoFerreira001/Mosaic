package dev.catbit.mosaic.client.ui.sdui.foundation.mappings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.client.ui.sdui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.models.IconUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.models.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.BorderUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.ClipUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.RadiusUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.SizeUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState
import dev.catbit.mosaic.core.data.color.ColorModel
import dev.catbit.mosaic.core.data.icon.IconModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.BorderModel
import dev.catbit.mosaic.core.data.tile.style.ClipModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.PaddingModel
import dev.catbit.mosaic.core.data.tile.style.RadiusModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel
import dev.catbit.mosaic.core.mapping.mapTo
import dev.catbit.mosaic.core.mapping.mapping

object BaseUIMappings {
    val mappings by lazy {
        listOf(
            mapping<SizeModel.Behavior.Horizontal.Fill, SizeUIState.Behavior.Horizontal> {
                SizeUIState.Behavior.Horizontal.Fill
            },
            mapping<SizeModel.Behavior.Horizontal.Fixed, SizeUIState.Behavior.Horizontal> {
                SizeUIState.Behavior.Horizontal.Fixed(value = value.dp)
            },
            mapping<SizeModel.Behavior.Horizontal.Span, SizeUIState.Behavior.Horizontal> {
                SizeUIState.Behavior.Horizontal.Span(value = value)
            },
            mapping<SizeModel.Behavior.Horizontal.Weight, SizeUIState.Behavior.Horizontal> {
                SizeUIState.Behavior.Horizontal.Weight(value = value)
            },
            mapping<SizeModel.Behavior.Horizontal.Wrap, SizeUIState.Behavior.Horizontal> {
                SizeUIState.Behavior.Horizontal.Wrap
            },
            mapping<SizeModel.Behavior.Vertical.Fill, SizeUIState.Behavior.Vertical> {
                SizeUIState.Behavior.Vertical.Fill
            },
            mapping<SizeModel.Behavior.Vertical.Wrap, SizeUIState.Behavior.Vertical> {
                SizeUIState.Behavior.Vertical.Wrap
            },
            mapping<SizeModel.Behavior.Vertical.Weight, SizeUIState.Behavior.Vertical> {
                SizeUIState.Behavior.Vertical.Weight(value)
            },
            mapping<SizeModel.Behavior.Vertical.Fixed, SizeUIState.Behavior.Vertical> {
                SizeUIState.Behavior.Vertical.Fixed(value.dp)
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
            mapping<WindowInsetsModel.CaptionBar, WindowInsetsUIModel> {
                WindowInsetsUIModel.CaptionBar
            },
            mapping<WindowInsetsModel.DisplayCutout, WindowInsetsUIModel> {
                WindowInsetsUIModel.DisplayCutout
            },
            mapping<WindowInsetsModel.Ime, WindowInsetsUIModel> {
                WindowInsetsUIModel.Ime
            },
            mapping<WindowInsetsModel.NavigationBar, WindowInsetsUIModel> {
                WindowInsetsUIModel.NavigationBar
            },
            mapping<WindowInsetsModel.StatusBar, WindowInsetsUIModel> {
                WindowInsetsUIModel.StatusBar
            },
            mapping<WindowInsetsModel.SystemBars, WindowInsetsUIModel> {
                WindowInsetsUIModel.SystemBars
            },
            mapping<WindowInsetsModel.Waterfall, WindowInsetsUIModel> {
                WindowInsetsUIModel.Waterfall
            },
            mapping<ArrangementModel.Horizontal.Start, Arrangement.Horizontal> {
                Arrangement.Start
            },
            mapping<ArrangementModel.Horizontal.End, Arrangement.Horizontal> {
                Arrangement.End
            },
            mapping<ArrangementModel.Horizontal.SpacedBy, Arrangement.Horizontal> { mapper ->
                Arrangement.spacedBy(
                    space = space.dp,
                    alignment = alignment.mapTo<Alignment.Horizontal>(mapper)
                )
            },
            mapping<ArrangementModel.Vertical.Top, Arrangement.Vertical> {
                Arrangement.Top
            },
            mapping<ArrangementModel.Vertical.Bottom, Arrangement.Vertical> {
                Arrangement.Bottom
            },
            mapping<ArrangementModel.Vertical.SpacedBy, Arrangement.Vertical> { mapper ->
                Arrangement.spacedBy(
                    space = space.dp,
                    alignment = alignment.mapTo<Alignment.Vertical>(mapper)
                )
            },
            mapping<ArrangementModel.HorizontalOrVertical.Center, Arrangement.HorizontalOrVertical> {
                Arrangement.Center
            },
            mapping<ArrangementModel.HorizontalOrVertical.SpaceAround, Arrangement.HorizontalOrVertical> {
                Arrangement.SpaceAround
            },
            mapping<ArrangementModel.HorizontalOrVertical.SpaceBetween, Arrangement.HorizontalOrVertical> {
                Arrangement.SpaceBetween
            },
            mapping<ArrangementModel.HorizontalOrVertical.SpaceEvenly, Arrangement.HorizontalOrVertical> {
                Arrangement.SpaceEvenly
            },
            mapping<AlignmentModel.Vertical.Top, Alignment.Vertical> {
                Alignment.Top
            },
            mapping<AlignmentModel.Vertical.Center, Alignment.Vertical> {
                Alignment.CenterVertically
            },
            mapping<AlignmentModel.Vertical.Bottom, Alignment.Vertical> {
                Alignment.Bottom
            },
            mapping<AlignmentModel.Horizontal.Start, Alignment.Horizontal> {
                Alignment.Start
            },
            mapping<AlignmentModel.Horizontal.Center, Alignment.Horizontal> {
                Alignment.CenterHorizontally
            },
            mapping<AlignmentModel.Horizontal.End, Alignment.Horizontal> {
                Alignment.End
            },
            mapping<AlignmentModel.TwoDimensional.BottomCenter, Alignment> {
                Alignment.BottomCenter
            },
            mapping<AlignmentModel.TwoDimensional.BottomEnd, Alignment> {
                Alignment.BottomEnd
            },
            mapping<AlignmentModel.TwoDimensional.BottomStart, Alignment> {
                Alignment.BottomStart
            },
            mapping<AlignmentModel.TwoDimensional.Center, Alignment> {
                Alignment.Center
            },
            mapping<AlignmentModel.TwoDimensional.CenterEnd, Alignment> {
                Alignment.CenterEnd
            },
            mapping<AlignmentModel.TwoDimensional.CenterStart, Alignment> {
                Alignment.CenterStart
            },
            mapping<AlignmentModel.TwoDimensional.TopCenter, Alignment> {
                Alignment.TopCenter
            },
            mapping<AlignmentModel.TwoDimensional.TopEnd, Alignment> {
                Alignment.TopEnd
            },
            mapping<AlignmentModel.TwoDimensional.TopStart, Alignment> {
                Alignment.TopStart
            },
            mapping<RadiusModel, RadiusUIState> {
                RadiusUIState(
                    topStart = topStart.dp,
                    topEnd = topEnd.dp,
                    bottomStart = bottomStart.dp,
                    bottomEnd = bottomEnd.dp
                )
            },
            mapping<ColorModel.Hex, ColorUIModel> {
                ColorUIModel.Hex(value = value)
            },
            mapping<ColorModel.Rgba, ColorUIModel> {
                ColorUIModel.Rgba(
                    r = r,
                    g = g,
                    b = b,
                    alpha = alpha
                )
            },
            mapping<ColorModel.Theme, ColorUIModel> {
                ColorUIModel.Theme(value = ColorUIModel.Theme.Color.valueOf(value.name))
            },
            mapping<BorderModel, BorderUIState> { mapper ->
                BorderUIState(
                    color = color.mapTo(mapper),
                    thickness = thickness.dp,
                    radius = radius?.mapTo(mapper)
                )
            },
            mapping<MarginModel, PaddingValues> {
                PaddingValues(
                    start = start.dp,
                    top = top.dp,
                    end = end.dp,
                    bottom = bottom.dp,
                )
            },
            mapping<PaddingModel, PaddingValues> {
                PaddingValues(
                    start = start.dp,
                    top = top.dp,
                    end = end.dp,
                    bottom = bottom.dp,
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
            mapping<TileModel.Visibility, TileUIState.Visibility> {
                TileUIState.Visibility.valueOf(name)
            },
            mapping<IconModel, IconUIModel> { mapper ->
                IconUIModel(
                    name = name,
                    color = color?.mapTo(mapper),
                    size = size?.dp,
                    style = when(style) {
                        IconModel.Style.OUTLINED -> MaterialSymbolStyle.OUTLINED
                        IconModel.Style.ROUNDED -> MaterialSymbolStyle.ROUNDED
                        IconModel.Style.SHARP -> MaterialSymbolStyle.SHARP
                    }
                )
            }
        )
    }
}