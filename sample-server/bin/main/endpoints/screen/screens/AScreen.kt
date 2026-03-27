package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondA() {
    respond(
        Screen(
            id = "a",
        ) {
            Column(
                style = {
                    margin(top = 24)
                    size(height = fillVertically())
                }
            ) {
                SearchBar(
                    placeholder = "Search something...",
                    leadingIcon = {
                        Icon(
                            icon = icon("search")
                        )
                    },
                    style = {
                        margin(horizontal = 24)
                    }
                )

                LazyTiles(
                    id = "LAZY#1",
                    placeholderTiles = {
                        Shimmer(
                            style = {
                                size(
                                    height = fixedVertically(200)
                                )
                                margin(horizontal = 24)
                            }
                        ) {
                            Box(
                              style = {
                                  background(color(themeColorOutline()))
                              }
                            )
                        }
                    },
                    failureTiles = {
                        Column(
                            style = {
                                size(
                                    height = fixedVertically(200)
                                )
                                margin(horizontal = 24)
                            },
                            arrangement = arrangeToCenter(),
                            alignment = alignHorizontallyToCenter()
                        ) {
                            Icon("error")
                            SimpleText(text = "Erro ao carregar tiles")
                            Button(
                                text = "Recarregar",
                                events = {
                                    ReloadLazyTiles(
                                        trigger = EventTriggers.onClick(),
                                        lazyTileId = "LAZY#1"
                                    )
                                }
                            )
                        }
                    },
                    url = "http://192.168.3.84:8080/tiles/a"
                )

                Column(
                    style = {
                        size(height = fillVertically())
                    },
                    arrangement = arrangeToCenter(),
                    alignment = alignHorizontallyToCenter()
                ) {
                    SimpleText(text = "Tela A")
                    Button(
                        text = "Reload",
                        events = {
                            RefreshScreen(
                                trigger = EventTriggers.onClick()
                            )
                        },
                        style = {
                            margin(horizontal = 24)
                        }
                    )
                }
            }
        }
    )
}