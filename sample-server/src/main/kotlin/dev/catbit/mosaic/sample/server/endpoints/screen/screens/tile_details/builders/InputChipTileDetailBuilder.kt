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
                category = "Chips",
                description = "Chip Material 3 representando um valor discreto escolhido pelo usuário — tags, contatos, itens removíveis."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Sem variante elevated (Material 3 não define uma pro InputChip). O trailingIcon " +
                    "(geralmente um \"close\") é só visual — clicar em qualquer parte do chip dispara " +
                    "onCheck/onUncheck; é o servidor que decide remover o tile com RemoveTiles."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório."),
                    ShowroomParam("selected", "Boolean", "Padrão false."),
                    ShowroomParam("leadingIcon / trailingIcon", "IconSchema?", "trailingIcon tipicamente um ícone de fechar/remover."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
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

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque num chip pra removê-lo de verdade via RemoveTiles") {
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
                SimpleText(text = "Recarregue a página pra trazer os chips de volta")
            }

            ShowroomRelated(
                names = listOf("AssistChip", "FilterChip", "SuggestionChip"),
                destination = "tileDetails"
            )
        }
    }
}
