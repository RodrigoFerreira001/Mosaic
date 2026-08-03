package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
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
                category = "Pull to Refresh",
                description = "Sinaliza um PullToRefresh pra parar o indicador de carregamento e voltar ao " +
                    "estado ocioso — sempre encadeado depois de um RefreshScreen ou requisição disparada por onPull."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Sempre encadeie este evento depois de RefreshScreen (ou qualquer chamada de rede disparada " +
                    "por onPull), tanto no onSuccess quanto no onFailure — esquecer de chamá-lo deixa o " +
                    "indicador de carregamento girando indefinidamente."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tileId", "String", "Obrigatório. ID do PullToRefresh a parar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
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

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Puxe para atualizar e veja o indicador parar sozinho") {
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
                                text = "Puxe para baixo aqui dentro",
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                )
                ShowroomNote(
                    "Nesta demo, StopRefreshing dispara no mesmo trigger onPull() que inicia o indicador — " +
                        "só pra provar visualmente que o spinner some. Em produção, StopRefreshing vai " +
                        "encadeado depois do RefreshScreen/SendNetworkRequest de verdade terminar (onSuccess/" +
                        "onFailure), não junto do onPull()."
                )
            }

            ShowroomRelated(
                names = listOf("RefreshScreen", "PullToRefresh"),
                destination = "eventDetails"
            )
        }
    }
}
