package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
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
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderHeaders
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SetIncomingDataToNetworkParamsHolderHeadersEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderHeaders"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Guarda o incomingData (deve ser Map<String, String>) como headers da próxima " +
                    "requisição de rede da cadeia."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use tipicamente pra injetar headers de autenticação — cookie de sessão, Bearer token — " +
                    "buscados via GetData no request seguinte. Falha de cast (incomingData não é " +
                    "Map<String, String>) dispara onFailure. Os headers guardados aqui são mesclados com os " +
                    "do schema do SendNetworkRequest; em colisão de chave, o schema vence: " +
                    "finalHeaders = holder + schema."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "Nenhum além de trigger/events — o valor guardado é sempre o incomingData atual."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                GetData(trigger = EventTriggers.onStart(), readings = {
                    reading(segmentedDataBase("auth"), singleAccessMode("sessionCookie"))
                }, events = {
                    TransformData(trigger = EventTriggers.onSuccess(), template = mapOf("Cookie" to "<||>"), events = {
                        SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess(), events = {
                            SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/protected", method = HttpMethod.GET)
                        })
                    })
                })
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Envie um header custom e veja-o ecoado de volta pela API") {
                SimpleText(
                    id = "set_headers_status",
                    text = "Toque no botão para disparar o GET com o header custom."
                )
                Button(
                    text = "Enviar X-Mosaic-Demo: showroom para httpbin.org/headers",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("X-Mosaic-Demo" to "showroom"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderHeaders(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        SendNetworkRequest(
                                            trigger = EventTriggers.onSuccess(),
                                            url = "https://httpbin.org/headers",
                                            method = HttpMethod.GET,
                                            events = {
                                                TransformData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    template = mapOf("text" to "Header ecoado: <|headers.X-Mosaic-Demo|>"),
                                                    events = {
                                                        UpdateTiles(
                                                            trigger = EventTriggers.onSuccess(),
                                                            updates = {
                                                                update("set_headers_status", incomingTileUpdateData())
                                                            }
                                                        )
                                                    }
                                                )
                                                UpdateTiles(
                                                    trigger = EventTriggers.onFailure(),
                                                    updates = {
                                                        update(
                                                            "set_headers_status",
                                                            inlineTileUpdateData("text" to "Falha na requisição")
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "httpbin.org/headers devolve exatamente os headers que recebeu, como JSON — é uma forma " +
                        "confiável de provar que o header X-Mosaic-Demo realmente saiu do holder e chegou na " +
                        "requisição, sem precisar de um backend próprio pra inspecionar isso."
                )
            }

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SetIncomingDataToNetworkParamsHolderBody", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
