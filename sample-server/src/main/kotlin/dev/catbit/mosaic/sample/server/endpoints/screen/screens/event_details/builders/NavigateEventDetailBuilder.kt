package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "Navigate"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "Empurra um novo destino na pilha de um navigator, opcionalmente removendo entradas " +
                    "anteriores (popUpTo) e levando dados (data) para a tela de destino."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para qualquer navegação entre telas dentro do escopo de um navigator — abrir um detalhe, " +
                    "iniciar um fluxo, ir para uma tela de configurações. destination é o id da Screen/Graph.Entry " +
                    "de destino; navigatorId identifica qual navigator (podem existir vários aninhados) deve " +
                    "processar a navegação. incomingData é convertido para Map<String, Any> e mesclado com data " +
                    "(data vence em caso de conflito de chave), virando o navigationData da tela de destino, " +
                    "lido via screenNavigationData()."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("destination", "String", "Obrigatório. Id da rota/tela de destino."),
                    ShowroomParam("navigatorId", "String", "Obrigatório. Id do navigator registrado que deve navegar."),
                    ShowroomParam("popUpTo", "PopUpTo?", "Opcional. poppingUpTo(destination, inclusive) — remove entradas da pilha antes de navegar."),
                    ShowroomParam("data", "Map<String, Any>?", "Opcional. Mesclado com incomingData; vira navigationData da tela de destino."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Navigate(
                    trigger = EventTriggers.onClick(),
                    destination = "environment_detail",
                    navigatorId = "main_navigator",
                    popUpTo = poppingUpTo(destination = "home", inclusive = false),
                    data = mapOf("environmentId" to env.id)
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Navegue de verdade para a página de outro evento") {
                ShowroomParagraph(
                    "O botão abaixo dispara um Navigate real (não é encenação) para esta mesma tela " +
                        "\"eventDetails\", trocando apenas o evento documentado — é o mesmo mecanismo usado " +
                        "pelos chips em \"Relacionados\", no fim de cada página."
                )
                Button(
                    text = "Ver evento GetScreen",
                    buttonType = outlinedButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "GetScreen"),
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
            }

            ShowroomNote(
                "Por que não usamos o parâmetro data aqui: esta tela lê o evento a documentar via query " +
                    "parameter da requisição HTTP feita pelo GetScreen automático da entry (request." +
                    "queryParameters[\"event\"]), não via navigationData. Por isso a demo usa " +
                    "SetIncomingDataToNetworkParamsHolderQueryParameters antes do Navigate, em vez do campo " +
                    "data — que é ideal quando a tela de destino lê com screenNavigationData()."
            )

            ShowroomRelated(
                names = listOf("NavigateUp", "NavigateClearingStack", "GetScreen"),
                destination = "eventDetails"
            )
        }
    }
}
