package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object CardTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Card"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Container Material 3 com elevação, forma e cor do tema — agrupa conteúdo relacionado com destaque visual."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "O clique fica sempre \"ligado\" no Card, mesmo sem events registrados — só não " +
                    "acontece nada visível se nenhum evento estiver escutando onClick. Internamente " +
                    "usa ColumnScope, então filhos podem usar modifiers de Column."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("kind", "Kind", "DEFAULT (padrão, preenchido), ELEVATED (sombra), OUTLINED (borda)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Card(
                    id = "env_card_${'$'}{env.id}",
                    kind = CardTileSchema.Kind.OUTLINED,
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "env_detail")
                    }
                ) {
                    SimpleText(text = env.name, typography = typographyTitleMedium())
                    SimpleText(text = env.url, color = color(themeColorOnSurfaceVariant()))
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Os 3 kinds lado a lado — todos clicáveis de verdade") {
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    listOf(
                        CardTileSchema.Kind.DEFAULT to "Default",
                        CardTileSchema.Kind.ELEVATED to "Elevated",
                        CardTileSchema.Kind.OUTLINED to "Outlined",
                    ).forEach { (kind, label) ->
                        Card(
                            kind = kind,
                            style = { size(width = weightHorizontally(1f), height = wrapVertically()) },
                            events = {
                                DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Card $label clicado")
                            }
                        ) {
                            SimpleText(
                                text = label,
                                typography = typographyTitleMedium(),
                                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(horizontal = 16, vertical = 16) }
                            )
                            SimpleText(
                                text = "kind = CardTileSchema.Kind.${kind.name}",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant()),
                                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(horizontal = 16, bottom = 16) }
                            )
                        }
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Column", "Box", "Carousel"),
                destination = "tileDetails"
            )
        }
    }
}
