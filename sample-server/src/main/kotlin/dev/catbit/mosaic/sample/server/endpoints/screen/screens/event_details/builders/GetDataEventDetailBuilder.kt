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
import dev.catbit.mosaic.server.builder.data.screenPlainData
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData
import dev.catbit.mosaic.server.builder.event.builders.data.explicitIncomingUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object GetDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Lê um ou mais dados de fontes como tile, DataHolder (memória da tela) ou " +
                    "banco persistente, e encaminha o resultado agregado como incomingData."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use sempre que uma cadeia de eventos precisar ler estado guardado antes de agir — por " +
                    "exemplo, ler campos de formulário antes de enviar uma requisição, ou checar um token " +
                    "de sessão antes de anexar um header. Múltiplas readings dentro do mesmo GetData são " +
                    "mescladas num único mapa; em caso de colisão de chave, a leitura posterior vence."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("readings", "GetDataReadingBuilderScope", "Obrigatório. Uma ou mais chamadas reading(dataSource, accessMode)."),
                    ShowroomParam("dataSource", "DataSource", "screenPlainData(), screenSegmentedData(seg), tile(id, key), plainDataBase(), segmentedDataBase(seg), broadcastData()..."),
                    ShowroomParam("accessMode", "AccessMode", "fullAccessMode(), singleAccessMode(key) ou batchAccessMode(listOf(...))."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                GetData(
                    trigger = EventTriggers.onClick(),
                    readings = {
                        reading(
                            dataSource = screenPlainData(),
                            accessMode = singleAccessMode("showroom_value")
                        )
                    },
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("result_label", incomingTileUpdateData())
                        })
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Nada guardado ainda")
                    }
                )
                """
            )

            ShowroomNote(
                "singleAccessMode() dispara onFailure() (DataNotFoundException) se a chave não existir. " +
                    "Use fullAccessMode() quando não tiver certeza se o valor já foi escrito."
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite, salve no DataHolder e leia de volta com GetData") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "gd_value",
                        kind = outlinedTextField(),
                        label = "Valor",
                        placeholder = "Digite algo...",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) },
                        events = {
                            UpdateData(
                                trigger = EventTriggers.onTextChanged(),
                                updates = {
                                    update(
                                        dataSource = screenPlainData(),
                                        updateData = explicitIncomingUpdateData(dataId = "gd_showroom_value")
                                    )
                                }
                            )
                        }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Ler",
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = screenPlainData(),
                                            accessMode = singleAccessMode("gd_showroom_value")
                                        )
                                    },
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "gd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Lido do DataHolder: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "gd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nada guardado ainda nessa chave.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "gd_status", text = "Digite algo e clique em \"Ler\".")
                }
            }

            ShowroomRelated(
                names = listOf("UpdateData", "RemoveData", "EvaluateData"),
                destination = "eventDetails"
            )
        }
    }
}
