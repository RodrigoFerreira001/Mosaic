package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.navigation.AdaptiveNavigation
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(id = "home") {
            Column(
                style = {
                    windowInsets(windowInsetsSystemBars())
                    size(
                        width = fillHorizontally(),
                        height = fillVertically()
                    )
                }
            ) {
                AdaptiveNavigation(
                    navigatorId = "home",
                    startEntryId = "a",
                    style = {
                        size(
                            width = fillHorizontally(),
                            height = fillVertically()
                        )
                    }
                ) {
                    entry(
                        id = "a",
                        icon = icon("hdr_auto"),
                        label = "Auto",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                    entry(
                        id = "b",
                        icon = icon("currency_bitcoin"),
                        label = "Bitcoin",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                    entry(
                        id = "c",
                        icon = icon("copyright"),
                        label = "Copyright",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                    entry(
                        id = "d",
                        icon = icon("dialpad"),
                        label = "Dialpad",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                    entry(
                        id = "e",
                        icon = icon("explicit"),
                        label = "Explicit",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                    entry(
                        id = "f",
                        icon = icon("fire_extinguisher"),
                        label = "Fire Extinguisher",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Carregando")
                            }
                        },
                        failureTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Error")
                            }
                        }
                    )
                }
            }
        }
    )
}
