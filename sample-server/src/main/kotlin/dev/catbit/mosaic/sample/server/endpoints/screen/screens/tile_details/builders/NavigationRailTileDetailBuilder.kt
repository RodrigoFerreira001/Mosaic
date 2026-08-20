package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationRail
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object NavigationRailTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NavigationRail"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 NavigationRail — a vertical list of destinations, with optional " +
                    "header and footer, for medium and large widths. Shares the same semantics as " +
                    "NavigationBar (selectedItemId is server-controlled), just laid out vertically. header is " +
                    "handy for docking a FAB at the top of the rail; footer is pushed to the bottom edge by a " +
                    "weighted spacer, good for settings/account icons. An item's label is optional — null " +
                    "renders icon-only."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Header FAB, 4 items (one with no label), footer settings icon") {
                NavigationRail(
                    id = "nav_rail_demo",
                    selectedItemId = "home",
                    style = { size(width = wrapHorizontally(), height = fixedVertically(360)) },
                    header = {
                        FloatingActionButton(
                            icon = icon("add"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "New item") }
                        )
                    },
                    footer = {
                        IconButton(
                            icon = icon("settings"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Settings tapped") }
                        )
                    },
                    items = {
                        addItem(id = "home", icon = icon("home"), label = "Home")
                        addItem(id = "library", icon = icon("library_books"), label = "Library")
                        addItem(id = "favorites", icon = icon("favorite"), label = "Favorites")
                        addItem(id = "more", icon = icon("more_horiz"), label = null)
                    },
                    events = {
                        listOf("home", "library", "favorites", "more").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onNavigationRailItemClick(itemId = id),
                                updates = {
                                    update(tileId = "nav_rail_demo", updateData = inlineTileUpdateData("selectedItemId" to id))
                                    update(tileId = "nav_rail_demo_label", updateData = inlineTileUpdateData("text" to "Active: $id"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "nav_rail_demo_label", text = "Active: home")
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                NavigationRail(
                    id = "sideNav",
                    selectedItemId = currentDestination,
                    header = { FloatingActionButton(icon = icon("add"), events = { Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "create") }) },
                    footer = { IconButton(icon = icon("settings"), events = { Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "settings") }) },
                    items = {
                        addItem(id = "home", icon = icon("home"), label = "Home")
                        addItem(id = "library", icon = icon("library_books"), label = "Library")
                        addItem(id = "more", icon = icon("more_horiz"), label = null)
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("NavigationBar", "AdaptiveVisibility", "FloatingActionButton"),
                destination = "tileDetails"
            )
        }
    }
}
