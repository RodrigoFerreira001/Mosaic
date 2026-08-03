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
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object SetIncomingDataToNetworkParamsHolderQueryParametersEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderQueryParameters"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Guarda o incomingData como query parameters da próxima requisição de rede da " +
                    "cadeia — inclusive do GetScreen automático de uma navegação."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Este é literalmente o mecanismo que faz TODA navegação de card pra card funcionar neste " +
                    "app de amostra: cada card do catálogo de Tiles/Events (CatalogItem) e cada chip " +
                    "\"Relacionado\" no fim de uma página de detalhe usa exatamente esta sequência — " +
                    "TransformData(template = mapOf(\"event\" to nome)) → " +
                    "SetIncomingDataToNetworkParamsHolderQueryParameters → Navigate. A entry de destino " +
                    "(\"eventDetails\"/\"tileDetails\") dispara um GetScreen automaticamente ao ser exibida, e " +
                    "esse GetScreen usa os query parameters guardados aqui — é assim que a tela sabe qual " +
                    "evento/tile mostrar, lendo request.queryParameters[\"event\"] no servidor."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "Nenhum além de trigger/events — o valor guardado é sempre o incomingData atual (deve ser Map<String, String>)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Uso real deste app: CatalogItem.kt e ShowroomComponents.kt (ShowroomRelatedChip)
                TransformData(
                    trigger = EventTriggers.onClick(),
                    template = mapOf("event" to name),
                    events = {
                        SetIncomingDataToNetworkParamsHolderQueryParameters(
                            trigger = EventTriggers.onSuccess(),
                            events = {
                                Navigate(
                                    trigger = EventTriggers.onSuccess(),
                                    navigatorId = "root",
                                    destination = destination
                                )
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Você já usou este evento pra chegar até aqui") {
                ShowroomNote(
                    "Sério — o botão \"$eventName\" que você tocou na tela de Events, ou o chip " +
                        "\"Relacionados\" que trouxe você aqui de outra página, disparou exatamente esta " +
                        "cadeia. O botão abaixo dispara de novo, agora pra uma página vizinha."
                )
                Button(
                    text = "Ver evento SetIncomingDataToNetworkParamsHolderBody",
                    buttonType = outlinedButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "SetIncomingDataToNetworkParamsHolderBody"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        Navigate(
                                            trigger = EventTriggers.onSuccess(),
                                            navigatorId = "root",
                                            destination = "eventDetails"
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("Navigate", "TransformData", "SetIncomingDataToNetworkParamsHolderBody"),
                destination = "eventDetails"
            )
        }
    }
}
