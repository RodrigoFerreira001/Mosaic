package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.SuggestionChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.elevatedSuggestionChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SuggestionChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SuggestionChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 chip displaying dynamically generated suggestions — search, " +
                    "autocomplete, quick replies. Visually similar to AssistChip, but semantically it represents " +
                    "a generated suggestion (not a fixed action). A common use: filling a search field on tap."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap a suggestion — it really fills the text below") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    listOf("Mosaic SDUI", "Jetpack Compose", "Kotlin Multiplatform").forEach { suggestion ->
                        SuggestionChip(
                            id = "suggestion_chip_demo_${suggestion.hashCode()}",
                            text = suggestion,
                            icon = icon("lightbulb"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = {
                                        update(
                                            tileId = "suggestion_chip_demo_result",
                                            updateData = inlineTileUpdateData("text" to "You picked: $suggestion")
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
                SimpleText(id = "suggestion_chip_demo_result", text = "No suggestion picked yet")
            }

            ShowroomSectionTitle("variant, enabled = false")
            ShowroomDemoCard(title = "defaultSuggestionChip() (outlined) vs elevatedSuggestionChip()") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    SuggestionChip(text = "Elevated", icon = icon("lightbulb"), variant = elevatedSuggestionChip())
                    SuggestionChip(text = "Disabled", icon = icon("lightbulb"), enabled = false)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SuggestionChip(
                    id = "suggestion_${'$'}{suggestion.id}",
                    text = suggestion.text,
                    icon = icon("lightbulb"),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onClick(),
                            updates = {
                                update(tileId = "searchBar", updateData = inlineTileUpdateData("query" to suggestion.text))
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AssistChip", "SearchBar", "InputChip"),
                destination = "tileDetails"
            )
        }
    }
}
