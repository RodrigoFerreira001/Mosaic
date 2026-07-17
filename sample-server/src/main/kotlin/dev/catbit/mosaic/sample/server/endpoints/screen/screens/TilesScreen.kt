package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.catalog.CatalogItem
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

private val tileCatalogEntries = listOf(
    CatalogEntry("SimpleText", "text_fields", "Text"),
    CatalogEntry("Button", "smart_button", "Buttons"),
    CatalogEntry("IconButton", "touch_app", "Buttons"),
    CatalogEntry("FloatingActionButton", "add_circle", "Buttons"),
    CatalogEntry("AssistChip", "label", "Chips"),
    CatalogEntry("FilterChip", "filter_alt", "Chips"),
    CatalogEntry("InputChip", "input", "Chips"),
    CatalogEntry("SuggestionChip", "lightbulb", "Chips"),
    CatalogEntry("TextField", "edit", "Inputs"),
    CatalogEntry("Checkbox", "check_box", "Inputs"),
    CatalogEntry("RadioButton", "radio_button_checked", "Inputs"),
    CatalogEntry("Switch", "toggle_on", "Inputs"),
    CatalogEntry("Icon", "emoji_symbols", "Images and Icons"),
    CatalogEntry("Image", "image", "Images and Icons"),
    CatalogEntry("AsyncImage", "cloud_download", "Images and Icons"),
    CatalogEntry("Column", "view_agenda", "Containers"),
    CatalogEntry("LazyColumn", "list", "Containers"),
    CatalogEntry("Row", "view_column", "Containers"),
    CatalogEntry("LazyRow", "reorder", "Containers"),
    CatalogEntry("Box", "layers", "Containers"),
    CatalogEntry("Card", "dashboard", "Containers"),
    CatalogEntry("Grid", "grid_view", "Containers"),
    CatalogEntry("FlexBox", "space_dashboard", "Containers"),
    CatalogEntry("FlowRow", "wrap_text", "Containers"),
    CatalogEntry("Pager", "swipe", "Containers"),
    CatalogEntry("Carousel", "view_carousel", "Containers"),
    CatalogEntry("Shimmer", "blur_on", "Containers"),
    CatalogEntry("PullToRefresh", "refresh", "Containers"),
    CatalogEntry("AdaptiveVisibility", "visibility", "Containers"),
    CatalogEntry("LazyTiles", "cloud_sync", "Containers"),
    CatalogEntry("TopAppBar", "web_asset", "App Bars"),
    CatalogEntry("BottomAppBar", "vertical_align_bottom", "App Bars"),
    CatalogEntry("Tabs", "tab", "Navigation"),
    CatalogEntry("NavigationBar", "dock_to_bottom", "Navigation"),
    CatalogEntry("NavigationRail", "vertical_split", "Navigation"),
    CatalogEntry("NestedNavigationGraph", "account_tree", "Navigation"),
    CatalogEntry("CircularProgressIndicator", "progress_activity", "Progress Indicators"),
    CatalogEntry("LinearProgressIndicator", "linear_scale", "Progress Indicators"),
    CatalogEntry("Badge", "notifications", "Badge"),
    CatalogEntry("SearchBar", "search", "Search"),
    CatalogEntry("Menu", "menu", "Menu"),
    CatalogEntry("Popup", "picture_in_picture", "Popup"),
    CatalogEntry("DropdownList", "arrow_drop_down_circle", "Inputs"),
    CatalogEntry("DatePicker", "calendar_month", "Inputs"),
    CatalogEntry("TimePicker", "schedule", "Inputs"),
    CatalogEntry("SystemBroadcastListener", "sensors", "System"),
)

suspend fun RoutingCall.respondTiles() = respond(
    Screen(id = "tiles") {
        Column(
            id = "tiles_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 16)
            },
            arrangement = arrangeVerticallySpacedBy(12)
        ) {
            SearchBar(
                id = "tiles_search",
                placeholder = "Buscar tiles",
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
                                    update(
                                        tileId = "tiles_catalog_list",
                                        updateData = incomingTileUpdateData()
                                    )
                                }
                            )
                        }
                    )
                }
            )
            Column(
                id = "tiles_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                },
                arrangement = arrangeVerticallySpacedBy(8),
                scrollable = true
            ) {
                tileCatalogEntries.forEach { entry ->
                    CatalogItem(
                        name = entry.name,
                        iconName = entry.icon,
                        category = entry.category
                    )
                }
            }
        }
    }
)
