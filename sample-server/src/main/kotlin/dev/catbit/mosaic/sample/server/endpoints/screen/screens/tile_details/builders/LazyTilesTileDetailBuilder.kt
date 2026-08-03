package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyTilesTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LazyTiles"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Container autossuficiente que busca seus próprios tiles filhos numa URL na primeira composição — seções delegadas a outro endpoint."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "A chamada de rede é um SingleEffect (dispara uma vez só). Se tiles já vier preenchido " +
                    "(ex: via UpdateTiles), a busca é pulada. Pra forçar recarregar, use o evento " +
                    "ReloadLazyTiles."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("url", "String", "Obrigatório. Endpoint que retorna List<TileSchema> em JSON."),
                    ShowroomParam("method / body / headers", "HttpMethod / AnySerializable? / Map?", "Configuração da requisição, GET por padrão."),
                    ShowroomParam("placeholderTiles", "TileSchemaBuilderScope.() -> Unit", "Mostrado durante o carregamento."),
                    ShowroomParam("failureTiles", "TileSchemaBuilderScope.() -> Unit", "Mostrado em caso de erro."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                LazyTiles(
                    id = "recommendationsSection",
                    url = "/api/recommendations",
                    placeholderTiles = { Shimmer(id = "recShimmer") { /* placeholders */ } },
                    failureTiles = { SimpleText(text = "Falha ao carregar recomendações") }
                )
                """
            )

            ShowroomSectionTitle("Demo (ilustrativa)")
            ShowroomDemoCard(title = "Como ficaria carregando de /screens/tileDetailsLazyDemo") {
                Shimmer {
                    Box(style = { size(width = fillHorizontally(), height = fixedVertically(56)) }) {
                        SimpleText(text = "Carregando via LazyTiles...")
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Shimmer", "PullToRefresh", "LazyColumn"),
                destination = "tileDetails"
            )
        }
    }
}
