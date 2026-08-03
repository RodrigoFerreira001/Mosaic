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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object ColumnTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Column"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Empilha tiles filhos de cima pra baixo — o container vertical mais usado do Mosaic."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "scrollable = true liga scroll vertical quando todos os itens já são conhecidos de " +
                    "antemão (senão, use LazyColumn). filterChildrenByTerm filtra os filhos pelas " +
                    "searchableTerms deles — é exatamente o mecanismo usado nos catálogos de Tiles e " +
                    "Events deste showroom."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("arrangement", "Vertical", "arrangeVerticallyToTop() (padrão) e variantes com espaçamento."),
                    ShowroomParam("alignment", "Horizontal", "alignHorizontallyToStart() (padrão)."),
                    ShowroomParam("scrollable", "Boolean", "Padrão false."),
                    ShowroomParam("filterChildrenByTerm", "String?", "Filtra filhos por searchableTerms; null = mostra todos."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Column(
                    id = "environmentList",
                    arrangement = arrangeVerticallyToTop(),
                    style = { size(width = fillHorizontally(), height = fillVertically()) }
                ) {
                    environments.forEach { env -> EnvironmentTile(env) }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Busque abaixo — filterChildrenByTerm filtra os itens de verdade") {
                SearchBar(
                    id = "column_demo_search",
                    placeholder = "Filtrar frutas",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onQueryChanged(),
                            template = mapOf("filterChildrenByTerm" to "<||>"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(tileId = "column_demo_list", updateData = incomingTileUpdateData())
                                    }
                                )
                            }
                        )
                    }
                )
                Column(
                    id = "column_demo_list",
                    arrangement = arrangeVerticallySpacedBy(4)
                ) {
                    listOf("Maçã", "Banana", "Laranja", "Uva", "Manga").forEach { fruit ->
                        SimpleText(text = fruit, searchableTerms = listOf(fruit))
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Row", "LazyColumn", "Box"),
                destination = "tileDetails"
            )
        }
    }
}
