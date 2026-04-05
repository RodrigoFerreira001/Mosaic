package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.endpoints.local_compositions.LocalNameProvider
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocalProvider
import dev.catbit.mosaic.server.builder.data.fullAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondA() {
    respond(
        Screen(
            id = "a",
        ) {
            CompositionLocalProvider(
                LocalNameProvider provides "Rodrigo"
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
                                    clip(roundedCornerShape(16))
                                    size(
                                        height = fixedVertically(200)
                                    )
                                    margin(horizontal = 24, top = 24)
                                }
                            ) {
                                Box(
                                    style = {
                                        background(color(themeColorOutline()))
                                        size(
                                            height = fillVertically(),
                                            width = fillHorizontally()
                                        )
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
                                Icon(icon = icon("error"))
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
                        url = "http://192.168.3.84:9090/tiles/a"
                    )

                    Column(
                        style = {
                            size(height = fillVertically())
                        },
                        arrangement = arrangeToCenter(),
                        alignment = alignHorizontallyToCenter()
                    ) {
                        SimpleText(text = "Name: ${LocalNameProvider.current()}")
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
                        Button(
                            text = "Enable IconButton",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = {
                                        update(
                                            tileId = "MENU_TESTER",
                                            data = mapOf("enabled" to true)
                                        )
                                    }
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                        Button(
                            text = "Disable IconButton",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = {
                                        update(
                                            tileId = "MENU_TESTER",
                                            data = mapOf("enabled" to false)
                                        )
                                    }
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                        TextField(
                            id = "#TEMPLATE_HELPER",
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                        Button(
                            text = "Trigger event",
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = tile(
                                                tileId = "#TEMPLATE_HELPER",
                                                dataKey = "name"
                                            ),
                                            accessMode = fullAccessMode()
                                        )
                                    },
                                    events = {
                                        TransformData(
                                            trigger = EventTriggers.onSuccess(),
                                            eventTemplate = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            tileId = "#SIMPLE_TEXT",
                                                            data = mapOf(
                                                                "text" to "<|name|>"
                                                            )
                                                        )
                                                    }
                                                )
                                            },
                                            events = {
                                                ProcessData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    processWith = "EVENT_RUNNER",
                                                    events = {
                                                        TriggerEvent(
                                                            eventId = "#DISPLAY_BOTTOM_SHEET",
                                                            trigger = EventTriggers.onSuccess()
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                    }
                }
            }
        }
    )
}