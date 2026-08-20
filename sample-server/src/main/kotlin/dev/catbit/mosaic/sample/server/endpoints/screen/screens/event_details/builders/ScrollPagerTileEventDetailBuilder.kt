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
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.ScrollPager
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToNextPage
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToPreviousPage
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Pager
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.pageFill
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object ScrollPagerTileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ScrollPagerTile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Imperatively navigates a Pager to a target page via broadcast — programmatic " +
                    "tab/page switching, without relying on a swipe. Use it for button-driven page/tab " +
                    "switching — advancing an onboarding flow, going back a page in a step carousel. If " +
                    "tileId doesn't match any Pager in the tree, the broadcast is silently ignored."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Move pages forward and back with buttons, no dragging") {
                Pager(
                    id = "scroll_pager_demo",
                    pageSize = pageFill(),
                    style = { size(width = fillHorizontally(), height = fixedVertically(120)) }
                ) {
                    (1..3).forEach { pageNumber ->
                        Box(
                            alignment = alignToCenter(),
                            style = {
                                size(width = fillHorizontally(), height = fillVertically())
                                clip(roundedCornerShape(all = 16))
                                background(color(themeColorSecondaryContainer()))
                            }
                        ) {
                            SimpleText(
                                text = "Page $pageNumber",
                                typography = typographyTitleMedium(),
                                color = color(themeColorOnSecondaryContainer())
                            )
                        }
                    }
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Previous",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollPager(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_pager_demo",
                                where = scrollPageToPreviousPage(),
                                smoothly = true
                            )
                        }
                    )
                    Button(
                        text = "Next",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollPager(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_pager_demo",
                                where = scrollPageToNextPage(),
                                smoothly = true
                            )
                        }
                    )
                }
                ShowroomNote(
                    "scrollPageToNextPage()/scrollPageToPreviousPage() do nothing (silently) when already " +
                        "on the last/first page — there's no wrap-around."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ScrollPager(trigger = EventTriggers.onClick(), tileId = "onboarding_pager", where = scrollPageToNextPage(), smoothly = true)
                """
            )

            ShowroomRelated(
                names = listOf("ScrollColumnTile", "ScrollRowTile", "Pager"),
                destination = "eventDetails"
            )
        }
    }
}
