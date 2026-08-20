package dev.catbit.mosaic.sample.server.endpoints.screen.screens.events

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

private val eventCatalogEntries = listOf(
    CatalogEntry("AddTiles", "add_box", "Tile Management", "Inserts one or more new tiles into a container's child list, at a specific position, without reloading the screen."),
    CatalogEntry("RemoveTiles", "delete", "Tile Management", "Removes specific tiles from a container's child list by their ids."),
    CatalogEntry("UpdateTiles", "sync", "Tile Management", "Applies data patches to existing tiles without changing the tree structure."),
    CatalogEntry("ReplaceTiles", "swap_horiz", "Tile Management", "Atomically replaces a container's entire child list with a new set."),
    CatalogEntry("WipeTiles", "delete_sweep", "Tile Management", "Removes all children of a container in a single operation, leaving it empty."),
    CatalogEntry("ReloadLazyTiles", "autorenew", "Tile Management", "Signals a LazyColumn/LazyRow to discard its content and fetch tiles again, resetting pagination."),
    CatalogEntry("CheckIfTileContainsChildren", "checklist", "Tile Management", "Synchronously checks whether a container holds all the specified children."),
    CatalogEntry("GetTileChildrenCount", "pin", "Tile Management", "Returns the current number of direct children of a container."),
    CatalogEntry("RunEvents", "playlist_play", "Tile Management", "Runs all child events unconditionally, grouping a chain under a single trigger."),
    CatalogEntry("UpdateEvents", "edit_note", "Tile Management", "Patches the incomingData of already-registered events without a network round-trip."),
    CatalogEntry("Navigate", "arrow_forward", "Navigation", "Pushes a destination onto a navigator's stack, optionally popping earlier entries and carrying data."),
    CatalogEntry("NavigateClearingStack", "restart_alt", "Navigation", "Navigates to a destination while clearing the entire current navigation stack."),
    CatalogEntry("NavigateUp", "arrow_back", "Navigation", "Removes the current destination from the stack, equivalent to the system back button."),
    CatalogEntry("GetScreen", "download", "Screen", "Fetches a screen definition from the server and exposes the result as incomingData."),
    CatalogEntry("RefreshScreen", "refresh", "Screen", "Reloads the current screen from scratch, automatically fetching and applying the new definition."),
    CatalogEntry("ChangeScreenState", "tune", "Screen", "Immediately transitions the screen to Success, Failure, or Initial without a network call."),
    CatalogEntry("SendData", "outbox", "Data", "Publishes a value onto the DataMailer bus, available to other screens via CheckForReceivedData."),
    CatalogEntry("CheckForReceivedData", "inbox", "Data", "Reads a value off the DataMailer bus and forwards it as incomingData."),
    CatalogEntry("GetData", "download_for_offline", "Data", "Reads data from one or more sources — tile, screen memory, persistent database — and aggregates the result."),
    CatalogEntry("UpdateData", "save", "Data", "Writes key-value data to one or more data stores, in-memory or persistent."),
    CatalogEntry("RemoveData", "delete_forever", "Data", "Deletes data from a data store — a single key, a batch of keys, or the entire store."),
    CatalogEntry("ProcessData", "memory", "Data", "Delegates the incomingData to a DataProcessor registered on the client for platform-native logic."),
    CatalogEntry("TransformData", "transform", "Data", "Reshapes the incomingData by substituting placeholders with values resolved from it."),
    CatalogEntry("EvaluateData", "rule", "Data", "Evaluates a boolean expression tree and branches the chain between onSuccess and onFailure."),
    CatalogEntry("SendNetworkRequest", "cloud", "Networking", "Performs an HTTP request and propagates the response through the child events."),
    CatalogEntry("DownloadFile", "cloud_download", "Networking", "Downloads a file from a URL directly to the device's public storage."),
    CatalogEntry("DownloadFileToDisk", "save_alt", "Networking", "Downloads a file from a URL into the app's own private storage."),
    CatalogEntry("DownloadFileToMemory", "memory", "Networking", "Downloads a file from a URL entirely in memory, without touching the filesystem."),
    CatalogEntry("UploadFile", "cloud_upload", "Networking", "Uploads a PlatformFile to a URL, typically a pre-signed storage URL."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderBody", "data_object", "Networking", "Stores the incomingData as the body of the chain's next network request."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderHeaders", "list_alt", "Networking", "Stores the incomingData as the headers of the chain's next network request."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderUrl", "link", "Networking", "Stores the incomingData as the URL of the chain's next network request."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderQueryParameters", "filter_list", "Networking", "Stores the incomingData as the query parameters of the chain's next network request."),
    CatalogEntry("SaveFile", "save", "File System", "Saves data to a local file on the device."),
    CatalogEntry("GetFile", "folder_open", "File System", "Reads a locally stored file, shaped by the configured outputType."),
    CatalogEntry("DeleteFile", "delete", "File System", "Deletes a locally stored file identified by name."),
    CatalogEntry("OpenFilePicker", "attach_file", "File System", "Opens the system file picker for the user to choose a file."),
    CatalogEntry("TakePicture", "photo_camera", "File System", "Opens the device camera for the user to take a photo."),
    CatalogEntry("GetImageFromGallery", "photo_library", "File System", "Opens the device gallery for the user to choose an image."),
    CatalogEntry("DisplayDialog", "open_in_new", "Overlays", "Shows a modal dialog with a tile tree defined by the server."),
    CatalogEntry("DismissDialog", "close", "Overlays", "Programmatically closes the currently displayed dialog."),
    CatalogEntry("DisplayModalBottomSheet", "vertical_align_bottom", "Overlays", "Shows a modal bottom sheet with a tile tree defined by the server."),
    CatalogEntry("DismissModalBottomSheet", "expand_more", "Overlays", "Closes the modal bottom sheet with the given id."),
    CatalogEntry("DisplayBottomSheet", "bottom_panel_open", "Overlays", "Shows a non-modal bottom sheet, without a scrim, that coexists with the screen behind it."),
    CatalogEntry("DismissBottomSheet", "bottom_panel_close", "Overlays", "Closes the non-modal bottom sheet with the given id."),
    CatalogEntry("DisplayNavigationDrawer", "menu_open", "Overlays", "Opens the screen's side navigation drawer."),
    CatalogEntry("DismissNavigationDrawer", "menu", "Overlays", "Closes the screen's side navigation drawer."),
    CatalogEntry("DisplaySnackbar", "chat_bubble", "Overlays", "Shows a Material 3 snackbar with a message, optional action, and configurable duration."),
    CatalogEntry("DismissSnackbar", "cancel", "Overlays", "Programmatically closes the currently displayed snackbar."),
    CatalogEntry("TriggerEvent", "bolt", "Events / Meta", "Locates another registered event by id and runs it inline, like a reusable subroutine."),
    CatalogEntry("RunCancellableEvents", "play_circle", "Events / Meta", "Runs an event chain that can be interrupted by a matching CancelEvents."),
    CatalogEntry("CancelEvents", "cancel", "Events / Meta", "Interrupts execution of a cancellable event chain started by RunCancellableEvents."),
    CatalogEntry("ToggleMenu", "more_vert", "Menu", "Toggles the open/closed state of a Menu tile."),
    CatalogEntry("TogglePopup", "picture_in_picture", "Popup", "Toggles the open/closed state of a Popup tile."),
    CatalogEntry("StartCountdownTimer", "timer", "Time", "Starts a countdown on the client, firing triggers every second and when it reaches zero."),
    CatalogEntry("StartTimeLoop", "loop", "Time", "Starts a recurring time loop on the client, firing triggers at configured intervals."),
    CatalogEntry("ScrollColumnTile", "swap_vert", "Scroll", "Imperatively scrolls a Column or LazyColumn to a position via broadcast."),
    CatalogEntry("ScrollRowTile", "swap_horiz", "Scroll", "Imperatively scrolls a Row or LazyRow to a position via broadcast."),
    CatalogEntry("ScrollPagerTile", "view_carousel", "Scroll", "Imperatively navigates a Pager to a target page via broadcast."),
    CatalogEntry("StopRefreshing", "check_circle", "Pull to Refresh", "Signals a PullToRefresh to stop its loading indicator and return to the idle state."),
    CatalogEntry("RequestPermission", "lock", "Security", "Requests one or more runtime permissions using each platform's native mechanism."),
    CatalogEntry("BroadcastToSystem", "podcasts", "System", "Emits a named broadcast, notifying any app subscriber listening on the broadcastId."),
    CatalogEntry("CheckIfHasInternetConnection", "wifi", "System", "Checks whether the device has an active internet connection."),
    CatalogEntry("DropCaches", "delete_sweep", "System", "Clears internal Mosaic caches — data, images, or screens — held in memory or on disk."),
    CatalogEntry("OpenExternalLink", "open_in_new", "System", "Opens a URL in the browser or an external system app, outside the Mosaic context."),
    CatalogEntry("SetTheme", "palette", "Theme", "Overrides the app's Material 3 color scheme at runtime, until reverted."),
    CatalogEntry("ResetTheme", "format_color_reset", "Theme", "Reverts a previous SetTheme, restoring the app's default color scheme."),
)

// Preserves first-seen order, same grouping m3.material.io uses to label its component grid.
private val eventCatalogEntriesByCategory = eventCatalogEntries.groupBy { it.category }

// Cycled per card thumbnail — same role as the varied blob/illustration colors on m3.material.io cards.
private val catalogThumbAccents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

/**
 * A single catalog card in the "Components"-grid style: a full-width colored thumbnail, a title,
 * and the event's actual one-line purpose below — mirrors m3.material.io's `.thumb-container` +
 * `.title`/`.description` card anatomy. Mirrors [dev.catbit.mosaic.sample.server.endpoints.screen.screens.tiles.TilesScreenBuilder]'s
 * TileCatalogCard but navigates to "eventDetails".
 */
private fun TileSchemaBuilderScope.EventCatalogCard(
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
                                destination = "eventDetails"
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

object EventsScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "events"

    override suspend fun RoutingCall.build() = Screen(id = "events") {
        Column(
            id = "events_screen_root",
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
                id = "events_hero",
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
                        background(color(themeColorSecondaryContainer()))
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
                        text = "Events",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Events are what happens when the user interacts: read data, transform it, " +
                            "call an API, navigate, update other tiles. They chain together, with " +
                            "incomingData flowing from a parent event to its children at every step.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            SearchBar(
                id = "events_search",
                placeholder = "Search events",
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
                                    eventCatalogEntriesByCategory.keys.forEach { category ->
                                        update(
                                            tileId = "events_category_$category",
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
                id = "events_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                arrangement = arrangeVerticallySpacedBy(24)
            ) {
                eventCatalogEntriesByCategory.forEach { (category, entries) ->
                    Column(
                        id = "events_category_$category",
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
                                    EventCatalogCard(
                                        entry = entry,
                                        accentIndex = eventCatalogEntries.indexOf(entry) + pairIndex
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
