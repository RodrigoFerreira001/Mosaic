package dev.catbit.mosaic.sample.server.endpoints.screen.screens.style

import dev.catbit.mosaic.core.data.schemas.tile.style.TileModeSchema
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.UnderConstructionBadge
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.color.themeColorOutlineVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiary
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopEnd
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.style.horizontalGradient
import dev.catbit.mosaic.server.builder.style.linearGradient
import dev.catbit.mosaic.server.builder.style.offset
import dev.catbit.mosaic.server.builder.style.radialGradient
import dev.catbit.mosaic.server.builder.style.sweepGradient
import dev.catbit.mosaic.server.builder.style.verticalGradient
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.gone
import dev.catbit.mosaic.server.builder.tile.invisible
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall
import io.ktor.server.routing.RoutingCall

/**
 * Visual identity ported from m3.material.io: dark blob hero (mirrors About/Tiles/Events/Get
 * started/Mechanisms/Extend), then each topic as a big headline + prose paragraph + optional live
 * preview + code sample directly on the page background — not boxed into small uniform cards.
 *
 * Topics cover every field of `StyleSchema` (size, margin, padding, background, border, clip,
 * windowInsets) plus the adjacent concepts a `style { }` block interacts with: visibility, theme
 * tokens and transitions.
 */
private data class StyleTopic(
    val title: String,
    val description: String,
    val code: String,
    val demo: (TileSchemaBuilderScope.() -> Unit)? = null
)

/** Small labelled swatch used by the live previews. */
private fun TileSchemaBuilderScope.DemoLabel(text: String) {
    SimpleText(
        text = text,
        typography = typographyLabelSmall(),
        color = color(themeColorOnSurfaceVariant())
    )
}

private val styleTopics = listOf(
    StyleTopic(
        title = "Anatomy of style { }",
        description = "Every tile accepts a style { } block that describes a StyleSchema. The client " +
            "always applies the properties in this order, and the order matters: clip comes before " +
            "background (which is why a background ends up already clipped), and border is drawn " +
            "over the background, before the inner padding. size is the only required field in the " +
            "schema; inside the builder it defaults to wrap/wrap, while StyleSchema.default() uses " +
            "fill/fill. All dimensions are Int in dp.",
        code = """
            // Application order on the client (Modifier.styledWith):
            // 1. windowInsets  — pushes away from system bars / IME
            // 2. margin        — outer space
            // 3. size          — width and height
            // 4. clip          — shape clipping
            // 5. background    — fill (already clipped by clip)
            // 6. onClick       — injected by the tile, not part of the schema
            // 7. border        — drawn over the background
            // 8. padding       — inner space, inside the border

            style = {
                windowInsets(windowInsetsSystemBars())
                margin(horizontal = 16, top = 8, bottom = 8)
                size(width = fillHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 16))
                background(color(themeColorSurfaceContainer()))
                border(color = color(themeColorOutline()), thickness = 1)
                padding(horizontal = 16, vertical = 12)
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Size — width",
        description = "fillHorizontally() takes up all the available width and accepts an optional " +
            "max (a ceiling in dp, not a fixed width). wrapHorizontally() shrinks to fit the content. " +
            "fixedHorizontally(dp) locks in an exact value. weightHorizontally(f), spanHorizontally(n) " +
            "and flexHorizontally(...) depend on the parent container — see the topics ahead.",
        code = """
            size(width = fillHorizontally())            // full width
            size(width = fillHorizontally(max = 400))   // full width, at most 400dp
            size(width = wrapHorizontally())            // sized to content
            size(width = fixedHorizontally(200))        // exactly 200dp
            size(width = weightHorizontally(1f))        // only inside Row / FlowRow
            size(width = spanHorizontally(2))           // only inside Grid
            size(width = flexHorizontally(grow = 1f))   // only inside FlexBox
        """.trimIndent(),
        demo = {
            DemoLabel("fillHorizontally()")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorPrimaryContainer()))
                }
            ) {}
            DemoLabel("fillHorizontally(max = 160)")
            Box(
                style = {
                    size(width = fillHorizontally(max = 160), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorSecondaryContainer()))
                }
            ) {}
            DemoLabel("fixedHorizontally(96)")
            Box(
                style = {
                    size(width = fixedHorizontally(96), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorTertiaryContainer()))
                }
            ) {}
            DemoLabel("wrapHorizontally() — shrinks to fit the text")
            Box(
                style = {
                    size(width = wrapHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorErrorContainer()))
                    padding(horizontal = 12, vertical = 6)
                }
            ) {
                SimpleText(text = "content", typography = typographyLabelMedium())
            }
        }
    ),
    StyleTopic(
        title = "Size — height",
        description = "Mirrors width: fillVertically() (with an optional max), wrapVertically() and " +
            "fixedVertically(dp). weightVertically(f) only works inside a Column, spanVertically(n) " +
            "inside a Grid, and fillRowHeight(fraction) inside a FlowRow — the latter makes the tile " +
            "take up a fraction of the height of the row it landed in.",
        code = """
            size(height = fillVertically())            // full height
            size(height = fillVertically(max = 300))   // full height, at most 300dp
            size(height = wrapVertically())            // sized to content
            size(height = fixedVertically(56))         // exactly 56dp
            size(height = weightVertically(1f))        // only inside Column
            size(height = spanVertically(2))           // only inside Grid
            size(height = fillRowHeight())             // 100% of the row height (FlowRow)
            size(height = fillRowHeight(0.5f))         // 50% of the row height (FlowRow)
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(90)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "fill", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = fixedVertically(48))
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorSecondaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "fixed 48", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = wrapVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                        padding(horizontal = 8, vertical = 8)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "wrap", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Size — weight inside Row and Column",
        description = "weightHorizontally(f) distributes the remaining horizontal space among the " +
            "children of a Row (or FlowRow), proportionally to the value. weightVertically(f) does " +
            "the same on the vertical axis inside a Column. Outside these containers the value is " +
            "simply ignored — it's not an error, it just has no effect.",
        code = """
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) }
            ) {
                Box(style = { size(width = weightHorizontally(1f), height = fillVertically()) }) {}
                Box(style = { size(width = weightHorizontally(2f), height = fillVertically()) }) {}
                Box(style = { size(width = fixedHorizontally(64), height = fillVertically()) }) {}
            }
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(44)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "weight 1f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(2f), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorSecondaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "weight 2f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(64), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "64dp", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Size — span (Grid) and flex (FlexBox)",
        description = "spanHorizontally(n)/spanVertically(n) make an item occupy n cells of a Grid. " +
            "flexHorizontally(...) exposes the full Flexbox model inside a FlexBox: grow (how much " +
            "it grows into free space), shrink (how much it shrinks when space is tight), basis " +
            "(base size: flexBasisAuto(), flexBasisFixed(dp) or flexBasisFraction(0f..1f)), " +
            "alignSelf (alignment on the cross axis) and order (visual order, independent of " +
            "declaration order).",
        code = """
            // Grid — item spanning 2 columns
            Grid(columns = gridFixed(3)) {
                Box(style = { size(width = spanHorizontally(2), height = fixedVertically(80)) }) {}
                Box(style = { size(width = wrapHorizontally(), height = fixedVertically(80)) }) {}
            }

            // FlexBox — full control
            style = {
                size(
                    width = flexHorizontally(
                        grow = 1f,
                        shrink = 0f,
                        basis = flexBasisFraction(0.5f),  // or flexBasisAuto() / flexBasisFixed(120)
                        alignSelf = flexAlignSelfCenter(), // Auto, Start, Center, End, Stretch, Baseline
                        order = 2
                    ),
                    height = wrapVertically()
                )
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Padding — inner spacing",
        description = "padding() is the space between the tile's edge and its content: it sits " +
            "inside the background and inside the border. There are three overloads " +
            "(horizontal/vertical, horizontal/top/bottom, and all four sides) — there's no " +
            "single-argument overload: padding(16) doesn't compile.",
        code = """
            padding(horizontal = 16, vertical = 8)              // start/end = 16, top/bottom = 8
            padding(horizontal = 16, top = 8, bottom = 24)      // mixed
            padding(top = 8, end = 16, bottom = 8, start = 16)  // four explicit sides
            // padding(16)  ← DOES NOT EXIST, won't compile
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeHorizontallySpacedBy(8),
                alignment = alignVerticallyToCenter()
            ) {
                Box(
                    style = {
                        size(width = wrapHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {
                    SimpleText(
                        text = "no padding",
                        typography = typographyLabelMedium(),
                        color = color(themeColorOnPrimaryContainer())
                    )
                }
                Box(
                    style = {
                        size(width = wrapHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorTertiaryContainer()))
                        padding(horizontal = 20, vertical = 12)
                    }
                ) {
                    SimpleText(
                        text = "padding(20, 12)",
                        typography = typographyLabelMedium(),
                        color = color(themeColorOnTertiaryContainer())
                    )
                }
            }
        }
    ),
    StyleTopic(
        title = "Margin — outer spacing",
        description = "margin() is the space between the tile and its neighbors: it sits outside " +
            "the background, so the background color doesn't paint that area. It has exactly the " +
            "same three overloads as padding and likewise doesn't accept a single argument. Note " +
            "that margin is applied before size, so a fillHorizontally() already accounts for the " +
            "margins.",
        code = """
            margin(horizontal = 24, vertical = 0)
            margin(horizontal = 24, top = 0, bottom = 8)
            margin(top = 8, end = 0, bottom = 16, start = 0)
            // margin(16)  ← DOES NOT EXIST, won't compile
        """.trimIndent(),
        demo = {
            Box(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 8))
                    background(color(themeColorSurfaceContainerHigh()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(40))
                        margin(horizontal = 24, top = 12, bottom = 12)
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(
                        text = "margin(horizontal = 24, top = 12, bottom = 12)",
                        typography = typographyLabelSmall(),
                        color = color(themeColorOnPrimaryContainer())
                    )
                }
            }
        }
    ),
    StyleTopic(
        title = "Background — solid color",
        description = "background() takes a BackgroundSchema. The most common case is a solid " +
            "color, and for that there's the shortcut background(color(...)), which also accepts " +
            "an optional alpha (0f..1f) multiplying the opacity of the whole fill. Use " +
            "color(themeColorX()) to follow the Material 3 theme (automatic light/dark) and " +
            "color(hex = \"#RRGGBB\") or color(r, g, b, alpha) when the color needs to be fixed.",
        code = """
            background(color(themeColorSurfaceContainer()))            // shortcut for SolidColor
            background(color(themeColorPrimary()), alpha = 0.12f)      // with transparency
            background(solidColor(color(hex = "#FF5722"), alpha = 0.5f)) // explicit form
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(56)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 1f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()), alpha = 0.5f)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 0.5f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()), alpha = 0.15f)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 0.15f", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Background — gradients",
        description = "Besides solid color, background() accepts five gradients that mirror " +
            "Compose's Brush factories: verticalGradient, horizontalGradient, linearGradient (any " +
            "two points), radialGradient (circular) and sweepGradient (angular, starting at 3 " +
            "o'clock and rotating clockwise). Each has two overloads: a list of evenly distributed " +
            "colors, or stop-to-color pairs with explicit positions.",
        code = """
            // Evenly distributed colors
            background(verticalGradient(listOf(color(themeColorPrimary()), color(themeColorTertiary()))))

            // Explicit positions (0f..1f along the gradient axis)
            background(
                verticalGradient(
                    0f to color(themeColorPrimary()),
                    0.3f to color(themeColorSecondary()),
                    1f to color(themeColorTertiary())
                )
            )

            background(horizontalGradient(listOf(color(...), color(...))))
            background(linearGradient(listOf(color(...), color(...))))        // diagonal, corner to corner
            background(radialGradient(listOf(color(...), color(...))))        // centered circle
            background(sweepGradient(listOf(color(...), color(...), color(...))))
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            verticalGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            horizontalGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            linearGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
            }
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            radialGradient(
                                listOf(color(themeColorTertiary()), color(themeColorPrimary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(circleShape())
                        background(
                            sweepGradient(
                                listOf(
                                    color(themeColorPrimary()),
                                    color(themeColorTertiary()),
                                    color(themeColorPrimary())
                                )
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            verticalGradient(
                                0f to color(themeColorPrimary()),
                                0.3f to color(themeColorSecondaryContainer()),
                                1f to color(themeColorTertiary())
                            )
                        )
                    }
                ) {}
            }
        }
    ),
    StyleTopic(
        title = "Background — geometry and tileMode",
        description = "All the geometry of gradients is optional and in dp. When omitted, Compose's " +
            "default applies, resolved only at draw time: end/endX/endY become the opposite edge, " +
            "center becomes the tile's center and radius becomes the largest radius that fits. " +
            "That's what lets the server describe the gradient without knowing the tile's size. " +
            "When the gradient ends before the edge, tileMode decides what happens with the " +
            "remaining space: CLAMP repeats the last color, REPEATED restarts it, MIRROR mirrors it " +
            "and DECAL leaves it transparent.",
        code = """
            // Gradient that ends at 60dp, repeating in bands
            background(
                horizontalGradient(
                    listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                    startX = 0,
                    endX = 60,
                    tileMode = TileModeSchema.REPEATED   // or CLAMP / MIRROR / DECAL
                )
            )

            // Linear between two arbitrary points
            background(
                linearGradient(
                    listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                    start = offset(x = 0, y = 0),
                    end = offset(x = 120, y = 60)
                )
            )

            // Offset radial, with explicit radius
            background(
                radialGradient(
                    listOf(color(themeColorTertiary()), color(themeColorPrimary())),
                    center = offset(x = 40, y = 40),
                    radius = 80
                )
            )

            // offset() with an omitted axis = opposite edge (Offset.Infinite)
            background(linearGradient(listOf(color(...), color(...)), end = offset(x = 200)))

            // alpha works the same way for gradients
            background(verticalGradient(listOf(color(...), color(...)), alpha = 0.4f))
        """.trimIndent(),
        demo = {
            DemoLabel("horizontalGradient(endX = 60, tileMode = REPEATED)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        horizontalGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            endX = 60,
                            tileMode = TileModeSchema.REPEATED
                        )
                    )
                }
            ) {}
            DemoLabel("horizontalGradient(endX = 60, tileMode = MIRROR)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        horizontalGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            endX = 60,
                            tileMode = TileModeSchema.MIRROR
                        )
                    )
                }
            ) {}
            DemoLabel("radialGradient(center = offset(40, 24), radius = 80)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        radialGradient(
                            listOf(color(themeColorTertiary()), color(themeColorPrimary())),
                            center = offset(x = 40, y = 24),
                            radius = 80
                        )
                    )
                }
            ) {}
            DemoLabel("linearGradient(start = offset(0, 0), end = offset(120, 48))")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        linearGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            start = offset(x = 0, y = 0),
                            end = offset(x = 120, y = 48)
                        )
                    )
                }
            ) {}
        }
    ),
    StyleTopic(
        title = "Border",
        description = "Draws a border over the background, with color, thickness in dp and an " +
            "optional radius. Note: radius is a RadiusSchema, not an Int — use radius(topStart = " +
            "..., topEnd = ..., bottomStart = ..., bottomEnd = ...). Without radius, the border is " +
            "rectangular.",
        code = """
            border(color = color(themeColorOutline()), thickness = 1)

            border(
                color = color(themeColorPrimary()),
                thickness = 2,
                radius = radius(topStart = 12, topEnd = 12, bottomStart = 12, bottomEnd = 12)
            )

            // border(color = ..., thickness = 1, radius = 12)  ← does NOT compile: radius is a RadiusSchema
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(56)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(color = color(themeColorOutline()), thickness = 1)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "1dp, straight", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(
                            color = color(themeColorPrimary()),
                            thickness = 2,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "2dp, r16", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(
                            color = color(themeColorTertiary()),
                            thickness = 3,
                            radius = radius(topStart = 20, topEnd = 0, bottomStart = 0, bottomEnd = 20)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "mixed corners", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Clip and Shape",
        description = "clip() clips the tile — and, since it comes before background in the " +
            "application order, it also clips the fill. There are three shapes: circleShape(), " +
            "rectangleShape() and roundedCornerShape(), the latter with a uniform overload (all), " +
            "per-corner, or taking a ready-made RadiusSchema. Corners are direction-aware: " +
            "topStart/topEnd flip in RTL.",
        code = """
            clip(circleShape())
            clip(rectangleShape())
            clip(roundedCornerShape(all = 16))
            clip(roundedCornerShape(topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0))
            clip(roundedCornerShape(radius = radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)))
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(64)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(64), height = fillVertically())
                        clip(circleShape())
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(rectangleShape())
                        background(color(themeColorSecondaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorTertiaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(topStart = 24, topEnd = 0, bottomStart = 0, bottomEnd = 24))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
            }
        }
    ),
    StyleTopic(
        title = "Clip vs. border radius",
        description = "These are independent things and easy to mix up. clip() rounds the content " +
            "and the background; border(radius = ...) rounds only the border stroke. For a card " +
            "with both a rounded background and a rounded border, use both with the same radius — " +
            "clip alone leaves the border straight over a rounded background, and border alone " +
            "leaves the background bleeding at the corners.",
        code = """
            // Correct card: clip + border with the same radius
            style = {
                clip(roundedCornerShape(all = 16))
                background(color(themeColorSurfaceContainer()))
                border(
                    color = color(themeColorOutline()),
                    thickness = 1,
                    radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                )
            }
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(64)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        background(color(themeColorSurfaceContainerHigh()))
                        border(
                            color = color(themeColorOutline()),
                            thickness = 1,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "border only", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorSurfaceContainerHigh()))
                        border(color = color(themeColorOutline()), thickness = 1)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "clip only", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorSurfaceContainerHigh()))
                        border(
                            color = color(themeColorOutline()),
                            thickness = 1,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "both", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Window Insets",
        description = "Applies the system areas' spacing as padding on the tile, before any other " +
            "property. Use it at the screen root. Only one inset type can be declared per style " +
            "{ } — to cover both the status bar and the navigation bar at once, use " +
            "windowInsetsSystemBars(). On screens with a text field, a container with " +
            "windowInsetsIme() tracks the keyboard.",
        code = """
            windowInsets(windowInsetsSystemBars())      // status bar + navigation bar
            windowInsets(windowInsetsStatusBar())
            windowInsets(windowInsetsNavigationBar())
            windowInsets(windowInsetsIme())             // keyboard
            windowInsets(windowInsetsCaptionBar())      // title bar (desktop / freeform)
            windowInsets(windowInsetsDisplayCutout())   // notch / camera cutout
            windowInsets(windowInsetsWaterfall())       // curved edges
        """.trimIndent()
    ),
    StyleTopic(
        title = "Visibility",
        description = "Controls whether a tile is rendered, via the visibility parameter (present " +
            "on every tile builder, not inside style { }). visible() is the default. invisible() " +
            "hides the tile but keeps the space it occupied in the layout — equivalent to CSS's " +
            "visibility: hidden. gone() removes the tile from the layout entirely, as if it didn't " +
            "exist. Changing visibility at runtime is done via UpdateTiles.",
        code = """
            Box(
                id = "banner",
                visibility = gone(),           // or invisible() / visible()
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) }
            )

            // Toggling at runtime:
            UpdateTiles(
                trigger = EventTriggers.onClick(),
                updates = {
                    update(tileId = "banner", updateData = inlineTileUpdateData("visibility" to "GONE"))
                }
            )
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "visible", typography = typographyLabelSmall())
                }
                Box(
                    visibility = invisible(),
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
                Box(
                    visibility = gone(),
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "after", typography = typographyLabelSmall())
                }
            }
            DemoLabel(
                "The gap between the two blocks is the invisible() one — it still takes up 80dp. " +
                    "gone() leaves no trace."
            )
        }
    ),
    StyleTopic(
        title = "Animation Transitions",
        description = "Define how a screen enters/exits the composition — used in Graph's " +
            "defaultTransition/transition and entry (navigation), and in animated content. All of " +
            "them return a ContentTransitionSchema and accept an optional animationSpec (Tween() " +
            "by default, or Spring()) to adjust duration and curve.",
        code = """
            slideInFromRightTransition()   slideInFromLeftTransition()
            slideInFromBottomTransition()  slideInFromTopTransition()
            slideOutToRightTransition()    slideOutToLeftTransition()
            slideOutToBottomTransition()   slideOutToTopTransition()
            slideHorizontalTransition()    slideVerticalTransition()
            fadeTransition()               fadeAndSlideHorizontalTransition()
            slideOverTransition()          // new screen slides over the previous one, which stays still

            // Usage in a Graph:
            Graph(
                startEntryId = "home",
                defaultTransition = slideOverTransition(),
                defaultPopTransition = fadeTransition(animationSpec = AnimationSpecSchema.Spring()),
                entries = { /* ... */ }
            )
        """.trimIndent()
    ),
    StyleTopic(
        title = "Theme Colors and Typography",
        description = "themeColorX() and typographyX() follow the app's Material 3 design tokens " +
            "— they switch automatically between light/dark and react to SetTheme at runtime. " +
            "Always prefer these tokens over a fixed color(\"#hex\"), except when the color really " +
            "needs to stay the same in both modes.",
        code = """
            // Colors — all available M3 roles:
            themeColorPrimary()            themeColorOnPrimary()
            themeColorPrimaryContainer()   themeColorOnPrimaryContainer()
            themeColorSecondary()          themeColorOnSecondary()
            themeColorSecondaryContainer() themeColorOnSecondaryContainer()
            themeColorTertiary()           themeColorOnTertiary()
            themeColorTertiaryContainer()  themeColorOnTertiaryContainer()
            themeColorError()              themeColorOnError()
            themeColorErrorContainer()     themeColorOnErrorContainer()
            themeColorBackground()         themeColorOnBackground()
            themeColorSurface()            themeColorOnSurface()
            themeColorSurfaceVariant()     themeColorOnSurfaceVariant()
            themeColorOutline()            themeColorOutlineVariant()
            themeColorScrim()
            themeColorInverseSurface()     themeColorInverseOnSurface()   themeColorInversePrimary()
            themeColorSurfaceDim()         themeColorSurfaceBright()
            themeColorSurfaceContainerLowest()   themeColorSurfaceContainerLow()
            themeColorSurfaceContainer()         themeColorSurfaceContainerHigh()
            themeColorSurfaceContainerHighest()

            // Other ways to specify color:
            color(hex = "#FF5722")            // 6 or 8 chars (#AARRGGBB)
            color(r = 1f, g = 0f, b = 0f, alpha = 1f)

            // Typography — the 15 M3 roles:
            typographyDisplayLarge()   typographyDisplayMedium()   typographyDisplaySmall()
            typographyHeadlineLarge()  typographyHeadlineMedium()  typographyHeadlineSmall()
            typographyTitleLarge()     typographyTitleMedium()     typographyTitleSmall()
            typographyBodyLarge()      typographyBodyMedium()      typographyBodySmall()
            typographyLabelLarge()     typographyLabelMedium()     typographyLabelSmall()
        """.trimIndent()
    ),
    StyleTopic(
        title = "Dynamic Theme (SetTheme / ResetTheme)",
        description = "SetTheme swaps the whole app's M3 colors at runtime, defining a pair of " +
            "schemes (light and dark) via colorsScheme(lightColorScheme, darkColorScheme), each " +
            "built with colorScheme(...) — one hex color per role from the table above. The effect " +
            "is global: every screen using themeColorX() reflects the change immediately, on any " +
            "screen, not just the current one. ResetTheme undoes it and reverts to the app's " +
            "default theme. See the details of these two events on the Events screen.",
        code = """
            SetTheme(
                trigger = EventTriggers.onClick(),
                colorsScheme = colorsScheme(
                    lightColorScheme = colorScheme(
                        primary = "#6750A4", onPrimary = "#FFFFFF",
                        primaryContainer = "#EADDFF", onPrimaryContainer = "#21005D",
                        // ... one hex entry per ColorScheme role
                    ),
                    darkColorScheme = colorScheme(
                        primary = "#D0BCFF", onPrimary = "#381E72",
                        primaryContainer = "#4F378B", onPrimaryContainer = "#EADDFF",
                        // ...
                    )
                )
            )

            ResetTheme(trigger = EventTriggers.onClick())
        """.trimIndent()
    ),
)

private fun TileSchemaBuilderScope.StyleTopicSection(topic: StyleTopic) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(
            text = topic.title,
            typography = typographyHeadlineSmall()
        )
        SimpleText(
            text = topic.description,
            typography = typographyBodyLarge(),
            color = color(themeColorOnSurfaceVariant())
        )
        topic.demo?.let { demo ->
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 16))
                    background(color(themeColorSurfaceContainer()))
                    border(
                        color = color(themeColorOutlineVariant()),
                        thickness = 1,
                        radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                    )
                    padding(horizontal = 16, vertical = 16)
                },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                demo()
            }
        }
        CodeViewer(
            code = topic.code,
            language = CodeViewerTileSchema.Language.KOTLIN,
            theme = CodeViewerTileSchema.Theme.ATOM_ONE,
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
            }
        )
    }
}

object StyleScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "style"

    override suspend fun RoutingCall.build() = Screen(id = "style") {
        Column(
            id = "style_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 32)
            },
            arrangement = arrangeVerticallySpacedBy(36),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as
            // About/Tiles/Events/Get started/Mechanisms/Extend.
            Column(
                id = "style_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    alignment = alignToTopEnd(),
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorTertiaryContainer()))
                    }
                ) {
                    UnderConstructionBadge()
                }
                Column(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(horizontal = 24, top = 20, bottom = 24)
                    },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    SimpleText(
                        text = "Style",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Every tile accepts a style { } block with properties for size, " +
                            "spacing, appearance and insets. The sections below cover every field " +
                            "of StyleSchema, with a live preview wherever seeing it makes a difference.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            styleTopics.forEach { topic ->
                StyleTopicSection(topic)
            }
        }
    }
}
