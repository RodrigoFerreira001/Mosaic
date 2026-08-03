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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.AssistChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.defaultAssistChip

object AssistChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AssistChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Chips",
                description = "Chip Material 3 para ações inteligentes/automatizadas que atravessam apps — como \"Adicionar ao Calendário\" ou \"Compartilhar\"."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para ações disparadas por contexto, não seleção. Diferente de FilterChip/InputChip, " +
                    "AssistChip não tem estado selected — é sempre um botão de ação com aparência de chip."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório."),
                    ShowroomParam("leadingIcon / trailingIcon", "IconSchema?", "Opcionais."),
                    ShowroomParam("variant", "Variant", "defaultAssistChip() (padrão, outlined) ou elevatedAssistChip()."),
                    ShowroomParam("enabled", "Boolean", "Padrão true. Desabilitado não dispara clique."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                AssistChip(
                    id = "calendarChip",
                    text = "Add to Calendar",
                    leadingIcon = icon("calendar_today"),
                    variant = defaultAssistChip(),
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), screenId = "calendar")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque para disparar a ação de verdade") {
                AssistChip(
                    id = "assist_chip_demo",
                    text = "Adicionar ao calendário",
                    leadingIcon = icon("calendar_today"),
                    variant = defaultAssistChip(),
                    events = {
                        DisplaySnackbar(
                            trigger = EventTriggers.onClick(),
                            message = "AssistChip disparou um evento de verdade"
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("FilterChip", "InputChip", "SuggestionChip"),
                destination = "tileDetails"
            )
        }
    }
}
