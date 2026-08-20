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
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object UpdateTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Applies data patches to already-existing tiles, without changing the tree's " +
                    "structure — changes text, color, visibility, and so on, without replacing the tile. It's " +
                    "the most-used event for reacting to interactions: changing a label's text, a TextField's " +
                    "state, a button's visibility. It accepts several updates in a single call, each targeting " +
                    "a tileId."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click the card to update its text and color") {
                Card(
                    id = "update_tiles_card",
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 16))
                        padding(horizontal = 16, vertical = 16)
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onClick(),
                            updates = {
                                update(
                                    tileId = "update_tiles_label",
                                    updateData = inlineTileUpdateData(
                                        "text" to "Updated via UpdateTiles!",
                                        "color" to color(themeColorOnPrimaryContainer())
                                    )
                                )
                                update(
                                    tileId = "update_tiles_sublabel",
                                    updateData = inlineTileUpdateData(
                                        "text" to "The tile was only patched, not recreated."
                                    )
                                )
                            }
                        )
                    }
                ) {
                    Column(arrangement = arrangeVerticallySpacedBy(4)) {
                        SimpleText(
                            id = "update_tiles_label",
                            text = "Click this card",
                            typography = typographyTitleMedium()
                        )
                        SimpleText(
                            id = "update_tiles_sublabel",
                            text = "UpdateTiles changes text and color without recreating the tile",
                            typography = typographyBodyMedium(),
                            color = color(themeColorOnSurfaceVariant())
                        )
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                UpdateTiles(
                    trigger = EventTriggers.onSuccess(),
                    updates = {
                        update(
                            tileId = "result_count",
                            updateData = inlineTileUpdateData("text" to "${'$'}count results")
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AddTiles", "ReplaceTiles", "UpdateEvents"),
                destination = "eventDetails"
            )
        }
    }
}
