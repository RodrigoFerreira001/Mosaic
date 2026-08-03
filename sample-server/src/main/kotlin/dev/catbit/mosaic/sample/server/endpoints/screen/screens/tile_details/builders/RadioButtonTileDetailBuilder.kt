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
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.RadioButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

private val frequencies = listOf("daily" to "Diário", "weekly" to "Semanal", "monthly" to "Mensal")

object RadioButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "RadioButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Radio button Material 3 — tiles com o mesmo groupId formam um grupo de seleção única."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Ao tocar, o client dispara OnSelect com o id e o groupId do tile tocado — mas quem " +
                    "decide quem fica selected continua sendo o servidor: é preciso um UpdateTiles " +
                    "desmarcando os outros membros do grupo manualmente."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("selected", "Boolean", "Padrão false. Controlado pelo servidor."),
                    ShowroomParam("groupId", "String", "Obrigatório. Tiles com o mesmo groupId formam um grupo."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                listOf("daily", "weekly", "monthly").forEachIndexed { i, freq ->
                    RadioButton(
                        id = "freq_${'$'}freq",
                        groupId = "frequency_group",
                        selected = i == 0,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onSelect(),
                                updates = {
                                    listOf("daily", "weekly", "monthly").forEach { f ->
                                        update(
                                            tileId = "freq_${'$'}f",
                                            updateData = inlineTileUpdateData("selected" to (f == freq))
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Selecione uma frequência — os outros dois se desmarcam") {
                frequencies.forEach { (id, label) ->
                    Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                        RadioButton(
                            id = "radio_demo_$id",
                            groupId = "radio_demo_group",
                            selected = id == "daily",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSelect(),
                                    updates = {
                                        frequencies.forEach { (otherId, _) ->
                                            update(
                                                tileId = "radio_demo_$otherId",
                                                updateData = inlineTileUpdateData("selected" to (otherId == id))
                                            )
                                        }
                                        update(
                                            tileId = "radio_demo_selected_label",
                                            updateData = inlineTileUpdateData("text" to "Selecionado: $label")
                                        )
                                    }
                                )
                            }
                        )
                        SimpleText(text = label)
                    }
                }
                SimpleText(id = "radio_demo_selected_label", text = "Selecionado: Diário")
            }

            ShowroomRelated(
                names = listOf("Checkbox", "DropdownList", "Switch"),
                destination = "tileDetails"
            )
        }
    }
}
