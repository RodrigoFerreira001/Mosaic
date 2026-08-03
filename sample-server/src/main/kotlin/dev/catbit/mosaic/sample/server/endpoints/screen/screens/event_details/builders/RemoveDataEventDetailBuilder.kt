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
import dev.catbit.mosaic.server.builder.data.fullAccessMode
import dev.catbit.mosaic.server.builder.data.screenSegmentedData
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.RemoveData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData
import dev.catbit.mosaic.server.builder.event.builders.data.inlineUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object RemoveDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RemoveData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Apaga dados de um data store — uma chave, um lote de chaves ou o store " +
                    "inteiro (fullAccessMode())."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use ao limpar dados de sessão no logout, remover um valor em cache ou resetar um segmento " +
                    "de formulário. ScreenNavigationData e Tile são ignorados como alvo (sem efeito). " +
                    "fullAccessMode() apaga o segmento inteiro — use com cuidado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("deletions", "DeleteDataDeletionBuilderScope.() -> Unit", "Obrigatório. Um addDeletion(dataSource, accessMode) por remoção."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                RemoveData(
                    trigger = EventTriggers.onClick(),
                    deletions = {
                        addDeletion(
                            dataSource = segmentedDataBase("auth"),
                            accessMode = fullAccessMode()
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Grave um valor, confirme que existe, depois remova") {
                SimpleText(
                    id = "remove_data_value",
                    text = "(nenhum valor lido ainda)",
                    typography = typographyTitleMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Gravar valor",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateData(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        updateData = inlineUpdateData("note" to "Valor de exemplo gravado")
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Consultar",
                        buttonType = outlinedButton(),
                        events = {
                            GetData(
                                trigger = EventTriggers.onClick(),
                                readings = {
                                    reading(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        accessMode = singleAccessMode("note")
                                    )
                                },
                                events = {
                                    TransformData(
                                        trigger = EventTriggers.onSuccess(),
                                        template = "<||>",
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onSuccess(),
                                                updates = {
                                                    update("remove_data_value", incomingTileUpdateData())
                                                }
                                            )
                                        }
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onFailure(),
                                        updates = {
                                            update(
                                                "remove_data_value",
                                                inlineTileUpdateData("text" to "Não encontrado — foi removido ou nunca gravado")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Remover com RemoveData",
                        buttonType = filledTonalButton(),
                        events = {
                            RemoveData(
                                trigger = EventTriggers.onClick(),
                                deletions = {
                                    addDeletion(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        accessMode = fullAccessMode()
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "Depois de remover, clique em \"Consultar\" de novo: o singleAccessMode não encontra mais " +
                        "a chave \"note\" e o GetData dispara onFailure, atualizando o texto para o aviso de " +
                        "\"não encontrado\"."
                )
            }

            ShowroomRelated(
                names = listOf("UpdateData", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
