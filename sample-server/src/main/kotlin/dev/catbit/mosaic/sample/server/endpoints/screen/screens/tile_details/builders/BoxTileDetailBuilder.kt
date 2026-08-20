package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.color.themeColorTertiary
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomCenter
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToBottomStart
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToCenterEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenterStart
import dev.catbit.mosaic.server.builder.placement.alignToTopCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopEnd
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.style.verticalGradient
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.badges.Badge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object BoxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Box"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stacks child tiles on top of each other — a badge over an icon, buttons " +
                    "floating over an image, a centered indicator. alignment sets the default alignment of " +
                    "children within the Box (2D: horizontal + vertical, 9 positions). It doesn't support " +
                    "scrolling and doesn't expose a scope CompositionLocal, unlike Column/Row."
            )

            ShowroomSectionTitle("Real usage: a gradient hero card")
            ShowroomDemoCard(title = "Background gradient + title anchored to the bottom, in one Box") {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(160))
                        clip(roundedCornerShape(all = 16))
                        background(
                            verticalGradient(
                                colors = listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    },
                    alignment = alignToBottomStart()
                ) {
                    Column(
                        style = { padding(horizontal = 16, vertical = 16) },
                        arrangement = arrangeVerticallySpacedBy(4)
                    ) {
                        SimpleText(text = "Summer collection", typography = typographyTitleLarge(), color = color(themeColorOnPrimaryContainer()))
                        SimpleText(text = "New arrivals every week", typography = typographyBodySmall(), color = color(themeColorOnPrimaryContainer()))
                    }
                }
            }

            ShowroomSectionTitle("Real usage: a badge positioned over an icon")
            ShowroomDemoCard(title = "Two nested Boxes — one for the icon chip, one purely for badge placement") {
                Box(
                    style = { size(width = fixedHorizontally(64), height = fixedVertically(64)) },
                    alignment = alignToCenter()
                ) {
                    Box(
                        style = {
                            size(width = fixedHorizontally(56), height = fixedVertically(56))
                            clip(roundedCornerShape(all = 16))
                            background(color(themeColorPrimaryContainer()))
                        },
                        alignment = alignToCenter()
                    ) {
                        Icon(icon = icon("notifications", size = 28))
                    }
                    Box(
                        style = { size(width = fixedHorizontally(64), height = fixedVertically(64)) },
                        alignment = alignToBottomEnd()
                    ) {
                        Badge(content = "3")
                    }
                }
            }

            ShowroomSectionTitle("alignment — all 9 positions")
            ShowroomDemoCard(title = "Every AlignmentSchema.TwoDimensional value, same container") {
                Column(arrangement = arrangeVerticallySpacedBy(8)) {
                    listOf(
                        listOf("alignToTopStart()" to alignToTopStart(), "alignToTopCenter()" to alignToTopCenter(), "alignToTopEnd()" to alignToTopEnd()),
                        listOf("alignToCenterStart()" to alignToCenterStart(), "alignToCenter()" to alignToCenter(), "alignToCenterEnd()" to alignToCenterEnd()),
                        listOf("alignToBottomStart()" to alignToBottomStart(), "alignToBottomCenter()" to alignToBottomCenter(), "alignToBottomEnd()" to alignToBottomEnd())
                    ).forEach { rowOfAlignments ->
                        Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                            rowOfAlignments.forEach { (label, alignmentValue) ->
                                AlignmentDemoCell(label, alignmentValue)
                            }
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Box(id = "avatarBox", alignment = alignToCenter()) {
                    AsyncImage(id = "avatar", model = AsyncImageTileSchema.Model.Url(user.avatarUrl))
                    Badge(id = "onlineBadge", content = null)
                }
                """
            )

            ShowroomRelated(
                names = listOf("Column", "Row", "Badge"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.AlignmentDemoCell(label: String, alignment: AlignmentSchema.TwoDimensional) {
    Column(arrangement = arrangeVerticallySpacedBy(4)) {
        SimpleText(text = label, typography = typographyLabelSmall())
        Box(
            style = {
                size(width = fixedHorizontally(88), height = fixedVertically(64))
                clip(roundedCornerShape(all = 10))
                background(color(themeColorSurfaceContainerHigh()))
            },
            alignment = alignment
        ) {
            Box(
                style = {
                    size(width = fixedHorizontally(16), height = fixedVertically(16))
                    clip(circleShape())
                    background(color(themeColorSecondaryContainer()))
                    margin(horizontal = 6, vertical = 6)
                }
            ) {}
        }
    }
}
