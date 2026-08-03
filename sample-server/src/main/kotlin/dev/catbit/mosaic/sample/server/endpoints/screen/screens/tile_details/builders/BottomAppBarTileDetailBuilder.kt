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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.BottomAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton

object BottomAppBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "BottomAppBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "App Bars",
                description = "Barra de fundo Material 3 com ações à esquerda e um FAB opcional ancorado à direita."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "actions roda dentro de um RowScope — os filhos podem usar modifiers de peso (weight) " +
                    "via LocalRowScope. floatingActionButton fica ancorado à direita, separado do fluxo " +
                    "normal de actions."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("actions", "TileSchemaBuilderScope.() -> Unit", "Padrão {}. Tiles de ação à esquerda."),
                    ShowroomParam("floatingActionButton", "( -> Unit)?", "FAB ancorado à direita."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                BottomAppBar(
                    id = "bottomBar",
                    actions = {
                        IconButton(icon = icon("delete"), events = { SendNetworkRequest(trigger = EventTriggers.onClick()) })
                        IconButton(icon = icon("share"), events = { TriggerEvent(eventId = "shareEvent", trigger = EventTriggers.onClick()) })
                    },
                    floatingActionButton = {
                        FloatingActionButton(icon = icon("add"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "create") })
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "2 ações + FAB, todos disparando eventos reais") {
                BottomAppBar(
                    style = { size(width = fillHorizontally()) },
                    actions = {
                        IconButton(
                            icon = icon("delete"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Excluir clicado") }
                        )
                        IconButton(
                            icon = icon("share"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Compartilhar clicado") }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = icon("add"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "FAB clicado") }
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("TopAppBar", "FloatingActionButton", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
