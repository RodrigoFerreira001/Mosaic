package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tiles

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.UnderConstructionBadge
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.CatalogEntry
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopEnd
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.routing.RoutingCall

private val tileCatalogEntries = listOf(
    CatalogEntry("SimpleText", "text_fields", "Text", "Displays a non-interactive text label, with typography and color fully configurable by the server."),
    CatalogEntry("Button", "smart_button", "Buttons", "Material 3 button in five visual styles — filled, elevated, tonal, outlined, or text."),
    CatalogEntry("IconButton", "touch_app", "Buttons", "Compact button with a single icon, in four Material 3 style variations."),
    CatalogEntry("FloatingActionButton", "add_circle", "Buttons", "Circular FAB with a single icon, in standard, medium, or large size, for the screen's primary action."),
    CatalogEntry("AssistChip", "label", "Chips", "Chip for smart, automated actions that span multiple apps, with optional icons."),
    CatalogEntry("FilterChip", "filter_alt", "Chips", "Chip with a toggleable selected state, used to filter a list's content."),
    CatalogEntry("InputChip", "input", "Chips", "Chip that represents a discrete piece of information chosen by the user, such as a tag or contact."),
    CatalogEntry("SuggestionChip", "lightbulb", "Chips", "Chip for displaying suggestions dynamically generated from the screen's context."),
    CatalogEntry("TextField", "edit", "Inputs", "Filled or outlined text field, with rich decoration and full keyboard configuration."),
    CatalogEntry("Checkbox", "check_box", "Inputs", "Material 3 checkbox whose checked state is fully controlled by the server."),
    CatalogEntry("RadioButton", "radio_button_checked", "Inputs", "Single radio button; several sharing the same groupId form a mutually exclusive selection group."),
    CatalogEntry("Switch", "toggle_on", "Inputs", "Material 3 on/off toggle switch with state fully controlled by the server."),
    CatalogEntry("Icon", "emoji_symbols", "Images and Icons", "Renders a single Material Symbol icon from an IconSchema."),
    CatalogEntry("Image", "image", "Images and Icons", "Displays a static image bundled with the app, resolved by resource name."),
    CatalogEntry("AsyncImage", "cloud_download", "Images and Icons", "Loads an image via Coil from a URL, raw bytes, or base64, with placeholders."),
    CatalogEntry("Column", "view_agenda", "Containers", "Stacks children vertically, with optional scrolling and search-term filtering of children."),
    CatalogEntry("LazyColumn", "list", "Containers", "Lazy vertical list that only composes visible items; each child is keyed by its id."),
    CatalogEntry("Row", "view_column", "Containers", "Positions children side by side horizontally, like a Compose Row."),
    CatalogEntry("LazyRow", "reorder", "Containers", "Lazy horizontal list that only composes items visible in the viewport."),
    CatalogEntry("Box", "layers", "Containers", "Stacks children on top of one another, useful for badges over icons or centered overlays."),
    CatalogEntry("Card", "dashboard", "Containers", "Groups content on a Material 3 surface with theme elevation, shape, and color."),
    CatalogEntry("Grid", "grid_view", "Containers", "Two-dimensional layout in the CSS grid style, with columns and rows defined as typed tracks."),
    CatalogEntry("FlexBox", "space_dashboard", "Containers", "Flexible container in the CSS flexbox style, with control over direction, alignment, and line wrapping."),
    CatalogEntry("FlowRow", "wrap_text", "Containers", "Row that automatically wraps onto new lines when children exceed the available width."),
    CatalogEntry("Pager", "swipe", "Containers", "Swipeable horizontal pager where each child occupies a full page of the screen."),
    CatalogEntry("Carousel", "view_carousel", "Containers", "Horizontal carousel of navigable cards, in multi-browse or uncontained mode."),
    CatalogEntry("Shimmer", "blur_on", "Containers", "Applies an animated shimmer effect over children while the real content is still loading."),
    CatalogEntry("PullToRefresh", "refresh", "Containers", "Wraps children with the Material 3 pull-to-refresh gesture and its spinning indicator."),
    CatalogEntry("AdaptiveVisibility", "visibility", "Containers", "Shows children only when the window meets the configured breakpoint constraints."),
    CatalogEntry("LazyTiles", "cloud_sync", "Containers", "Self-loading container that fetches its own children from a remote endpoint on first composition."),
    CatalogEntry("SelectionContainer", "highlight_alt", "Containers", "Transparent wrapper that makes text rendered by its descendants selectable and copyable."),
    CatalogEntry("TopAppBar", "web_asset", "App Bars", "Material 3 top bar in four styles: default, center-aligned, medium, or collapsible large."),
    CatalogEntry("BottomAppBar", "vertical_align_bottom", "App Bars", "Bottom bar with actions on the left and an optional FAB anchored to the right."),
    CatalogEntry("Tabs", "tab", "Navigation", "Material 3 tab row in four variants, with or without horizontal scrolling."),
    CatalogEntry("NavigationBar", "dock_to_bottom", "Navigation", "Bottom navigation bar with a fixed set of destinations, ideal for compact screens."),
    CatalogEntry("NavigationRail", "vertical_split", "Navigation", "Vertical navigation rail with optional header and footer, ideal for medium-sized screens."),
    CatalogEntry("NestedNavigationGraph", "account_tree", "Navigation", "Hosts a complete Mosaic screen back-stack embedded inside the current screen."),
    CatalogEntry("CircularProgressIndicator", "progress_activity", "Progress Indicators", "Material 3 circular indicator, determinate when a progress value is set or spinning when indeterminate."),
    CatalogEntry("LinearProgressIndicator", "linear_scale", "Progress Indicators", "Material 3 linear indicator, determinate or animated according to the progress value."),
    CatalogEntry("Badge", "notifications", "Badge", "Small colored indicator — a dot or a pill with text — for counts and notifications."),
    CatalogEntry("SearchBar", "search", "Search", "Search field with Material 3 surface styling, a leading icon, and an animated clear button."),
    CatalogEntry("Menu", "menu", "Menu", "Overlays a DropdownMenu anchored to the child content, shown or hidden via the expanded state."),
    CatalogEntry("Popup", "picture_in_picture", "Popup", "Freeform content displayed over the anchor, without a Menu's fixed item list."),
    CatalogEntry("Tooltip", "help_outline", "Popup", "Shows text on long-press/hover of an anchor tile, purely client-side with no server-controlled state."),
    CatalogEntry("DropdownList", "arrow_drop_down_circle", "Inputs", "Material 3 dropdown menu with a list of selectable options, filled or outlined."),
    CatalogEntry("DatePicker", "calendar_month", "Inputs", "Field that opens a DatePickerDialog to select a date when tapped."),
    CatalogEntry("TimePicker", "schedule", "Inputs", "Field that opens a TimePickerDialog to select a time when tapped."),
    CatalogEntry("SystemBroadcastListener", "sensors", "System", "Transparent container that listens for system broadcasts and fires events when received."),
)

// Preserves first-seen order, same grouping m3.material.io uses to label its component grid
// ("Buttons", "Navigation", "All other components") above each row of cards.
private val tileCatalogEntriesByCategory = tileCatalogEntries.groupBy { it.category }

// Cycled per card thumbnail — same role as the varied blob/illustration colors on m3.material.io cards.
private val catalogThumbAccents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

/**
 * A single catalog card in the "Components"-grid style: a full-width colored thumbnail (not a
 * small inline icon), a title, and the tile's actual one-line purpose below — mirrors
 * m3.material.io's `.thumb-container` + `.title`/`.description` card anatomy.
 */
private fun TileSchemaBuilderScope.TileCatalogCard(
    entry: CatalogEntry,
    accentIndex: Int,
    id: String = randomId(),
) {
    val (thumbColor, onThumbColor) = catalogThumbAccents[accentIndex % catalogThumbAccents.size]
    Card(
        id = id,
        style = {
            size(width = weightHorizontally(1f), height = wrapVertically())
            clip(roundedCornerShape(all = 20))
            background(color(themeColorSurfaceContainer()))
        },
        events = {
            TransformData(
                trigger = EventTriggers.onClick(),
                template = mapOf("event" to entry.name),
                events = {
                    SetIncomingDataToNetworkParamsHolderQueryParameters(
                        trigger = EventTriggers.onSuccess(),
                        events = {
                            Navigate(
                                trigger = EventTriggers.onSuccess(),
                                navigatorId = "root",
                                destination = "tileDetails"
                            )
                        }
                    )
                }
            )
        }
    ) {
        Column(
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
            }
        ) {
            Box(
                alignment = alignToCenter(),
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(72))
                    background(color(thumbColor))
                }
            ) {
                Icon(
                    icon = icon(
                        name = entry.icon,
                        size = 30,
                        color = color(onThumbColor)
                    )
                )
            }
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    padding(horizontal = 12, vertical = 10)
                },
                arrangement = arrangeVerticallySpacedBy(4)
            ) {
                SimpleText(
                    text = entry.name,
                    typography = typographyTitleMedium()
                )
                SimpleText(
                    text = entry.description,
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant())
                )
            }
        }
    }
}

object TilesScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "tiles"

    override suspend fun RoutingCall.build() = Screen(id = "tiles") {
        Column(
            id = "tiles_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 16)
            },
            arrangement = arrangeVerticallySpacedBy(16),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as the
            // colorful collage that sits beside/behind every m3.material.io hero title.
            Column(
                id = "tiles_hero",
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
                        background(color(themeColorTertiaryContainer()))
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
                        text = "Tiles",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Tiles are the interactive building blocks used to compose any screen: text, " +
                            "buttons, inputs, layout containers, navigation and progress indicators. Each one " +
                            "is described on the server as a typed schema and rendered on the client with Compose Multiplatform.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            SearchBar(
                id = "tiles_search",
                placeholder = "Search tiles",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                events = {
                    TransformData(
                        trigger = EventTriggers.onQueryChanged(),
                        template = mapOf("filterChildrenByTerm" to "<||>"),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onSuccess(),
                                updates = {
                                    // filterChildrenByTerm only filters DIRECT children, so it must be
                                    // pushed to every per-category column, not to the outer list.
                                    tileCatalogEntriesByCategory.keys.forEach { category ->
                                        update(
                                            tileId = "tiles_category_$category",
                                            updateData = incomingTileUpdateData()
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
            Column(
                id = "tiles_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                arrangement = arrangeVerticallySpacedBy(24)
            ) {
                tileCatalogEntriesByCategory.forEach { (category, entries) ->
                    Column(
                        id = "tiles_category_$category",
                        style = {
                            size(width = fillHorizontally(), height = wrapVertically())
                        },
                        arrangement = arrangeVerticallySpacedBy(8)
                    ) {
                        SimpleText(
                            text = category,
                            typography = typographyHeadlineSmall(),
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(start = 4, bottom = 4)
                            }
                        )
                        // Two cards per row, mirroring the m3.material.io component grid. Each
                        // row's searchableTerms combines both cards' terms since
                        // filterChildrenByTerm only inspects direct children of this column.
                        entries.chunked(2).forEach { pair ->
                            Row(
                                searchableTerms = pair.flatMap { listOfNotNull(it.name, it.category) },
                                style = {
                                    size(width = fillHorizontally(), height = wrapVertically())
                                },
                                arrangement = arrangeHorizontallySpacedBy(8)
                            ) {
                                pair.forEachIndexed { pairIndex, entry ->
                                    TileCatalogCard(
                                        entry = entry,
                                        accentIndex = tileCatalogEntries.indexOf(entry) + pairIndex
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
