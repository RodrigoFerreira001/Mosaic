package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.BorderSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ClipSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.MarginSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.PaddingSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.RadiusSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.SizeSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.WindowInsetsSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope

class StyleSchemaBuilderScope : GenericBuilderScope<StyleSchema, GenericBuilder<StyleSchema>>() {

    private var size: SizeSchema = SizeSchema(
        width = SizeSchema.Behavior.Horizontal.Wrap,
        height = SizeSchema.Behavior.Vertical.Wrap
    )
    private var margin: MarginSchema? = null
    private var padding: PaddingSchema? = null
    private var background: ColorSchema? = null
    private var border: BorderSchema? = null
    private var clip: ClipSchema? = null
    private var windowInsetsSchema: WindowInsetsSchema? = null

    fun size(
        width: SizeSchema.Behavior.Horizontal = fillHorizontally(),
        height: SizeSchema.Behavior.Vertical = wrapVertically()
    ) {
        size = SizeSchema(
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
        margin = MarginSchema(
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
        margin = MarginSchema(
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
        margin = MarginSchema(
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
        padding = PaddingSchema(
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
        padding = PaddingSchema(
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
        padding = PaddingSchema(
            top = top,
            end = horizontal,
            bottom = bottom,
            start = horizontal,
        )
    }

    fun background(
        color: ColorSchema
    ) {
        background = color
    }

    fun border(
        color: ColorSchema,
        thickness: Int,
        radius: RadiusSchema? = null
    ) {
        border = BorderSchema(
            color = color,
            thickness = thickness,
            radius = radius,
        )
    }

    fun clip(
        shape: ShapeSchema
    ) {
        clip = ClipSchema(
            shape = shape
        )
    }

    fun windowInsets(
        windowInsets: WindowInsetsSchema
    ) {
        windowInsetsSchema = windowInsets
    }

    fun fillHorizontally(max: Int? = null) = SizeSchema.Behavior.Horizontal.Fill(max)

    fun wrapHorizontally() = SizeSchema.Behavior.Horizontal.Wrap

    fun fixedHorizontally(value: Int) = SizeSchema.Behavior.Horizontal.Fixed(value)

    fun weightHorizontally(value: Float) = SizeSchema.Behavior.Horizontal.Weight(value)

    fun spanHorizontally(value: Int) = SizeSchema.Behavior.Horizontal.Span(value)

    fun fillVertically(max: Int? = null) = SizeSchema.Behavior.Vertical.Fill(max)

    fun wrapVertically() = SizeSchema.Behavior.Vertical.Wrap

    fun fixedVertically(value: Int) = SizeSchema.Behavior.Vertical.Fixed(value)

    fun weightVertically(value: Float) = SizeSchema.Behavior.Vertical.Weight(value)

    fun spanVertically(value: Int) = SizeSchema.Behavior.Vertical.Span(value)

    fun fillRowHeight(fraction: Float = 1f) = SizeSchema.Behavior.Vertical.FillRow(fraction)

    fun flexHorizontally(
        grow: Float? = null,
        shrink: Float? = null,
        basis: SizeSchema.Behavior.Horizontal.Flex.FlexBasis? = null,
        alignSelf: SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf? = null,
        order: Int? = null
    ) = SizeSchema.Behavior.Horizontal.Flex(
        grow = grow,
        shrink = shrink,
        basis = basis,
        alignSelf = alignSelf,
        order = order
    )

    fun flexBasisAuto() = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Auto

    fun flexBasisFixed(value: Int) = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fixed(value)

    fun flexBasisFraction(value: Float) = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fraction(value)

    fun flexAlignSelfAuto() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Auto

    fun flexAlignSelfStart() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Start

    fun flexAlignSelfCenter() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Center

    fun flexAlignSelfEnd() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.End

    fun flexAlignSelfStretch() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Stretch

    fun flexAlignSelfBaseline() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Baseline

    fun radius(
        topStart: Int = 0,
        topEnd: Int = 0,
        bottomStart: Int = 0,
        bottomEnd: Int = 0
    ) = RadiusSchema(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd
    )

    fun circleShape() = ShapeSchema.Circle

    fun rectangleShape() = ShapeSchema.Rectangle

    fun roundedCornerShape(
        radius: RadiusSchema
    ) = ShapeSchema.RoundedCornerRectangle(radius)

    fun roundedCornerShape(
        topStart: Int,
        topEnd: Int,
        bottomStart: Int,
        bottomEnd: Int
    ) = ShapeSchema.RoundedCornerRectangle(
        RadiusSchema(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )
    )

    fun roundedCornerShape(
        all: Int
    ) = ShapeSchema.RoundedCornerRectangle(
        RadiusSchema(
            topStart = all,
            topEnd = all,
            bottomStart = all,
            bottomEnd = all
        )
    )

    fun windowInsetsSystemBars() = WindowInsetsSchema.SystemBars

    fun windowInsetsCaptionBar() = WindowInsetsSchema.CaptionBar

    fun windowInsetsStatusBar() = WindowInsetsSchema.StatusBar

    fun windowInsetsNavigationBar() = WindowInsetsSchema.NavigationBar

    fun windowInsetsIme() = WindowInsetsSchema.Ime

    fun windowInsetsDisplayCutout() = WindowInsetsSchema.DisplayCutout

    fun windowInsetsWaterfall() = WindowInsetsSchema.Waterfall

    fun buildStyle() = StyleSchema(
        size = size,
        margin = margin,
        padding = padding,
        background = background,
        border = border,
        clip = clip,
        windowInsets = windowInsetsSchema
    )
}