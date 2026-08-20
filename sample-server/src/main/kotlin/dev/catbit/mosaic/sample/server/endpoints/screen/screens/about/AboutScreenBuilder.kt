package dev.catbit.mosaic.sample.server.endpoints.screen.screens.about

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema
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
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimary
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
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
import dev.catbit.mosaic.server.builder.typography.typographyLabelLarge
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
        description = "A single client codebase runs on Android, iOS, Desktop, and Web, rendering " +
            "with Compose Multiplatform without rewriting the UI for every platform."
    ),
    Highlight(
        icon = "data_object",
        title = "Typed server-side DSL",
        description = "Screens, tiles, and events are described as a tree of Kotlin data classes, with " +
            "autocomplete and compile-time type checking — not hand-written JSON."
    ),
    Highlight(
        icon = "block",
        title = "Zero client-side logic",
        description = "The app only deserializes the schema and renders it. All business rules, " +
            "validation, and navigation decisions live and are versioned on the server."
    ),
    Highlight(
        icon = "bolt",
        title = "Updates without a release",
        description = "Since the UI is described by the server on every request, changing a flow, " +
            "fixing a string, or reordering a screen doesn't require a new app store release."
    ),
    Highlight(
        icon = "link",
        title = "Event chaining",
        description = "Events nest into chains — read data, transform it, call the network, update tiles — " +
            "with incomingData flowing from parent to child at each step."
    ),
    Highlight(
        icon = "extension",
        title = "Extensible",
        description = "New tiles and events join the framework by following a simple schema + " +
            "builder + renderer pattern, without touching what already exists."
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
                    alignment = alignToTopEnd(),
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(180))
                        background(color(themeColorPrimaryContainer()))
                    }
                ) {
                    UnderConstructionBadge()
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
                        text = "A Server-Driven UI (SDUI) framework for Kotlin Multiplatform. The server " +
                            "describes each screen as a typed tree of tiles and events in Kotlin; the client " +
                            "simply deserializes that schema and renders it with Compose Multiplatform — with " +
                            "no business logic embedded in the app.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface()),
                        textAlign = TextAlignSchema.START
                    )
                }
            }

            // Loud, standalone claim: every screen in this showroom — including this very About
            // page — is itself a real Mosaic-rendered screen, not a mockup describing the framework.
            Row(
                style = {
                    size(width = wrapHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 50))
                    background(color(themeColorPrimary()))
                    padding(horizontal = 16, vertical = 10)
                },
                arrangement = arrangeHorizontallySpacedBy(8),
                alignment = alignVerticallyToCenter()
            ) {
                Icon(icon = icon(name = "verified", size = 18, color = color(themeColorOnPrimary())))
                SimpleText(
                    text = "This entire showroom is running live on real Mosaic",
                    typography = typographyLabelLarge(),
                    color = color(themeColorOnPrimary())
                )
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
                    text = "How it works",
                    typography = typographyHeadlineSmall(),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
                SimpleText(
                    text = "On every navigation, the client asks the server for the screen definition by " +
                        "its id. The server assembles the tree of tiles (what appears on screen) and events " +
                        "(what happens when the user interacts) using the Kotlin DSL, serializes it all to " +
                        "JSON, and returns it. The client deserializes that schema and delegates each tile " +
                        "to its matching Compose renderer — Button becomes a real Button, Column becomes a " +
                        "real Column.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant()),
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(start = 4)
                    }
                )
                SimpleText(
                    text = "Interactions — a click, a network response, a typed value — trigger chained " +
                        "events: read data, transform it, call an API, update other tiles, or navigate. That " +
                        "entire chain is described on the server, so changing a flow means editing Kotlin on " +
                        "the backend, not shipping a new app version.",
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
                text = "Why Mosaic",
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
                    text = "Explore the catalog",
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
                            // Switches Home's own AdaptiveNavigation tab (navigatorId = "home", the
                            // nested graph this entry lives in) instead of pushing a redundant screen
                            // onto "root", and syncs the shell's selected tab via UpdateTiles since this
                            // switch isn't a real click on the shell's own nav item.
                            NavigateClearingStack(
                                trigger = EventTriggers.onClick(),
                                navigatorId = "home",
                                destination = "tiles"
                            )
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
                            Row(
                                arrangement = arrangeHorizontallySpacedBy(8),
                                alignment = alignVerticallyToCenter()
                            ) {
                                Icon(icon = icon(name = "grid_view", size = 20, color = color(themeColorOnSurface())))
                                SimpleText(text = "Tiles", typography = typographyTitleMedium())
                            }
                            SimpleText(
                                text = "The catalog of all 46 available tiles, grouped by category.",
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
                            NavigateClearingStack(
                                trigger = EventTriggers.onClick(),
                                navigatorId = "home",
                                destination = "events"
                            )
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
                            Row(
                                arrangement = arrangeHorizontallySpacedBy(8),
                                alignment = alignVerticallyToCenter()
                            ) {
                                Icon(icon = icon(name = "bolt", size = 20, color = color(themeColorOnSurface())))
                                SimpleText(text = "Events", typography = typographyTitleMedium())
                            }
                            SimpleText(
                                text = "The catalog of all 63 available events for composing logic chains.",
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
