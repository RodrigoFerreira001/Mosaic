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

object DismissNavigationDrawerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DismissNavigationDrawer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha o menu de navegação lateral da tela — dispara onSuccess() " +
                    "assim que o sinal de fechamento é enviado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use dentro dos itens do próprio drawer, depois que o usuário seleciona uma opção de " +
                    "navegação — fechar o menu antes ou junto da navegação para a tela escolhida."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "DismissNavigationDrawer não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Dentro do conteúdo do drawer:
                Button(
                    text = "Configurações",
                    events = {
                        DismissNavigationDrawer(trigger = EventTriggers.onClick(), events = {
                            Navigate(trigger = EventTriggers.onSuccess(), destination = "settings", navigatorId = "root")
                        })
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra o drawer desta página e feche-o de dentro dele") {
                Button(
                    text = "Abrir drawer (o botão de fechar está dentro)",
                    events = {
                        DisplayNavigationDrawer(trigger = EventTriggers.onClick())
                    }
                )
                ShowroomNote(
                    "O botão \"Fechar com DismissNavigationDrawer\" dentro do drawer que abre aqui é a " +
                        "demonstração real deste evento — veja o código-fonte do drawer desta própria página " +
                        "em EventDetailsScreenBuilder.kt, navigationDrawerTiles."
                )
            }

            ShowroomRelated(
                names = listOf("DisplayNavigationDrawer", "NavigateUp"),
                destination = "eventDetails"
            )
        }
    }
}
