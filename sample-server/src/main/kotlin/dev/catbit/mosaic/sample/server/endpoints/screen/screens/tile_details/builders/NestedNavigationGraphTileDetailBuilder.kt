package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object NestedNavigationGraphTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NestedNavigationGraph"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "Embute um NavDisplay completo do Navigation 3 dentro de uma tela — uma pilha de navegação própria, aninhada dentro da tela pai."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra layouts master-detail ou uma seção com back stack próprio (ex: um wizard " +
                    "dentro de uma tela maior). O navigator é registrado em NavigatorsHolder sob " +
                    "navigatorId e desregistrado automaticamente quando o tile sai de composição."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("navigatorId", "String", "Obrigatório. Chave de registro em NavigatorsHolder."),
                    ShowroomParam("startEntryId", "String", "Obrigatório. screenId da entrada inicial."),
                    ShowroomParam("defaultTransition / defaultPopTransition / defaultPredictivePopTransition", "ContentTransitionSchema?", "Transições padrão do grafo, sobrescrevíveis por entry."),
                    ShowroomParam("entries", "NestedNavigationGraphEntryBuilderScope.() -> Unit", "Obrigatório. Use entry(screenId, initialTiles, initialEvents, failureTiles, failureEvents)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                NestedNavigationGraph(
                    id = "mainGraph",
                    navigatorId = "main_navigator",
                    startEntryId = "home",
                    entries = {
                        entry(
                            screenId = "home",
                            initialTiles = { HomeScreen() },
                            initialEvents = { GetScreen(trigger = EventTriggers.onDisplay()) }
                        )
                        entry(
                            screenId = "detail",
                            initialTiles = { DetailScreen() },
                            failureTiles = { SimpleText(text = "Erro ao carregar detalhe") }
                        )
                    }
                )
                """
            )

            ShowroomNote(
                "É o tile mais estrutural do Mosaic — embutir um NestedNavigationGraph dentro de uma " +
                    "tela de detalhe isolada, aqui neste showroom, exigiria montar um sub-grafo completo " +
                    "com duas telas reais. Prefira consultar o próprio HomeScreenBuilder.kt do sample-server " +
                    "(ele usa AdaptiveNavigation, o mecanismo de nível mais alto que compõe navegação " +
                    "adaptável em cima do mesmo conceito de navigators registrados) como referência viva " +
                    "deste padrão em produção."
            )

            ShowroomRelated(
                names = listOf("NavigationBar", "NavigationRail", "Tabs"),
                destination = "tileDetails"
            )
        }
    }
}
