package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.WipeTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object WipeTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "WipeTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Removes every child of a container in a single operation, leaving it empty — " +
                    "more direct than RemoveTiles when the goal is to clear everything. Use it when the goal " +
                    "is to empty a container entirely, without having to enumerate the children's ids (which " +
                    "RemoveTiles would require). Emptying an already-empty container is a no-op. After a " +
                    "WipeTiles, use AddTiles or ReplaceTiles to repopulate the container."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Empty the list and put the items back") {
                Column(
                    id = "wipe_tiles_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    WipeTilesListItem(label = "Item 1")
                    WipeTilesListItem(label = "Item 2")
                    WipeTilesListItem(label = "Item 3")
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Empty with WipeTiles",
                        buttonType = outlinedButton(),
                        events = {
                            WipeTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "wipe_tiles_list"
                            )
                        }
                    )
                    Button(
                        text = "Restore with AddTiles",
                        events = {
                            AddTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "wipe_tiles_list",
                                position = insertAtEnd(),
                                tiles = {
                                    WipeTilesListItem(label = "Restored item")
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "After emptying, the list stays empty until another event (AddTiles or ReplaceTiles) " +
                        "repopulates it — WipeTiles has no position or content to restore on its own."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                WipeTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "search_results"
                )
                """
            )

            ShowroomRelated(
                names = listOf("RemoveTiles", "ReplaceTiles", "AddTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.WipeTilesListItem(label: String) {
    Row(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon("check_circle", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
