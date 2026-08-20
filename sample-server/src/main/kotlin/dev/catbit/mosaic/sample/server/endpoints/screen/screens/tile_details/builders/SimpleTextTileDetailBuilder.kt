package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.text.blackFontWeight
import dev.catbit.mosaic.server.builder.text.boldFontWeight
import dev.catbit.mosaic.server.builder.text.centerTextAlign
import dev.catbit.mosaic.server.builder.text.clipTextOverflow
import dev.catbit.mosaic.server.builder.text.cursiveFontFamily
import dev.catbit.mosaic.server.builder.text.defaultFontFamily
import dev.catbit.mosaic.server.builder.text.ellipsisTextOverflow
import dev.catbit.mosaic.server.builder.text.endTextAlign
import dev.catbit.mosaic.server.builder.text.extraBoldFontWeight
import dev.catbit.mosaic.server.builder.text.extraLightFontWeight
import dev.catbit.mosaic.server.builder.text.italicFontStyle
import dev.catbit.mosaic.server.builder.text.justifyTextAlign
import dev.catbit.mosaic.server.builder.text.leftTextAlign
import dev.catbit.mosaic.server.builder.text.lightFontWeight
import dev.catbit.mosaic.server.builder.text.lineThroughTextDecoration
import dev.catbit.mosaic.server.builder.text.mediumFontWeight
import dev.catbit.mosaic.server.builder.text.monospaceFontFamily
import dev.catbit.mosaic.server.builder.text.normalFontStyle
import dev.catbit.mosaic.server.builder.text.normalFontWeight
import dev.catbit.mosaic.server.builder.text.rightTextAlign
import dev.catbit.mosaic.server.builder.text.sansSerifFontFamily
import dev.catbit.mosaic.server.builder.text.semiBoldFontWeight
import dev.catbit.mosaic.server.builder.text.serifFontFamily
import dev.catbit.mosaic.server.builder.text.stepBasedAutoSize
import dev.catbit.mosaic.server.builder.text.startTextAlign
import dev.catbit.mosaic.server.builder.text.thinFontWeight
import dev.catbit.mosaic.server.builder.text.underlineTextDecoration
import dev.catbit.mosaic.server.builder.text.visibleTextOverflow
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyDisplayLarge
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyDisplaySmall
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineLarge
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyLabelLarge
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall

object SimpleTextTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SimpleText"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Renders a non-interactive text label, Mosaic's most basic tile — used for " +
                    "titles, labels, descriptions, and any server-displayed value. SimpleText has no state of " +
                    "its own: the server sends the text, color, and typography ready-made, and the client just " +
                    "draws them. typography sets the base TextStyle; every other styling field (color, " +
                    "fontWeight, fontStyle, fontFamily, letterSpacing, textDecoration, textAlign, lineHeight, " +
                    "autoSize) overrides a single property on top of that base, and is ignored when null. " +
                    "It's the only text tile in Mosaic — no rich text, no markdown, no inline links, and no " +
                    "triggers at all — wrap it in a Box or Card for tap handling."
            )

            ShowroomSectionTitle("Interactive demo — server-driven updates")
            ShowroomDemoCard(title = "Each click fires a real UpdateTiles(\"text\" to ...)") {
                SimpleText(
                    id = "simple_text_demo_target",
                    text = "This text is a real SimpleText",
                    typography = typographyHeadlineSmall(),
                    color = color(themeColorPrimary())
                )
                Row {
                    Button(
                        text = "Message 1",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "simple_text_demo_target",
                                        updateData = inlineTileUpdateData("text" to "This text came from a second UpdateTiles on the server")
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Message 2",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "simple_text_demo_target",
                                        updateData = inlineTileUpdateData("text" to "UpdateTiles only swaps what changes — the rest of the tile stays the same")
                                    )
                                }
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("typography — all 15 Material 3 type scale levels")
            ShowroomDemoCard(title = "Display → Headline → Title → Body → Label, each in Large/Medium/Small") {
                Column(arrangement = arrangeVerticallySpacedBy(4)) {
                    listOf(
                        "Display Large" to typographyDisplayLarge(),
                        "Display Medium" to typographyDisplayMedium(),
                        "Display Small" to typographyDisplaySmall(),
                        "Headline Large" to typographyHeadlineLarge(),
                        "Headline Medium" to typographyHeadlineMedium(),
                        "Headline Small" to typographyHeadlineSmall(),
                        "Title Large" to typographyTitleLarge(),
                        "Title Medium" to typographyTitleMedium(),
                        "Title Small" to typographyTitleSmall(),
                        "Body Large" to typographyBodyLarge(),
                        "Body Medium" to typographyBodyMedium(),
                        "Body Small" to typographyBodySmall(),
                        "Label Large" to typographyLabelLarge(),
                        "Label Medium" to typographyLabelMedium(),
                        "Label Small" to typographyLabelSmall()
                    ).forEach { (label, typography) ->
                        SimpleText(text = label, typography = typography)
                    }
                }
            }

            ShowroomSectionTitle("fontWeight — all 9 values")
            ShowroomDemoCard(title = "thin → black, same typography base") {
                Column(arrangement = arrangeVerticallySpacedBy(4)) {
                    listOf(
                        "Thin (100)" to thinFontWeight(),
                        "Extra Light (200)" to extraLightFontWeight(),
                        "Light (300)" to lightFontWeight(),
                        "Normal (400)" to normalFontWeight(),
                        "Medium (500)" to mediumFontWeight(),
                        "Semi Bold (600)" to semiBoldFontWeight(),
                        "Bold (700)" to boldFontWeight(),
                        "Extra Bold (800)" to extraBoldFontWeight(),
                        "Black (900)" to blackFontWeight()
                    ).forEach { (label, weight) ->
                        SimpleText(text = label, typography = typographyBodyLarge(), fontWeight = weight)
                    }
                }
            }

            ShowroomSectionTitle("fontStyle, fontFamily, textDecoration — every value")
            ShowroomDemoCard(title = "2 styles, 5 families, 3 decorations") {
                Column(arrangement = arrangeVerticallySpacedBy(4)) {
                    SimpleText(text = "normalFontStyle()", typography = typographyBodyLarge(), fontStyle = normalFontStyle())
                    SimpleText(text = "italicFontStyle()", typography = typographyBodyLarge(), fontStyle = italicFontStyle())
                    SimpleText(text = "defaultFontFamily()", typography = typographyBodyLarge(), fontFamily = defaultFontFamily())
                    SimpleText(text = "serifFontFamily()", typography = typographyBodyLarge(), fontFamily = serifFontFamily())
                    SimpleText(text = "sansSerifFontFamily()", typography = typographyBodyLarge(), fontFamily = sansSerifFontFamily())
                    SimpleText(text = "monospaceFontFamily()", typography = typographyBodyLarge(), fontFamily = monospaceFontFamily())
                    SimpleText(text = "cursiveFontFamily()", typography = typographyBodyLarge(), fontFamily = cursiveFontFamily())
                    SimpleText(text = "noneTextDecoration()", typography = typographyBodyLarge())
                    SimpleText(text = "underlineTextDecoration()", typography = typographyBodyLarge(), textDecoration = underlineTextDecoration())
                    SimpleText(text = "lineThroughTextDecoration()", typography = typographyBodyLarge(), textDecoration = lineThroughTextDecoration())
                }
            }

            ShowroomSectionTitle("textAlign — all 6 values")
            ShowroomDemoCard(title = "Each rendered in a fixed-width box so the alignment is visible") {
                Column(arrangement = arrangeVerticallySpacedBy(8)) {
                    listOf(
                        "leftTextAlign()" to leftTextAlign(),
                        "rightTextAlign()" to rightTextAlign(),
                        "centerTextAlign()" to centerTextAlign(),
                        "justifyTextAlign()" to justifyTextAlign(),
                        "startTextAlign()" to startTextAlign(),
                        "endTextAlign()" to endTextAlign()
                    ).forEach { (label, align) ->
                        Box(style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                            SimpleText(
                                text = "$label — the quick brown fox jumps",
                                typography = typographyBodyMedium(),
                                textAlign = align,
                                style = { size(width = fillHorizontally(), height = wrapVertically()) }
                            )
                        }
                    }
                }
            }

            ShowroomSectionTitle("overflow + maxLines — clipping long text")
            ShowroomDemoCard(title = "Same long sentence, maxLines = 1, 3 overflow strategies") {
                Column(arrangement = arrangeVerticallySpacedBy(10)) {
                    listOf(
                        "clipTextOverflow()" to clipTextOverflow(),
                        "ellipsisTextOverflow()" to ellipsisTextOverflow(),
                        "visibleTextOverflow()" to visibleTextOverflow()
                    ).forEach { (label, overflow) ->
                        Column(arrangement = arrangeVerticallySpacedBy(2)) {
                            SimpleText(text = label, typography = typographyLabelMedium())
                            Box(
                                style = {
                                    size(width = fixedHorizontally(220), height = wrapVertically())
                                    clip(roundedCornerShape(all = 4))
                                }
                            ) {
                                SimpleText(
                                    text = "This sentence is intentionally much longer than the box that contains it",
                                    typography = typographyBodyMedium(),
                                    maxLines = 1,
                                    overflow = overflow,
                                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                                )
                            }
                        }
                    }
                }
            }

            ShowroomSectionTitle("autoSize — stepBasedAutoSize")
            ShowroomDemoCard(title = "Font size steps down from 32sp to 12sp until the text fits its box") {
                Box(style = { size(width = fixedHorizontally(160), height = fixedVertically(48)) }) {
                    SimpleText(
                        text = "Shrinks to fit this narrow box",
                        autoSize = stepBasedAutoSize(minFontSize = 12f, maxFontSize = 32f, stepSize = 2f),
                        maxLines = 1,
                        style = { size(width = fillHorizontally(), height = fillVertically()) }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SimpleText(
                    id = "greeting",
                    text = "Hello, ${'$'}{user.name}",
                    color = color(themeColorPrimary()),
                    typography = typographyTitleLarge(),
                    fontWeight = semiBoldFontWeight(),
                    textAlign = centerTextAlign(),
                    maxLines = 2,
                    overflow = ellipsisTextOverflow()
                )
                """
            )

            ShowroomRelated(
                names = listOf("Button", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
