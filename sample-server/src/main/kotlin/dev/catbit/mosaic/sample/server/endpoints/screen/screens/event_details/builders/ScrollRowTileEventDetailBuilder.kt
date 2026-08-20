package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.ScrollRow
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.scrollRowToEnd
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.scrollRowToStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ScrollRowTileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ScrollRowTile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Imperatively scrolls a Row or LazyRow to a position via broadcast — " +
                    "horizontal navigation and carousel control. Use it for navigation in horizontal lists " +
                    "— filter chips, card carousels — when an external button (not a swipe) should move the " +
                    "scroll. If tileId doesn't match any Row/LazyRow in the tree, the broadcast is silently " +
                    "ignored."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Scroll a horizontal row to the start and to the end") {
                Row(
                    id = "scroll_row_demo_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8),
                    scrollable = true
                ) {
                    (1..15).forEach { index ->
                        Column(
                            style = {
                                size(width = fixedHorizontally(90), height = wrapVertically())
                                padding(horizontal = 12, vertical = 16)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainer()))
                            }
                        ) {
                            SimpleText(
                                text = "Chip $index",
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Scroll to start",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollRow(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_row_demo_list",
                                where = scrollRowToStart(),
                                smoothly = true
                            )
                        }
                    )
                    Button(
                        text = "Scroll to end",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollRow(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_row_demo_list",
                                where = scrollRowToEnd(),
                                smoothly = true
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ScrollRow(trigger = EventTriggers.onClick(), tileId = "chip_row", where = scrollRowToStart(), smoothly = false)
                """
            )

            ShowroomRelated(
                names = listOf("ScrollColumnTile", "ScrollPagerTile"),
                destination = "eventDetails"
            )
        }
    }
}
