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
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall

object ReloadLazyTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ReloadLazyTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Signals a LazyColumn/LazyRow to discard its content and fetch tiles again, " +
                    "resetting pagination — the correct reload for lazy lists. Use it after a data mutation " +
                    "(create/delete/update) that should make a lazy list fetch everything again from the " +
                    "start. Prefer this event over WipeTiles for LazyColumn/LazyRow lists because it lets the " +
                    "tile itself manage its own reload cycle — including the pagination reset — instead of " +
                    "just erasing the visible children."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Fire the reload broadcast on a lazy list") {
                SimpleText(
                    id = "reload_lazy_count",
                    text = "No reload triggered yet",
                    typography = typographyBodyMedium()
                )
                LazyColumn(
                    id = "reload_lazy_list",
                    style = { size(width = fillHorizontally(), height = fixedVertically(160)) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ReloadLazyRow(label = "Lazy item 1")
                    ReloadLazyRow(label = "Lazy item 2")
                    ReloadLazyRow(label = "Lazy item 3")
                }
                Button(
                    text = "Reload with ReloadLazyTiles",
                    buttonType = filledTonalButton(),
                    events = {
                        ReloadLazyTiles(
                            trigger = EventTriggers.onClick(),
                            lazyTileId = "reload_lazy_list",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "reload_lazy_count",
                                            updateData = inlineTileUpdateData("text" to "Reload broadcast sent ✓ (see the note below)")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "In this demo the LazyColumn has fixed children declared by the server, not a real " +
                        "remote data source (that would be a LazyTiles, with its own endpoint). The " +
                        "ReloadLazyTiles broadcast is really sent, but since there's nothing new to fetch, " +
                        "the visible effect here is just the scroll reset — in production, with a LazyTiles " +
                        "pointing at an endpoint, the whole content would be discarded and fetched from " +
                        "scratch."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ReloadLazyTiles(
                    trigger = EventTriggers.onSuccess(),
                    lazyTileId = "environment_list"
                )
                """
            )

            ShowroomRelated(
                names = listOf("LazyTiles", "WipeTiles", "ReplaceTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ReloadLazyRow(label: String) {
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
        Icon(icon = icon("cloud_sync", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyLabelSmall())
    }
}
