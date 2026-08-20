package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow
import dev.catbit.mosaic.server.builder.tile.builders.image.Image
import dev.catbit.mosaic.server.builder.tile.builders.image.imageCropContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.imageFillBoundsContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.imageFillHeightContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.imageFillWidthContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.imageFitContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.imageInsideContentScale
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object ImageTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Image"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A static image from a drawable already bundled in the app, resolved by " +
                    "resourceName via DrawableResourcesHolder. Use it for images that already ship inside the " +
                    "app (splash, logo, illustrations) — unlike AsyncImage, it makes no network request. This " +
                    "sample-client registers \"mosaic_logo\" for real (mapOf(\"mosaic_logo\" to " +
                    "Res.drawable.ic_mosaic_logo) in App.kt), so every demo below renders the actual framework " +
                    "logo, exactly as it would in production."
            )

            ShowroomSectionTitle("contentScale — all 6 values")
            ShowroomDemoCard(title = "Same wide, short box (220×110) — the crop/fit differences are only visible off-square") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(16),
                    verticalArrangement = arrangeVerticallySpacedBy(16)
                ) {
                    listOf(
                        "CROP" to imageCropContentScale(),
                        "FIT" to imageFitContentScale(),
                        "FILL_HEIGHT" to imageFillHeightContentScale(),
                        "FILL_WIDTH" to imageFillWidthContentScale(),
                        "INSIDE" to imageInsideContentScale(),
                        "FILL_BOUNDS" to imageFillBoundsContentScale()
                    ).forEach { (label, scale) ->
                        Column(arrangement = arrangeVerticallySpacedBy(6)) {
                            SimpleText(text = label, typography = typographyLabelMedium())
                            Box(
                                style = {
                                    size(width = fixedHorizontally(220), height = fixedVertically(110))
                                    clip(roundedCornerShape(all = 12))
                                    background(color(themeColorSurfaceContainerHigh()))
                                }
                            ) {
                                Image(
                                    resourceName = "mosaic_logo",
                                    contentScale = scale,
                                    contentDescription = "Mosaic logo, $label",
                                    style = { size(width = fillHorizontally(), height = fillVertically()) }
                                )
                            }
                        }
                    }
                }
            }

            ShowroomSectionTitle("alpha — opacity from 0f to 1f")
            ShowroomDemoCard(title = "1.0, 0.6, 0.25") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    listOf(1.0f, 0.6f, 0.25f).forEach { alphaValue ->
                        Column(arrangement = arrangeVerticallySpacedBy(6)) {
                            SimpleText(text = "alpha = $alphaValue", typography = typographyLabelMedium())
                            Box(
                                style = {
                                    size(width = fixedHorizontally(120), height = fixedVertically(80))
                                    clip(roundedCornerShape(all = 12))
                                    background(color(themeColorSurfaceContainerHigh()))
                                }
                            ) {
                                Image(
                                    resourceName = "mosaic_logo",
                                    contentScale = imageFitContentScale(),
                                    alpha = alphaValue,
                                    style = { size(width = fillHorizontally(), height = fillVertically()) }
                                )
                            }
                        }
                    }
                }
            }

            ShowroomSectionTitle("alignment — position within bounds, using imageInsideContentScale so it doesn't fill the box")
            ShowroomDemoCard(title = "topStart, center, bottomEnd") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    listOf(
                        "alignToTopStart()" to alignToTopStart(),
                        "alignToCenter()" to alignToCenter(),
                        "alignToBottomEnd()" to alignToBottomEnd()
                    ).forEach { (label, alignmentValue) ->
                        Column(arrangement = arrangeVerticallySpacedBy(6)) {
                            SimpleText(text = label, typography = typographyLabelMedium())
                            Box(
                                style = {
                                    size(width = fixedHorizontally(140), height = fixedVertically(100))
                                    clip(roundedCornerShape(all = 12))
                                    background(color(themeColorSurfaceContainerHigh()))
                                }
                            ) {
                                Image(
                                    resourceName = "mosaic_logo",
                                    contentScale = imageInsideContentScale(),
                                    alignment = alignmentValue,
                                    style = {
                                        size(width = fillHorizontally(), height = fillVertically())
                                        padding(horizontal = 8, vertical = 8)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            ShowroomSectionTitle("Registering a drawable (client-side, once)")
            ShowroomCode(
                """
                // sample-client/.../App.kt
                MosaicApplication(
                    dependencyInjectionConfig = mosaicDependencyInjectionConfig(
                        drawableResources = mapOf(
                            "mosaic_logo" to Res.drawable.ic_mosaic_logo
                        )
                    ),
                    ...
                )
                """
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Image(
                    id = "logo",
                    resourceName = "mosaic_logo",
                    contentScale = imageFitContentScale(),
                    contentDescription = "App logo"
                )
                """
            )

            ShowroomNote(
                "resourceName is just a lookup key — if the client never registered it in " +
                    "DrawableResourcesHolder, the Image renders empty, silently (documented behavior, not a " +
                    "bug, and no failure trigger to catch it). For images that come from the network or from " +
                    "raw bytes instead of the app bundle, use AsyncImage."
            )

            ShowroomRelated(
                names = listOf("AsyncImage", "Icon"),
                destination = "tileDetails"
            )
        }
    }
}
