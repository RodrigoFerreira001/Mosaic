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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.DatePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedDatePicker
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DatePickerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "DatePicker"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Campo estilo TextField que abre um DatePickerDialog nativo ao ser tocado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "selectedDate é uma String ISO-8601 (\"2026-07-07\") ou null. O client controla a " +
                        "abertura/fechamento do diálogo — o servidor só recebe OnDateSelected com a data " +
                        "escolhida no incomingData."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam(
                        "selectedDate",
                        "String?",
                        "ISO-8601, ex: \"2026-07-07\". null = nenhuma data."
                    ),
                    ShowroomParam(
                        "kind",
                        "Kind",
                        "outlinedDatePicker() (padrão) ou filledDatePicker()."
                    ),
                    ShowroomParam(
                        "dialogOptions",
                        "DialogOptions",
                        "Obrigatório. dialogOptions(confirmLabel, dismissLabel)."
                    ),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DatePicker(
                    id = "dp_birthdate",
                    selectedDate = "1990-01-01",
                    kind = outlinedDatePicker(),
                    dialogOptions = dialogOptions(confirmLabel = "OK", dismissLabel = "Cancelar"),
                    events = {
                        SendData(trigger = EventTriggers.onDateSelected(), dataKey = "birthdate")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Escolha uma data — o texto abaixo mostra o que chegou no servidor") {
                DatePicker(
                    id = "date_picker_demo",
                    selectedDate = null,
                    kind = outlinedDatePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancelar",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDateSelected(),
                            updates = {
                                update(
                                    tileId = "date_picker_demo",
                                    updateData = mappedIncomingTileUpdateData("selectedDate" to "<||>")
                                )
                                update(
                                    tileId = "date_picker_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Data escolhida: <||>")
                                )
                            }
                        )
                    }
                )
                SimpleText(id = "date_picker_demo_label", text = "Data escolhida: nenhuma ainda")
            }

            ShowroomRelated(
                names = listOf("TimePicker", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
