package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.image.Image
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

private data class Highlight(
    val icon: String,
    val title: String,
    val description: String
)

private val highlights = listOf(
    Highlight(
        icon = "devices",
        title = "Kotlin Multiplatform",
        description = "Um único cliente roda em Android, iOS, Desktop e Web sem duplicar UI."
    ),
    Highlight(
        icon = "data_object",
        title = "DSL tipada no servidor",
        description = "Telas, tiles e events são descritos em Kotlin, com autocomplete e checagem de tipos."
    ),
    Highlight(
        icon = "block",
        title = "Zero lógica no cliente",
        description = "O app só desserializa e renderiza. Toda a lógica de negócio e navegação vive no servidor."
    ),
    Highlight(
        icon = "extension",
        title = "Extensível",
        description = "Novos tiles e events podem ser adicionados ao framework seguindo um padrão simples."
    ),
)

suspend fun RoutingCall.respondAbout() = respond(
    Screen(id = "about") {
        Column(
            id = "about_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 24, top = 32, bottom = 32)
            },
            alignment = alignHorizontallyToCenter(),
            arrangement = arrangeVerticallySpacedBy(24),
            scrollable = true
        ) {
            Image(
                id = "about_logo",
                resourceName = "ic_mosaic_logo",
                contentDescription = "Logo do Mosaic",
                style = {
                    size(width = fixedHorizontally(72), height = fixedVertically(72))
                }
            )
            Column(
                id = "about_title_block",
                style = {
                    size(width = fillHorizontally(max = 480), height = wrapVertically())
                },
                alignment = alignHorizontallyToCenter(),
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Mosaic",
                    typography = typographyHeadlineSmall(),
                    textAlign = TextAlignSchema.CENTER
                )
                SimpleText(
                    text = "Um framework de Server-Driven UI (SDUI) para Kotlin Multiplatform. " +
                        "O servidor descreve a árvore de tiles e events em Kotlin; o cliente apenas " +
                        "desserializa e renderiza com Compose Multiplatform.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant()),
                    textAlign = TextAlignSchema.CENTER
                )
            }
            Column(
                id = "about_highlights",
                style = {
                    size(width = fillHorizontally(max = 480), height = wrapVertically())
                },
                arrangement = arrangeVerticallySpacedBy(20)
            ) {
                highlights.forEach { highlight ->
                    Row(
                        style = {
                            size(width = fillHorizontally(), height = wrapVertically())
                        },
                        arrangement = arrangeHorizontallySpacedBy(16),
                        alignment = alignVerticallyToCenter()
                    ) {
                        Icon(
                            icon = icon(
                                name = highlight.icon,
                                size = 28,
                                color = color(themeColorOnSurface())
                            )
                        )
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                            },
                            arrangement = arrangeVerticallySpacedBy(2)
                        ) {
                            SimpleText(
                                text = highlight.title,
                                typography = typographyTitleMedium()
                            )
                            SimpleText(
                                text = highlight.description,
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                }
            }
        }
    }
)
