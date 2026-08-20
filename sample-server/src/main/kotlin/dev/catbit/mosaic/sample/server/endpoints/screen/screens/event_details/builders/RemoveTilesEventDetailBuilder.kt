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
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object RemoveTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RemoveTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Removes specific tiles from a container by their IDs, without reloading the " +
                    "whole screen. Use it when you already know exactly which ID(s) to remove — for example, " +
                    "deleting a specific list item. To empty a container completely, prefer WipeTiles instead."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Remove items individually from the list") {
                Column(
                    id = "remove_tiles_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    RemoveTilesListItem(id = "remove_tiles_item_1", label = "Item 1")
                    RemoveTilesListItem(id = "remove_tiles_item_2", label = "Item 2")
                    RemoveTilesListItem(id = "remove_tiles_item_3", label = "Item 3")
                }
                ShowroomNote("Each \"remove\" button calls RemoveTiles with groupingTileId = \"remove_tiles_list\" and tileIds = listOf(this item's own id).")
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RemoveTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "item_list",
                    tileIds = listOf("item_42")
                )
                """
            )

            ShowroomRelated(
                names = listOf("AddTiles", "WipeTiles", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.RemoveTilesListItem(id: String, label: String) {
    Row(
        id = id,
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeSpaceBetween(),
        alignment = alignVerticallyToCenter()
    ) {
        SimpleText(text = label, typography = typographyBodyMedium())
        IconButton(
            icon = icon("delete", size = 20, color = color(themeColorOnSurfaceVariant())),
            events = {
                RemoveTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "remove_tiles_list",
                    tileIds = listOf(id)
                )
            }
        )
    }
}
