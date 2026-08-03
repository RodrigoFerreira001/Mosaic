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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object CheckboxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Checkbox"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Checkbox Material 3 cujo estado marcado/desmarcado é totalmente controlado pelo servidor."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Componente 100% controlado: o visual sempre volta pro valor checked que o servidor " +
                    "mandou por último. Um clique dispara OnCheck ou OnUncheck — cabe ao servidor " +
                    "responder com UpdateTiles confirmando (ou negando) a mudança."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("checked", "Boolean", "Padrão false. Controlado pelo servidor."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Checkbox(
                    id = "termsCheckbox",
                    checked = false,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onCheck(),
                            updates = {
                                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData("checked" to true))
                                update(tileId = "submitButton", updateData = inlineTileUpdateData("enabled" to true))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onUncheck(),
                            updates = {
                                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData("checked" to false))
                                update(tileId = "submitButton", updateData = inlineTileUpdateData("enabled" to false))
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Aceitar os termos libera o botão — igual a um form real") {
                Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                    Checkbox(
                        id = "checkbox_demo_terms",
                        checked = false,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onCheck(),
                                updates = {
                                    update(tileId = "checkbox_demo_terms", updateData = inlineTileUpdateData("checked" to true))
                                    update(tileId = "checkbox_demo_submit", updateData = inlineTileUpdateData("enabled" to true))
                                }
                            )
                            UpdateTiles(
                                trigger = EventTriggers.onUncheck(),
                                updates = {
                                    update(tileId = "checkbox_demo_terms", updateData = inlineTileUpdateData("checked" to false))
                                    update(tileId = "checkbox_demo_submit", updateData = inlineTileUpdateData("enabled" to false))
                                }
                            )
                        }
                    )
                    SimpleText(text = "Aceito os termos de uso")
                }
                Button(
                    id = "checkbox_demo_submit",
                    text = "Enviar",
                    enabled = false
                )
            }

            ShowroomRelated(
                names = listOf("Switch", "RadioButton", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
