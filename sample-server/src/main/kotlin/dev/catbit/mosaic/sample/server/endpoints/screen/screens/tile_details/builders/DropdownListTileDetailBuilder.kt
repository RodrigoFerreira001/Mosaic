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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.DropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedDropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.selectOption
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DropdownListTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "DropdownList"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "ExposedDropdownMenuBox do Material 3 — escolha de uma opção entre uma lista predefinida."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "O client sempre manda expanded = false — quem controla se o menu está aberto é o " +
                    "próprio client, não o servidor. O servidor só precisa reagir a OnDropdownListItemSelected(id) " +
                    "e manter selectedOptionId sincronizado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("options", "List<SelectOption>", "Obrigatório. Construído com selectOption(id, label)."),
                    ShowroomParam("selectedOptionId", "String", "Obrigatório. Precisa bater com um id de options."),
                    ShowroomParam("kind", "Kind", "outlinedDropdownList() (padrão) ou filledDropdownList()."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DropdownList(
                    id = "dd_status",
                    options = listOf(
                        selectOption("active", "Ativo"),
                        selectOption("inactive", "Inativo"),
                        selectOption("pending", "Pendente"),
                    ),
                    selectedOptionId = "active",
                    kind = outlinedDropdownList(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("inactive"),
                            updates = {
                                update(tileId = "warning_text", updateData = inlineTileUpdateData("visibility" to "VISIBLE"))
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Escolha um status — cada opção dispara seu próprio trigger") {
                DropdownList(
                    id = "dropdown_demo",
                    options = listOf(
                        selectOption("active", "Ativo"),
                        selectOption("inactive", "Inativo"),
                        selectOption("pending", "Pendente"),
                    ),
                    selectedOptionId = "active",
                    kind = outlinedDropdownList(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("active"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "active"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Ativo"))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("inactive"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "inactive"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Inativo"))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("pending"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "pending"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Pendente"))
                            }
                        )
                    }
                )
                SimpleText(id = "dropdown_demo_label", text = "Status: Ativo")
            }

            ShowroomRelated(
                names = listOf("RadioButton", "TextField", "Menu"),
                destination = "tileDetails"
            )
        }
    }
}
