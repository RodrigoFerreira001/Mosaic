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
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object AddTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "AddTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Inserts one or more new tiles into a container's list of children, at a specific " +
                    "position — without reloading the whole screen. Use it to dynamically append items to an " +
                    "already-rendered container: a new card in a list after the user confirms an action, an item " +
                    "appended to a cart, and so on. The insertion point is controlled by the position parameter."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click to add a new item to the list") {
                Column(
                    id = "add_tiles_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    AddTilesListItem(label = "Item 1")
                    AddTilesListItem(label = "Item 2")
                }
                Button(
                    text = "Add item",
                    icon = icon("add"),
                    buttonType = filledTonalButton(),
                    events = {
                        AddTiles(
                            trigger = EventTriggers.onClick(),
                            groupingTileId = "add_tiles_list",
                            position = insertAtEnd(),
                            tiles = {
                                AddTilesListItem(label = "New item")
                            }
                        )
                    }
                )
                ShowroomNote(
                    "In this static demo the server describes a fixed tree, so the inserted item always has the " +
                        "same id — clicking repeatedly duplicates the id. In production, generate a unique id per " +
                        "item (e.g. randomId() inside a loop over data coming from the database) on each real " +
                        "execution of the event."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                AddTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "list_container",
                    position = insertAtEnd(),
                    tiles = {
                        SimpleText(id = randomId(), text = "New item")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("RemoveTiles", "ReplaceTiles", "WipeTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.AddTilesListItem(label: String) {
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
