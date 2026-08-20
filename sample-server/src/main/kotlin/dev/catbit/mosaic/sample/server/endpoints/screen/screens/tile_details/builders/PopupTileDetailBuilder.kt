package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.popup.TogglePopup
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLow
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomCenter
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenterEnd
import dev.catbit.mosaic.server.builder.placement.alignToTopCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.popup.Popup
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object PopupTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Popup"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A free-form overlay anchored to its content — unlike Menu, popupTiles can be any " +
                    "content, not just a fixed list of items. Use it for tooltips, custom dropdowns/pickers, or " +
                    "any overlay whose content isn't a simple list (for that, use Menu instead). Like Menu, " +
                    "expanded is server-controlled; TogglePopup(popupId) is the dedicated event for opening and " +
                    "closing it."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap the icon — the Popup really opens and closes via TogglePopup") {
                Popup(
                    id = "popup_demo",
                    expanded = false,
                    alignment = alignToBottomCenter(),
                    offsetY = 8,
                    tiles = {
                        IconButton(
                            icon = icon("info"),
                            events = {
                                TogglePopup(trigger = EventTriggers.onClick(), popupId = "popup_demo")
                            }
                        )
                    },
                    popupTiles = {
                        Column(
                            style = {
                                size(width = wrapHorizontally(), height = wrapVertically())
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainerLow()))
                                padding(horizontal = 16, vertical = 12)
                            }
                        ) {
                            SimpleText(text = "Free-form content inside the Popup")
                        }
                    }
                )
            }

            ShowroomSectionTitle("alignment — 3 more of the 9 positions")
            ShowroomDemoCard(title = "alignToTopCenter(), alignToCenterEnd(), alignToBottomEnd() — same anchor pattern") {
                Row(arrangement = arrangeHorizontallySpacedBy(24)) {
                    listOf(
                        "topCenter" to Pair("alignToTopCenter()", alignToTopCenter()),
                        "centerEnd" to Pair("alignToCenterEnd()", alignToCenterEnd()),
                        "bottomEnd" to Pair("alignToBottomEnd()", alignToBottomEnd())
                    ).forEach { (popupId, labelAndAlignment) ->
                        val (label, alignment) = labelAndAlignment
                        Popup(
                            id = "popup_align_demo_$popupId",
                            expanded = false,
                            alignment = alignment,
                            offsetY = 8,
                            tiles = {
                                IconButton(
                                    icon = icon("more_vert"),
                                    events = { TogglePopup(trigger = EventTriggers.onClick(), popupId = "popup_align_demo_$popupId") }
                                )
                            },
                            popupTiles = {
                                Column(
                                    style = {
                                        clip(roundedCornerShape(all = 12))
                                        background(color(themeColorSurfaceContainerLow()))
                                        padding(horizontal = 12, vertical = 8)
                                    }
                                ) { SimpleText(text = label) }
                            }
                        )
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Popup(
                    id = "infoPopup",
                    expanded = false,
                    alignment = alignToBottomCenter(),
                    offsetY = 8,
                    tiles = {
                        IconButton(icon = icon("info"), events = { TogglePopup(trigger = EventTriggers.onClick(), popupId = "infoPopup") })
                    },
                    popupTiles = {
                        Column(
                            style = {
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainerLow()))
                                padding(horizontal = 16, vertical = 12)
                            }
                        ) { SimpleText(text = "Additional information") }
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Menu", "Tooltip", "Card"),
                destination = "tileDetails"
            )
        }
    }
}
