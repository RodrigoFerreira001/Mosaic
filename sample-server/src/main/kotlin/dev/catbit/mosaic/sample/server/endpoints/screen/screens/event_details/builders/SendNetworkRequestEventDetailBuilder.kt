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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SendNetworkRequestEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SendNetworkRequest"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Faz uma requisição HTTP para uma URL e propaga a resposta pelos eventos filhos — a base de qualquer chamada a API."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para qualquer chamada a API de servidor: CRUD, autenticação, busca de dados. " +
                    "O corpo da resposta é entregue como incomingData aos eventos filhos — JSON vira " +
                    "Map/List/valor primitivo, qualquer outro content-type vira ByteArray. " +
                    "O body da requisição é resolvido como body (schema) ?? holder.body (definido por " +
                    "SetIncomingDataToNetworkParamsHolderBody) — o schema sempre tem prioridade."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("url", "String", "Obrigatório. URL completa ou path relativo ao servidor configurado."),
                    ShowroomParam("method", "HttpMethod", "Obrigatório. GET, POST, PUT, DELETE, PATCH."),
                    ShowroomParam("body", "AnySerializable?", "Padrão null. Vence sobre o body armazenado no holder."),
                    ShowroomParam("headers", "Map<String, String>?", "Padrão null. Mesclado com os headers do holder; o schema vence em colisão."),
                    ShowroomParam("timeoutMillis", "Long?", "Padrão null → usa o timeout global do client (HttpTimeout, 20s)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SendNetworkRequest(
                    trigger = EventTriggers.onClick(),
                    url = "https://jsonplaceholder.typicode.com/todos/1",
                    method = HttpMethod.GET,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSuccess(),
                            updates = { update("result_text", incomingTileUpdateData()) }
                        )
                        Navigate(
                            trigger = EventTriggers.onNetworkFailure(401),
                            destination = "login",
                            navigatorId = "root"
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "GET real para uma API pública, sem autenticação") {
                SimpleText(
                    id = "network_request_status_text",
                    text = "Toque no botão para disparar a requisição."
                )
                Button(
                    text = "Buscar https://jsonplaceholder.typicode.com/todos/1",
                    events = {
                        SendNetworkRequest(
                            trigger = EventTriggers.onClick(),
                            url = "https://jsonplaceholder.typicode.com/todos/1",
                            method = HttpMethod.GET,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update(
                                            tileId = "network_request_status_text",
                                            updateData = inlineTileUpdateData("text" to "Enviando requisição...")
                                        )
                                    }
                                )
                                TransformData(
                                    trigger = EventTriggers.onSuccess(),
                                    template = mapOf("text" to "onSuccess · título recebido: \"<|title|>\""),
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = { update("network_request_status_text", incomingTileUpdateData()) }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            tileId = "network_request_status_text",
                                            updateData = inlineTileUpdateData("text" to "onFailure · falha de conexão ou resposta não-2xx")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "onNetworkResponse(code)/onNetworkFailure(code) substituem onSuccess/onFailure para " +
                    "aquele status HTTP específico quando declarados como filho. onFailure nunca dispara " +
                    "para códigos com onNetworkFailure(code) correspondente."
            )

            ShowroomRelated(
                names = listOf("SetIncomingDataToNetworkParamsHolderBody", "SetIncomingDataToNetworkParamsHolderQueryParameters", "DownloadFile"),
                destination = "eventDetails"
            )
        }
    }
}
