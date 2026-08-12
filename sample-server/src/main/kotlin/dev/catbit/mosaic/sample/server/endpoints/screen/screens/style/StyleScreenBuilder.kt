package dev.catbit.mosaic.sample.server.endpoints.screen.screens.style

import dev.catbit.mosaic.core.data.schemas.tile.style.TileModeSchema
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.color.themeColorOutlineVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiary
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.style.horizontalGradient
import dev.catbit.mosaic.server.builder.style.linearGradient
import dev.catbit.mosaic.server.builder.style.offset
import dev.catbit.mosaic.server.builder.style.radialGradient
import dev.catbit.mosaic.server.builder.style.sweepGradient
import dev.catbit.mosaic.server.builder.style.verticalGradient
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.gone
import dev.catbit.mosaic.server.builder.tile.invisible
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall
import io.ktor.server.routing.RoutingCall

/**
 * Visual identity ported from m3.material.io: dark blob hero (mirrors About/Tiles/Events/Get
 * started/Mechanisms/Extend), then each topic as a big headline + prose paragraph + optional live
 * preview + code sample directly on the page background — not boxed into small uniform cards.
 *
 * Topics cover every field of `StyleSchema` (size, margin, padding, background, border, clip,
 * windowInsets) plus the adjacent concepts a `style { }` block interacts with: visibility, theme
 * tokens and transitions.
 */
private data class StyleTopic(
    val title: String,
    val description: String,
    val code: String,
    val demo: (TileSchemaBuilderScope.() -> Unit)? = null
)

/** Small labelled swatch used by the live previews. */
private fun TileSchemaBuilderScope.DemoLabel(text: String) {
    SimpleText(
        text = text,
        typography = typographyLabelSmall(),
        color = color(themeColorOnSurfaceVariant())
    )
}

private val styleTopics = listOf(
    StyleTopic(
        title = "Anatomia do style { }",
        description = "Todo tile aceita um bloco style { } que descreve um StyleSchema. O cliente " +
            "aplica as propriedades sempre nesta ordem, e a ordem importa: o clip vem antes do " +
            "background (por isso um background já sai recortado), e o border é desenhado sobre o " +
            "background, antes do padding interno. size é o único campo obrigatório do schema; " +
            "dentro do builder ele tem default wrap/wrap, enquanto StyleSchema.default() usa fill/fill. " +
            "Todas as dimensões são Int em dp.",
        code = """
            // Ordem de aplicação no cliente (Modifier.styledWith):
            // 1. windowInsets  — afasta das system bars / IME
            // 2. margin        — espaço externo
            // 3. size          — largura e altura
            // 4. clip          — recorte de forma
            // 5. background    — preenchimento (já recortado pelo clip)
            // 6. onClick       — injetado pelo tile, não faz parte do schema
            // 7. border        — desenhado sobre o background
            // 8. padding       — espaço interno, dentro da borda

            style = {
                windowInsets(windowInsetsSystemBars())
                margin(horizontal = 16, top = 8, bottom = 8)
                size(width = fillHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 16))
                background(color(themeColorSurfaceContainer()))
                border(color = color(themeColorOutline()), thickness = 1)
                padding(horizontal = 16, vertical = 12)
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Size — largura",
        description = "fillHorizontally() ocupa toda a largura disponível e aceita um max opcional " +
            "(um teto em dp, não uma largura fixa). wrapHorizontally() encolhe até o conteúdo. " +
            "fixedHorizontally(dp) trava um valor exato. weightHorizontally(f), spanHorizontally(n) " +
            "e flexHorizontally(...) dependem do container pai — veja os tópicos adiante.",
        code = """
            size(width = fillHorizontally())            // toda a largura
            size(width = fillHorizontally(max = 400))   // toda a largura, no máximo 400dp
            size(width = wrapHorizontally())            // do tamanho do conteúdo
            size(width = fixedHorizontally(200))        // exatamente 200dp
            size(width = weightHorizontally(1f))        // só dentro de Row / FlowRow
            size(width = spanHorizontally(2))           // só dentro de Grid
            size(width = flexHorizontally(grow = 1f))   // só dentro de FlexBox
        """.trimIndent(),
        demo = {
            DemoLabel("fillHorizontally()")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorPrimaryContainer()))
                }
            ) {}
            DemoLabel("fillHorizontally(max = 160)")
            Box(
                style = {
                    size(width = fillHorizontally(max = 160), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorSecondaryContainer()))
                }
            ) {}
            DemoLabel("fixedHorizontally(96)")
            Box(
                style = {
                    size(width = fixedHorizontally(96), height = fixedVertically(28))
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorTertiaryContainer()))
                }
            ) {}
            DemoLabel("wrapHorizontally() — encolhe até o texto")
            Box(
                style = {
                    size(width = wrapHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 6))
                    background(color(themeColorErrorContainer()))
                    padding(horizontal = 12, vertical = 6)
                }
            ) {
                SimpleText(text = "conteúdo", typography = typographyLabelMedium())
            }
        }
    ),
    StyleTopic(
        title = "Size — altura",
        description = "Espelha a largura: fillVertically() (com max opcional), wrapVertically() e " +
            "fixedVertically(dp). weightVertically(f) só funciona dentro de Column, " +
            "spanVertically(n) dentro de Grid e fillRowHeight(fraction) dentro de FlowRow — esse " +
            "último faz o tile ocupar uma fração da altura da linha em que caiu.",
        code = """
            size(height = fillVertically())            // toda a altura
            size(height = fillVertically(max = 300))   // toda a altura, no máximo 300dp
            size(height = wrapVertically())            // do tamanho do conteúdo
            size(height = fixedVertically(56))         // exatamente 56dp
            size(height = weightVertically(1f))        // só dentro de Column
            size(height = spanVertically(2))           // só dentro de Grid
            size(height = fillRowHeight())             // 100% da altura da linha (FlowRow)
            size(height = fillRowHeight(0.5f))         // 50% da altura da linha (FlowRow)
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(90)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "fill", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = fixedVertically(48))
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorSecondaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "fixed 48", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(72), height = wrapVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                        padding(horizontal = 8, vertical = 8)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "wrap", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Size — weight dentro de Row e Column",
        description = "weightHorizontally(f) distribui o espaço horizontal restante entre os filhos " +
            "de um Row (ou FlowRow), proporcionalmente ao valor. weightVertically(f) faz o mesmo no " +
            "eixo vertical dentro de um Column. Fora desses containers o valor é simplesmente " +
            "ignorado — não é erro, apenas não tem efeito.",
        code = """
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) }
            ) {
                Box(style = { size(width = weightHorizontally(1f), height = fillVertically()) }) {}
                Box(style = { size(width = weightHorizontally(2f), height = fillVertically()) }) {}
                Box(style = { size(width = fixedHorizontally(64), height = fillVertically()) }) {}
            }
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(44)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "weight 1f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(2f), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorSecondaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "weight 2f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = fixedHorizontally(64), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "64dp", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Size — span (Grid) e flex (FlexBox)",
        description = "spanHorizontally(n)/spanVertically(n) fazem um item ocupar n células de um " +
            "Grid. flexHorizontally(...) expõe o modelo Flexbox completo dentro de um FlexBox: grow " +
            "(quanto cresce sobre o espaço livre), shrink (quanto encolhe na falta de espaço), basis " +
            "(tamanho base: flexBasisAuto(), flexBasisFixed(dp) ou flexBasisFraction(0f..1f)), " +
            "alignSelf (alinhamento no eixo cruzado) e order (ordem visual, independente da ordem " +
            "de declaração).",
        code = """
            // Grid — item ocupando 2 colunas
            Grid(columns = gridFixed(3)) {
                Box(style = { size(width = spanHorizontally(2), height = fixedVertically(80)) }) {}
                Box(style = { size(width = wrapHorizontally(), height = fixedVertically(80)) }) {}
            }

            // FlexBox — controle completo
            style = {
                size(
                    width = flexHorizontally(
                        grow = 1f,
                        shrink = 0f,
                        basis = flexBasisFraction(0.5f),  // ou flexBasisAuto() / flexBasisFixed(120)
                        alignSelf = flexAlignSelfCenter(), // Auto, Start, Center, End, Stretch, Baseline
                        order = 2
                    ),
                    height = wrapVertically()
                )
            }
        """.trimIndent()
    ),
    StyleTopic(
        title = "Padding — espaço interno",
        description = "padding() é o espaço entre a borda do tile e seu conteúdo: fica dentro do " +
            "background e dentro do border. Existem três overloads (horizontal/vertical, " +
            "horizontal/top/bottom e os quatro lados) — não existe overload de um argumento só: " +
            "padding(16) não compila.",
        code = """
            padding(horizontal = 16, vertical = 8)              // start/end = 16, top/bottom = 8
            padding(horizontal = 16, top = 8, bottom = 24)      // mistura
            padding(top = 8, end = 16, bottom = 8, start = 16)  // quatro lados explícitos
            // padding(16)  ← NÃO EXISTE, não compila
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeHorizontallySpacedBy(8),
                alignment = alignVerticallyToCenter()
            ) {
                Box(
                    style = {
                        size(width = wrapHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {
                    SimpleText(
                        text = "sem padding",
                        typography = typographyLabelMedium(),
                        color = color(themeColorOnPrimaryContainer())
                    )
                }
                Box(
                    style = {
                        size(width = wrapHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorTertiaryContainer()))
                        padding(horizontal = 20, vertical = 12)
                    }
                ) {
                    SimpleText(
                        text = "padding(20, 12)",
                        typography = typographyLabelMedium(),
                        color = color(themeColorOnTertiaryContainer())
                    )
                }
            }
        }
    ),
    StyleTopic(
        title = "Margin — espaço externo",
        description = "margin() é o espaço entre o tile e seus vizinhos: fica fora do background, " +
            "então a cor de fundo não pinta essa área. Tem exatamente os mesmos três overloads do " +
            "padding e também não aceita um único argumento. Repare que a margem é aplicada antes " +
            "do size, então um fillHorizontally() já desconta as margens.",
        code = """
            margin(horizontal = 24, vertical = 0)
            margin(horizontal = 24, top = 0, bottom = 8)
            margin(top = 8, end = 0, bottom = 16, start = 0)
            // margin(16)  ← NÃO EXISTE, não compila
        """.trimIndent(),
        demo = {
            Box(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 8))
                    background(color(themeColorSurfaceContainerHigh()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(40))
                        margin(horizontal = 24, top = 12, bottom = 12)
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(
                        text = "margin(horizontal = 24, top = 12, bottom = 12)",
                        typography = typographyLabelSmall(),
                        color = color(themeColorOnPrimaryContainer())
                    )
                }
            }
        }
    ),
    StyleTopic(
        title = "Background — cor sólida",
        description = "background() recebe um BackgroundSchema. O caso mais comum é a cor sólida, e " +
            "para ela existe o atalho background(color(...)), que aceita ainda um alpha opcional " +
            "(0f..1f) multiplicando a opacidade do preenchimento inteiro. Use color(themeColorX()) " +
            "para seguir o tema Material 3 (light/dark automático) e color(hex = \"#RRGGBB\") ou " +
            "color(r, g, b, alpha) quando a cor precisar ser fixa.",
        code = """
            background(color(themeColorSurfaceContainer()))            // atalho para SolidColor
            background(color(themeColorPrimary()), alpha = 0.12f)      // com transparência
            background(solidColor(color(hex = "#FF5722"), alpha = 0.5f)) // forma explícita
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(56)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 1f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()), alpha = 0.5f)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 0.5f", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(color(themeColorPrimary()), alpha = 0.15f)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "alpha 0.15f", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Background — gradientes",
        description = "Além da cor sólida, background() aceita cinco gradientes que espelham as " +
            "fábricas de Brush do Compose: verticalGradient, horizontalGradient, linearGradient " +
            "(dois pontos quaisquer), radialGradient (circular) e sweepGradient (angular, começando " +
            "às 3 horas e girando no sentido horário). Cada um tem dois overloads: uma lista de " +
            "cores distribuídas igualmente, ou pares stop-to-cor com as posições explícitas.",
        code = """
            // Cores distribuídas igualmente
            background(verticalGradient(listOf(color(themeColorPrimary()), color(themeColorTertiary()))))

            // Posições explícitas (0f..1f ao longo do eixo do gradiente)
            background(
                verticalGradient(
                    0f to color(themeColorPrimary()),
                    0.3f to color(themeColorSecondary()),
                    1f to color(themeColorTertiary())
                )
            )

            background(horizontalGradient(listOf(color(...), color(...))))
            background(linearGradient(listOf(color(...), color(...))))        // diagonal, canto a canto
            background(radialGradient(listOf(color(...), color(...))))        // círculo centrado
            background(sweepGradient(listOf(color(...), color(...), color(...))))
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            verticalGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            horizontalGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            linearGradient(
                                listOf(color(themeColorPrimary()), color(themeColorTertiary()))
                            )
                        )
                    }
                ) {}
            }
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            radialGradient(
                                listOf(color(themeColorTertiary()), color(themeColorPrimary()))
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(circleShape())
                        background(
                            sweepGradient(
                                listOf(
                                    color(themeColorPrimary()),
                                    color(themeColorTertiary()),
                                    color(themeColorPrimary())
                                )
                            )
                        )
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 8))
                        background(
                            verticalGradient(
                                0f to color(themeColorPrimary()),
                                0.3f to color(themeColorSecondaryContainer()),
                                1f to color(themeColorTertiary())
                            )
                        )
                    }
                ) {}
            }
        }
    ),
    StyleTopic(
        title = "Background — geometria e tileMode",
        description = "Toda a geometria dos gradientes é opcional e em dp. Quando omitida, vale o " +
            "default do Compose, resolvido só na hora de desenhar: end/endX/endY viram a borda " +
            "oposta, center vira o centro do tile e radius vira o maior raio que cabe. Isso é o que " +
            "permite o servidor descrever o gradiente sem conhecer o tamanho do tile. Quando o " +
            "gradiente termina antes da borda, tileMode decide o que acontece no espaço restante: " +
            "CLAMP repete a última cor, REPEATED reinicia, MIRROR espelha e DECAL deixa transparente.",
        code = """
            // Gradiente que termina em 60dp, repetindo em faixas
            background(
                horizontalGradient(
                    listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                    startX = 0,
                    endX = 60,
                    tileMode = TileModeSchema.REPEATED   // ou CLAMP / MIRROR / DECAL
                )
            )

            // Linear entre dois pontos arbitrários
            background(
                linearGradient(
                    listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                    start = offset(x = 0, y = 0),
                    end = offset(x = 120, y = 60)
                )
            )

            // Radial deslocado, com raio explícito
            background(
                radialGradient(
                    listOf(color(themeColorTertiary()), color(themeColorPrimary())),
                    center = offset(x = 40, y = 40),
                    radius = 80
                )
            )

            // offset() com eixo omitido = borda oposta (Offset.Infinite)
            background(linearGradient(listOf(color(...), color(...)), end = offset(x = 200)))

            // alpha funciona igual para gradientes
            background(verticalGradient(listOf(color(...), color(...)), alpha = 0.4f))
        """.trimIndent(),
        demo = {
            DemoLabel("horizontalGradient(endX = 60, tileMode = REPEATED)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        horizontalGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            endX = 60,
                            tileMode = TileModeSchema.REPEATED
                        )
                    )
                }
            ) {}
            DemoLabel("horizontalGradient(endX = 60, tileMode = MIRROR)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        horizontalGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            endX = 60,
                            tileMode = TileModeSchema.MIRROR
                        )
                    )
                }
            ) {}
            DemoLabel("radialGradient(center = offset(40, 24), radius = 80)")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        radialGradient(
                            listOf(color(themeColorTertiary()), color(themeColorPrimary())),
                            center = offset(x = 40, y = 24),
                            radius = 80
                        )
                    )
                }
            ) {}
            DemoLabel("linearGradient(start = offset(0, 0), end = offset(120, 48))")
            Box(
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(48))
                    clip(roundedCornerShape(all = 8))
                    background(
                        linearGradient(
                            listOf(color(themeColorPrimary()), color(themeColorTertiary())),
                            start = offset(x = 0, y = 0),
                            end = offset(x = 120, y = 48)
                        )
                    )
                }
            ) {}
        }
    ),
    StyleTopic(
        title = "Border",
        description = "Desenha uma borda sobre o background, com cor, espessura em dp e raio " +
            "opcional. Atenção: radius é um RadiusSchema, não um Int — use radius(topStart = ..., " +
            "topEnd = ..., bottomStart = ..., bottomEnd = ...). Sem radius, a borda é retangular.",
        code = """
            border(color = color(themeColorOutline()), thickness = 1)

            border(
                color = color(themeColorPrimary()),
                thickness = 2,
                radius = radius(topStart = 12, topEnd = 12, bottomStart = 12, bottomEnd = 12)
            )

            // border(color = ..., thickness = 1, radius = 12)  ← NÃO compila: radius é RadiusSchema
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(56)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(color = color(themeColorOutline()), thickness = 1)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "1dp, reto", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(
                            color = color(themeColorPrimary()),
                            thickness = 2,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "2dp, r16", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        border(
                            color = color(themeColorTertiary()),
                            thickness = 3,
                            radius = radius(topStart = 20, topEnd = 0, bottomStart = 0, bottomEnd = 20)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "cantos mistos", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Clip e Shape",
        description = "clip() recorta o tile — e, por vir antes do background na ordem de " +
            "aplicação, recorta também o preenchimento. São três formas: circleShape(), " +
            "rectangleShape() e roundedCornerShape(), esta última com overload uniforme (all), por " +
            "canto, ou recebendo um RadiusSchema pronto. Os cantos são sensíveis à direção do " +
            "layout: topStart/topEnd invertem em RTL.",
        code = """
            clip(circleShape())
            clip(rectangleShape())
            clip(roundedCornerShape(all = 16))
            clip(roundedCornerShape(topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0))
            clip(roundedCornerShape(radius = radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)))
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(64)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(64), height = fillVertically())
                        clip(circleShape())
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(rectangleShape())
                        background(color(themeColorSecondaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorTertiaryContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(topStart = 24, topEnd = 0, bottomStart = 0, bottomEnd = 24))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
            }
        }
    ),
    StyleTopic(
        title = "Clip x radius do border",
        description = "São coisas independentes e é comum confundir. clip() arredonda o conteúdo e " +
            "o background; border(radius = ...) arredonda apenas o traço da borda. Para um cartão " +
            "com fundo e borda arredondados, use os dois com o mesmo raio — só o clip deixa a borda " +
            "reta por cima do fundo arredondado, e só o border deixa o fundo vazando nos cantos.",
        code = """
            // Cartão correto: clip + border com o mesmo raio
            style = {
                clip(roundedCornerShape(all = 16))
                background(color(themeColorSurfaceContainer()))
                border(
                    color = color(themeColorOutline()),
                    thickness = 1,
                    radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                )
            }
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(64)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        background(color(themeColorSurfaceContainerHigh()))
                        border(
                            color = color(themeColorOutline()),
                            thickness = 1,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "só border", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorSurfaceContainerHigh()))
                        border(color = color(themeColorOutline()), thickness = 1)
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "só clip", typography = typographyLabelSmall())
                }
                Box(
                    style = {
                        size(width = weightHorizontally(1f), height = fillVertically())
                        clip(roundedCornerShape(all = 16))
                        background(color(themeColorSurfaceContainerHigh()))
                        border(
                            color = color(themeColorOutline()),
                            thickness = 1,
                            radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                        )
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "os dois", typography = typographyLabelSmall())
                }
            }
        }
    ),
    StyleTopic(
        title = "Window Insets",
        description = "Aplica o espaçamento das áreas do sistema como padding do tile, antes de " +
            "qualquer outra propriedade. Use na raiz da tela. Só é possível declarar um tipo de " +
            "inset por style { } — para cobrir status bar e navigation bar de uma vez, use " +
            "windowInsetsSystemBars(). Em telas com campo de texto, um container com " +
            "windowInsetsIme() acompanha o teclado.",
        code = """
            windowInsets(windowInsetsSystemBars())      // status bar + navigation bar
            windowInsets(windowInsetsStatusBar())
            windowInsets(windowInsetsNavigationBar())
            windowInsets(windowInsetsIme())             // teclado
            windowInsets(windowInsetsCaptionBar())      // barra de título (desktop / freeform)
            windowInsets(windowInsetsDisplayCutout())   // notch / furo de câmera
            windowInsets(windowInsetsWaterfall())       // bordas curvas
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
        """.trimIndent(),
        demo = {
            Row(
                style = { size(width = fillHorizontally(), height = fixedVertically(48)) },
                arrangement = arrangeHorizontallySpacedBy(8)
            ) {
                Box(
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorPrimaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "visible", typography = typographyLabelSmall())
                }
                Box(
                    visibility = invisible(),
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
                Box(
                    visibility = gone(),
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorErrorContainer()))
                    }
                ) {}
                Box(
                    style = {
                        size(width = fixedHorizontally(80), height = fillVertically())
                        clip(roundedCornerShape(all = 6))
                        background(color(themeColorTertiaryContainer()))
                    },
                    alignment = alignToCenter()
                ) {
                    SimpleText(text = "depois", typography = typographyLabelSmall())
                }
            }
            DemoLabel(
                "O buraco entre os dois blocos é o invisible() — ele ainda ocupa 80dp. " +
                    "O gone() não deixa rastro."
            )
        }
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

            // Outras formas de cor:
            color(hex = "#FF5722")            // 6 ou 8 chars (#AARRGGBB)
            color(r = 1f, g = 0f, b = 0f, alpha = 1f)

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
        topic.demo?.let { demo ->
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 16))
                    background(color(themeColorSurfaceContainer()))
                    border(
                        color = color(themeColorOutlineVariant()),
                        thickness = 1,
                        radius = radius(topStart = 16, topEnd = 16, bottomStart = 16, bottomEnd = 16)
                    )
                    padding(horizontal = 16, vertical = 16)
                },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                demo()
            }
        }
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
                            "espaçamento, aparência e insets. As seções abaixo cobrem todos os " +
                            "campos do StyleSchema, com preview ao vivo onde faz diferença ver.",
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
