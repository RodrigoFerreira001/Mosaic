package dev.catbit.mosaic.sample.server.endpoints.screen.screens.get_started

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.UnderConstructionBadge
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
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateClearingStack
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopEnd
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
    PrerequisiteItem("terminal", "JDK 17+", "Required to run the Gradle wrapper and compile the JVM/Android modules."),
    PrerequisiteItem("code", "IntelliJ IDEA or Android Studio", "With the Kotlin Multiplatform plugin enabled."),
    PrerequisiteItem("smartphone", "Android emulator (optional)", "Only needed if you're running the sample client on Android instead of Desktop."),
)

private data class ModuleItem(
    val icon: String,
    val name: String,
    val description: String,
)

private val modules = listOf(
    ModuleItem("hub", "mosaic-core", "Shared schemas (@Serializable) and the polymorphic MosaicSerializer — the source of truth between server and client."),
    ModuleItem("dns", "mosaic-server", "The type-safe Kotlin DSL used to describe screens, tiles and events — what a backend dev writes."),
    ModuleItem("devices", "mosaic-client", "Deserializes the schema and renders it with Compose Multiplatform, and runs the events' logic."),
    ModuleItem("dns", "sample-server", "The reference Ktor backend — this very app you're browsing right now."),
    ModuleItem("smartphone", "sample-client", "The reference Android/Desktop client that consumes the sample-server."),
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
                    alignment = alignToTopEnd(),
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorErrorContainer()))
                    }
                ) {
                    UnderConstructionBadge()
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
                        text = "Clone the repository, run the sample server and a client, and start " +
                            "editing screens in Kotlin without touching any client code.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            // Prerequisites
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Prerequisites",
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

            // Adding Mosaic to your own project (outside this sample monorepo)
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Adding Mosaic to your project",
                    typography = typographyHeadlineSmall(),
                    style = { padding(start = 4) }
                )
                ShowroomParagraph(
                    "All three libraries are published on Maven Central under dev.catbit. mosaic-core " +
                        "holds the shared schemas that both the server DSL and the client renderer use " +
                        "— declare it explicitly (it's an implementation, not an api, dependency) alongside " +
                        "whichever module you're consuming."
                )
            }

            // Server setup — real excerpt from the README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("Backend (mosaic-server)")
                ShowroomParagraph(
                    "That's the entire backend footprint: mosaic-server is a plain JVM library, it doesn't " +
                        "require any HTTP server on its own — sample-server only uses Ktor to expose the DSL " +
                        "over the network, as this sample project's own choice."
                )
                CodeViewer(
                    code = """
                        // gradle/libs.versions.toml
                        [versions]
                        mosaic = "1.1.0"

                        [libraries]
                        mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
                        mosaic-server = { module = "dev.catbit:mosaic-server", version.ref = "mosaic" }

                        // build.gradle.kts
                        dependencies {
                            implementation(libs.mosaic.core)
                            implementation(libs.mosaic.server)
                        }

                        // Or with direct coordinates:
                        dependencies {
                            implementation("dev.catbit:mosaic-core:1.1.0")
                            implementation("dev.catbit:mosaic-server:1.1.0")
                        }
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Client setup — real excerpt from the README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("Client (mosaic-client)")
                ShowroomParagraph(
                    "mosaic-client compiles for Android, iOS (iosArm64/iosSimulatorArm64), Desktop (jvm) " +
                        "and Web (wasmJs) — pick whichever targets your Compose Multiplatform module already " +
                        "uses, nothing extra to configure per platform. mavenCentral() needs to be in the " +
                        "dependencyResolutionManagement/repositories block, like any other Central " +
                        "dependency."
                )
                CodeViewer(
                    code = """
                        // gradle/libs.versions.toml
                        [versions]
                        mosaic = "1.1.0"

                        [libraries]
                        mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
                        mosaic-client = { module = "dev.catbit:mosaic-client", version.ref = "mosaic" }

                        // build.gradle.kts (inside the Compose Multiplatform module's commonMain)
                        kotlin {
                            sourceSets {
                                commonMain.dependencies {
                                    implementation(libs.mosaic.core)
                                    implementation(libs.mosaic.client)
                                }
                            }
                        }

                        // Or with direct coordinates:
                        commonMain.dependencies {
                            implementation("dev.catbit:mosaic-core:1.1.0")
                            implementation("dev.catbit:mosaic-client:1.1.0")
                        }
                    """.trimIndent(),
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                ShowroomParagraph(
                    "With the dependency added, initializing the client is a single entry-point composable:"
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

            // Transition: dependencies (your own project) → running this sample monorepo
            SimpleText(
                text = "Running this sample repository",
                typography = typographyHeadlineSmall(),
                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(start = 4) }
            )

            // Step 1
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("1. Run the sample server")
                ShowroomParagraph(
                    "sample-server is a plain Ktor backend — mosaic-server doesn't require an HTTP server " +
                        "on its own, sample-server only uses Ktor to expose the DSL over the network. It " +
                        "starts on port 9090."
                )
                CodeViewer(
                    code = "./gradlew sample-server:run",
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Step 2
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("2. Run a client")
                ShowroomParagraph(
                    "With the server up, run the Desktop sample client, which already comes configured to " +
                        "point at localhost:9090. mosaic-client also compiles for Android, iOS " +
                        "(iosArm64/iosSimulatorArm64) and Web (wasmJs) — this repository's sample-client runs " +
                        "on Android and Desktop."
                )
                CodeViewer(
                    code = "./gradlew sample-client:run",
                    language = CodeViewerTileSchema.Language.KOTLIN,
                    theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
            }

            // Project structure
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Project structure",
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

            // A screen, in the DSL — real excerpt from the README
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                ShowroomSectionTitle("A screen, in the DSL")
                ShowroomParagraph(
                    "Nothing in this tree references platform code. The client that renders it doesn't " +
                        "know that \"login\" exists ahead of time — it knows how to render a Column, a " +
                        "TextField, a Button, and run SendNetworkRequest → UpdateData → " +
                        "Navigate/DisplaySnackbar as a graph of chained events, because that's part of the " +
                        "fixed vocabulary of mosaic-core/mosaic-client."
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

            // Same "Next steps" pattern used in About — switches Home's own AdaptiveNavigation tab
            // (navigatorId = "home", the nested graph this entry itself lives in) rather than
            // pushing a redundant screen onto "root", and syncs the shell's selected tab via
            // UpdateTiles since this switch isn't a real click on the shell's own nav item.
            Column(
                style = { size(width = fillHorizontally(), height = wrapVertically()) },
                arrangement = arrangeVerticallySpacedBy(8)
            ) {
                SimpleText(
                    text = "Next step",
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
                            NavigateClearingStack(trigger = EventTriggers.onClick(), navigatorId = "home", destination = "tiles")
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "home_adaptive_navigation",
                                        updateData = inlineTileUpdateData("selectedEntryId" to "tiles")
                                    )
                                }
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
                            SimpleText(text = "Explore Tiles", typography = typographyTitleMedium())
                            SimpleText(
                                text = "See all 46 available tiles, grouped by category.",
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
                            NavigateClearingStack(trigger = EventTriggers.onClick(), navigatorId = "home", destination = "events")
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "home_adaptive_navigation",
                                        updateData = inlineTileUpdateData("selectedEntryId" to "events")
                                    )
                                }
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
                            SimpleText(text = "Explore Events", typography = typographyTitleMedium())
                            SimpleText(
                                text = "See all 63 available events for building logic chains.",
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
