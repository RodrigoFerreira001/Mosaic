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
import dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer.DisplayNavigationDrawer
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button

object DisplayNavigationDrawerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplayNavigationDrawer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Abre o menu de navegação lateral da tela — o conteúdo do drawer é definido " +
                    "no tile da tela (navigationDrawerTiles), não neste evento."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use disparado por um botão de hambúrguer ou equivalente. Este evento não tem efeito " +
                    "visível se a tela atual não configurar navigationDrawerTiles no Screen(...) — o drawer " +
                    "precisa existir antes de poder ser aberto. Esta própria página \"eventDetails\" configura " +
                    "um drawer mínimo só pra esta demonstração (nenhuma outra tela deste app o abre)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "DisplayNavigationDrawer não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Configurado uma vez, no nível da tela:
                Screen(
                    id = "home",
                    navigationDrawerTiles = {
                        Column(id = "drawer_content") { /* itens do menu */ }
                    }
                ) { /* tiles da tela */ }

                // Disparado de qualquer lugar dentro dessa tela:
                IconButton(icon = icon("menu"), events = {
                    DisplayNavigationDrawer(trigger = EventTriggers.onClick())
                })
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra o drawer configurado nesta própria página") {
                Button(
                    text = "Abrir com DisplayNavigationDrawer",
                    events = {
                        DisplayNavigationDrawer(trigger = EventTriggers.onClick())
                    }
                )
                ShowroomNote(
                    "O drawer que abre contém um botão \"Fechar com DismissNavigationDrawer\" — a " +
                        "demonstração do evento par, no fim do drawer."
                )
            }

            ShowroomRelated(
                names = listOf("DismissNavigationDrawer", "ToggleMenu", "DisplayModalBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
