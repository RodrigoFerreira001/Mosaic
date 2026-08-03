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
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Switch
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SwitchTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Switch"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Switch Material 3 cujo estado ligado/desligado é totalmente controlado pelo servidor — mesma semântica de triggers do Checkbox."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para configurações on/off — preferências de notificação, feature flags visíveis " +
                    "ao usuário. Assim como Checkbox, é um componente totalmente controlado: o servidor " +
                    "precisa confirmar a mudança via UpdateTiles."
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
                Switch(
                    id = "notificationsSwitch",
                    checked = user.notificationsEnabled,
                    events = {
                        UpdateData(
                            trigger = EventTriggers.onCheck(),
                            updates = {
                                update(dataSource = screenPlainData(), updateData = inlineUpdateData("notifications_enabled" to true))
                            }
                        )
                        UpdateData(
                            trigger = EventTriggers.onUncheck(),
                            updates = {
                                update(dataSource = screenPlainData(), updateData = inlineUpdateData("notifications_enabled" to false))
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Alterne e veja o status mudar ao vivo") {
                Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                    Switch(
                        id = "switch_demo_toggle",
                        checked = false,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onCheck(),
                                updates = {
                                    update(tileId = "switch_demo_toggle", updateData = inlineTileUpdateData("checked" to true))
                                    update(tileId = "switch_demo_status", updateData = inlineTileUpdateData("text" to "Notificações: ativadas"))
                                }
                            )
                            UpdateTiles(
                                trigger = EventTriggers.onUncheck(),
                                updates = {
                                    update(tileId = "switch_demo_toggle", updateData = inlineTileUpdateData("checked" to false))
                                    update(tileId = "switch_demo_status", updateData = inlineTileUpdateData("text" to "Notificações: desativadas"))
                                }
                            )
                        }
                    )
                    SimpleText(id = "switch_demo_status", text = "Notificações: desativadas")
                }
            }

            ShowroomRelated(
                names = listOf("Checkbox", "RadioButton", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
