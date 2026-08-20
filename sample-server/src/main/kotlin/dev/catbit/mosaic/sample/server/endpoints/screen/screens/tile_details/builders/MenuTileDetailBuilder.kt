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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.menu.Menu

object MenuTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Menu"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A box that overlays a DropdownMenu on top of its anchor content — contextual " +
                    "action menus, dropdown pickers. expanded is server-controlled: when the user taps outside, " +
                    "the renderer fires a toggle event asking the server to close the menu, and it's your " +
                    "responsibility to respond with expanded = false."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap the icon, pick an item — fires a real event") {
                Menu(
                    id = "menu_demo",
                    expanded = false,
                    items = {
                        addMenuItem(id = "edit", label = "Edit", leadingIcon = icon("edit"))
                        addMenuItem(id = "share", label = "Share", leadingIcon = icon("share"))
                        addMenuItem(id = "shortcut", label = "Rename", trailingIcon = icon("keyboard"))
                        addMenuItem(id = "delete", label = "Delete", leadingIcon = icon("delete"))
                    },
                    tiles = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = { update(tileId = "menu_demo", updateData = inlineTileUpdateData("expanded" to true)) }
                                )
                            }
                        )
                    },
                    events = {
                        listOf("edit", "share", "shortcut", "delete").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onMenuItemClick(itemId = id),
                                updates = { update(tileId = "menu_demo", updateData = inlineTileUpdateData("expanded" to false)) }
                            )
                            DisplaySnackbar(
                                trigger = EventTriggers.onMenuItemClick(itemId = id),
                                message = "Item \"$id\" clicked"
                            )
                        }
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Menu(
                    id = "actionsMenu",
                    expanded = false,
                    items = {
                        addMenuItem(id = "edit", label = "Edit", leadingIcon = icon("edit"))
                        addMenuItem(id = "delete", label = "Delete", leadingIcon = icon("delete"))
                    },
                    tiles = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = {
                                UpdateTiles(trigger = EventTriggers.onClick(), updates = {
                                    update(tileId = "actionsMenu", updateData = inlineTileUpdateData("expanded" to true))
                                })
                            }
                        )
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onMenuItemClick(itemId = "edit"),
                            updates = { update(tileId = "actionsMenu", updateData = inlineTileUpdateData("expanded" to false)) }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Popup", "DropdownList", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
