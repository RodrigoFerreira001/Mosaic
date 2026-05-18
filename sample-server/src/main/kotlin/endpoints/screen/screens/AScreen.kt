package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.endpoints.local_compositions.LocalNameProvider
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocalProvider
import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.event.builders.scroll.column.ScrollColumn
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedIconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.gone
import dev.catbit.mosaic.server.builder.tile.visible
import dev.catbit.mosaic.server.builder.trigger.on_scrolled.onScrolledToBottom
import dev.catbit.mosaic.server.builder.trigger.on_scrolled.onScrolledToTop
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlin.repeat

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
                        size(height = fillVertically())
                    }
                ) {
                    Row(
                        style = {
                            margin(horizontal = 24, bottom = 24)
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
                                size(
                                    width = weightHorizontally(1f)
                                )
                            }
                        )

                        IconButton(
                            icon = icon("refresh"),
                            buttonType = outlinedIconButton(),
                            events = {
                                RefreshScreen(
                                    trigger = EventTriggers.onClick()
                                )
                            },
                            style = {
                                size(
                                    width = fixedHorizontally(56),
                                    height = fixedVertically(56),
                                )
                                margin(start = 16)
                            }
                        )
                    }

                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(
                                width = fillHorizontally(),
                                height = weightVertically(1f)
                            )
                        }
                    ) {
                        LazyColumn(
                            id = "PAGINATED_LIST",
                            scrollThreshold = 10,
                            style = {
                                margin(horizontal = 24)
                            },
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onScrolled(direction = onScrolledToTop()),
                                    updates = {
                                        update(
                                            tileId = "SCROLL_TO_TOP",
                                            data = mapOf(
                                                "visibility" to gone()
                                            )
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onScrolled(direction = onScrolledToBottom()),
                                    updates = {
                                        update(
                                            tileId = "SCROLL_TO_TOP",
                                            data = mapOf(
                                                "visibility" to visible()
                                            )
                                        )
                                    }
                                )
                                SendNetworkRequest(
                                    id = "PAGINATION_EVENT",
                                    trigger = EventTriggers.onScrollThresholdReached(),
                                    url = "http://192.168.3.84:9090/pagination?page=1",
                                    method = HttpMethod.GET,
                                    events = {
                                        AddTiles(
                                            trigger = EventTriggers.onStart(),
                                            groupingTileId = "PAGINATED_LIST"
                                        ) {
                                            Row(
                                                id = "LOADING",
                                                alignment = alignVerticallyToCenter(),
                                                arrangement = arrangeToCenter()
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                        RemoveTiles(
                                            trigger = EventTriggers.onFailure(),
                                            groupingTileId = "PAGINATED_LIST",
                                            tileIds = listOf("LOADING")
                                        )
                                        AddTiles(
                                            trigger = EventTriggers.onFailure(),
                                            groupingTileId = "PAGINATED_LIST"
                                        ) {
                                            Row(
                                                id = "LOADING",
                                                alignment = alignVerticallyToCenter(),
                                                arrangement = arrangeToCenter()
                                            ) {
                                                SimpleText("Failure loading next page")
                                            }
                                        }
                                        ProcessData(
                                            trigger = EventTriggers.onSuccess(),
                                            processWith = "EVENT_RUNNER"
                                        )
                                    }
                                )
                            }
                        ) {
                            repeat(30) { index ->
                                Card(
                                    style = {
                                        margin(top = 8)
                                    }
                                ) {
                                    Row(
                                        style = {
                                            padding(
                                                horizontal = 16,
                                                vertical = 8
                                            )
                                        }
                                    ) {
                                        SimpleText(index.toString())
                                    }
                                }
                            }
                        }

                        IconButton(
                            id = "SCROLL_TO_TOP",
                            icon = icon("arrow_upward"),
                            buttonType = filledIconButton(),
                            visibility = gone(),
                            events = {
                                ScrollColumn(
                                    trigger = EventTriggers.onClick(),
                                    tileId = "PAGINATED_LIST",
                                    where = ScrollColumnTileEventSchema.Where.Top,
                                    smoothly = true
                                )
                            },
                            style = {
                                margin(end = 24, bottom = 24)
                                size(
                                    width = fixedHorizontally(56),
                                    height = fixedVertically(56),
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}