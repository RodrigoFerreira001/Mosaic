package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.EvaluateData
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData
import dev.catbit.mosaic.server.builder.event.builders.data.incomingData
import dev.catbit.mosaic.server.builder.event.builders.data.valueAtKeyEquals
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DisplayBottomSheet
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import kotlinx.serialization.json.JsonPrimitive
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
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
import io.ktor.server.util.url

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
                    TextField(
                        id = "NAME_FIELD",
                        label = "Nome",
                        placeholder = "Digite seu nome",
                        style = {
                            margin(horizontal = 24, top = 24)
                            size(width = fillHorizontally())
                        }
                    )
                    Button(
                        id = "SEND_NAME_BTN",
                        text = "Enviar nome",
                        style = {
                            margin(horizontal = 24)
                        },
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "SEND_NAME_BTN",
                                        data = mapOf("loading" to true, "enabled" to false)
                                    )
                                }
                            )
                            GetData(
                                trigger = EventTriggers.onClick(),
                                readings = {
                                    addReading(
                                        dataSource = tile(tileId = "NAME_FIELD", dataKey = "name"),
                                        accessMode = singleAccessMode(dataId = "name")
                                    )
                                },
                                events = {
                                    EvaluateData(
                                        trigger = EventTriggers.onSuccess(),
                                        expression = incomingData().valueAtKeyEquals("name", JsonPrimitive("Rodrigo")),
                                        events = {
                                            SendNetworkRequest(
                                                trigger = EventTriggers.onSuccess(),
                                                url = "http://192.168.3.84:9090/events/helloName",
                                                method = HttpMethod.POST,
                                                events = {
                                                    ProcessData(
                                                        trigger = EventTriggers.onSuccess(),
                                                        processWith = "EVENT_RUNNER"
                                                    )
                                                    UpdateTiles(
                                                        trigger = EventTriggers.onSuccess(),
                                                        updates = {
                                                            update(
                                                                tileId = "SEND_NAME_BTN",
                                                                data = mapOf("loading" to false, "enabled" to true)
                                                            )
                                                        }
                                                    )
                                                    UpdateTiles(
                                                        trigger = EventTriggers.onFailure(),
                                                        updates = {
                                                            update(
                                                                tileId = "SEND_NAME_BTN",
                                                                data = mapOf("loading" to false, "enabled" to true)
                                                            )
                                                        }
                                                    )
                                                }
                                            )
                                            DisplayBottomSheet(
                                                trigger = EventTriggers.onFailure()
                                            ) {
                                                Column(
                                                    style = {
                                                        size(height = fixedVertically(200))
                                                        padding(horizontal = 24)
                                                    },
                                                    arrangement = arrangeToCenter(),
                                                    alignment = alignHorizontallyToCenter()
                                                ) {
                                                    SimpleText(text = "O nome deve ser Rodrigo.")
                                                }
                                            }
                                            UpdateTiles(
                                                trigger = EventTriggers.onFailure(),
                                                updates = {
                                                    update(
                                                        tileId = "SEND_NAME_BTN",
                                                        data = mapOf("loading" to false, "enabled" to true)
                                                    )
                                                }
                                            )
                                        }
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onFailure(),
                                        updates = {
                                            update(
                                                tileId = "SEND_NAME_BTN",
                                                data = mapOf("loading" to false, "enabled" to true)
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        id = "NETWORK_CALL",
                        text = "Make a network call",
                        events = {
                            SendNetworkRequest(
                                trigger = EventTriggers.onClick(),
                                url = "http://192.168.3.84:9090/events/BS",
                                method = HttpMethod.GET,
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onStart(),
                                        updates = {
                                            update(
                                                tileId = "NETWORK_CALL",
                                                data = mapOf(
                                                    "loading" to true,
                                                    "enabled" to false
                                                )
                                            )
                                        }
                                    )
                                    ProcessData(
                                        trigger = EventTriggers.onSuccess(),
                                        processWith = "EVENT_RUNNER"
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(
                                                tileId = "NETWORK_CALL",
                                                data = mapOf(
                                                    "loading" to false,
                                                    "enabled" to true
                                                )
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
    )
}