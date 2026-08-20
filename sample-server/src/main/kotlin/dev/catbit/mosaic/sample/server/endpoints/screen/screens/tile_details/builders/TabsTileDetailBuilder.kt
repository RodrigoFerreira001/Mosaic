package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
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
import dev.catbit.mosaic.server.builder.tile.builders.navigation.Tabs
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TabsTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Tabs"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 tab row in 4 variants (PRIMARY/SECONDARY × fixed/scrollable) — " +
                    "secondary navigation within a screen. selectedTabId is 100% server-controlled. " +
                    "OnTabItemClick carries the clicked tab's id — it's up to the server to respond with " +
                    "UpdateTiles swapping selectedTabId (and, typically, the content below the tabs)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Switch tabs — selectedTabId and the text below really change") {
                Tabs(
                    id = "tabs_demo",
                    selectedTabId = "all",
                    tabType = TabsTileSchema.Type.PRIMARY,
                    tabItems = {
                        addTab(id = "all", label = "All")
                        addTab(id = "active", label = "Active")
                        addTab(id = "archived", label = "Archived")
                    },
                    events = {
                        listOf("all" to "All", "active" to "Active", "archived" to "Archived").forEach { (id, label) ->
                            UpdateTiles(
                                trigger = EventTriggers.onTabItemClick(itemId = id),
                                updates = {
                                    update(tileId = "tabs_demo", updateData = inlineTileUpdateData("selectedTabId" to id))
                                    update(tileId = "tabs_demo_content", updateData = inlineTileUpdateData("text" to "Showing: $label"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "tabs_demo_content", text = "Showing: All")
            }

            ShowroomSectionTitle("tabType = SECONDARY, icons, and badges")
            ShowroomDemoCard(title = "Secondary emphasis, one icon-only tab, one dot badge, one text badge") {
                Tabs(
                    id = "tabs_secondary_demo",
                    selectedTabId = "inbox",
                    tabType = TabsTileSchema.Type.SECONDARY,
                    tabItems = {
                        addTab(id = "inbox", label = "Inbox", icon = icon("inbox"), badgeText = "12")
                        addTab(id = "starred", label = "Starred", icon = icon("star"), badgeText = "")
                        addTab(id = "sent", icon = icon("send"))
                    }
                )
            }

            ShowroomSectionTitle("scrollable = true — more tabs than fit the width")
            ShowroomDemoCard(title = "6 tabs in a PRIMARY, scrollable row") {
                Tabs(
                    id = "tabs_scrollable_demo",
                    selectedTabId = "tab_0",
                    tabType = TabsTileSchema.Type.PRIMARY,
                    scrollable = true,
                    tabItems = {
                        repeat(6) { i -> addTab(id = "tab_$i", label = "Category ${i + 1}") }
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Tabs(
                    id = "contentTabs",
                    selectedTabId = "all",
                    tabType = TabsTileSchema.Type.PRIMARY,
                    tabItems = {
                        addTab(id = "all", label = "All")
                        addTab(id = "active", label = "Active")
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTabItemClick(itemId = "all"),
                            updates = { update(tileId = "contentTabs", updateData = inlineTileUpdateData("selectedTabId" to "all")) }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("NavigationBar", "Pager", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
