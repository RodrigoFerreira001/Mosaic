package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.GetTileChildrenCount
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object GetTileChildrenCountEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetTileChildrenCount"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Returns the current number of direct children of a container as an Int, " +
                    "delivered via incomingData on onSuccess. Use it when an event's logic depends on knowing " +
                    "how many items exist in a list — for example, disabling a \"checkout\" button when the " +
                    "cart is empty, or showing \"3 items selected\" above a multi-select list. An empty " +
                    "container fires onSuccess(0) normally — that's not a failure."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Add items and count how many exist now") {
                Column(
                    id = "children_count_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ChildrenCountRow("Initial item 1")
                    ChildrenCountRow("Initial item 2")
                }
                SimpleText(
                    id = "children_count_label",
                    text = "?",
                    typography = typographyTitleMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Add item",
                        events = {
                            AddTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "children_count_list",
                                position = insertAtEnd(),
                                tiles = { ChildrenCountRow("New item") }
                            )
                        }
                    )
                    Button(
                        text = "Count with GetTileChildrenCount",
                        buttonType = filledTonalButton(),
                        events = {
                            GetTileChildrenCount(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "children_count_list",
                                events = {
                                    TransformData(
                                        trigger = EventTriggers.onSuccess(),
                                        template = "<||> items in the list",
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onSuccess(),
                                                updates = {
                                                    update(
                                                        tileId = "children_count_label",
                                                        updateData = incomingTileUpdateData()
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "The TransformData above uses the template \"<||> items in the list\" — since the " +
                        "<||> placeholder isn't alone in the string, the Int returned by GetTileChildrenCount " +
                        "is coerced to text before becoming the tile's new value."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                GetTileChildrenCount(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "selected_items",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onSuccess(),
                            template = "<||>",
                            events = {
                                UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                                    update("count_label", incomingTileUpdateData())
                                })
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("CheckIfTileContainsChildren", "AddTiles", "TransformData"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ChildrenCountRow(label: String) {
    Row(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon("check_circle", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
