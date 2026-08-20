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
import dev.catbit.mosaic.server.builder.event.builders.scroll.column.ScrollColumn
import dev.catbit.mosaic.server.builder.event.builders.scroll.column.scrollColumnToBottom
import dev.catbit.mosaic.server.builder.event.builders.scroll.column.scrollColumnToTop
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ScrollColumnTileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ScrollColumnTile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Imperatively scrolls a Column or LazyColumn to a position via broadcast — " +
                    "without relying on manually managed scroll state. Use it to scroll a list back to the " +
                    "top after reloading data, or to a specific item programmatically. If tileId doesn't " +
                    "match any Column/LazyColumn in the tree, the broadcast is silently ignored — there's no " +
                    "failure trigger."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Scroll a long list to the top and to the bottom") {
                Column(
                    id = "scroll_column_demo_list",
                    style = { size(width = fillHorizontally(), height = fixedVertically(180)) },
                    arrangement = arrangeVerticallySpacedBy(8),
                    scrollable = true
                ) {
                    (1..20).forEach { index ->
                        Row(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 12, vertical = 10)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainer()))
                            }
                        ) {
                            SimpleText(
                                text = "Item $index",
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
                        text = "Scroll to top",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollColumn(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_column_demo_list",
                                where = scrollColumnToTop(),
                                smoothly = true
                            )
                        }
                    )
                    Button(
                        text = "Scroll to bottom",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollColumn(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_column_demo_list",
                                where = scrollColumnToBottom(),
                                smoothly = true
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ScrollColumn(
                    trigger = EventTriggers.onSuccess(),
                    tileId = "result_list",
                    where = scrollColumnToTop(),
                    smoothly = true
                )
                """
            )

            ShowroomRelated(
                names = listOf("ScrollRowTile", "ScrollPagerTile"),
                destination = "eventDetails"
            )
        }
    }
}
