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
import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem
import dev.catbit.mosaic.server.builder.event.builders.system.inlineBroadcastData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.system.SystemBroadcastListener
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SystemBroadcastListenerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SystemBroadcastListener"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "System",
                description = "Container transparente que escuta broadcasts do sistema (emitidos por BroadcastToSystem de qualquer tela) e dispara onSystemBroadcast."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Não introduz nenhum wrapper visual — renderiza os filhos direto. O caso de uso real " +
                    "é cross-screen (uma tela emite, outra ouve), mas esta demo emite e ouve na mesma " +
                    "tela pra mostrar o mecanismo funcionando de ponta a ponta."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Filhos renderizados sem wrapper."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SystemBroadcastListener(
                    id = "session_listener",
                    events = {
                        Navigate(trigger = EventTriggers.onSystemBroadcast("session_expired"), destination = "login", navigatorId = "main")
                    }
                ) {
                    SimpleText(text = "Conteúdo normal")
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Botão emite BroadcastToSystem; o listener reage de verdade") {
                Button(
                    text = "Emitir broadcast \"ping\"",
                    events = {
                        BroadcastToSystem(
                            trigger = EventTriggers.onClick(),
                            broadcastId = "showroom_ping",
                            data = inlineBroadcastData("Ping!")
                        )
                    }
                )
                SystemBroadcastListener(
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSystemBroadcast("showroom_ping"),
                            updates = {
                                update(
                                    tileId = "system_broadcast_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Recebido: <||>")
                                )
                            }
                        )
                    }
                ) {
                    SimpleText(id = "system_broadcast_demo_label", text = "Aguardando broadcast...")
                }
            }

            ShowroomRelated(
                names = listOf("SimpleText", "Column"),
                destination = "tileDetails"
            )
        }
    }
}
