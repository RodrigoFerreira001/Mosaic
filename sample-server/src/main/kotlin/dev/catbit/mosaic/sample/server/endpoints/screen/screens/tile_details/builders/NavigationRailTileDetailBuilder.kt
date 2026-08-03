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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationRail
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object NavigationRailTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "NavigationRail"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "NavigationRail Material 3 — lista vertical de destinos, com header/footer opcionais, pra larguras médias e grandes."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mesma semântica de NavigationBar (selectedItemId controlado pelo servidor), mas em " +
                    "layout vertical. header é útil pra encaixar um FAB no topo do rail."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("selectedItemId", "String", "Obrigatório."),
                    ShowroomParam("header / footer", "( -> Unit)?", "Tiles opcionais no topo/fundo do rail."),
                    ShowroomParam("items", "NavigationRailItemSchemaBuilderScope.() -> Unit", "Obrigatório. Use addItem(id, icon, label)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                NavigationRail(
                    id = "sideNav",
                    selectedItemId = currentDestination,
                    header = { FloatingActionButton(icon = icon("add"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "create") }) },
                    items = {
                        addItem(id = "home", icon = icon("home"), label = "Home")
                        addItem(id = "library", icon = icon("library_books"), label = "Biblioteca")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Rail vertical com header FAB — troque de item de verdade") {
                NavigationRail(
                    id = "nav_rail_demo",
                    selectedItemId = "home",
                    header = {
                        FloatingActionButton(icon = icon("add"))
                    },
                    items = {
                        addItem(id = "home", icon = icon("home"), label = "Home")
                        addItem(id = "library", icon = icon("library_books"), label = "Biblioteca")
                    },
                    events = {
                        listOf("home", "library").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onNavigationRailItemClick(itemId = id),
                                updates = {
                                    update(tileId = "nav_rail_demo", updateData = inlineTileUpdateData("selectedItemId" to id))
                                    update(tileId = "nav_rail_demo_label", updateData = inlineTileUpdateData("text" to "Ativo: $id"))
                                }
                            )
                        }
                    }
                )
                SimpleText(id = "nav_rail_demo_label", text = "Ativo: home")
            }

            ShowroomRelated(
                names = listOf("NavigationBar", "AdaptiveVisibility", "FloatingActionButton"),
                destination = "tileDetails"
            )
        }
    }
}
