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
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TimePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTimePicker
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TimePickerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TimePicker"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Campo estilo TextField que abre um TimePickerDialog nativo ao ser tocado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "selectedTime é uma String \"HH:mm\" (24h) ou null. Mesmo contrato de DatePicker: o " +
                    "client controla o diálogo, o servidor só recebe OnTimeSelected com o horário " +
                    "escolhido no incomingData."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("selectedTime", "String?", "\"HH:mm\" 24h, ex: \"09:00\". null = nenhum horário."),
                    ShowroomParam("kind", "Kind", "outlinedTimePicker() (padrão) ou filledTimePicker()."),
                    ShowroomParam("dialogOptions", "DialogOptions", "Obrigatório. dialogOptions(confirmLabel, dismissLabel)."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                TimePicker(
                    id = "tp_reminder",
                    selectedTime = "09:00",
                    kind = outlinedTimePicker(),
                    dialogOptions = dialogOptions(confirmLabel = "OK", dismissLabel = "Cancelar"),
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Escolha um horário — o texto abaixo mostra o que chegou no servidor") {
                TimePicker(
                    id = "time_picker_demo",
                    selectedTime = null,
                    kind = outlinedTimePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancelar",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTimeSelected(),
                            updates = {
                                update(
                                    tileId = "time_picker_demo",
                                    updateData = mappedIncomingTileUpdateData("selectedTime" to "<||>")
                                )
                                update(
                                    tileId = "time_picker_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Horário escolhido: <||>")
                                )
                            }
                        )
                    }
                )
                SimpleText(id = "time_picker_demo_label", text = "Horário escolhido: nenhum ainda")
            }

            ShowroomRelated(
                names = listOf("DatePicker", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
