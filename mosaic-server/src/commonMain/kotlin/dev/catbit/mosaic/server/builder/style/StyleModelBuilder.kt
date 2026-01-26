package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.color.ColorModel
import dev.catbit.mosaic.core.data.tile.style.BorderModel
import dev.catbit.mosaic.core.data.tile.style.ClipModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.PaddingModel
import dev.catbit.mosaic.core.data.tile.style.RadiusModel
import dev.catbit.mosaic.core.data.tile.style.ShapeModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel
import dev.catbit.mosaic.server.builder.GenericBuilder

class StyleModelBuilder : GenericBuilder<StyleModel> {

    private var size: SizeModel = SizeModel(
        width = SizeModel.Behavior.Horizontal.Fill,
        height = SizeModel.Behavior.Vertical.Wrap
    )
    private var margin: MarginModel? = null
    private var padding: PaddingModel? = null
    private var background: ColorModel? = null
    private var border: BorderModel? = null
    private var clip: ClipModel? = null
    private var windowInsetsModel: WindowInsetsModel? = null

    override fun build() = StyleModel(
        size = size,
        margin = margin,
        padding = padding,
        background = background,
        border = border,
        clip = clip,
        windowInsets = windowInsetsModel
    )

    inner class StyleModelBuilderScope {

        fun size(
            width: SizeModel.Behavior.Horizontal = fillHorizontally(),
            height: SizeModel.Behavior.Vertical = wrapVertically()
        ) {
            size = SizeModel(
                width = width,
                height = height
            )
        }

        fun margin(
            top: Int = 0,
            end: Int = 0,
            bottom: Int = 0,
            start: Int = 0
        ) {
            margin = MarginModel(
                top = top,
                end = end,
                bottom = bottom,
                start = start,
            )
        }

        fun margin(
            horizontal: Int = 0,
            vertical: Int = 0
        ) {
            margin = MarginModel(
                top = vertical,
                end = horizontal,
                bottom = vertical,
                start = horizontal,
            )
        }

        fun margin(
            horizontal: Int = 0,
            top: Int = 0,
            bottom: Int = 0,
        ) {
            margin = MarginModel(
                top = top,
                end = horizontal,
                bottom = bottom,
                start = horizontal,
            )
        }

        fun padding(
            top: Int = 0,
            end: Int = 0,
            bottom: Int = 0,
            start: Int = 0
        ) {
            padding = PaddingModel(
                top = top,
                end = end,
                bottom = bottom,
                start = start,
            )
        }

        fun padding(
            horizontal: Int = 0,
            vertical: Int = 0
        ) {
            padding = PaddingModel(
                top = vertical,
                end = horizontal,
                bottom = vertical,
                start = horizontal,
            )
        }

        fun padding(
            horizontal: Int = 0,
            top: Int = 0,
            bottom: Int = 0,
        ) {
            padding = PaddingModel(
                top = top,
                end = horizontal,
                bottom = bottom,
                start = horizontal,
            )
        }

        fun background(
            color: ColorModel
        ) {
            background = color
        }

        fun border(
            color: ColorModel,
            thickness: Int,
            radius: RadiusModel? = null
        ) {
            border = BorderModel(
                color = color,
                thickness = thickness,
                radius = radius,
            )
        }

        fun clip(
            shape: ShapeModel
        ) {
            clip = ClipModel(
                shape = shape
            )
        }

        fun windowInsets(
            windowInsets: WindowInsetsModel
        ) {
            windowInsetsModel = windowInsets
        }

        fun fillHorizontally() = SizeModel.Behavior.Horizontal.Fill

        fun wrapHorizontally() = SizeModel.Behavior.Horizontal.Wrap

        fun fixedHorizontally(value: Int) = SizeModel.Behavior.Horizontal.Fixed(value)

        fun weightHorizontally(value: Float) = SizeModel.Behavior.Horizontal.Weight(value)

        fun spanHorizontally(value: Int) = SizeModel.Behavior.Horizontal.Span(value)

        fun fillVertically() = SizeModel.Behavior.Vertical.Fill

        fun wrapVertically() = SizeModel.Behavior.Vertical.Wrap

        fun fixedVertically(value: Int) = SizeModel.Behavior.Vertical.Fixed(value)

        fun weightVertically(value: Float) = SizeModel.Behavior.Vertical.Weight(value)

        fun color(
            hex: String
        ) = ColorModel.Hex(
            value = hex
        )

        fun color(
            r: Float = 0f,
            g: Float = 0f,
            b: Float = 0f,
            alpha: Float = 1f
        ) = ColorModel.Rgba(
            r = r,
            g = g,
            b = b,
            alpha = alpha
        )

        fun color(
            value: ColorModel.Theme.Color
        ) = ColorModel.Theme(
            value = value
        )

        fun radius(
            topStart: Int = 0,
            topEnd: Int = 0,
            bottomStart: Int = 0,
            bottomEnd: Int = 0
        ) = RadiusModel(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )

        fun circleShape() = ShapeModel.Circle

        fun rectangleShape() = ShapeModel.Rectangle

        fun roundedCornerShape(
            radius: RadiusModel
        ) = ShapeModel.RoundedCornerRectangle(radius)

        fun windowInsetsSystemBars() = WindowInsetsModel.SystemBars

        fun windowInsetsCaptionBar() = WindowInsetsModel.CaptionBar

        fun windowInsetsStatusBar() = WindowInsetsModel.StatusBar

        fun windowInsetsNavigationBar() = WindowInsetsModel.NavigationBar

        fun windowInsetsIme() = WindowInsetsModel.Ime

        fun windowInsetsDisplayCutout() = WindowInsetsModel.DisplayCutout

        fun windowInsetsWaterfall() = WindowInsetsModel.Waterfall
    }
}