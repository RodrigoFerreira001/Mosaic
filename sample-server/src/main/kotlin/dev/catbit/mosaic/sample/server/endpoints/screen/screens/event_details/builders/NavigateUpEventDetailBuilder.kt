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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateUpEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "NavigateUp"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "Remove o destino atual da pilha de um navigator — equivalente ao botão de " +
                    "voltar do sistema, restrito ao escopo daquele navigator."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para fechar telas navegadas como destino (dialogs de tela cheia, sub-fluxos) ou para " +
                    "implementar um botão de voltar customizado numa top bar — é exatamente esse padrão que a " +
                    "própria tela \"eventDetails\" usa: o ícone de voltar da TopAppBar chama NavigateUp no " +
                    "navigator \"root\"."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("navigatorId", "String", "Obrigatório. Id do navigator registrado que deve voltar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Uso real deste app: EventDetailsScreenBuilder.kt, botão de voltar da TopAppBar
                IconButton(
                    icon = icon("arrow_back"),
                    events = {
                        NavigateUp(
                            trigger = EventTriggers.onClick(),
                            navigatorId = "root"
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Navegue para outra página e volte com NavigateUp") {
                ShowroomParagraph(
                    "Primeiro navegue para o evento ChangeScreenState; a própria TopAppBar dessa página " +
                        "usa NavigateUp no botão de voltar — o mesmo evento sendo documentado aqui."
                )
                Button(
                    text = "Ver evento ChangeScreenState",
                    buttonType = filledButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "ChangeScreenState"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        Navigate(
                                            trigger = EventTriggers.onSuccess(),
                                            destination = "eventDetails",
                                            navigatorId = "root"
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                Button(
                    text = "Voltar direto com NavigateUp",
                    buttonType = outlinedButton(),
                    events = {
                        NavigateUp(
                            trigger = EventTriggers.onClick(),
                            navigatorId = "root"
                        )
                    }
                )
            }

            ShowroomNote(
                "NavigateUp falha silenciosamente (onFailure) se o navigator \"root\" não tiver mais " +
                    "nenhuma entrada anterior na pilha para voltar."
            )

            ShowroomRelated(
                names = listOf("Navigate", "NavigateClearingStack"),
                destination = "eventDetails"
            )
        }
    }
}
