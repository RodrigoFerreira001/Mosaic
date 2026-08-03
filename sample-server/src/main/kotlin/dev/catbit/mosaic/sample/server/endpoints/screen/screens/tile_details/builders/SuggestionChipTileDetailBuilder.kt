package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
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
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SuggestionChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SuggestionChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Chips",
                description = "Chip Material 3 exibindo sugestões geradas dinamicamente — busca, autocomplete, respostas rápidas."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Visualmente parecido com AssistChip, mas semanticamente representa uma sugestão " +
                    "gerada (não uma ação fixa). Um uso comum: preencher um campo de busca ao tocar."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório."),
                    ShowroomParam("icon", "IconSchema?", "Opcional."),
                    ShowroomParam("variant", "Variant", "defaultSuggestionChip() (padrão) ou elevatedSuggestionChip()."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
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

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque numa sugestão — ela preenche o texto abaixo de verdade") {
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
                                            updateData = inlineTileUpdateData("text" to "Você escolheu: $suggestion")
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
                SimpleText(id = "suggestion_chip_demo_result", text = "Nenhuma sugestão escolhida ainda")
            }

            ShowroomRelated(
                names = listOf("AssistChip", "SearchBar", "InputChip"),
                destination = "tileDetails"
            )
        }
    }
}
