package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
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
                category = "Containers",
                description = "PullToRefreshBox do Material 3 — o indicador de loading fica visível enquanto isRefreshing = true."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "isRefreshing é 100% controlado pelo servidor: puxar dispara OnPull, mas o spinner só " +
                    "some quando o servidor manda isRefreshing = false via UpdateTiles ou o evento " +
                    "StopRefreshing (mais direto, dedicado a esse tile)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("isRefreshing", "Boolean", "Obrigatório. Controlado pelo servidor."),
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo dentro do container."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
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

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Puxe pra baixo — 2 segundos depois StopRefreshing encerra de verdade") {
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
                                    timerData = seconds(initial = 2, step = 2),
                                    events = {
                                        StopRefreshing(
                                            trigger = EventTriggers.onTimeFinish(),
                                            tileId = "pull_to_refresh_demo"
                                        )
                                        DisplaySnackbar(
                                            trigger = EventTriggers.onTimeFinish(),
                                            message = "Refresh concluído"
                                        )
                                    }
                                )
                            }
                        )
                    },
                    tiles = {
                        Column {
                            SimpleText(text = "Puxe esta área pra baixo")
                        }
                    }
                )
            }

            ShowroomRelated(
                names = listOf("LazyColumn", "CircularProgressIndicator", "Shimmer"),
                destination = "tileDetails"
            )
        }
    }
}
