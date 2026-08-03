package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.CheckIfHasInternetConnection
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object CheckIfHasInternetConnectionEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckIfHasInternetConnection"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "System",
                description = "Verifica se o dispositivo tem conexão ativa com a internet no momento em que o evento roda."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use antes de tentar operações de rede em cenários offline-first, evitando disparar " +
                    "um SendNetworkRequest fadado ao timeout quando já se sabe de antemão que não há " +
                    "conectividade."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "CheckIfHasInternetConnection não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                CheckIfHasInternetConnection(
                    trigger = EventTriggers.onDisplay(),
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/data", method = HttpMethod.GET)
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Sem conexão com a internet")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Botão checa a conexão de verdade") {
                SimpleText(
                    id = "check_internet_status",
                    text = "Status: aguardando"
                )
                Button(
                    text = "Checar conexão",
                    events = {
                        CheckIfHasInternetConnection(
                            trigger = EventTriggers.onClick(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "check_internet_status",
                                            updateData = inlineTileUpdateData("text" to "Status: conectado ✅")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            tileId = "check_internet_status",
                                            updateData = inlineTileUpdateData("text" to "Status: sem conexão ❌")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("BroadcastToSystem", "SendNetworkRequest", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
