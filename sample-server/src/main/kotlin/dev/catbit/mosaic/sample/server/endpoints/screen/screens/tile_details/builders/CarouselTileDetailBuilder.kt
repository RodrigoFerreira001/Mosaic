package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.ScrollPager
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToNextPage
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToPreviousPage
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Carousel
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.multiBrowse
import dev.catbit.mosaic.server.builder.tile.builders.grouping.uncontained
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall

object CarouselTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Carousel"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 horizontal carousel showing navigable cards — highlights, " +
                    "galleries, product showcases. multiBrowse shows several items with a \"peek\" at the " +
                    "edges; uncontained uses a fixed width without constraining partial items. The item count " +
                    "is derived from tiles.size at render time. OnPageChanged carries the new item index as " +
                    "incomingData, same as Pager, and the two tiles share the same scroll-control broadcast " +
                    "(ScrollPager targets either one by id)."
            )

            ShowroomSectionTitle("type — multiBrowse vs uncontained")
            ShowroomDemoCard(title = "multiBrowse(preferredItemWidth = 180) — shrunken edge items peek in") {
                Carousel(
                    style = { size(width = fillHorizontally(), height = fixedVertically(120)) },
                    type = multiBrowse(preferredItemWidth = 180, minSmallItemWidth = 40, maxSmallItemWidth = 80),
                    itemSpacing = 8,
                    contentPadding = 8
                ) {
                    repeat(5) { i -> HighlightCard(i, themeColorSecondaryContainer()) }
                }
            }
            ShowroomDemoCard(title = "uncontained(itemWidth = 200) — fixed width, no edge shrinking") {
                Carousel(
                    style = { size(width = fillHorizontally(), height = fixedVertically(120)) },
                    type = uncontained(itemWidth = 200),
                    itemSpacing = 8,
                    contentPadding = 8
                ) {
                    repeat(5) { i -> HighlightCard(i, themeColorTertiaryContainer()) }
                }
            }

            ShowroomSectionTitle("userScrollEnabled = false — driven entirely by ScrollPager")
            ShowroomDemoCard(title = "Swiping is disabled — use the buttons below") {
                Carousel(
                    id = "carousel_controlled_demo",
                    style = { size(width = fillHorizontally(), height = fixedVertically(100)) },
                    type = uncontained(itemWidth = 160),
                    itemSpacing = 8,
                    contentPadding = 8,
                    userScrollEnabled = false,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Any),
                            updates = {
                                update(
                                    tileId = "carousel_controlled_indicator",
                                    updateData = mappedIncomingTileUpdateData("text" to "Current index: <||>")
                                )
                            }
                        )
                    }
                ) {
                    repeat(4) { i -> HighlightCard(i, themeColorSecondaryContainer()) }
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeSpaceBetween(),
                    alignment = alignVerticallyToCenter()
                ) {
                    IconButton(
                        icon = icon("chevron_left"),
                        events = { ScrollPager(trigger = EventTriggers.onClick(), tileId = "carousel_controlled_demo", where = scrollPageToPreviousPage()) }
                    )
                    SimpleText(
                        id = "carousel_controlled_indicator",
                        text = "Current index: 0",
                        typography = typographyLabelMedium(),
                        color = color(themeColorOnSurfaceVariant())
                    )
                    IconButton(
                        icon = icon("chevron_right"),
                        events = { ScrollPager(trigger = EventTriggers.onClick(), tileId = "carousel_controlled_demo", where = scrollPageToNextPage()) }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Carousel(
                    id = "featuredCarousel",
                    type = multiBrowse(preferredItemWidth = 240, minSmallItemWidth = 40, maxSmallItemWidth = 80),
                    itemSpacing = 8,
                    contentPadding = 16
                ) {
                    featured.forEach { item ->
                        Card(id = "featured_${'$'}{item.id}", kind = CardTileSchema.Kind.ELEVATED) {
                            SimpleText(text = item.title, typography = typographyTitleSmall())
                        }
                    }
                }
                """
            )

            ShowroomRelated(
                names = listOf("Pager", "LazyRow", "Card"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.HighlightCard(index: Int, containerColor: ColorSchema.Theme.Color) {
    Card(
        kind = CardTileSchema.Kind.ELEVATED,
        style = { background(color(containerColor)) }
    ) {
        SimpleText(
            text = "Highlight ${index + 1}",
            typography = typographyTitleSmall(),
            style = { padding(horizontal = 16, vertical = 16) }
        )
    }
}
