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

object CheckForReceivedDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckForReceivedData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Lê o DataMailer por um valor postado sob dataKey e o encaminha como " +
                    "incomingData para os eventos filhos. Contraparte de leitura do SendData."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Tipicamente disparado no onDisplay() da tela de destino, para pegar o valor que a tela " +
                    "anterior postou via SendData antes de navegar. Também pode ser usado sob demanda — " +
                    "por exemplo, um botão \"Atualizar\" que faz o polling manualmente."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("dataKey", "String", "Obrigatório. Chave a procurar no DataMailer."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                CheckForReceivedData(
                    trigger = EventTriggers.onDisplay(),
                    dataKey = "selected_environment",
                    events = {
                        UpdateTiles(trigger = EventTriggers.onDataReceived(), updates = {
                            update("env_name_label", incomingTileUpdateData())
                        })
                    }
                )
                """
            )

            ShowroomNote(
                "Dispara onDataReceived(value) e em seguida onSuccess(value) quando encontra algo; " +
                    "onFailure() quando não há nada postado sob essa dataKey."
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Envie um valor e depois cheque se ele chegou") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "cfrd_value",
                        kind = outlinedTextField(),
                        label = "Valor a enviar",
                        placeholder = "Ex: ola-mosaic",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Enviar",
                            buttonType = outlinedButton(),
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = tile("cfrd_value", "value"),
                                            accessMode = singleAccessMode("value")
                                        )
                                    },
                                    events = {
                                        SendData(
                                            trigger = EventTriggers.onSuccess(),
                                            dataKey = "showroom_mailbox_cfrd",
                                            events = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            tileId = "cfrd_status",
                                                            updateData = inlineTileUpdateData("text" to "Enviado. Clique em \"Checar\".")
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
                            text = "Checar",
                            events = {
                                CheckForReceivedData(
                                    trigger = EventTriggers.onClick(),
                                    dataKey = "showroom_mailbox_cfrd",
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "cfrd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Recebido: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "cfrd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nada recebido ainda.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "cfrd_status", text = "Nada enviado ainda.")
                }
            }

            ShowroomRelated(
                names = listOf("SendData", "GetData", "TransformData"),
                destination = "eventDetails"
            )
        }
    }
}
