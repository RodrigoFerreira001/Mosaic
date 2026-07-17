package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

private data class StyleTopic(
    val title: String,
    val description: String,
    val code: String
)

private val styleTopics = listOf(
    StyleTopic(
        title = "Size",
        description = "Controla largura e altura. fillHorizontally()/fillVertically() preenchem o " +
            "espaço disponível (com max opcional), wrapHorizontally()/wrapVertically() ajustam ao " +
            "conteúdo, e fixedHorizontally(dp)/fixedVertically(dp) fixam um tamanho exato.",
        code = """
            style = {
                size(
                    width = fillHorizontally(max = 400),
                    height = wrapVertically()
                )
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Padding e Margin",
        description = "padding() adiciona espaço interno (entre a borda do tile e seu conteúdo); " +
            "margin() adiciona espaço externo (entre o tile e seus vizinhos). Ambos aceitam " +
            "horizontal/vertical ou top/end/bottom/start individualmente.",
        code = """
            style = {
                padding(horizontal = 16, vertical = 8)
                margin(top = 4, bottom = 4)
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Background",
        description = "Define a cor de fundo do tile. Use color(\"#hex\") para uma cor fixa ou " +
            "color(themeColorX()) para seguir o tema Material 3 (light/dark automático).",
        code = """
            style = {
                background(color(themeColorSurfaceContainer()))
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Border",
        description = "Desenha uma borda ao redor do tile, com cor, espessura e raio configuráveis.",
        code = """
            style = {
                border(
                    color = color(themeColorOutline()),
                    thickness = 1,
                    radius = radius(all = 12)
                )
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Clip e Shape",
        description = "Recorta o tile em uma forma. roundedCornerShape(16) arredonda todos os " +
            "cantos igualmente; também é possível arredondar cantos individuais, usar circleShape() " +
            "ou rectangleShape().",
        code = """
            style = {
                clip(roundedCornerShape(all = 16))
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Window Insets",
        description = "Aplica o espaçamento de system bars (status bar, navigation bar) ou IME " +
            "(teclado) diretamente como padding do tile — útil na raiz de uma tela.",
        code = """
            style = {
                windowInsets(windowInsetsSystemBars())
            }
        """.trimIndent()
    ),
)

private fun TileSchemaBuilderScope.StyleTopicSection(topic: StyleTopic) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(8)
    ) {
        SimpleText(
            text = topic.title,
            typography = typographyTitleMedium()
        )
        SimpleText(
            text = topic.description,
            typography = typographyBodyMedium(),
            color = color(themeColorOnSurfaceVariant())
        )
        CodeViewer(
            code = topic.code,
            language = CodeViewerTileSchema.Language.KOTLIN,
            theme = CodeViewerTileSchema.Theme.ATOM_ONE,
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
            }
        )
    }
}

suspend fun RoutingCall.respondStyle() = respond(
    Screen(id = "style") {
        Column(
            id = "style_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 24, top = 24, bottom = 24)
            },
            arrangement = arrangeVerticallySpacedBy(24),
            scrollable = true
        ) {
            SimpleText(
                text = "Estilizando tiles",
                typography = typographyHeadlineSmall()
            )
            SimpleText(
                text = "Todo tile aceita um bloco style { } com propriedades de tamanho, " +
                    "espaçamento, aparência e insets. Os exemplos abaixo cobrem as principais.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            styleTopics.forEach { topic ->
                StyleTopicSection(topic)
            }
        }
    }
)
