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
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.event.builders.popup.TogglePopup
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.popup.Popup
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall

object TogglePopupEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TogglePopup"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Toggles (opens/closes) the state of a PopupTile identified by popupId — " +
                    "ideal for rich tooltips, small floating forms, and custom menus. PopupTile renders two " +
                    "groups of tiles: tiles (the anchor, always visible) and popupTiles (the floating " +
                    "content, shown when expanded = true). TogglePopup flips that state. Unlike Menu, the " +
                    "Popup's content is 100% free-form — any tile tree."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click the button to open/close the popup") {
                Popup(
                    id = "toggle_popup_demo",
                    alignment = alignToBottomCenter(),
                    dismissOnClickOutside = true,
                    tiles = {
                        Button(
                            text = "Toggle popup",
                            icon = icon("info"),
                            events = {
                                TogglePopup(
                                    trigger = EventTriggers.onClick(),
                                    popupId = "toggle_popup_demo"
                                )
                            }
                        )
                    },
                    popupTiles = {
                        Column(
                            style = {
                                size(width = fixedHorizontally(220), height = wrapVertically())
                                padding(horizontal = 16, vertical = 12)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainerHigh()))
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(
                                text = "Popup open!",
                                typography = typographyBodySmall()
                            )
                            SimpleText(
                                text = "Click outside or the button again to close.",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Popup(
                    id = "info_popup",
                    alignment = alignToBottomCenter(),
                    dismissOnClickOutside = true,
                    tiles = {
                        IconButton(
                            icon = icon("info"),
                            events = {
                                TogglePopup(trigger = EventTriggers.onClick(), popupId = "info_popup")
                            }
                        )
                    },
                    popupTiles = {
                        Column(style = { padding(horizontal = 12, vertical = 8) }) {
                            SimpleText(text = "Free-form floating content here")
                        }
                    }
                )
                """
            )
            ShowroomNote(
                "dismissOnClickOutside = true (default) automatically closes the popup when tapping " +
                    "outside it — no need for another TogglePopup for that."
            )

            ShowroomRelated(
                names = listOf("ToggleMenu", "DisplayDialog", "DisplayModalBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
