package dev.catbit.mosaic.sample.server.endpoints.screen.screens.about

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Grid
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.gridColumnFraction
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.routing.RoutingCall

/**
 * Visual identity ported from m3.material.io: a dark hero with a big blobby, multi-color
 * illustration panel above the title (mirrors the site's hero + colored-shape collage), an
 * explanatory "how it works" section (mirrors "What's Material?"), a "Components"-style card grid
 * where every card carries a full-width colored thumbnail, and a "Next steps" style pair of cards
 * linking into the sample's own Tiles/Events catalogs.
 */
private data class Highlight(
    val icon: String,
    val title: String,
    val description: String
)

private val highlights = listOf(
    Highlight(
        icon = "devices",
        title = "Kotlin Multiplatform",
        description = "Um único código-fonte de cliente roda em Android, iOS, Desktop e Web renderizando " +
            "com Compose Multiplatform, sem reescrever a UI para cada plataforma."
    ),
    Highlight(
        icon = "data_object",
        title = "DSL tipada no servidor",
        description = "Telas, tiles e events são descritos como uma árvore de data classes Kotlin, com " +
            "autocomplete e checagem de tipos em tempo de compilação — não JSON escrito à mão."
    ),
    Highlight(
        icon = "block",
        title = "Zero lógica no cliente",
        description = "O app apenas desserializa o schema e renderiza. Toda regra de negócio, validação e " +
            "decisão de navegação vive e é versionada no servidor."
    ),
    Highlight(
        icon = "bolt",
        title = "Atualiza sem release",
        description = "Como a UI é descrita pelo servidor a cada requisição, mudar um fluxo, corrigir um " +
            "texto ou reordenar uma tela não exige nova versão nas lojas de aplicativo."
    ),
    Highlight(
        icon = "link",
        title = "Encadeamento de events",
        description = "Events se aninham em cadeias — ler dado, transformar, chamar rede, atualizar tiles — " +
            "com o incomingData fluindo de pai para filho a cada passo."
    ),
    Highlight(
        icon = "extension",
        title = "Extensível",
        description = "Novos tiles e events entram no framework seguindo um padrão simples de schema + " +
            "builder + renderer, sem tocar no que já existe."
    ),
)

// Cycled per highlight thumbnail — same role as the varied blob colors behind each M3 illustration.
private val highlightAccents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

object AboutScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "about"

    override suspend fun RoutingCall.build() = Screen(id = "about") {
        Column(
            id = "about_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 32)
            },
            arrangement = arrangeVerticallySpacedBy(28),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as the
            // colorful collage that sits beside/behind every m3.material.io hero title.
            Column(
                id = "about_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(180))
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(100), height = fixedVertically(100))
                            clip(circleShape())
                            background(color(themeColorTertiaryContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(130), height = fixedVertically(130))
                            clip(circleShape())
                            background(color(themeColorSecondaryContainer()))
                            margin(bottom = 8, end = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToCenter(),
                        style = {
                            size(width = fixedHorizontally(88), height = fixedVertically(88))
                            clip(circleShape())
                            background(color(themeColorErrorContainer()))
                        }
                    ) {}
                }
                Column(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(horizontal = 24, top = 24, bottom = 28)
                    },
                    arrangement = arrangeVerticallySpacedBy(10)
                ) {
                    SimpleText(
                        text = "Mosaic",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Um framework de Server-Driven UI (SDUI) para Kotlin Multiplatform. O servidor " +
                            "descreve cada tela como uma árvore tipada de tiles e events em Kotlin; o cliente " +
                            "apenas desserializa esse schema e renderiza com Compose Multiplatform — sem lógica " +
                            "de negócio embarcada no app.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface()),
                        textAlign = TextAlignSchema.START
                    )
                }
            }

            // Explanatory section, same role as "What's Material?" on m3.material.io: a real
            // paragraph, not a bullet list, before the card grid.
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Como funciona",
                    typography = typographyHeadlineSmall(),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
                SimpleText(
                    text = "A cada navegação, o cliente pede ao servidor a definição da tela pelo seu id. O " +
                        "servidor monta a árvore de tiles (o que aparece na tela) e de events (o que acontece " +
                        "quando o usuário interage) usando a DSL Kotlin, serializa tudo em JSON e devolve. O " +
                        "cliente desserializa esse schema e delega cada tile ao seu renderer Compose " +
                        "correspondente — Button vira um Button real, Column vira um Column real.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant()),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
                SimpleText(
                    text = "Interações — um clique, uma resposta de rede, um valor digitado — disparam events " +
                        "encadeados: ler dado, transformar, chamar uma API, atualizar outros tiles ou navegar. " +
                        "Essa cadeia inteira é descrita no servidor, então mudar um fluxo é editar Kotlin no " +
                        "backend, não publicar uma nova versão do app.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant()),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
            }

            // Section label, same role as m3.material.io's category titles ("Buttons", "Navigation").
            SimpleText(
                text = "Por que Mosaic",
                typography = typographyHeadlineSmall(),
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    padding(start = 4)
                }
            )

            Grid(
                id = "about_highlights",
                columns = listOf(gridColumnFraction(0.5f), gridColumnFraction(0.5f)),
                rows = emptyList(),
                columnGap = 12,
                rowGap = 12,
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                }
            ) {
                highlights.forEachIndexed { index, highlight ->
                    val (thumbColor, onThumbColor) = highlightAccents[index % highlightAccents.size]
                    Card(
                        id = "about_highlight_${index}",
                        style = {
                            size(width = fillHorizontally(), height = wrapVertically())
                            clip(roundedCornerShape(all = 24))
                            background(color(themeColorSurfaceContainer()))
                        }
                    ) {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                            }
                        ) {
                            // Full-width colored thumbnail, mirroring the M3 card's .thumb-container —
                            // not a small inline badge.
                            Box(
                                alignment = alignToCenter(),
                                style = {
                                    size(width = fillHorizontally(), height = fixedVertically(88))
                                    background(color(thumbColor))
                                }
                            ) {
                                Icon(
                                    icon = icon(
                                        name = highlight.icon,
                                        size = 36,
                                        color = color(onThumbColor)
                                    )
                                )
                            }
                            Column(
                                style = {
                                    size(width = fillHorizontally(), height = wrapVertically())
                                    padding(horizontal = 16, vertical = 16)
                                },
                                arrangement = arrangeVerticallySpacedBy(4)
                            ) {
                                SimpleText(
                                    text = highlight.title,
                                    typography = typographyTitleMedium()
                                )
                                SimpleText(
                                    text = highlight.description,
                                    typography = typographyBodySmall(),
                                    color = color(themeColorOnSurfaceVariant())
                                )
                            }
                        }
                    }
                }
            }

            // "Next steps": links into the sample's own catalogs, same role as the Get Started/Develop
            // cards that close out the m3.material.io homepage.
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Explore o catálogo",
                    typography = typographyHeadlineSmall(),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
                Row(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                    },
                    arrangement = arrangeHorizontallySpacedBy(12)
                ) {
                    Card(
                        id = "about_explore_tiles",
                        style = {
                            size(width = weightHorizontally(1f), height = wrapVertically())
                            clip(roundedCornerShape(all = 20))
                            background(color(themeColorSurfaceContainer()))
                        },
                        events = {
                            Navigate(
                                trigger = EventTriggers.onClick(),
                                navigatorId = "root",
                                destination = "tiles"
                            )
                        }
                    ) {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            Row(
                                arrangement = arrangeHorizontallySpacedBy(8),
                                alignment = alignVerticallyToCenter()
                            ) {
                                Icon(icon = icon(name = "grid_view", size = 20, color = color(themeColorOnSurface())))
                                SimpleText(text = "Tiles", typography = typographyTitleMedium())
                            }
                            SimpleText(
                                text = "Catálogo com os 46 tiles disponíveis, agrupados por categoria.",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                    Card(
                        id = "about_explore_events",
                        style = {
                            size(width = weightHorizontally(1f), height = wrapVertically())
                            clip(roundedCornerShape(all = 20))
                            background(color(themeColorSurfaceContainer()))
                        },
                        events = {
                            Navigate(
                                trigger = EventTriggers.onClick(),
                                navigatorId = "root",
                                destination = "events"
                            )
                        }
                    ) {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            Row(
                                arrangement = arrangeHorizontallySpacedBy(8),
                                alignment = alignVerticallyToCenter()
                            ) {
                                Icon(icon = icon(name = "bolt", size = 20, color = color(themeColorOnSurface())))
                                SimpleText(text = "Events", typography = typographyTitleMedium())
                            }
                            SimpleText(
                                text = "Catálogo com os 63 events disponíveis para compor cadeias de lógica.",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                }
            }
        }
    }
}
