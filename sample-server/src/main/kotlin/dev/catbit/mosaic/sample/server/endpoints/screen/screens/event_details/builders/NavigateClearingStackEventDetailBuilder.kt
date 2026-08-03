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
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateClearingStack
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateClearingStackEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "NavigateClearingStack"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Navigation",
                description = "Navega para um destino limpando toda a pilha de navegação atual — o destino " +
                    "vira a única entrada da back stack, sem histórico anterior."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando o destino não deve empilhar sobre a navegação anterior — trocar de aba principal " +
                    "num app com navegação de nível superior (é exatamente assim que este próprio app de " +
                    "amostra troca entre About/Style/Tiles/Events/Get started/Extend/Mechanisms na rail " +
                    "lateral), ou finalizar um fluxo (checkout, onboarding) sem deixar as telas intermediárias " +
                    "no back stack. launchSingleTop = true (padrão) evita empilhar o mesmo destino duas vezes " +
                    "se ele já for o atual."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("destination", "String", "Obrigatório. Id da rota/tela de destino."),
                    ShowroomParam("navigatorId", "String", "Obrigatório. Id do navigator registrado que deve navegar."),
                    ShowroomParam("launchSingleTop", "Boolean", "true (padrão). Evita empilhar o destino de novo se ele já for o topo da pilha."),
                    ShowroomParam("data", "Map<String, Any>?", "Opcional. Mesclado com incomingData; vira navigationData da tela de destino."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Uso real deste app de amostra: HomeScreenBuilder.kt troca de aba assim,
                // uma vez por item do rail lateral (About, Style, Tiles, Events...)
                HomeEntries.entries.forEach { homeEntry ->
                    NavigateClearingStack(
                        trigger = EventTriggers.onAdaptiveNavigationItemClick(homeEntry.id),
                        navigatorId = "home",
                        destination = homeEntry.id
                    )
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Navegue de verdade, limpando a pilha, para outro evento") {
                ShowroomParagraph(
                    "O botão abaixo dispara um NavigateClearingStack real para esta mesma tela " +
                        "\"eventDetails\", trocando o evento documentado — como o destino é o mesmo tipo de " +
                        "tela, o efeito de \"limpar a pilha\" fica mais visível ao notar que voltar não retorna " +
                        "para GetScreen, e sim para a tela de onde você veio antes dele."
                )
                Button(
                    text = "Ver evento GetScreen (limpando a pilha)",
                    buttonType = outlinedButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "GetScreen"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        NavigateClearingStack(
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
                "Diferença para Navigate: Navigate empilha (opcionalmente com popUpTo parcial); " +
                    "NavigateClearingStack sempre remove TODA a pilha do navigator antes de empurrar o novo " +
                    "destino — não há popUpTo parcial aqui porque não sobra nada da pilha anterior."
            )

            ShowroomRelated(
                names = listOf("Navigate", "NavigateUp", "GetScreen"),
                destination = "eventDetails"
            )
        }
    }
}
