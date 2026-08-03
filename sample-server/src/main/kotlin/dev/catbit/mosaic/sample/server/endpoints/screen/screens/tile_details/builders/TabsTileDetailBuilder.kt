package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.Tabs
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TabsTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Tabs"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "Fileira de abas Material 3 em 4 variantes (PRIMARY/SECONDARY × fixa/scrollable) — navegação secundária dentro de uma tela."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "selectedTabId é 100% controlado pelo servidor. OnTabItemClick carrega o id da aba " +
                    "clicada — cabe ao servidor responder com UpdateTiles trocando selectedTabId (e, " +
                    "tipicamente, o conteúdo abaixo das abas)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("selectedTabId", "String", "Obrigatório. Controlado pelo servidor."),
                    ShowroomParam("tabType", "Type", "PRIMARY (padrão) ou SECONDARY."),
                    ShowroomParam("scrollable", "Boolean", "Padrão false."),
                    ShowroomParam("tabItems", "TabItemSchemaBuilderScope.() -> Unit", "Obrigatório. Use addTab(id, label, icon, badgeText)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Tabs(
                    id = "contentTabs",
                    selectedTabId = "all",
                    tabType = TabsTileSchema.Type.PRIMARY,
                    tabItems = {
                        addTab(id = "all", label = "Todos")
                        addTab(id = "active", label = "Ativos")
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTabItemClick(itemId = "all"),
                            updates = { update(tileId = "contentTabs", updateData = inlineTileUpdateData("selectedTabId" to "all")) }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Troque de aba — selectedTabId e o texto abaixo mudam de verdade") {
                Tabs(
                    id = "tabs_demo",
                    selectedTabId = "all",
                    tabType = TabsTileSchema.Type.PRIMARY,
                    tabItems = {
                        addTab(id = "all", label = "Todos")
                        addTab(id = "active", label = "Ativos")
                        addTab(id = "archived", label = "Arquivados")
                    },
                    events = {
                        listOf("all" to "Todos", "active" to "Ativos", "archived" to "Arquivados").forEach { (id, label) ->
                            UpdateTiles(
                                trigger = EventTriggers.onTabItemClick(itemId = id),
                                updates = {
                                    update(tileId = "tabs_demo", updateData = inlineTileUpdateData("selectedTabId" to id))
                                    update(tileId = "tabs_demo_content", updateData = inlineTileUpdateData("text" to "Mostrando: $label"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "tabs_demo_content", text = "Mostrando: Todos")
            }

            ShowroomRelated(
                names = listOf("NavigationBar", "Pager", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
