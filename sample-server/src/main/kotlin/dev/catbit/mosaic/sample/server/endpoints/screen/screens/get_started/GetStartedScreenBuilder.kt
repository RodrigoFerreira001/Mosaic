package dev.catbit.mosaic.sample.server.endpoints.screen.screens.get_started

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
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
 * Teaches how to set up and run this project locally — mirrors the visual identity of
 * About/Tiles/Events (dark blob hero, colored-thumbnail cards) and reuses the same Showroom
 * building blocks (ShowroomSectionTitle/ShowroomParagraph/CodeViewer) used by every tile/event
 * detail page, so the whole app reads as one system.
 */
private data class PrerequisiteItem(
    val icon: String,
    val title: String,
    val description: String,
)

private val prerequisites = listOf(
    PrerequisiteItem("terminal", "JDK 17+", "Necessário para rodar o Gradle wrapper e compilar os módulos JVM/Android."),
    PrerequisiteItem("code", "IntelliJ IDEA ou Android Studio", "Com o plugin Kotlin Multiplatform habilitado."),
    PrerequisiteItem("smartphone", "Emulador Android (opcional)", "Só necessário se for rodar o cliente de amostra no Android em vez do Desktop."),
)

private data class ModuleItem(
    val icon: String,
    val name: String,
    val description: String,
)

private val modules = listOf(
    ModuleItem("hub", "mosaic-core", "Schemas compartilhados (@Serializable) e o MosaicSerializer polimórfico — a fonte da verdade entre servidor e cliente."),
    ModuleItem("dns", "mosaic-server", "A DSL Kotlin type-safe usada para descrever telas, tiles e events — o que um dev de backend escreve."),
    ModuleItem("devices", "mosaic-client", "Desserializa o schema e renderiza com Compose Multiplatform, além de executar a lógica dos events."),
    ModuleItem("dns", "sample-server", "Backend Ktor de referência — este app que você está navegando agora."),
    ModuleItem("smartphone", "sample-client", "Cliente Android/Desktop de referência que consome o sample-server."),
)

// Cycled per card thumbnail — same role as the varied blob/illustration colors on m3.material.io cards.
private val accents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

object GetStartedScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "get_started"

    override suspend fun RoutingCall.build() = Screen(id = "get_started") {
        Column(
            id = "get_started_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 32)
            },
            arrangement = arrangeVerticallySpacedBy(28),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as
            // About/Tiles/Events.
            Column(
                id = "get_started_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorErrorContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(90), height = fixedVertically(90))
                            clip(circleShape())
                            background(color(themeColorPrimaryContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(120), height = fixedVertically(120))
                            clip(circleShape())
                            background(color(themeColorTertiaryContainer()))
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
                        text = "Get started",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Clone o repositório, rode o servidor de amostra e um cliente, e comece a " +
                            "editar telas em Kotlin sem tocar em código de cliente.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            // Pré-requisitos
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Pré-requisitos",
                    typography = typographyHeadlineSmall(),
                    style = { padding(start = 4) }
                )
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    prerequisites.forEach { item ->
                        Row(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 16, vertical = 12)
                                clip(roundedCornerShape(all = 16))
                                background(color(themeColorSurfaceContainer()))
                            },
                            arrangement = arrangeHorizontallySpacedBy(16),
                            alignment = alignVerticallyToCenter()
                        ) {
                            Icon(icon = icon(item.icon, size = 24, color = color(themeColorOnSurface())))
                            Column(style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                                SimpleText(text = item.title, typography = typographyTitleMedium())
                                SimpleText(
                                    text = item.description,
                                    typography = typographyBodySmall(),
                                    color = color(themeColorOnSurfaceVariant())
                                )
                            }
                        }
                    }
                }
            }

            // Adicionando o Mosaic a um projeto próprio (fora deste monorepo de amostra)
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Adicionando o Mosaic ao seu projeto",
                    typography = typographyHeadlineSmall(),
                    style = { padding(start = 4) }
                )
                ShowroomParagraph(
                    "As três bibliotecas são publicadas no Maven Central sob dev.catbit. mosaic-core " +
                        "guarda os schemas compartilhados que a DSL do servidor e o renderer do cliente usam " +
                        "— declare-o explicitamente (é implementation, não api) junto com o módulo que você " +
                        "estiver consumindo."
                )
            }

            // Server setup — trecho real do README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("Backend (mosaic-server)")
                ShowroomParagraph(
                    "É toda a pegada no backend: mosaic-server é uma lib JVM comum, não exige nenhum " +
                        "servidor HTTP por conta própria — o sample-server só usa Ktor pra expor a DSL pela " +
                        "rede, por escolha própria deste projeto de amostra."
                )
                CodeViewer(
                    code = """
                        // gradle/libs.versions.toml
                        [versions]
                        mosaic = "1.0.0"

                        [libraries]
                        mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
                        mosaic-server = { module = "dev.catbit:mosaic-server", version.ref = "mosaic" }

                        // build.gradle.kts
                        dependencies {
                            implementation(libs.mosaic.core)
                            implementation(libs.mosaic.server)
                        }

                        // Ou com coordenadas diretas:
                        dependencies {
                            implementation("dev.catbit:mosaic-core:1.0.0")
                            implementation("dev.catbit:mosaic-server:1.0.0")
                        }
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Client setup — trecho real do README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("Cliente (mosaic-client)")
                ShowroomParagraph(
                    "mosaic-client compila para Android, iOS (iosArm64/iosSimulatorArm64), Desktop (jvm) e " +
                        "Web (wasmJs) — escolha os alvos que o seu módulo Compose Multiplatform já usa, nada " +
                        "extra pra configurar por plataforma. mavenCentral() precisa estar no bloco " +
                        "dependencyResolutionManagement/repositories, como qualquer outra dependência do " +
                        "Central."
                )
                CodeViewer(
                    code = """
                        // gradle/libs.versions.toml
                        [versions]
                        mosaic = "1.0.0"

                        [libraries]
                        mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
                        mosaic-client = { module = "dev.catbit:mosaic-client", version.ref = "mosaic" }

                        // build.gradle.kts (dentro do commonMain do módulo Compose Multiplatform)
                        kotlin {
                            sourceSets {
                                commonMain.dependencies {
                                    implementation(libs.mosaic.core)
                                    implementation(libs.mosaic.client)
                                }
                            }
                        }

                        // Ou com coordenadas diretas:
                        commonMain.dependencies {
                            implementation("dev.catbit:mosaic-core:1.0.0")
                            implementation("dev.catbit:mosaic-client:1.0.0")
                        }
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                ShowroomParagraph(
                    "Com a dependência adicionada, inicializar o cliente é um único composable de entrada:"
                )
                CodeViewer(
                    code = """
                        MosaicApplication(
                            applicationId = "MyApp",
                            baseUrl = "https://api.example.com",
                            appSplash = { Text("Loading…") }
                        )
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Transição: dependências (projeto próprio) → rodando este monorepo de amostra
            SimpleText(
                text = "Rodando este repositório de amostra",
                typography = typographyHeadlineSmall(),
                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(start = 4) }
            )

            // Passo 1
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("1. Rode o servidor de amostra")
                ShowroomParagraph(
                    "O sample-server é um backend Ktor comum — mosaic-server não exige um servidor HTTP por " +
                        "si só, o sample-server só usa Ktor pra expor a DSL pela rede. Ele sobe na porta 9090."
                )
                CodeViewer(
                    code = "./gradlew sample-server:run",
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Passo 2
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("2. Rode um cliente")
                ShowroomParagraph(
                    "Com o servidor no ar, rode o cliente de amostra Desktop, que já vem configurado pra " +
                        "apontar para localhost:9090. mosaic-client também compila para Android, iOS " +
                        "(iosArm64/iosSimulatorArm64) e Web (wasmJs) — o sample-client deste repositório roda " +
                        "em Android e Desktop."
                )
                CodeViewer(
                    code = "./gradlew sample-client:run",
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Estrutura do projeto
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Estrutura do projeto",
                    typography = typographyHeadlineSmall(),
                    style = { padding(start = 4) }
                )
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    modules.forEachIndexed { index, module ->
                        val (thumbColor, onThumbColor) = accents[index % accents.size]
                        Card(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                clip(roundedCornerShape(all = 16))
                                background(color(themeColorSurfaceContainer()))
                            }
                        ) {
                            Row(
                                style = {
                                    size(width = fillHorizontally(), height = wrapVertically())
                                    padding(horizontal = 16, vertical = 12)
                                },
                                arrangement = arrangeHorizontallySpacedBy(16),
                                alignment = alignVerticallyToCenter()
                            ) {
                                Box(
                                    alignment = alignToCenter(),
                                    style = {
                                        size(width = fixedHorizontally(40), height = fixedVertically(40))
                                        clip(circleShape())
                                        background(color(thumbColor))
                                    }
                                ) {
                                    Icon(icon = icon(module.icon, size = 20, color = color(onThumbColor)))
                                }
                                Column(style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                                    SimpleText(text = module.name, typography = typographyTitleMedium())
                                    SimpleText(
                                        text = module.description,
                                        typography = typographyBodySmall(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Um screen na DSL — trecho real do README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("Uma tela, na DSL")
                ShowroomParagraph(
                    "Nada nesta árvore referencia código de plataforma. O cliente que renderiza não sabe " +
                        "que \"login\" existe de antemão — ele sabe renderizar um Column, um TextField, um " +
                        "Button, e executar SendNetworkRequest → UpdateData → Navigate/DisplaySnackbar como " +
                        "um grafo de eventos encadeados, porque isso faz parte do vocabulário fixo do " +
                        "mosaic-core/mosaic-client."
                )
                CodeViewer(
                    code = """
                        Screen(id = "login") {
                            Column {
                                TextField(id = "email", label = "Email")
                                TextField(id = "password", label = "Password", visualTransformation = passwordTransformation())

                                Button(
                                    id = "submit",
                                    text = "Sign in",
                                    events = {
                                        SendNetworkRequest(
                                            trigger = EventTriggers.onClick(),
                                            url = "/api/login",
                                            method = HttpMethod.POST,
                                            events = {
                                                UpdateData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            dataSource = applicationSegmentedData("auth"),
                                                            updateData = explicitIncomingUpdateData(dataId = "session")
                                                        )
                                                    },
                                                    events = {
                                                        Navigate(trigger = EventTriggers.onSuccess(), destination = "home", navigatorId = "root")
                                                    }
                                                )
                                                DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Login failed")
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Explore o catálogo — mesmo padrão de "Next steps" usado no About
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Próximo passo",
                    typography = typographyHeadlineSmall(),
                    style = { padding(start = 4) }
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(12)
                ) {
                    Card(
                        style = {
                            size(width = weightHorizontally(1f), height = wrapVertically())
                            clip(roundedCornerShape(all = 20))
                            background(color(themeColorSurfaceContainer()))
                        },
                        events = {
                            Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "tiles")
                        }
                    ) {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(text = "Explorar Tiles", typography = typographyTitleMedium())
                            SimpleText(
                                text = "Veja os 46 tiles disponíveis, agrupados por categoria.",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                    Card(
                        style = {
                            size(width = weightHorizontally(1f), height = wrapVertically())
                            clip(roundedCornerShape(all = 20))
                            background(color(themeColorSurfaceContainer()))
                        },
                        events = {
                            Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "events")
                        }
                    ) {
                        Column(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(text = "Explorar Events", typography = typographyTitleMedium())
                            SimpleText(
                                text = "Veja os 63 events disponíveis para montar cadeias de lógica.",
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
