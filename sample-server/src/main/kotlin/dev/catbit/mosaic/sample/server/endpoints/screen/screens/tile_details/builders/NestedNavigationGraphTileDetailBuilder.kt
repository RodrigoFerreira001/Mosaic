package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.chips.AssistChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraph
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object NestedNavigationGraphTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NestedNavigationGraph"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Embeds a complete Navigation 3 NavDisplay inside a screen — its own navigation " +
                    "stack, nested within the parent screen. Use it for master-detail layouts or a section with " +
                    "its own back stack (e.g. a wizard inside a larger screen). The navigator is registered in " +
                    "NavigatorsHolder under navigatorId and automatically unregistered when the tile leaves " +
                    "composition. The system back gesture pops this graph's own stack, not the outer screen's."
            )

            ShowroomSectionTitle("Interactive demo — a real 2-entry nested stack")
            ShowroomDemoCard(title = "Tap an item — navigates inside this box only, the outer showroom doesn't move") {
                Column(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(260))
                        clip(roundedCornerShape(all = 12))
                    }
                ) {
                    NestedNavigationGraph(
                        style = { size(width = fillHorizontally(), height = fillVertically()) },
                        navigatorId = "nested_nav_demo",
                        startEntryId = "nested_demo_list",
                        entries = {
                            entry(
                                screenId = "nested_demo_list",
                                initialEvents = { /* static content — no GetScreen round trip */ },
                                initialTiles = {
                                    Column(
                                        style = {
                                            size(width = fillHorizontally(), height = fillVertically())
                                            padding(horizontal = 20, vertical = 20)
                                        },
                                        arrangement = arrangeVerticallySpacedBy(12)
                                    ) {
                                        SimpleText(text = "Trails", typography = typographyTitleMedium())
                                        listOf("Mountain trail", "Coastal walk", "Forest loop").forEach { trail ->
                                            Row(
                                                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                                                arrangement = arrangeSpaceBetween(),
                                                alignment = alignVerticallyToCenter(),
                                                events = {
                                                    Navigate(
                                                        trigger = EventTriggers.onClick(),
                                                        navigatorId = "nested_nav_demo",
                                                        destination = "nested_demo_detail"
                                                    )
                                                }
                                            ) {
                                                SimpleText(text = trail, typography = typographyBodyMedium())
                                                IconButton(icon = icon("chevron_right"))
                                            }
                                        }
                                    }
                                }
                            )
                            entry(
                                screenId = "nested_demo_detail",
                                initialEvents = { /* static content — no GetScreen round trip */ },
                                initialTiles = {
                                    Column(
                                        style = {
                                            size(width = fillHorizontally(), height = fillVertically())
                                            padding(horizontal = 20, vertical = 20)
                                        },
                                        arrangement = arrangeVerticallySpacedBy(12)
                                    ) {
                                        Row(
                                            arrangement = arrangeHorizontallySpacedBy(8),
                                            alignment = alignVerticallyToCenter()
                                        ) {
                                            IconButton(
                                                icon = icon("arrow_back"),
                                                events = {
                                                    NavigateUp(trigger = EventTriggers.onClick(), navigatorId = "nested_nav_demo")
                                                }
                                            )
                                            SimpleText(text = "Back", typography = typographyBodyMedium())
                                        }
                                        SimpleText(text = "Trail detail", typography = typographyTitleMedium())
                                        SimpleText(
                                            text = "This is entry #2 of the nested graph's own back stack — the " +
                                                "outer showroom screen never moved.",
                                            typography = typographyBodyMedium(),
                                            color = color(themeColorOnSurfaceVariant())
                                        )
                                        AssistChip(
                                            text = "NavigateUp targets navigatorId = \"nested_nav_demo\"",
                                            events = {}
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                NestedNavigationGraph(
                    id = "mainGraph",
                    navigatorId = "main_navigator",
                    startEntryId = "home",
                    entries = {
                        entry(
                            screenId = "home",
                            initialTiles = { HomeScreen() },
                            initialEvents = { GetScreen(trigger = EventTriggers.onDisplay()) }
                        )
                        entry(
                            screenId = "detail",
                            initialTiles = { DetailScreen() },
                            failureTiles = { SimpleText(text = "Failed to load detail") }
                        )
                    }
                )
                """
            )

            ShowroomNote(
                text = "The demo above overrides initialEvents to an empty block per entry, so its content " +
                    "is purely static (no GetScreen round trip, no server route needed for \"nested_demo_list\"/" +
                    "\"nested_demo_detail\"). In production you'd usually leave initialEvents at its default — " +
                    "a GetScreen against a real screenId — so each entry fetches its own screen the same way " +
                    "the outer app does. See HomeScreenBuilder.kt (AdaptiveNavigation) for that fuller, " +
                    "network-backed pattern in this same sample-server."
            )

            ShowroomRelated(
                names = listOf("NavigationBar", "NavigationRail", "Tabs"),
                destination = "tileDetails"
            )
        }
    }
}
