package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.BackgroundSchema
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

/**
 * Receiver of every tile builder's `style = { }` lambda. Accumulates size, spacing, background,
 * border, clip and window-insets settings and compiles them into a [StyleSchema] via
 * [buildStyle]. Every setter (`size`, `margin`, `padding`, `background`, `border`, `clip`,
 * `windowInsets`) simply overwrites its own field — calling one twice keeps only the last value.
 */
class StyleSchemaBuilderScope : GenericBuilderScope<StyleSchema, GenericBuilder<StyleSchema>>() {

    private var size: SizeSchema = SizeSchema(
        width = SizeSchema.Behavior.Horizontal.Wrap,
        height = SizeSchema.Behavior.Vertical.Wrap
    )
    private var margin: MarginSchema? = null
    private var padding: PaddingSchema? = null
    private var background: BackgroundSchema? = null
    private var border: BorderSchema? = null
    private var clip: ClipSchema? = null
    private var windowInsetsSchema: WindowInsetsSchema? = null

    /**
     * Sets the tile's width/height behavior.
     *
     * @param width Horizontal sizing behavior, built with [fillHorizontally], [wrapHorizontally], [fixedHorizontally], [weightHorizontally], [spanHorizontally] or [flexHorizontally]. Defaults to fill.
     * @param height Vertical sizing behavior, built with [fillVertically], [wrapVertically], [fixedVertically], [weightVertically], [spanVertically] or [fillRowHeight]. Defaults to wrap.
     */
    fun size(
        width: SizeSchema.Behavior.Horizontal = fillHorizontally(),
        height: SizeSchema.Behavior.Vertical = wrapVertically()
    ) {
        size = SizeSchema(
            width = width,
            height = height
        )
    }

    /**
     * Sets external spacing (outside the tile's border) independently per edge, in dp.
     *
     * @param top Space above the tile, in dp. Defaults to 0.
     * @param end Space after the tile (mirrored under RTL), in dp. Defaults to 0.
     * @param bottom Space below the tile, in dp. Defaults to 0.
     * @param start Space before the tile (mirrored under RTL), in dp. Defaults to 0.
     */
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

    /**
     * Sets external spacing (outside the tile's border), symmetric on each axis, in dp.
     *
     * @param horizontal Space before and after the tile, in dp. Defaults to 0.
     * @param vertical Space above and below the tile, in dp. Defaults to 0.
     */
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

    /**
     * Sets external spacing (outside the tile's border), symmetric horizontally but independent
     * top/bottom, in dp.
     *
     * @param horizontal Space before and after the tile, in dp. Defaults to 0.
     * @param top Space above the tile, in dp. Defaults to 0.
     * @param bottom Space below the tile, in dp. Defaults to 0.
     */
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

    /**
     * Sets internal spacing (inside the tile's border, around its content) independently per
     * edge, in dp.
     *
     * @param top Space above the content, in dp. Defaults to 0.
     * @param end Space after the content (mirrored under RTL), in dp. Defaults to 0.
     * @param bottom Space below the content, in dp. Defaults to 0.
     * @param start Space before the content (mirrored under RTL), in dp. Defaults to 0.
     */
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

    /**
     * Sets internal spacing (inside the tile's border, around its content), symmetric on each
     * axis, in dp.
     *
     * @param horizontal Space before and after the content, in dp. Defaults to 0.
     * @param vertical Space above and below the content, in dp. Defaults to 0.
     */
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

    /**
     * Sets internal spacing (inside the tile's border, around its content), symmetric
     * horizontally but independent top/bottom, in dp.
     *
     * @param horizontal Space before and after the content, in dp. Defaults to 0.
     * @param top Space above the content, in dp. Defaults to 0.
     * @param bottom Space below the content, in dp. Defaults to 0.
     */
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

    /**
     * Sets a solid-color background — shorthand for `background(solidColor(color, alpha))`.
     *
     * @param color Fill color of the background.
     * @param alpha Opacity applied on top of [color]'s own alpha, from 0f to 1f. Defaults to 1f.
     */
    fun background(
        color: ColorSchema,
        alpha: Float = 1f
    ) {
        background = BackgroundSchema.SolidColor(
            color = color,
            alpha = alpha
        )
    }

    /**
     * Sets the tile's background, built with [solidColor] or one of the gradient helpers
     * ([linearGradient], [horizontalGradient], [verticalGradient], [radialGradient],
     * [sweepGradient]).
     */
    fun background(
        background: BackgroundSchema
    ) {
        this.background = background
    }

    /**
     * Draws a border around the tile.
     *
     * @param color Color of the border stroke.
     * @param thickness Thickness of the border stroke, in dp.
     * @param radius Corner rounding applied to the border, built with [radius]. Defaults to none (square corners).
     */
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

    /**
     * Clips the tile's content and background to [shape], built with [circleShape],
     * [rectangleShape] or [roundedCornerShape].
     */
    fun clip(
        shape: ShapeSchema
    ) {
        clip = ClipSchema(
            shape = shape
        )
    }

    /**
     * Applies system window insets as extra padding on the tile, built with
     * [windowInsetsSystemBars], [windowInsetsCaptionBar], [windowInsetsStatusBar],
     * [windowInsetsNavigationBar], [windowInsetsIme], [windowInsetsDisplayCutout] or
     * [windowInsetsWaterfall].
     */
    fun windowInsets(
        windowInsets: WindowInsetsSchema
    ) {
        windowInsetsSchema = windowInsets
    }

    /** Fills the available width, optionally capped at [max] dp. */
    fun fillHorizontally(max: Int? = null) = SizeSchema.Behavior.Horizontal.Fill(max)

    /** Shrinks the width to wrap its content. */
    fun wrapHorizontally() = SizeSchema.Behavior.Horizontal.Wrap

    /** Fixes the width to exactly [value] dp. */
    fun fixedHorizontally(value: Int) = SizeSchema.Behavior.Horizontal.Fixed(value)

    /** Shares the parent's remaining width proportionally to [value], inside a `Row`/`FlowRow`. */
    fun weightHorizontally(value: Float) = SizeSchema.Behavior.Horizontal.Weight(value)

    /** Spans [value] tracks horizontally, inside a `Grid`. */
    fun spanHorizontally(value: Int) = SizeSchema.Behavior.Horizontal.Span(value)

    /** Fills the available height, optionally capped at [max] dp. */
    fun fillVertically(max: Int? = null) = SizeSchema.Behavior.Vertical.Fill(max)

    /** Shrinks the height to wrap its content. */
    fun wrapVertically() = SizeSchema.Behavior.Vertical.Wrap

    /** Fixes the height to exactly [value] dp. */
    fun fixedVertically(value: Int) = SizeSchema.Behavior.Vertical.Fixed(value)

    /** Shares the parent's remaining height proportionally to [value], inside a `Column`. */
    fun weightVertically(value: Float) = SizeSchema.Behavior.Vertical.Weight(value)

    /** Spans [value] tracks vertically, inside a `Grid`. */
    fun spanVertically(value: Int) = SizeSchema.Behavior.Vertical.Span(value)

    /** Fills [fraction] (0f–1f) of the tallest sibling's height, inside a `Row`/`FlowRow`. */
    fun fillRowHeight(fraction: Float = 1f) = SizeSchema.Behavior.Vertical.FillRow(fraction)

    /**
     * CSS-flexbox-style horizontal sizing, for children of a `FlexBox`.
     *
     * @param grow How much this child grows relative to siblings to fill extra space. Defaults to none.
     * @param shrink How much this child shrinks relative to siblings when space is tight. Defaults to none.
     * @param basis Initial main-axis size before growing/shrinking, built with [flexBasisAuto], [flexBasisFixed] or [flexBasisFraction]. Defaults to none.
     * @param alignSelf Cross-axis alignment override for this child, built with [flexAlignSelfAuto], [flexAlignSelfStart], [flexAlignSelfCenter], [flexAlignSelfEnd], [flexAlignSelfStretch] or [flexAlignSelfBaseline]. Defaults to none (inherits the container's `alignItems`).
     * @param order Paint/layout order override relative to siblings. Defaults to none (declaration order).
     */
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

    /** Flex basis derived automatically from the child's own content size. */
    fun flexBasisAuto() = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Auto

    /** Flex basis fixed to [value] dp before growing/shrinking is applied. */
    fun flexBasisFixed(value: Int) = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fixed(value)

    /** Flex basis as a fraction (0f–1f) of the container's main-axis size. */
    fun flexBasisFraction(value: Float) = SizeSchema.Behavior.Horizontal.Flex.FlexBasis.Fraction(value)

    /** Cross-axis self-alignment inherited from the container's `alignItems`. */
    fun flexAlignSelfAuto() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Auto

    /** Aligns this child to the start of the cross axis, overriding the container's `alignItems`. */
    fun flexAlignSelfStart() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Start

    /** Aligns this child to the center of the cross axis, overriding the container's `alignItems`. */
    fun flexAlignSelfCenter() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Center

    /** Aligns this child to the end of the cross axis, overriding the container's `alignItems`. */
    fun flexAlignSelfEnd() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.End

    /** Stretches this child across the cross axis, overriding the container's `alignItems`. */
    fun flexAlignSelfStretch() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Stretch

    /** Aligns this child by its text baseline, overriding the container's `alignItems`. */
    fun flexAlignSelfBaseline() = SizeSchema.Behavior.Horizontal.Flex.FlexAlignSelf.Baseline

    /**
     * Corner radius, in dp, individually per corner — the value fed into [roundedCornerShape]
     * and [border].
     *
     * @param topStart Radius of the top-start corner, in dp. Defaults to 0.
     * @param topEnd Radius of the top-end corner, in dp. Defaults to 0.
     * @param bottomStart Radius of the bottom-start corner, in dp. Defaults to 0.
     * @param bottomEnd Radius of the bottom-end corner, in dp. Defaults to 0.
     */
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

    /** Fully circular/oval shape — the tile is clipped to an ellipse inscribed in its bounds. */
    fun circleShape() = ShapeSchema.Circle

    /** Plain rectangular shape — no corner rounding, no clipping applied. */
    fun rectangleShape() = ShapeSchema.Rectangle

    /** Rounded-rectangle shape with a fully custom [radius] per corner. */
    fun roundedCornerShape(
        radius: RadiusSchema
    ) = ShapeSchema.RoundedCornerRectangle(radius)

    /**
     * Rounded-rectangle shape with an independent radius per corner, in dp.
     *
     * @param topStart Radius of the top-start corner, in dp.
     * @param topEnd Radius of the top-end corner, in dp.
     * @param bottomStart Radius of the bottom-start corner, in dp.
     * @param bottomEnd Radius of the bottom-end corner, in dp.
     */
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

    /** Rounded-rectangle shape with the same radius, in dp, applied to all four corners. */
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

    /** Insets for the status bar, navigation bar and cutouts combined. */
    fun windowInsetsSystemBars() = WindowInsetsSchema.SystemBars

    /** Insets for the app's caption bar (desktop/large-screen window chrome). */
    fun windowInsetsCaptionBar() = WindowInsetsSchema.CaptionBar

    /** Insets for the status bar only. */
    fun windowInsetsStatusBar() = WindowInsetsSchema.StatusBar

    /** Insets for the navigation bar only. */
    fun windowInsetsNavigationBar() = WindowInsetsSchema.NavigationBar

    /** Insets for the on-screen keyboard (IME), growing as it appears. */
    fun windowInsetsIme() = WindowInsetsSchema.Ime

    /** Insets for a display cutout (e.g. camera notch). */
    fun windowInsetsDisplayCutout() = WindowInsetsSchema.DisplayCutout

    /** Insets for a waterfall (curved-edge) display. */
    fun windowInsetsWaterfall() = WindowInsetsSchema.Waterfall

    /** Compiles all the settings accumulated on this scope into a [StyleSchema]. */
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
