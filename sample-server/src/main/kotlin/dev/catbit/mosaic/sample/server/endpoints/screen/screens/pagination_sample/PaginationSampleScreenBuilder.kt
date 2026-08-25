package dev.catbit.mosaic.sample.server.endpoints.screen.screens.pagination_sample

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.sample.server.endpoints.pagination.paginationUrl
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.WipeTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.text.centerTextAlign
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import io.ktor.server.routing.RoutingCall

private const val PAGINATION_SAMPLE_LIST_ID = "PAGINATION_SAMPLE_LIST"
private const val PAGINATION_EVENT_ID = "PAGINATION_EVENT"

/**
 * A live testbed for `LazyColumn`'s `scrollThreshold` infinite-scroll pagination, exercising a
 * control success case and three failure scenarios against one shared list: each button wipes
 * [PAGINATION_SAMPLE_LIST_ID], rewrites [PAGINATION_EVENT_ID]'s own url for that scenario's `mode`
 * (see [dev.catbit.mosaic.sample.server.endpoints.pagination.pagination]), and re-fires it from
 * page 1 — reusing the same `SendNetworkRequest` the list's own scroll-threshold trigger uses,
 * instead of duplicating the setup per scenario.
 */
object PaginationSampleScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "paginationSample"

    override suspend fun RoutingCall.build(): ScreenResponse = Screen(
        id = "paginationSample",
    ) {
        Column(
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
            }
        ) {
            TopAppBar(
                style = { size(width = fillHorizontally()) },
                navigationIcon = {
                    IconButton(
                        icon = icon("arrow_back"),
                        events = {
                            NavigateUp(
                                trigger = EventTriggers.onClick(),
                                navigatorId = "root"
                            )
                        }
                    )
                },
                title = {
                    SimpleText(text = "Pagination sample")
                }
            )

            Column(
                style = {
                    padding(horizontal = 24, top = 16, bottom = 24)
                    size(width = fillHorizontally(), height = weightVertically(1f))
                },
                arrangement = arrangeVerticallySpacedBy(16)
            ) {
                SimpleText(
                    text = "Pick a scenario, then scroll the list below to page through it. " +
                        "\"Success → Failure → Success\" is the one that proves the fix: without it, " +
                        "the list used to stay stuck after page 2 failed, even after scrolling more.",
                    typography = typographyBodyLarge(),
                    color = color(themeColorOnSurfaceVariant())
                )

                Row(
                    arrangement = arrangeHorizontallySpacedBy(8),
                    style = { size(width = fillHorizontally()) }
                ) {
                    ScenarioButton(text = "Always succeeds", mode = "success")
                    ScenarioButton(text = "Always fails", mode = "always_fail")
                    ScenarioButton(text = "Success, then failure", mode = "fail_second")
                    ScenarioButton(text = "Success → Failure → Success", mode = "recover")
                }

                // weightVertically goes on this Box, not on the LazyColumn itself — matching the
                // original AScreen.kt example. A LazyColumn sized with weight directly on its own
                // style never establishes a real bounded viewport here, so it grows to fit all
                // content instead of scrolling — LazyListState.layoutInfo then never changes on
                // scroll, and scrollThreshold's effect never re-fires past the first page.
                Box(
                    style = { size(width = fillHorizontally(), height = weightVertically(1f)) }
                ) {
                    LazyColumn(
                        id = PAGINATION_SAMPLE_LIST_ID,
                        scrollThreshold = 5,
                        style = { size(width = fillHorizontally(), height = fillVertically()) },
                        events = {
                            SendNetworkRequest(
                                id = PAGINATION_EVENT_ID,
                                trigger = EventTriggers.onScrollThresholdReached(),
                                url = paginationUrl(mode = "success", page = 1),
                                method = HttpMethod.GET,
                                events = {
                                    // Defensively clears a stale error tile left over from a previous
                                    // failed attempt under the same id — RemoveTiles targeting a tile
                                    // that isn't there just fails quietly (no child chained off its own
                                    // onFailure), it doesn't block the AddTiles sibling below.
                                    RemoveTiles(
                                        trigger = EventTriggers.onStart(),
                                        groupingTileId = PAGINATION_SAMPLE_LIST_ID,
                                        tileIds = listOf("LOADING")
                                    )
                                    AddTiles(
                                        trigger = EventTriggers.onStart(),
                                        groupingTileId = PAGINATION_SAMPLE_LIST_ID
                                    ) {
                                        Row(
                                            id = "LOADING",
                                            alignment = alignVerticallyToCenter(),
                                            arrangement = arrangeToCenter(),
                                            style = { padding(vertical = 16) }
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                    RemoveTiles(
                                        trigger = EventTriggers.onFailure(),
                                        groupingTileId = PAGINATION_SAMPLE_LIST_ID,
                                        tileIds = listOf("LOADING")
                                    )
                                    AddTiles(
                                        trigger = EventTriggers.onFailure(),
                                        groupingTileId = PAGINATION_SAMPLE_LIST_ID
                                    ) {
                                        Column(
                                            id = "LOADING",
                                            arrangement = arrangeVerticallySpacedBy(8),
                                            style = { padding(vertical = 16) }
                                        ) {
                                            SimpleText(
                                                text = "Failed to load the next page",
                                                typography = typographyBodyMedium(),
                                                style = { size(width = fillHorizontally()) },
                                                textAlign = centerTextAlign()
                                            )
                                            Row(
                                                arrangement = arrangeToCenter(),
                                                style = { size(width = fillHorizontally()) }
                                            ) {
                                                Button(
                                                    text = "Try again",
                                                    buttonType = filledTonalButton(),
                                                    events = {
                                                        TriggerEvent(
                                                            trigger = EventTriggers.onClick(),
                                                            eventId = PAGINATION_EVENT_ID
                                                        )
                                                    }
                                                )
                                            }
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
                        Row(
                            id = "PAGINATION_SAMPLE_PLACEHOLDER",
                            alignment = alignVerticallyToCenter(),
                            arrangement = arrangeToCenter(),
                            style = { padding(vertical = 32) }
                        ) {
                            SimpleText(
                                text = "Pick a scenario above to start",
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                }
            }
        }
    }

    private fun TileSchemaBuilderScope.ScenarioButton(
        text: String,
        mode: String
    ) {
        Button(
            text = text,
            buttonType = filledTonalButton(),
            events = {
                WipeTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = PAGINATION_SAMPLE_LIST_ID,
                    events = {
                        UpdateEvents(
                            trigger = EventTriggers.onSuccess(),
                            updates = {
                                update(
                                    eventId = PAGINATION_EVENT_ID,
                                    data = mapOf("url" to paginationUrl(mode = mode, page = 1))
                                )
                            },
                            events = {
                                TriggerEvent(
                                    trigger = EventTriggers.onSuccess(),
                                    eventId = PAGINATION_EVENT_ID
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}
