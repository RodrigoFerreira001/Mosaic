package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
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
                description = "A Material 3 switch whose on/off state is fully server-controlled — same trigger " +
                    "semantics as Checkbox. Use it for on/off settings — notification preferences, user-visible " +
                    "feature flags. Like Checkbox, it's a fully controlled component: the server must confirm " +
                    "the change via UpdateTiles."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Toggle it and watch the status change live") {
                Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                    Switch(
                        id = "switch_demo_toggle",
                        checked = false,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onCheck(),
                                updates = {
                                    update(tileId = "switch_demo_toggle", updateData = inlineTileUpdateData("checked" to true))
                                    update(tileId = "switch_demo_status", updateData = inlineTileUpdateData("text" to "Notifications: on"))
                                }
                            )
                            UpdateTiles(
                                trigger = EventTriggers.onUncheck(),
                                updates = {
                                    update(tileId = "switch_demo_toggle", updateData = inlineTileUpdateData("checked" to false))
                                    update(tileId = "switch_demo_status", updateData = inlineTileUpdateData("text" to "Notifications: off"))
                                }
                            )
                        }
                    )
                    SimpleText(id = "switch_demo_status", text = "Notifications: off")
                }
            }

            ShowroomSectionTitle("enabled = false")
            ShowroomDemoCard(title = "On and off, both disabled") {
                Row(arrangement = arrangeHorizontallySpacedBy(24), alignment = alignVerticallyToCenter()) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Switch(checked = true, enabled = false)
                        SimpleText(text = "On, disabled")
                    }
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Switch(checked = false, enabled = false)
                        SimpleText(text = "Off, disabled")
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("Checkbox", "RadioButton", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
