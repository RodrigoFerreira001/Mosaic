package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.badges.Badge
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object BadgeTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Badge"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 badge — a small dot or a colored pill with text. content = null " +
                    "renders a dot; with text, it renders a pill. Mosaic only has this one badge tile — there " +
                    "is no separate BadgeBox equivalent to Material3's BadgedBox. This Badge mirrors " +
                    "Material3's standalone Badge composable (the one that goes inside a BadgedBox), not " +
                    "BadgedBox itself: it doesn't auto-anchor to a sibling, so you position it yourself, " +
                    "typically inside a Box aligned over the icon it decorates (see the demo below)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Toggle between dot and pill with a real count") {
                Box(style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) }) {
                    IconButton(icon = icon("notifications"))
                    Box(style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) }, alignment = alignToBottomEnd()) {
                        Badge(id = "badge_demo", content = null)
                    }
                }
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Dot",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to null)) })
                        }
                    )
                    Button(
                        text = "3",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to "3")) })
                        }
                    )
                    Button(
                        text = "99+",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to "99+")) })
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Badge doesn't auto-anchor — position it yourself, e.g. a second same-size Box
                // aligned over the icon it decorates.
                Box(id = "notifBox", style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) }) {
                    IconButton(icon = icon("notifications"), events = { Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "notifications") })
                    Box(
                        style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) },
                        alignment = alignToBottomEnd()
                    ) {
                        Badge(id = "notifBadge", content = "3")
                    }
                }
                """
            )

            ShowroomNote(
                text = "Badge vs BadgeBox: Mosaic only exposes Badge, not a BadgedBox equivalent. It's the " +
                    "same relationship Material3 itself has between its standalone Badge composable and " +
                    "BadgedBox — Badge is the visual shape, positioning is the caller's job."
            )

            ShowroomRelated(
                names = listOf("Box", "Icon", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
