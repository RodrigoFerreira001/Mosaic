package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
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
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyColumnTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LazyColumn"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Lista vertical preguiçosa — só compõe os itens visíveis. Ideal para listas longas ou paginadas."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "scrollThreshold define a quantos itens do fim o OnScrollThresholdReached dispara — " +
                    "esse é o mecanismo padrão de paginação do Mosaic: o servidor reage adicionando " +
                    "mais itens via AddTiles(insertAtEnd())."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("scrollThreshold", "Int?", "Itens do fim pra disparar OnScrollThresholdReached."),
                    ShowroomParam("considerLoadingItemAtEndOnThresholdReached", "Boolean", "Padrão true. Considera um placeholder de loading no fim."),
                    ShowroomParam("arrangement / alignment", "Vertical / Horizontal", "Mesmos padrões de Column."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                LazyColumn(
                    id = "itemList",
                    scrollThreshold = 3,
                    style = { size(width = fillHorizontally(), height = fillVertically()) },
                    events = {
                        AddTiles(
                            trigger = EventTriggers.onScrollThresholdReached(),
                            groupingTileId = "itemList",
                            position = insertAtEnd(),
                            tiles = { /* próxima página */ }
                        )
                    }
                ) {
                    items.forEach { item -> ItemCard(item) }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Role até o fim — 3 itens novos chegam via AddTiles de verdade") {
                LazyColumn(
                    id = "lazy_column_demo",
                    scrollThreshold = 1,
                    style = { size(width = fillHorizontally(), height = fixedVertically(160)) },
                    events = {
                        AddTiles(
                            trigger = EventTriggers.onScrollThresholdReached(),
                            groupingTileId = "lazy_column_demo",
                            position = insertAtEnd(),
                            tiles = {
                                repeat(3) {
                                    SimpleText(id = randomId(), text = "Item carregado via paginação")
                                }
                            }
                        )
                    }
                ) {
                    repeat(6) { i -> SimpleText(id = randomId(), text = "Item inicial ${i + 1}") }
                }
            }

            ShowroomRelated(
                names = listOf("Column", "LazyRow", "PullToRefresh"),
                destination = "tileDetails"
            )
        }
    }
}
