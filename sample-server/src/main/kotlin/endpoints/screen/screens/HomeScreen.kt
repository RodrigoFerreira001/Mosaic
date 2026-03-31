package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBar
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraph
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(
            id = "home",
        ) {
            Column(
                arrangement = arrangeSpaceBetween(),
                alignment = alignHorizontallyToCenter(),
                isScrollable = true,
                style = {
                    windowInsets(windowInsetsSystemBars())
                    size(
                        width = fillHorizontally(),
                        height = fillVertically()
                    )
                }
            ) {
                NestedNavigationGraph(
                    navigatorId = "home",
                    startEntryId = "a",
                    style = {
                        size(
                            width = fillHorizontally(),
                            height = weightVertically(1f)
                        )
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onNavigationEntrySet("a")
                        ) {
                            update(
                                tileId = "NAV_BAR",
                                data = mapOf("selectedItemId" to "a")
                            )
                        }
                        UpdateTiles(
                            trigger = EventTriggers.onNavigationEntrySet("b")
                        ) {
                            update(
                                tileId = "NAV_BAR",
                                data = mapOf("selectedItemId" to "b")
                            )
                        }
                        UpdateTiles(
                            trigger = EventTriggers.onNavigationEntrySet("c")
                        ) {
                            update(
                                tileId = "NAV_BAR",
                                data = mapOf("selectedItemId" to "c")
                            )
                        }
                    }
                ) {
                    entry(
                        screenId = "a",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Carregando",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Error",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    entry(
                        screenId = "b",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Carregando",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Error",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    entry(
                        screenId = "c",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Carregando",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Error",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }

                NavigationBar(
                    id = "NAV_BAR",
                    selectedItemId = "a",
                    events = {
                        Navigate(
                            trigger = EventTriggers.onNavigationBarItemClick("a"),
                            navigatorId = "home",
                            destination = "a",
                        )
                        Navigate(
                            trigger = EventTriggers.onNavigationBarItemClick("b"),
                            navigatorId = "home",
                            destination = "b",
                        )
                        Navigate(
                            trigger = EventTriggers.onNavigationBarItemClick("c"),
                            navigatorId = "home",
                            destination = "c",
                        )
                    }
                ) {
                    item(
                        id = "a",
                        icon = icon("hdr_auto")
                    )
                    item(
                        id = "b",
                        icon = icon("currency_bitcoin")
                    )
                    item(
                        id = "c",
                        icon = icon("copyright")
                    )
                }
            }
        }
    )
}