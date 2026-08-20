package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.event.RunCancellableEvents
import dev.catbit.mosaic.server.builder.event.builders.event.CancelEvents
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh.StopRefreshing
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.time.StartCountdownTimer
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.PullToRefresh
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object PullToRefreshTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "PullToRefresh"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 PullToRefreshBox — the loading indicator stays visible while " +
                    "isRefreshing = true. isRefreshing is 100% server-controlled: pulling fires OnPull, but the " +
                    "spinner only disappears once the server sends isRefreshing = false via UpdateTiles or the " +
                    "StopRefreshing event (more direct, dedicated to this tile)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pull down — 2 seconds later StopRefreshing really ends it") {
                PullToRefresh(
                    id = "pull_to_refresh_demo",
                    isRefreshing = false,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPull(),
                            updates = {
                                update(tileId = "pull_to_refresh_demo", updateData = inlineTileUpdateData("isRefreshing" to true))
                            }
                        )
                        RunCancellableEvents(
                            trigger = EventTriggers.onPull(),
                            cancellableEventId = "pull_to_refresh_demo_timer",
                            events = {
                                StartCountdownTimer(
                                    trigger = EventTriggers.inline(),
                                    timerData = seconds(initial = 2, step = 1),
                                    events = {
                                        StopRefreshing(
                                            trigger = EventTriggers.onTimeFinish(),
                                            tileId = "pull_to_refresh_demo"
                                        )
                                        DisplaySnackbar(
                                            trigger = EventTriggers.onTimeFinish(),
                                            message = "Refresh complete"
                                        )
                                    }
                                )
                            }
                        )
                    },
                    tiles = {
                        Column {
                            SimpleText(text = "Pull down this area")
                        }
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                PullToRefresh(
                    id = "feedRefresh",
                    isRefreshing = false,
                    tiles = { LazyColumn(id = "feedList") { items.forEach { FeedItem(it) } } },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPull(),
                            updates = { update(tileId = "feedRefresh", updateData = inlineTileUpdateData("isRefreshing" to true)) }
                        )
                        SendNetworkRequest(
                            trigger = EventTriggers.onPull(),
                            events = {
                                StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "feedRefresh")
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("LazyColumn", "CircularProgressIndicator", "Shimmer"),
                destination = "tileDetails"
            )
        }
    }
}
