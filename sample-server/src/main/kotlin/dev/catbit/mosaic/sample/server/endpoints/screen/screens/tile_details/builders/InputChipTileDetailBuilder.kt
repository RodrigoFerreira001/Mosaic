package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.InputChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object InputChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "InputChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 chip representing a discrete value chosen by the user — tags, " +
                    "contacts, removable items. There's no elevated variant (Material 3 doesn't define one " +
                    "for InputChip). The trailingIcon (usually a \"close\") is purely visual — clicking " +
                    "anywhere on the chip fires onCheck/onUncheck; it's the server that decides to remove the " +
                    "tile with RemoveTiles."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap a chip to really remove it via RemoveTiles") {
                Row(
                    id = "input_chip_demo_container",
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    listOf("kotlin", "compose", "ktor").forEach { tag ->
                        InputChip(
                            id = "input_chip_demo_$tag",
                            text = tag,
                            trailingIcon = icon("close"),
                            selected = true,
                            events = {
                                RemoveTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    groupingTileId = "input_chip_demo_container",
                                    tileIds = listOf("input_chip_demo_$tag")
                                )
                            }
                        )
                    }
                }
                SimpleText(text = "Reload the page to bring the chips back")
            }

            ShowroomSectionTitle("selected = false, enabled = false")
            ShowroomDemoCard(title = "Unselected (unchecked look) vs disabled") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    InputChip(text = "Unselected", trailingIcon = icon("close"), selected = false)
                    InputChip(text = "Disabled", trailingIcon = icon("close"), selected = true, enabled = false)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                InputChip(
                    id = "tag_${'$'}{tag.id}",
                    text = tag.name,
                    trailingIcon = icon("close"),
                    selected = true,
                    events = {
                        RemoveTiles(
                            trigger = EventTriggers.onUncheck(),
                            groupingTileId = "tagContainer",
                            tileIds = listOf("tag_${'$'}{tag.id}")
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AssistChip", "FilterChip", "SuggestionChip"),
                destination = "tileDetails"
            )
        }
    }
}
