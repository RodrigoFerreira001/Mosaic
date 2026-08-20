package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object NavigationBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NavigationBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 NavigationBar with a fixed set of destinations — bottom navigation " +
                    "with 2 to 5 items (it's what powers this sample's main navigation). selectedItemId is " +
                    "fully server-controlled — icon fill (filled vs. outlined) is decided by comparing each " +
                    "item's id to selectedItemId."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Switch items — selectedItemId actually changes") {
                NavigationBar(
                    id = "nav_bar_demo",
                    selectedItemId = "home",
                    items = {
                        item(id = "home", icon = icon("home"), label = "Home")
                        item(id = "search", icon = icon("search"), label = "Search")
                        item(id = "profile", icon = icon("person"), label = "Profile")
                    },
                    events = {
                        listOf("home", "search", "profile").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onNavigationBarItemClick(itemId = id),
                                updates = {
                                    update(tileId = "nav_bar_demo", updateData = inlineTileUpdateData("selectedItemId" to id))
                                    update(tileId = "nav_bar_demo_label", updateData = inlineTileUpdateData("text" to "Active: $id"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "nav_bar_demo_label", text = "Active: home")
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                NavigationBar(
                    id = "bottomNav",
                    selectedItemId = "home",
                    items = {
                        item(id = "home", icon = icon("home"), label = "Home")
                        item(id = "search", icon = icon("search"), label = "Search")
                        item(id = "profile", icon = icon("person"), label = "Profile")
                    },
                    events = {
                        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "home"), destination = "home")
                        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "search"), destination = "search")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("NavigationRail", "Tabs", "BottomAppBar"),
                destination = "tileDetails"
            )
        }
    }
}
