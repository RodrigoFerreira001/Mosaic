package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.core.schemas.triggers.onAdaptiveNavigationItemClick
import dev.catbit.mosaic.sample.server.dsl.tiles.navigation.AdaptiveNavigation
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateClearingStack
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.initialState
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(id = "home") {
            AdaptiveNavigation(
                navigatorId = "home",
                startEntryId = "about",
                topBar = {
                    backgroundColor(color(themeColorSurfaceContainerLowest()))
                    title {
                        SimpleText(
                            text = "Mosaic",
                            typography = typographyTitleMedium()
                        )
                    }
                    actions {
                        IconButton(
                            icon = icon("search"),
                            events = {

                            }
                        )
                    }
                },
                events = {
                    HomeEntries.entries.forEach { homeEntry ->
                        NavigateClearingStack(
                            trigger = EventTriggers.onAdaptiveNavigationItemClick(homeEntry.id),
                            navigatorId = "home",
                            destination = homeEntry.id
                        )
                    }
                },
                entries = {
                    HomeEntries.entries.forEach { homeEntry ->
                        entry(
                            id = homeEntry.id,
                            icon = icon(homeEntry.iconName),
                            label = homeEntry.label,
                            initialTiles = {
                                Column(
                                    style = {
                                        size(
                                            width = fillHorizontally(),
                                            height = fillVertically()
                                        )
                                        windowInsets(windowInsetsSystemBars())
                                        background(color(themeColorSurfaceContainerLowest()))
                                    },
                                    alignment = alignHorizontallyToCenter(),
                                    arrangement = arrangeVerticallySpacedBy(
                                        space = 24,
                                        alignment = alignVerticallyToCenter()
                                    )
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            failureTiles = {
                                Column(
                                    style = {
                                        size(
                                            width = fillHorizontally(),
                                            height = fillVertically()
                                        )
                                        windowInsets(windowInsetsSystemBars())
                                        padding(
                                            horizontal = 24,
                                            bottom = 24
                                        )
                                    },
                                    alignment = alignHorizontallyToCenter(),
                                    arrangement = arrangeVerticallySpacedBy(
                                        space = 0,
                                        alignment = alignVerticallyToCenter()
                                    )
                                ) {
                                    Icon(
                                        style = {
                                            size(
                                                width = wrapHorizontally(),
                                                height = wrapVertically()
                                            )
                                            margin(bottom = 16)
                                        },
                                        icon = icon(
                                            name = "error",
                                            size = 72
                                        )
                                    )
                                    SimpleText(
                                        text = "Algo deu errado",
                                        style = {
                                            size(
                                                width = wrapHorizontally(),
                                                height = wrapVertically()
                                            )
                                            margin(bottom = 8)
                                        },
                                        typography = typographyHeadlineSmall()
                                    )
                                    SimpleText(
                                        text = "Não foi possível carregar as informações. Verifique sua conexão e tente novamente.",
                                        style = {
                                            size(
                                                width = wrapHorizontally(),
                                                height = wrapVertically()
                                            )
                                            margin(bottom = 24)
                                        },
                                        typography = typographyBodyMedium()
                                    )
                                    Button(
                                        text = "Tentar novamente",
                                        style = {
                                            size(
                                                width = fillHorizontally(max = 400),
                                                height = wrapVertically()
                                            )
                                        },
                                        events = {
                                            ChangeScreenState(
                                                trigger = EventTriggers.onClick(),
                                                state = initialState()
                                            )
                                        }
                                    )
                                }
                            },
                            initialEvents = {
                                GetScreen(
                                    trigger = EventTriggers.onDisplay(),
                                    events = {
                                        ChangeScreenState(
                                            trigger = EventTriggers.onSuccess(),
                                            state = successState()
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}

private enum class HomeEntries(
    val id: String,
    val label: String,
    val iconName: String
) {
    About(
        id = "about",
        label = "About",
        iconName = "info",
    ),
    Style(
        id = "style",
        label = "Style",
        iconName = "palette",
    ),
    Tiles(
        id = "tiles",
        label = "Tiles",
        iconName = "widgets",
    ),
    Events(
        id = "events",
        label = "Events",
        iconName = "bolt",
    ),
    Extend(
        id = "extend",
        label = "Extend",
        iconName = "extension",
    ),
    Mechanisms(
        id = "mechanisms",
        label = "Mechanisms",
        iconName = "architecture",
    ),
}
