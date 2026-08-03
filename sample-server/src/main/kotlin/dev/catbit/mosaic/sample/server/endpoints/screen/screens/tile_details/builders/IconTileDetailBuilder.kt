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
import dev.catbit.mosaic.server.builder.color.themeColorError
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorTertiary
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.icon.outlinedIcon
import dev.catbit.mosaic.server.builder.icon.roundedIcon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon

object IconTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Icon"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Image & Icon",
                description = "Renderiza um único ícone Material Symbols usando a composable Icon do Mosaic, resolvido a partir de um IconSchema — sem interação própria."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use Icon para elementos decorativos ou informativos que não respondem a toque — " +
                    "um indicador de status, um marcador ao lado de um texto, um ícone dentro de um Row. " +
                    "Para ícones clicáveis, use IconButton ou envolva o ícone em um Button. O IconSchema " +
                    "é montado com o helper icon(name, color, size, style), onde style controla a variante " +
                    "visual do Material Symbol: outlinedIcon() (padrão), roundedIcon() ou sharpIcon()."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("icon", "IconSchema", "Obrigatório. Monte com icon(\"nome\", color, size, style) — helpers outlinedIcon()/roundedIcon()/sharpIcon()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Icon(
                    id = "statusIcon",
                    icon = icon("check_circle"),
                    style = { size(width = fixedHorizontally(24), height = fixedVertically(24)) }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "4 ícones reais, cada um com nome, tamanho e cor diferentes") {
                Row(arrangement = arrangeHorizontallySpacedBy(20), alignment = alignVerticallyToCenter()) {
                    Icon(
                        icon = icon("favorite", size = 24, color = color(themeColorError()))
                    )
                    Icon(
                        icon = icon("star", size = 32, color = color(themeColorTertiary()), style = roundedIcon())
                    )
                    Icon(
                        icon = icon("verified", size = 40, color = color(themeColorPrimary()))
                    )
                    Icon(
                        icon = icon("notifications", size = 28, style = outlinedIcon())
                    )
                }
            }

            ShowroomRelated(
                names = listOf("IconButton", "Image", "AsyncImage"),
                destination = "tileDetails"
            )
        }
    }
}
