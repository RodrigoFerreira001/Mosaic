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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall

object SimpleTextTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SimpleText"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Text",
                description = "Renderiza um rótulo de texto não interativo, o tile mais básico do Mosaic — usado para títulos, labels, descrições e qualquer valor exibido pelo servidor."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "SimpleText não tem estado próprio: o servidor manda o texto, cor e tipografia " +
                    "prontos, e o client só desenha. Quando color é nulo, herda a cor ambiente " +
                    "(LocalTextStyle); quando typography é nulo, idem. É o único tile de texto do " +
                    "Mosaic — não existe variante \"rich text\" ou \"markdown\"."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório. A string exibida."),
                    ShowroomParam("color", "ColorSchema?", "Opcional. Sem valor, usa a cor ambiente do texto."),
                    ShowroomParam("typography", "TypographySchema?", "Opcional. Sem valor, usa a tipografia ambiente."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SimpleText(
                    id = "greeting",
                    text = "Olá, ${'$'}{user.name}",
                    color = color(themeColorPrimary()),
                    typography = typographyTitleLarge()
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Cada clique dispara um UpdateTiles(\"text\" to ...) real") {
                SimpleText(
                    id = "simple_text_demo_target",
                    text = "Este texto é um SimpleText de verdade",
                    typography = typographyHeadlineSmall(),
                    color = color(themeColorPrimary())
                )
                Row {
                    Button(
                        text = "Mensagem 1",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "simple_text_demo_target",
                                        updateData = inlineTileUpdateData("text" to "Esse texto veio de um segundo UpdateTiles no servidor")
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Mensagem 2",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "simple_text_demo_target",
                                        updateData = inlineTileUpdateData("text" to "UpdateTiles só troca o que muda — o resto do tile continua igual")
                                    )
                                }
                            )
                        }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("Button", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
