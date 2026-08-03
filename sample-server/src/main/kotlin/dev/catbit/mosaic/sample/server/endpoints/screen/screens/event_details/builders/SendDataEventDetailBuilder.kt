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
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.CheckForReceivedData
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.SendData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SendDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SendData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Posta um valor no DataMailer — um barramento em memória, global ao app — " +
                    "sob uma dataKey, para ser lido depois em qualquer tela via CheckForReceivedData."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para passar dados entre telas na navegação: a tela de origem posta o valor antes " +
                    "de navegar; a tela de destino faz o polling com CheckForReceivedData, tipicamente no " +
                    "onDisplay(). O DataMailer não é persistente — some ao encerrar o processo — e cada " +
                    "chave guarda apenas o último valor postado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("dataKey", "String", "Obrigatório. Chave sob a qual o valor é guardado no DataMailer."),
                    ShowroomParam("data", "AnySerializable?", "Padrão null. Payload estático; se null, usa o incomingData do evento pai."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SendData(
                    trigger = EventTriggers.onClick(),
                    dataKey = "selected_environment",
                    data = environment.id
                )
                """
            )

            ShowroomNote(
                "Se data for null e não houver incomingData, o evento dispara onFailure() sem postar nada. " +
                    "Postar de novo na mesma dataKey sobrescreve o valor anterior."
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Envie um valor e depois cheque se ele chegou") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "sd_value",
                        kind = outlinedTextField(),
                        label = "Valor a enviar",
                        placeholder = "Ex: ola-mosaic",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Enviar",
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = tile("sd_value", "value"),
                                            accessMode = singleAccessMode("value")
                                        )
                                    },
                                    events = {
                                        SendData(
                                            trigger = EventTriggers.onSuccess(),
                                            dataKey = "showroom_mailbox",
                                            events = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            tileId = "sd_status",
                                                            updateData = inlineTileUpdateData("text" to "Enviado para o DataMailer!")
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                        Button(
                            text = "Checar recebido",
                            buttonType = outlinedButton(),
                            events = {
                                CheckForReceivedData(
                                    trigger = EventTriggers.onClick(),
                                    dataKey = "showroom_mailbox",
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "sd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Recebido: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "sd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nada recebido ainda.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "sd_status", text = "Nada enviado ainda.")
                }
            }

            ShowroomRelated(
                names = listOf("CheckForReceivedData", "GetData", "UpdateData"),
                destination = "eventDetails"
            )
        }
    }
}
