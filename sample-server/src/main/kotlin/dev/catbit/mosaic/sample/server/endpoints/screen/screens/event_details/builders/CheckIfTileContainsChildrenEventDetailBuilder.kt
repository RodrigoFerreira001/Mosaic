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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.CheckIfTileContainsChildren
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

object CheckIfTileContainsChildrenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckIfTileContainsChildren"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Synchronously checks whether a container has all of the given children — useful " +
                    "for avoiding duplicate insertion of a tile that's already been added. Use it when logic " +
                    "needs to branch depending on whether certain ids are already present in a container — for " +
                    "example, before adding a \"favorited\" item, checking whether it's already in the favorites " +
                    "list. The check only looks at direct children, not deeper descendants, and an empty " +
                    "childrenIds list always results in success."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click twice: the second time detects the duplicate") {
                Column(
                    id = "check_children_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    CheckChildrenRow(id = "check_children_seed", label = "Seed item (always present)")
                }
                Button(
                    text = "Pin \"item_42\" if it isn't already in the list",
                    buttonType = filledTonalButton(),
                    events = {
                        CheckIfTileContainsChildren(
                            trigger = EventTriggers.onClick(),
                            groupingTileId = "check_children_list",
                            childrenIds = listOf("check_children_item_42"),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onSuccess(),
                                    message = "item_42 was already in the list — nothing was added"
                                )
                                AddTiles(
                                    trigger = EventTriggers.onFailure(),
                                    groupingTileId = "check_children_list",
                                    position = insertAtEnd(),
                                    tiles = {
                                        CheckChildrenRow(id = "check_children_item_42", label = "item_42 (just pinned)")
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "The first time, item_42 doesn't exist: CheckIfTileContainsChildren fails and the " +
                        "onFailure AddTiles inserts it. The second time, it's already a direct child of the " +
                        "container: the check succeeds and only the onSuccess snackbar fires — nothing gets " +
                        "duplicated."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                CheckIfTileContainsChildren(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "pinned_list",
                    childrenIds = listOf("item_42"),
                    events = {
                        // onSuccess = already pinned; onFailure = not yet, add it
                        AddTiles(trigger = EventTriggers.onFailure(), groupingTileId = "pinned_list", tiles = { /* ... */ })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AddTiles", "GetTileChildrenCount", "RemoveTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.CheckChildrenRow(id: String, label: String) {
    Row(
        id = id,
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon("push_pin", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
