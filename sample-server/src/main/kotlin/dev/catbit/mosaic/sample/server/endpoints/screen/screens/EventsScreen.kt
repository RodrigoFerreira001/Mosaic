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

private val eventCatalogEntries = listOf(
    CatalogEntry("AddTiles", "add_box", "Tile Management"),
    CatalogEntry("RemoveTiles", "delete", "Tile Management"),
    CatalogEntry("UpdateTiles", "sync", "Tile Management"),
    CatalogEntry("ReplaceTiles", "swap_horiz", "Tile Management"),
    CatalogEntry("WipeTiles", "delete_sweep", "Tile Management"),
    CatalogEntry("ReloadLazyTiles", "autorenew", "Tile Management"),
    CatalogEntry("CheckIfTileContainsChildren", "checklist", "Tile Management"),
    CatalogEntry("GetTileChildrenCount", "pin", "Tile Management"),
    CatalogEntry("RunEvents", "playlist_play", "Tile Management"),
    CatalogEntry("UpdateEvents", "edit_note", "Tile Management"),
    CatalogEntry("Navigate", "arrow_forward", "Navigation"),
    CatalogEntry("NavigateClearingStack", "restart_alt", "Navigation"),
    CatalogEntry("NavigateUp", "arrow_back", "Navigation"),
    CatalogEntry("GetScreen", "download", "Screen"),
    CatalogEntry("RefreshScreen", "refresh", "Screen"),
    CatalogEntry("ChangeScreenState", "tune", "Screen"),
    CatalogEntry("SendData", "outbox", "Data"),
    CatalogEntry("CheckForReceivedData", "inbox", "Data"),
    CatalogEntry("GetData", "download_for_offline", "Data"),
    CatalogEntry("UpdateData", "save", "Data"),
    CatalogEntry("RemoveData", "delete_forever", "Data"),
    CatalogEntry("ProcessData", "memory", "Data"),
    CatalogEntry("TransformData", "transform", "Data"),
    CatalogEntry("EvaluateData", "rule", "Data"),
    CatalogEntry("SendNetworkRequest", "cloud", "Networking"),
    CatalogEntry("DownloadFile", "cloud_download", "Networking"),
    CatalogEntry("SendFile", "cloud_upload", "Networking"),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderBody", "data_object", "Networking"),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderHeaders", "list_alt", "Networking"),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderUrl", "link", "Networking"),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderQueryParameters", "filter_list", "Networking"),
    CatalogEntry("SaveFile", "save", "File System"),
    CatalogEntry("GetFile", "folder_open", "File System"),
    CatalogEntry("DeleteFile", "delete", "File System"),
    CatalogEntry("OpenFilePicker", "attach_file", "File System"),
    CatalogEntry("TakePicture", "photo_camera", "File System"),
    CatalogEntry("GetImageFromGallery", "photo_library", "File System"),
    CatalogEntry("DisplayDialog", "open_in_new", "Overlays"),
    CatalogEntry("DismissDialog", "close", "Overlays"),
    CatalogEntry("DisplayBottomSheet", "vertical_align_bottom", "Overlays"),
    CatalogEntry("DismissBottomSheet", "expand_more", "Overlays"),
    CatalogEntry("DisplayNavigationDrawer", "menu_open", "Overlays"),
    CatalogEntry("DismissNavigationDrawer", "menu", "Overlays"),
    CatalogEntry("DisplaySnackbar", "chat_bubble", "Overlays"),
    CatalogEntry("DismissSnackbar", "cancel", "Overlays"),
    CatalogEntry("TriggerEvent", "bolt", "Events / Meta"),
    CatalogEntry("ToggleMenu", "more_vert", "Menu"),
    CatalogEntry("TogglePopup", "picture_in_picture", "Popup"),
    CatalogEntry("StartCountdownTimer", "timer", "Time"),
    CatalogEntry("ScrollColumnTile", "swap_vert", "Scroll"),
    CatalogEntry("ScrollRowTile", "swap_horiz", "Scroll"),
    CatalogEntry("ScrollPagerTile", "view_carousel", "Scroll"),
    CatalogEntry("StopRefreshing", "check_circle", "Pull to Refresh"),
    CatalogEntry("RequestPermission", "lock", "Security"),
    CatalogEntry("BroadcastToSystem", "podcasts", "System"),
    CatalogEntry("CheckIfHasInternetConnection", "wifi", "System"),
    CatalogEntry("DropCaches", "delete_sweep", "System"),
    CatalogEntry("SetTheme", "palette", "Theme"),
    CatalogEntry("ResetTheme", "format_color_reset", "Theme"),
)

suspend fun RoutingCall.respondEvents() = respond(
    Screen(id = "events") {
        Column(
            id = "events_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 16)
            },
            arrangement = arrangeVerticallySpacedBy(12)
        ) {
            SearchBar(
                id = "events_search",
                placeholder = "Buscar events",
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
                                        tileId = "events_catalog_list",
                                        updateData = incomingTileUpdateData()
                                    )
                                }
                            )
                        }
                    )
                }
            )
            Column(
                id = "events_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                },
                arrangement = arrangeVerticallySpacedBy(8),
                scrollable = true
            ) {
                eventCatalogEntries.forEach { entry ->
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
