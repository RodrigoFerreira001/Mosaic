package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow
import dev.catbit.mosaic.server.builder.tile.builders.image.AsyncImage
import dev.catbit.mosaic.server.builder.tile.builders.image.cropContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.fillBoundsContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.fillHeightContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.fillWidthContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.fitContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.insideContentScale
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object AsyncImageTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AsyncImage"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "An image loaded via Coil 3 from a URL, raw bytes, or base64 — for avatars, " +
                    "product photos, banners. model accepts three forms: Model.Url(url) for remote images, " +
                    "Model.ArrayOfBytes(bytes) and Model.Base64(string) for images already available on the " +
                    "server. It fires OnAsyncImageLoadStart/Success/Failure on every load-state change " +
                    "(including reloads if model changes), so you can react with a Shimmer while it loads. " +
                    "There's no built-in placeholder or error image — you render those yourself."
            )

            ShowroomSectionTitle("Interactive demo — real load-state triggers")
            ShowroomDemoCard(title = "Reload the page to see the status flip from \"Loading...\" to \"Loaded\" for real") {
                AsyncImage(
                    id = "async_image_demo",
                    model = AsyncImageTileSchema.Model.Url(
                        "https://kotlinlang.org/docs/images/mascot-in-action.png"
                    ),
                    contentDescription = "AsyncImage demo image",
                    contentScale = cropContentScale(),
                    style = {
                        size(width = fillHorizontally(max = 480), height = fixedVertically(200))
                        clip(roundedCornerShape(all = 16))
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onAsyncImageLoadStart(),
                            updates = { update(tileId = "async_image_demo_status", updateData = inlineTileUpdateData("text" to "Status: loading...")) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onAsyncImageLoadSuccess(),
                            updates = { update(tileId = "async_image_demo_status", updateData = inlineTileUpdateData("text" to "Status: loaded successfully")) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onAsyncImageLoadFailure(),
                            updates = { update(tileId = "async_image_demo_status", updateData = inlineTileUpdateData("text" to "Status: failed to load")) }
                        )
                    }
                )
                SimpleText(id = "async_image_demo_status", text = "Status: loading...", color = color(themeColorOnSurfaceVariant()))
            }

            ShowroomSectionTitle("OnAsyncImageLoadFailure — a real, honest failure")
            ShowroomDemoCard(title = "Points at a real, reachable SVG — Coil has no SVG decoder registered in this client, so it fails to decode") {
                AsyncImage(
                    id = "async_image_failure_demo",
                    model = AsyncImageTileSchema.Model.Url("https://icon.icepanel.io/Technology/svg/Kotlin.svg"),
                    contentDescription = "Broken image demo",
                    contentScale = cropContentScale(),
                    style = { size(width = fixedHorizontally(160), height = fixedVertically(100)); clip(roundedCornerShape(all = 12)) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onAsyncImageLoadFailure(),
                            updates = { update(tileId = "async_image_failure_status", updateData = inlineTileUpdateData("text" to "Failed — no built-in error image, this is empty on purpose")) }
                        )
                    }
                )
                SimpleText(id = "async_image_failure_status", text = "Waiting for the request to fail...", color = color(themeColorOnSurfaceVariant()))
            }

            ShowroomSectionTitle("contentScale — all 6 values")
            ShowroomDemoCard(title = "Same 220×110 box, same wide banner source image — the crop/fit differences are obvious off-square") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16), verticalArrangement = arrangeVerticallySpacedBy(16)) {
                    listOf(
                        "cropContentScale()" to cropContentScale(),
                        "fitContentScale()" to fitContentScale(),
                        "fillHeightContentScale()" to fillHeightContentScale(),
                        "fillWidthContentScale()" to fillWidthContentScale(),
                        "insideContentScale()" to insideContentScale(),
                        "fillBoundsContentScale()" to fillBoundsContentScale()
                    ).forEach { (label, scale) ->
                        Column(arrangement = arrangeVerticallySpacedBy(6)) {
                            SimpleText(text = label, typography = typographyLabelMedium())
                            AsyncImage(
                                model = AsyncImageTileSchema.Model.Url(
                                    "https://blog.jetbrains.com/wp-content/uploads/2023/04/DSGN-16174-Blog-post-banner-and-promo-materials-for-post-about-Kotlin-mascot_3.png"
                                ),
                                contentScale = scale,
                                style = {
                                    size(width = fixedHorizontally(220), height = fixedVertically(110))
                                    clip(roundedCornerShape(all = 12))
                                }
                            )
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                AsyncImage(
                    id = "avatar",
                    model = AsyncImageTileSchema.Model.Url(user.avatarUrl),
                    contentScale = cropContentScale(),
                    contentDescription = "${'$'}{user.name} avatar",
                    style = {
                        size(width = fixedHorizontally(48), height = fixedVertically(48))
                        clip(roundedCornerShape(all = 24))
                    }
                )
                """
            )

            ShowroomSectionTitle("Placeholder with Shimmer while loading")
            ShowroomCode(
                """
                Box {
                    Shimmer { CircularProgressIndicator() } // see OnAsyncImageLoadStart/Success event
                    AsyncImage(
                        model = AsyncImageTileSchema.Model.Url(url),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onAsyncImageLoadSuccess(),
                                updates = { update("shimmer", inlineTileUpdateData("visibility" to "GONE")) }
                            )
                        }
                    )
                }
                """,
                id = "async_image_shimmer_snippet"
            )

            ShowroomRelated(
                names = listOf("Image", "Icon", "Shimmer"),
                destination = "tileDetails"
            )
        }
    }
}
