package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

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
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Carousel
import dev.catbit.mosaic.server.builder.tile.builders.grouping.multiBrowse
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall

object CarouselTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Carousel"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Carrossel horizontal Material 3 exibindo cards navegáveis — destaques, galerias, vitrines de produto."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "multiBrowse mostra vários itens com \"espiada\" nas bordas; uncontained usa largura " +
                    "fixa sem restringir itens parciais. O número de itens é derivado de tiles.size em " +
                    "tempo de renderização."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("type", "CarouselTypeSchema", "Obrigatório. multiBrowse(preferredItemWidth, min?, max?) ou uncontained(itemWidth)."),
                    ShowroomParam("itemSpacing / contentPadding", "Int", "Padrão 0dp."),
                    ShowroomParam("userScrollEnabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
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

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "5 cards reais em multiBrowse — arraste pro lado") {
                Carousel(
                    style = { size(width = fillHorizontally(), height = fixedVertically(120)) },
                    type = multiBrowse(preferredItemWidth = 180, minSmallItemWidth = 40, maxSmallItemWidth = 80),
                    itemSpacing = 8,
                    contentPadding = 8
                ) {
                    repeat(5) { i ->
                        Card(
                            kind = CardTileSchema.Kind.ELEVATED,
                            style = { background(color(themeColorSecondaryContainer())) }
                        ) {
                            SimpleText(
                                text = "Destaque ${i + 1}",
                                typography = typographyTitleSmall(),
                                style = { padding(horizontal = 16, vertical = 16) }
                            )
                        }
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Pager", "LazyRow", "Card"),
                destination = "tileDetails"
            )
        }
    }
}
