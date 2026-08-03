package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

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
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.SelectionContainer
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelLarge
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object SelectionContainerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SelectionContainer"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Envolve tiles filhos no SelectionContainer do Compose, permitindo selecionar e copiar texto entre múltiplos tiles como uma seleção contígua."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Não tem arrangement/alignment nem layout próprio — é um wrapper transparente. " +
                    "Não dispara onClick/onLongPress/onDisplay: o long-press fica livre pro gesto de " +
                    "seleção de texto do próprio Compose."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Os tiles cujo texto fica selecionável em conjunto."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SelectionContainer(id = "profile_details") {
                    SimpleText(id = "profile_name", text = user.name)
                    SimpleText(id = "profile_email", text = user.email)
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Segure e arraste pra selecionar texto entre os dois SimpleText abaixo") {
                SelectionContainer {
                    Column(arrangement = arrangeVerticallySpacedBy(4)) {
                        SimpleText(text = "Mosaic SDUI Framework", typography = typographyTitleMedium())
                        SimpleText(
                            text = "showroom@mosaic.dev — copie este texto junto com o título acima",
                            typography = typographyLabelLarge(),
                            color = color(themeColorOnSurfaceVariant())
                        )
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Column", "SimpleText"),
                destination = "tileDetails"
            )
        }
    }
}
