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
import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents
import dev.catbit.mosaic.server.builder.event.builders.menu.ToggleMenu
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.menu.Menu

object ToggleMenuEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ToggleMenu"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Toggles (opens/closes) the state of a MenuTile identified by menuId — if it's " +
                    "open it closes, if it's closed it opens. ToggleMenu doesn't explicitly open or close: " +
                    "it flips the target MenuTile's current state. The most common pattern is using the " +
                    "same ToggleMenu both on the button that opens the menu and on each menu item (to close " +
                    "it after a choice)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click the button to open the menu, pick an item") {
                Menu(
                    id = "toggle_menu_demo",
                    items = {
                        addMenuItem(id = "toggle_menu_demo_share", label = "Share", leadingIcon = icon("share"))
                        addMenuItem(id = "toggle_menu_demo_favorite", label = "Favorite", leadingIcon = icon("star"))
                        addMenuItem(id = "toggle_menu_demo_delete", label = "Delete", leadingIcon = icon("delete"))
                    },
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_share"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Share selected")
                            }
                        )
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_favorite"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Favorite selected")
                            }
                        )
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_delete"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Delete selected")
                            }
                        )
                    }
                ) {
                    IconButton(
                        icon = icon("more_vert"),
                        events = {
                            ToggleMenu(
                                trigger = EventTriggers.onClick(),
                                menuId = "toggle_menu_demo"
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Menu(
                    id = "actions_menu",
                    items = {
                        addMenuItem(id = "actions_menu_edit", label = "Edit", leadingIcon = icon("edit"))
                        addMenuItem(id = "actions_menu_delete", label = "Delete", leadingIcon = icon("delete"))
                    },
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("actions_menu_edit"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "actions_menu")
                                // edit action here
                            }
                        )
                    }
                ) {
                    IconButton(
                        icon = icon("more_vert"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.onClick(), menuId = "actions_menu")
                        }
                    )
                }
                """
            )
            ShowroomNote(
                "Always close the menu (ToggleMenu again) when reacting to an addMenuItem — otherwise it " +
                    "stays open after the choice."
            )

            ShowroomRelated(
                names = listOf("TogglePopup", "TriggerEvent", "RunEvents"),
                destination = "eventDetails"
            )
        }
    }
}
