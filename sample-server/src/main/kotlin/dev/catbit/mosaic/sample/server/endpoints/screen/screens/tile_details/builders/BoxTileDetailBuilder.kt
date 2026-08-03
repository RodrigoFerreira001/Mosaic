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
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.badges.Badge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon

object BoxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Box"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Empilha tiles filhos uns sobre os outros — badge sobre ícone, botões flutuando sobre imagem, indicador centralizado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "alignment define o alinhamento padrão dos filhos dentro do Box (2D: horizontal + " +
                    "vertical). Não suporta scroll nem expõe CompositionLocal de escopo, ao contrário de " +
                    "Column/Row."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("alignment", "TwoDimensional", "alignToTopStart() (padrão), alignToCenter(), alignToBottomEnd(), etc."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Box(id = "avatarBox", alignment = alignToCenter()) {
                    AsyncImage(id = "avatar", model = AsyncImageTileSchema.Model.Url(user.avatarUrl))
                    Badge(id = "onlineBadge", content = null)
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Badge posicionado sobre um ícone — empilhamento real via Box") {
                Box(
                    style = { size(width = fixedHorizontally(64), height = fixedVertically(64)) },
                    alignment = alignToCenter()
                ) {
                    Box(
                        style = {
                            size(width = fixedHorizontally(56), height = fixedVertically(56))
                            clip(roundedCornerShape(all = 16))
                            background(color(themeColorPrimaryContainer()))
                        },
                        alignment = alignToCenter()
                    ) {
                        Icon(icon = icon("notifications", size = 28))
                    }
                    Box(
                        style = { size(width = fixedHorizontally(64), height = fixedVertically(64)) },
                        alignment = alignToBottomEnd()
                    ) {
                        Badge(content = "3")
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Column", "Row", "Badge"),
                destination = "tileDetails"
            )
        }
    }
}
