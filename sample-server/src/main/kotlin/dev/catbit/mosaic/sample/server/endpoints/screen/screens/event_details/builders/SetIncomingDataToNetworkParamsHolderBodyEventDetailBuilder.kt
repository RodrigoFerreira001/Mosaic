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
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderBody
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SetIncomingDataToNetworkParamsHolderBodyEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderBody"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Guarda o incomingData como body da próxima requisição de rede da cadeia, no " +
                    "NetworkParametersHolder."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando você precisa passar a saída de um evento anterior (dados de um formulário lidos " +
                    "com GetData, por exemplo) como body do SendNetworkRequest seguinte, sem construir o mapa " +
                    "manualmente de novo. Um incomingData nulo dispara onFailure e nada é guardado; o body do " +
                    "schema do SendNetworkRequest, se presente, sempre vence sobre o valor guardado aqui."
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
                GetData(trigger = EventTriggers.onClick(), readings = { reading(screenSegmentedData("form"), fullAccessMode()) }, events = {
                    SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/create", method = HttpMethod.POST)
                    })
                })
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite um título e envie-o como body de um POST real") {
                TextField(
                    id = "set_body_title_input",
                    placeholder = "Título do post",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                SimpleText(
                    id = "set_body_status",
                    text = "Toque no botão para disparar o POST."
                )
                Button(
                    text = "Enviar como body para jsonplaceholder.typicode.com/posts",
                    events = {
                        GetData(
                            trigger = EventTriggers.onClick(),
                            readings = {
                                reading(
                                    dataSource = tile("set_body_title_input", "text"),
                                    accessMode = singleAccessMode("title")
                                )
                            },
                            events = {
                                SetIncomingDataToNetworkParamsHolderBody(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        SendNetworkRequest(
                                            trigger = EventTriggers.onSuccess(),
                                            url = "https://jsonplaceholder.typicode.com/posts",
                                            method = HttpMethod.POST,
                                            events = {
                                                TransformData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    template = mapOf("text" to "Criado com id <|id|> — body enviado: <|title|>"),
                                                    events = {
                                                        UpdateTiles(
                                                            trigger = EventTriggers.onSuccess(),
                                                            updates = {
                                                                update("set_body_status", incomingTileUpdateData())
                                                            }
                                                        )
                                                    }
                                                )
                                                UpdateTiles(
                                                    trigger = EventTriggers.onFailure(),
                                                    updates = {
                                                        update(
                                                            "set_body_status",
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
            }

            ShowroomNote(
                "GetData lê o texto do TextField como {\"title\": \"...\"}; " +
                    "SetIncomingDataToNetworkParamsHolderBody guarda esse mapa como body no holder; e o " +
                    "SendNetworkRequest seguinte, sem informar seu próprio body, usa o valor guardado. A API " +
                    "jsonplaceholder.typicode.com é fake (não persiste nada de verdade), mas sempre ecoa o body " +
                    "enviado de volta com um novo id — por isso o texto final mostra tanto o id quanto o " +
                    "título que você digitou."
            )

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SetIncomingDataToNetworkParamsHolderHeaders", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
