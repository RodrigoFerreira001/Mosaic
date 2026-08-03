package dev.catbit.mosaic.sample.server.endpoints.screen.screens.style

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import io.ktor.server.routing.RoutingCall

/**
 * Visual identity ported from m3.material.io: dark blob hero (mirrors About/Tiles/Events/Get
 * started/Mechanisms/Extend), then each topic as a big headline + prose paragraph + code sample
 * directly on the page background — not boxed into small uniform cards.
 */
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
    StyleTopic(
        title = "Visibility",
        description = "Controla se um tile é renderizado, via o parâmetro visibility (presente em " +
            "todo builder de tile, não dentro de style { }). visible() é o padrão. invisible() " +
            "esconde o tile mas mantém o espaço que ele ocupava no layout — equivalente ao " +
            "visibility: hidden do CSS. gone() remove o tile do layout por completo, como se ele " +
            "não existisse. Trocar visibility em runtime é feito via UpdateTiles.",
        code = """
            Box(
                id = "banner",
                visibility = gone(),           // ou invisible() / visible()
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) }
            )

            // Alternando em runtime:
            UpdateTiles(
                trigger = EventTriggers.onClick(),
                updates = {
                    update(tileId = "banner", updateData = inlineTileUpdateData("visibility" to "GONE"))
                }
            )
        """.trimIndent()
    ),
    StyleTopic(
        title = "Animation Transitions",
        description = "Definem como uma tela entra/sai da composição — usadas em defaultTransition/" +
            "transition de Graph e entry (navegação) e em conteúdo animado. Todas retornam um " +
            "ContentTransitionSchema e aceitam animationSpec opcional (Tween() por padrão, ou " +
            "Spring()) para ajustar duração e curva.",
        code = """
            slideInFromRightTransition()   slideInFromLeftTransition()
            slideInFromBottomTransition()  slideInFromTopTransition()
            slideOutToRightTransition()    slideOutToLeftTransition()
            slideOutToBottomTransition()   slideOutToTopTransition()
            slideHorizontalTransition()    slideVerticalTransition()
            fadeTransition()               fadeAndSlideHorizontalTransition()
            slideOverTransition()          // tela nova desliza sobre a anterior, que fica parada

            // Uso em um Graph:
            Graph(
                startEntryId = "home",
                defaultTransition = slideOverTransition(),
                defaultPopTransition = fadeTransition(animationSpec = AnimationSpecSchema.Spring()),
                entries = { /* ... */ }
            )
        """.trimIndent()
    ),
    StyleTopic(
        title = "Cores e Tipografia do Tema",
        description = "themeColorX() e typographyX() seguem os design tokens Material 3 do app — " +
            "trocam sozinhos entre light/dark e reagem a SetTheme em runtime. Prefira sempre esses " +
            "tokens a color(\"#hex\") fixo, exceto quando a cor realmente precisa ser igual nos dois modos.",
        code = """
            // Cores — todas as roles M3 disponíveis:
            themeColorPrimary()            themeColorOnPrimary()
            themeColorPrimaryContainer()   themeColorOnPrimaryContainer()
            themeColorSecondary()          themeColorOnSecondary()
            themeColorSecondaryContainer() themeColorOnSecondaryContainer()
            themeColorTertiary()           themeColorOnTertiary()
            themeColorTertiaryContainer()  themeColorOnTertiaryContainer()
            themeColorError()              themeColorOnError()
            themeColorErrorContainer()     themeColorOnErrorContainer()
            themeColorBackground()         themeColorOnBackground()
            themeColorSurface()            themeColorOnSurface()
            themeColorSurfaceVariant()     themeColorOnSurfaceVariant()
            themeColorOutline()            themeColorOutlineVariant()
            themeColorScrim()
            themeColorInverseSurface()     themeColorInverseOnSurface()   themeColorInversePrimary()
            themeColorSurfaceDim()         themeColorSurfaceBright()
            themeColorSurfaceContainerLowest()   themeColorSurfaceContainerLow()
            themeColorSurfaceContainer()         themeColorSurfaceContainerHigh()
            themeColorSurfaceContainerHighest()

            // Tipografia — as 15 roles M3:
            typographyDisplayLarge()   typographyDisplayMedium()   typographyDisplaySmall()
            typographyHeadlineLarge()  typographyHeadlineMedium()  typographyHeadlineSmall()
            typographyTitleLarge()     typographyTitleMedium()     typographyTitleSmall()
            typographyBodyLarge()      typographyBodyMedium()      typographyBodySmall()
            typographyLabelLarge()     typographyLabelMedium()     typographyLabelSmall()
        """.trimIndent()
    ),
    StyleTopic(
        title = "Tema Dinâmico (SetTheme / ResetTheme)",
        description = "SetTheme troca as cores M3 do app inteiro em runtime, definindo um par de " +
            "esquemas (light e dark) via colorsScheme(lightColorScheme, darkColorScheme), cada um " +
            "montado com colorScheme(...) — uma cor hex para cada role da tabela acima. O efeito é " +
            "global: toda tela que use themeColorX() reflete a troca imediatamente, em qualquer " +
            "tela, não só a atual. ResetTheme desfaz e volta ao tema default do app. Veja os " +
            "detalhes desses dois events na tela de Events.",
        code = """
            SetTheme(
                trigger = EventTriggers.onClick(),
                colorsScheme = colorsScheme(
                    lightColorScheme = colorScheme(
                        primary = "#6750A4", onPrimary = "#FFFFFF",
                        primaryContainer = "#EADDFF", onPrimaryContainer = "#21005D",
                        // ... uma entrada hex por role de ColorScheme
                    ),
                    darkColorScheme = colorScheme(
                        primary = "#D0BCFF", onPrimary = "#381E72",
                        primaryContainer = "#4F378B", onPrimaryContainer = "#EADDFF",
                        // ...
                    )
                )
            )

            ResetTheme(trigger = EventTriggers.onClick())
        """.trimIndent()
    ),
)

private fun TileSchemaBuilderScope.StyleTopicSection(topic: StyleTopic) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(
            text = topic.title,
            typography = typographyHeadlineSmall()
        )
        SimpleText(
            text = topic.description,
            typography = typographyBodyLarge(),
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

object StyleScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "style"

    override suspend fun RoutingCall.build() = Screen(id = "style") {
        Column(
            id = "style_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 32)
            },
            arrangement = arrangeVerticallySpacedBy(36),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as
            // About/Tiles/Events/Get started/Mechanisms/Extend.
            Column(
                id = "style_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorTertiaryContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(90), height = fixedVertically(90))
                            clip(circleShape())
                            background(color(themeColorErrorContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(120), height = fixedVertically(120))
                            clip(circleShape())
                            background(color(themeColorPrimaryContainer()))
                            margin(bottom = 8, end = 8)
                        }
                    ) {}
                }
                Column(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(horizontal = 24, top = 20, bottom = 24)
                    },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    SimpleText(
                        text = "Style",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Todo tile aceita um bloco style { } com propriedades de tamanho, " +
                            "espaçamento, aparência e insets. As seções abaixo cobrem as principais.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            styleTopics.forEach { topic ->
                StyleTopicSection(topic)
            }
        }
    }
}
