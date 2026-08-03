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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object NavigationBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NavigationBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "NavigationBar Material 3 com um conjunto fixo de destinos — navegação inferior com 2 a 5 itens (é o que monta a navegação principal deste sample)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "selectedItemId é totalmente controlado pelo servidor — o preenchimento do ícone " +
                    "(filled vs outlined) é decidido comparando o id de cada item com selectedItemId."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("selectedItemId", "String", "Obrigatório. Controlado pelo servidor."),
                    ShowroomParam("items", "NavigationBarItemSchemaBuilderScope.() -> Unit", "Obrigatório. Use item(id, icon, label)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                NavigationBar(
                    id = "bottomNav",
                    selectedItemId = "home",
                    items = {
                        item(id = "home", icon = icon("home"), label = "Home")
                        item(id = "search", icon = icon("search"), label = "Busca")
                        item(id = "profile", icon = icon("person"), label = "Perfil")
                    },
                    events = {
                        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "home"), destination = "home")
                        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "search"), destination = "search")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Troque de item — selectedItemId muda de verdade") {
                NavigationBar(
                    id = "nav_bar_demo",
                    selectedItemId = "home",
                    items = {
                        item(id = "home", icon = icon("home"), label = "Home")
                        item(id = "search", icon = icon("search"), label = "Busca")
                        item(id = "profile", icon = icon("person"), label = "Perfil")
                    },
                    events = {
                        listOf("home", "search", "profile").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onNavigationBarItemClick(itemId = id),
                                updates = {
                                    update(tileId = "nav_bar_demo", updateData = inlineTileUpdateData("selectedItemId" to id))
                                    update(tileId = "nav_bar_demo_label", updateData = inlineTileUpdateData("text" to "Ativo: $id"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "nav_bar_demo_label", text = "Ativo: home")
            }

            ShowroomRelated(
                names = listOf("NavigationRail", "Tabs", "BottomAppBar"),
                destination = "tileDetails"
            )
        }
    }
}
