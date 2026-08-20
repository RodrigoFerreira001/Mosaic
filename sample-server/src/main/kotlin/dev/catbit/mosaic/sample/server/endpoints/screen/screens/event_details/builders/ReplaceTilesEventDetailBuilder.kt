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
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReplaceTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ReplaceTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ReplaceTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Atomically replaces an entire container's list of children with a new set " +
                    "— a wipe-then-add in a single operation. Use it when a container's whole content needs " +
                    "to change at once — swapping a tab's content, reloading a list with fresh data from the " +
                    "server. Unlike AddTiles, there's no position: the new tiles always fully replace the " +
                    "previous list."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Swap the entire content of the container below") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Set A",
                        buttonType = outlinedButton(),
                        events = {
                            ReplaceTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "replace_tiles_container",
                                tiles = {
                                    ReplaceTilesRow(icon = "looks_one", label = "Content A — item 1")
                                    ReplaceTilesRow(icon = "looks_two", label = "Content A — item 2")
                                }
                            )
                        }
                    )
                    Button(
                        text = "Set B",
                        buttonType = outlinedButton(),
                        events = {
                            ReplaceTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "replace_tiles_container",
                                tiles = {
                                    ReplaceTilesRow(icon = "star", label = "Content B — single highlight")
                                }
                            )
                        }
                    )
                }
                Column(
                    id = "replace_tiles_container",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ReplaceTilesRow(icon = "looks_one", label = "Content A — item 1")
                    ReplaceTilesRow(icon = "looks_two", label = "Content A — item 2")
                }
                ShowroomNote("Each click replaces 100% of replace_tiles_container's children — there's no position to configure, unlike AddTiles.")
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ReplaceTiles(
                    trigger = EventTriggers.onSuccess(),
                    groupingTileId = "tab_content",
                    tiles = {
                        items.forEach { item ->
                            Card(id = item.id) { /* ... */ }
                        }
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AddTiles", "WipeTiles", "ReloadLazyTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ReplaceTilesRow(icon: String, label: String) {
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
        Icon(icon = icon(icon, size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
