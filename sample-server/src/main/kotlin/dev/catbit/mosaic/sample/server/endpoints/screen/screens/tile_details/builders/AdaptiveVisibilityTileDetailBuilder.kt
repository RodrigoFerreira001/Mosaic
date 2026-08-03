package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.AdaptiveVisibility
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromMedium
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object AdaptiveVisibilityTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AdaptiveVisibility"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Renderiza os filhos só quando a janela satisfaz os breakpoints de largura/altura configurados — a base do design responsivo do Mosaic."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Container lógico transparente: não cria layout próprio (style não é aplicado — os " +
                    "filhos participam direto do layout do pai). Use pra mostrar um painel lateral só em " +
                    "telas largas, ou esconder um painel de detalhes em telas baixas."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("widthVisibility", "WidthVisibility", "widthVisibleFromCompact/Medium/Expanded/Large/ExtraLarge() ou widthVisibleUntil*()."),
                    ShowroomParam("heightVisibility", "HeightVisibility", "heightVisibleFromCompact/Medium/Expanded() ou heightVisibleUntil*()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                AdaptiveVisibility(
                    id = "sidePanel",
                    widthVisibility = widthVisibleFromMedium(),
                    heightVisibility = heightVisibleUntilExpanded()
                ) {
                    Column(id = "sidePanelContent") { /* painel de detalhe */ }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Este bloco só aparece em telas com largura Medium ou maior") {
                AdaptiveVisibility(widthVisibility = widthVisibleFromMedium()) {
                    Box(
                        style = {
                            size(width = fillHorizontally(), height = fixedVertically(56))
                            clip(roundedCornerShape(all = 12))
                            background(color(themeColorPrimaryContainer()))
                        }
                    ) {
                        SimpleText(text = "Visível — sua tela é Medium+", style = { padding(horizontal = 16, vertical = 16) })
                    }
                }
            }

            ShowroomNote("A demo depende do tamanho real da janela do seu device/emulador — redimensione a janela (desktop/tablet) pra ver o bloco aparecer/sumir.")

            ShowroomRelated(
                names = listOf("NavigationRail", "NavigationBar"),
                destination = "tileDetails"
            )
        }
    }
}
