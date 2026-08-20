package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh.StopRefreshing
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.PullToRefresh
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object StopRefreshingEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "StopRefreshing"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Signals a PullToRefresh to stop its loading indicator and return to the idle " +
                    "state — always chained after a RefreshScreen or a request fired by onPull. Always chain " +
                    "this event after RefreshScreen (or any network call fired by onPull), on both onSuccess " +
                    "and onFailure — forgetting to call it leaves the loading indicator spinning indefinitely."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pull to refresh and watch the indicator stop by itself") {
                PullToRefresh(
                    id = "stop_refreshing_demo",
                    isRefreshing = false,
                    style = { size(width = fillHorizontally(), height = fixedVertically(140)) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPull(),
                            updates = {
                                update("stop_refreshing_demo", inlineTileUpdateData("isRefreshing" to true))
                            }
                        )
                        StopRefreshing(
                            trigger = EventTriggers.onPull(),
                            tileId = "stop_refreshing_demo"
                        )
                    },
                    tiles = {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = fillVertically())
                                padding(horizontal = 16, vertical = 16)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainer()))
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(
                                text = "Pull down here",
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                )
                ShowroomNote(
                    "In this demo, StopRefreshing fires on the same onPull() trigger that starts the " +
                        "indicator — just to visually prove the spinner goes away. In production, " +
                        "StopRefreshing is chained after the real RefreshScreen/SendNetworkRequest finishes " +
                        "(onSuccess/onFailure), not alongside onPull()."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RefreshScreen(
                    trigger = EventTriggers.onPull(),
                    events = {
                        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "ptr_container")
                        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "ptr_container")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("RefreshScreen", "PullToRefresh"),
                destination = "eventDetails"
            )
        }
    }
}
