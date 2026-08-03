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
import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem
import dev.catbit.mosaic.server.builder.event.builders.system.inlineBroadcastData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.system.SystemBroadcastListener
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object BroadcastToSystemEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "BroadcastToSystem"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "System",
                description = "Emite um broadcast nomeado (broadcastId + payload) via SystemBroadcastChannel, notificando qualquer assinante do app — em qualquer tela — sem acoplamento direto à cadeia de eventos."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "O caso de uso real é cross-screen: uma tela emite, outra(s) escutam com " +
                    "onSystemBroadcast(\"id\") ou com a tile SystemBroadcastListener. Esta demo " +
                    "emite e escuta na mesma tela só pra provar o mecanismo de ponta a ponta — " +
                    "o comportamento entre telas diferentes é idêntico."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("broadcastId", "String", "Obrigatório. Identificador do canal nomeado."),
                    ShowroomParam(
                        "data",
                        "BroadcastData",
                        "Obrigatório. inlineBroadcastData(valor) para um payload estático, ou incomingBroadcastData() para repassar o incomingData atual."
                    ),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Emissor (qualquer tela)
                BroadcastToSystem(
                    trigger = EventTriggers.onClick(),
                    broadcastId = "ENVIRONMENT_CHANGE",
                    data = inlineBroadcastData(environment.id)
                )

                // Receptor (qualquer tela/tile)
                UpdateTiles(
                    trigger = EventTriggers.onSystemBroadcast("ENVIRONMENT_CHANGE"),
                    updates = { /* ... */ }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Botão emite BroadcastToSystem; o listener nesta mesma página reage de verdade") {
                Button(
                    text = "Emitir broadcast \"event_showroom_ping\"",
                    events = {
                        BroadcastToSystem(
                            trigger = EventTriggers.onClick(),
                            broadcastId = "event_showroom_ping",
                            data = inlineBroadcastData("Ping!")
                        )
                    }
                )
                SystemBroadcastListener(
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSystemBroadcast("event_showroom_ping"),
                            updates = {
                                update(
                                    tileId = "broadcast_to_system_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Recebido: <||>")
                                )
                            }
                        )
                    }
                ) {
                    SimpleText(id = "broadcast_to_system_demo_label", text = "Aguardando broadcast...")
                }
            }

            ShowroomRelated(
                names = listOf("CheckIfHasInternetConnection", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
