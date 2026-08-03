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
import dev.catbit.mosaic.server.builder.event.builders.data.explicitUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object UpdateDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Escreve dados chave-valor em uma ou mais fontes — memória da tela (DataHolder) " +
                    "ou banco persistente — para serem lidos depois com GetData."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use depois de receber um dado que precisa ser guardado — token de sessão, preferência do " +
                    "usuário, estado de formulário. Prefira sempre explicitUpdateData(dataId, value) / " +
                    "explicitIncomingUpdateData(dataId) para gravar um valor único ou um registro inteiro " +
                    "sob um dataId. As variantes legadas inlineUpdateData(...) / incomingUpdateData() " +
                    "explodem cada chave do mapa em um dataId separado — só corretas quando o mapa " +
                    "realmente representa pares independentes de dataId → valor."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("updates", "UpdateDataUpdateBuilderScope", "Obrigatório. Uma ou mais chamadas update(dataSource, updateData)."),
                    ShowroomParam("updateData", "UpdateData", "explicitUpdateData(dataId, value), explicitIncomingUpdateData(dataId), explicitNullUpdateData(dataId), ou as legadas inlineUpdateData(...) / incomingUpdateData()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                UpdateData(
                    trigger = EventTriggers.onCheck(),
                    updates = {
                        update(
                            dataSource = screenPlainData(),
                            updateData = explicitUpdateData(dataId = "accepted_terms", value = true)
                        )
                    }
                )
                """
            )

            ShowroomNote(
                "Sources ScreenNavigationData e Tile são ignoradas como alvo de escrita (no-op). " +
                    "explicitNullUpdateData/explicitUpdateData(dataId, null) só funcionam em fontes em " +
                    "memória — bancos persistentes descartam escritas de null silenciosamente."
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Marque, salve no DataHolder e leia de volta com GetData") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Checkbox(
                            id = "ud_checkbox",
                            checked = false,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(tileId = "ud_checkbox", updateData = inlineTileUpdateData("checked" to true))
                                    }
                                )
                                UpdateData(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(
                                            dataSource = screenPlainData(),
                                            updateData = explicitUpdateData(dataId = "ud_flag", value = true)
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(tileId = "ud_checkbox", updateData = inlineTileUpdateData("checked" to false))
                                    }
                                )
                                UpdateData(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(
                                            dataSource = screenPlainData(),
                                            updateData = explicitUpdateData(dataId = "ud_flag", value = false)
                                        )
                                    }
                                )
                            }
                        )
                        SimpleText(text = "Aceito os termos")
                    }
                    Button(
                        text = "Ler",
                        events = {
                            GetData(
                                trigger = EventTriggers.onClick(),
                                readings = {
                                    reading(
                                        dataSource = screenPlainData(),
                                        accessMode = singleAccessMode("ud_flag")
                                    )
                                },
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(
                                                tileId = "ud_status",
                                                updateData = mappedIncomingTileUpdateData("text" to "Gravado no DataHolder: <//>")
                                            )
                                        }
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onFailure(),
                                        updates = {
                                            update(
                                                tileId = "ud_status",
                                                updateData = inlineTileUpdateData("text" to "Nada gravado ainda.")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    SimpleText(id = "ud_status", text = "Marque a caixa e clique em \"Ler\".")
                }
            }

            ShowroomRelated(
                names = listOf("GetData", "RemoveData", "SendData"),
                destination = "eventDetails"
            )
        }
    }
}
