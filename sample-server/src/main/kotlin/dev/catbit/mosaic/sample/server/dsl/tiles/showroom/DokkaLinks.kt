package dev.catbit.mosaic.sample.server.dsl.tiles.showroom

/**
 * Deep-links from the showroom's tile/event detail pages into the published Dokka reference for
 * the matching `mosaic-server` DSL builder function, e.g. `Button` -> `.../-button.html`.
 *
 * The URL scheme (`<base>/<package>/-<kebab-function-name>.html`) is Dokka's own convention for a
 * top-level function page, verified against the generated output under `build/dokka/html/mosaic-server`.
 */
private const val DOKKA_BASE = "https://rodrigoferreira001.github.io/Mosaic/mosaic-server"

private fun String.toDokkaSlug(): String {
    val kebab = buildString {
        this@toDokkaSlug.forEachIndexed { index, char ->
            if (char.isUpperCase() && index > 0) append('-')
            append(char.lowercaseChar())
        }
    }
    return "-$kebab"
}

private fun dokkaUrl(packageName: String, functionName: String): String =
    "$DOKKA_BASE/$packageName/${functionName.toDokkaSlug()}.html"

private const val TILE_BUTTONS = "dev.catbit.mosaic.server.builder.tile.builders.buttons"
private const val TILE_PROGRESS = "dev.catbit.mosaic.server.builder.tile.builders.progress"
private const val TILE_CHIPS = "dev.catbit.mosaic.server.builder.tile.builders.chips"
private const val TILE_GROUPING = "dev.catbit.mosaic.server.builder.tile.builders.grouping"
private const val TILE_APP_BARS = "dev.catbit.mosaic.server.builder.tile.builders.app_bars"
private const val TILE_NAVIGATION = "dev.catbit.mosaic.server.builder.tile.builders.navigation"
private const val TILE_IMAGE = "dev.catbit.mosaic.server.builder.tile.builders.image"
private const val TILE_SEARCH = "dev.catbit.mosaic.server.builder.tile.builders.search"
private const val TILE_SYSTEM = "dev.catbit.mosaic.server.builder.tile.builders.system"
private const val TILE_MENU = "dev.catbit.mosaic.server.builder.tile.builders.menu"
private const val TILE_TEXT = "dev.catbit.mosaic.server.builder.tile.builders.text"
private const val TILE_BADGES = "dev.catbit.mosaic.server.builder.tile.builders.badges"
private const val TILE_INPUTS = "dev.catbit.mosaic.server.builder.tile.builders.inputs"
private const val TILE_POPUP = "dev.catbit.mosaic.server.builder.tile.builders.popup"
private const val TILE_TOOLTIP = "dev.catbit.mosaic.server.builder.tile.builders.tooltip"

/** Tile name -> DSL builder package, one entry per tile in the showroom catalog. */
private val TILE_PACKAGES: Map<String, String> = mapOf(
    "Popup" to TILE_POPUP,
    "Tooltip" to TILE_TOOLTIP,
    "Button" to TILE_BUTTONS,
    "IconButton" to TILE_BUTTONS,
    "FloatingActionButton" to TILE_BUTTONS,
    "CircularProgressIndicator" to TILE_PROGRESS,
    "LinearProgressIndicator" to TILE_PROGRESS,
    "AssistChip" to TILE_CHIPS,
    "SuggestionChip" to TILE_CHIPS,
    "InputChip" to TILE_CHIPS,
    "FilterChip" to TILE_CHIPS,
    "Shimmer" to TILE_GROUPING,
    "Box" to TILE_GROUPING,
    "Pager" to TILE_GROUPING,
    "LazyColumn" to TILE_GROUPING,
    "Card" to TILE_GROUPING,
    "AdaptiveVisibility" to TILE_GROUPING,
    "LazyTiles" to TILE_GROUPING,
    "SelectionContainer" to TILE_GROUPING,
    "Column" to TILE_GROUPING,
    "Grid" to TILE_GROUPING,
    "FlexBox" to TILE_GROUPING,
    "Row" to TILE_GROUPING,
    "LazyRow" to TILE_GROUPING,
    "FlowRow" to TILE_GROUPING,
    "PullToRefresh" to TILE_GROUPING,
    "Carousel" to TILE_GROUPING,
    "BottomAppBar" to TILE_APP_BARS,
    "TopAppBar" to TILE_APP_BARS,
    "NavigationRail" to TILE_NAVIGATION,
    "NavigationBar" to TILE_NAVIGATION,
    "Tabs" to TILE_NAVIGATION,
    "NestedNavigationGraph" to TILE_NAVIGATION,
    "Icon" to TILE_IMAGE,
    "AsyncImage" to TILE_IMAGE,
    "Image" to TILE_IMAGE,
    "SearchBar" to TILE_SEARCH,
    "SystemBroadcastListener" to TILE_SYSTEM,
    "Menu" to TILE_MENU,
    "SimpleText" to TILE_TEXT,
    "Badge" to TILE_BADGES,
    "RadioButton" to TILE_INPUTS,
    "TextField" to TILE_INPUTS,
    "TimePicker" to TILE_INPUTS,
    "Checkbox" to TILE_INPUTS,
    "DatePicker" to TILE_INPUTS,
    "Switch" to TILE_INPUTS,
    "DropdownList" to TILE_INPUTS,
)

private const val EVENT_PULL_TO_REFRESH = "dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh"
private const val EVENT_POPUP = "dev.catbit.mosaic.server.builder.event.builders.popup"
private const val EVENT_FILE = "dev.catbit.mosaic.server.builder.event.builders.file"
private const val EVENT_SECURITY = "dev.catbit.mosaic.server.builder.event.builders.security"
private const val EVENT_NETWORKING = "dev.catbit.mosaic.server.builder.event.builders.networking"
private const val EVENT_NAVIGATION = "dev.catbit.mosaic.server.builder.event.builders.navigation"
private const val EVENT_IMAGE = "dev.catbit.mosaic.server.builder.event.builders.image"
private const val EVENT_TIME = "dev.catbit.mosaic.server.builder.event.builders.time"
private const val EVENT_TILES = "dev.catbit.mosaic.server.builder.event.builders.tiles"
private const val EVENT_SYSTEM = "dev.catbit.mosaic.server.builder.event.builders.system"
private const val EVENT_THEME = "dev.catbit.mosaic.server.builder.event.builders.theme"
private const val EVENT_SCREEN = "dev.catbit.mosaic.server.builder.event.builders.screen"
private const val EVENT_MENU = "dev.catbit.mosaic.server.builder.event.builders.menu"
private const val EVENT_DATA = "dev.catbit.mosaic.server.builder.event.builders.data"
private const val EVENT_EVENT = "dev.catbit.mosaic.server.builder.event.builders.event"
private const val EVENT_SCROLL_COLUMN = "dev.catbit.mosaic.server.builder.event.builders.scroll.column"
private const val EVENT_SCROLL_ROW = "dev.catbit.mosaic.server.builder.event.builders.scroll.row"
private const val EVENT_SCROLL_PAGER = "dev.catbit.mosaic.server.builder.event.builders.scroll.pager"
private const val EVENT_OVERLAYS_SNACKBAR = "dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar"
private const val EVENT_OVERLAYS_MODAL_BOTTOM_SHEET = "dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet"
private const val EVENT_OVERLAYS_DIALOG = "dev.catbit.mosaic.server.builder.event.builders.overlays.dialog"
private const val EVENT_OVERLAYS_NAVIGATION_DRAWER = "dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer"
private const val EVENT_OVERLAYS_BOTTOM_SHEET = "dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet"

/**
 * Event name -> (DSL builder package, DSL builder function name), one entry per event in the
 * showroom catalog. The function name is usually identical to the event's display name, except
 * for the scroll events (`ScrollColumnTile` -> `ScrollColumn`, etc.) and `UploadFile`.
 */
private val EVENT_LINKS: Map<String, Pair<String, String>> = mapOf(
    "StopRefreshing" to (EVENT_PULL_TO_REFRESH to "StopRefreshing"),
    "TogglePopup" to (EVENT_POPUP to "TogglePopup"),
    "SaveFile" to (EVENT_FILE to "SaveFile"),
    "DeleteFile" to (EVENT_FILE to "DeleteFile"),
    "OpenFilePicker" to (EVENT_FILE to "OpenFilePicker"),
    "GetFile" to (EVENT_FILE to "GetFile"),
    "RequestPermission" to (EVENT_SECURITY to "RequestPermission"),
    "SendNetworkRequest" to (EVENT_NETWORKING to "SendNetworkRequest"),
    "DownloadFileToDisk" to (EVENT_NETWORKING to "DownloadFileToDisk"),
    "DownloadFile" to (EVENT_NETWORKING to "DownloadFile"),
    "DownloadFileToMemory" to (EVENT_NETWORKING to "DownloadFileToMemory"),
    "SetIncomingDataToNetworkParamsHolderBody" to (EVENT_NETWORKING to "SetIncomingDataToNetworkParamsHolderBody"),
    "UploadFile" to (EVENT_NETWORKING to "UploadFile"),
    "SetIncomingDataToNetworkParamsHolderUrl" to (EVENT_NETWORKING to "SetIncomingDataToNetworkParamsHolderUrl"),
    "SetIncomingDataToNetworkParamsHolderQueryParameters" to (EVENT_NETWORKING to "SetIncomingDataToNetworkParamsHolderQueryParameters"),
    "SetIncomingDataToNetworkParamsHolderHeaders" to (EVENT_NETWORKING to "SetIncomingDataToNetworkParamsHolderHeaders"),
    "NavigateClearingStack" to (EVENT_NAVIGATION to "NavigateClearingStack"),
    "NavigateUp" to (EVENT_NAVIGATION to "NavigateUp"),
    "Navigate" to (EVENT_NAVIGATION to "Navigate"),
    "TakePicture" to (EVENT_IMAGE to "TakePicture"),
    "GetImageFromGallery" to (EVENT_IMAGE to "GetImageFromGallery"),
    "StartCountdownTimer" to (EVENT_TIME to "StartCountdownTimer"),
    "StartTimeLoop" to (EVENT_TIME to "StartTimeLoop"),
    "ReloadLazyTiles" to (EVENT_TILES to "ReloadLazyTiles"),
    "CheckIfTileContainsChildren" to (EVENT_TILES to "CheckIfTileContainsChildren"),
    "ReplaceTiles" to (EVENT_TILES to "ReplaceTiles"),
    "WipeTiles" to (EVENT_TILES to "WipeTiles"),
    "UpdateTiles" to (EVENT_TILES to "UpdateTiles"),
    "AddTiles" to (EVENT_TILES to "AddTiles"),
    "GetTileChildrenCount" to (EVENT_TILES to "GetTileChildrenCount"),
    "RemoveTiles" to (EVENT_TILES to "RemoveTiles"),
    "OpenExternalLink" to (EVENT_SYSTEM to "OpenExternalLink"),
    "DropCaches" to (EVENT_SYSTEM to "DropCaches"),
    "CheckIfHasInternetConnection" to (EVENT_SYSTEM to "CheckIfHasInternetConnection"),
    "BroadcastToSystem" to (EVENT_SYSTEM to "BroadcastToSystem"),
    "ResetTheme" to (EVENT_THEME to "ResetTheme"),
    "SetTheme" to (EVENT_THEME to "SetTheme"),
    "ChangeScreenState" to (EVENT_SCREEN to "ChangeScreenState"),
    "RefreshScreen" to (EVENT_SCREEN to "RefreshScreen"),
    "GetScreen" to (EVENT_SCREEN to "GetScreen"),
    "ToggleMenu" to (EVENT_MENU to "ToggleMenu"),
    "EvaluateData" to (EVENT_DATA to "EvaluateData"),
    "RemoveData" to (EVENT_DATA to "RemoveData"),
    "TransformData" to (EVENT_DATA to "TransformData"),
    "CheckForReceivedData" to (EVENT_DATA to "CheckForReceivedData"),
    "SendData" to (EVENT_DATA to "SendData"),
    "GetData" to (EVENT_DATA to "GetData"),
    "UpdateData" to (EVENT_DATA to "UpdateData"),
    "ProcessData" to (EVENT_DATA to "ProcessData"),
    "RunEvents" to (EVENT_EVENT to "RunEvents"),
    "RunCancellableEvents" to (EVENT_EVENT to "RunCancellableEvents"),
    "UpdateEvents" to (EVENT_EVENT to "UpdateEvents"),
    "CancelEvents" to (EVENT_EVENT to "CancelEvents"),
    "TriggerEvent" to (EVENT_EVENT to "TriggerEvent"),
    "ScrollColumnTile" to (EVENT_SCROLL_COLUMN to "ScrollColumn"),
    "ScrollRowTile" to (EVENT_SCROLL_ROW to "ScrollRow"),
    "ScrollPagerTile" to (EVENT_SCROLL_PAGER to "ScrollPager"),
    "DisplaySnackbar" to (EVENT_OVERLAYS_SNACKBAR to "DisplaySnackbar"),
    "DismissSnackbar" to (EVENT_OVERLAYS_SNACKBAR to "DismissSnackbar"),
    "DismissModalBottomSheet" to (EVENT_OVERLAYS_MODAL_BOTTOM_SHEET to "DismissModalBottomSheet"),
    "DisplayModalBottomSheet" to (EVENT_OVERLAYS_MODAL_BOTTOM_SHEET to "DisplayModalBottomSheet"),
    "DismissDialog" to (EVENT_OVERLAYS_DIALOG to "DismissDialog"),
    "DisplayDialog" to (EVENT_OVERLAYS_DIALOG to "DisplayDialog"),
    "DisplayNavigationDrawer" to (EVENT_OVERLAYS_NAVIGATION_DRAWER to "DisplayNavigationDrawer"),
    "DismissNavigationDrawer" to (EVENT_OVERLAYS_NAVIGATION_DRAWER to "DismissNavigationDrawer"),
    "DismissBottomSheet" to (EVENT_OVERLAYS_BOTTOM_SHEET to "DismissBottomSheet"),
    "DisplayBottomSheet" to (EVENT_OVERLAYS_BOTTOM_SHEET to "DisplayBottomSheet"),
)

/** Dokka page for the DSL builder function behind [tileName], e.g. `"Button"`. */
fun dokkaTileDocsUrl(tileName: String): String {
    val packageName = requireNotNull(TILE_PACKAGES[tileName]) { "No Dokka package registered for tile \"$tileName\"" }
    return dokkaUrl(packageName, tileName)
}

/** Dokka page for the DSL builder function behind [eventName], e.g. `"OpenExternalLink"`. */
fun dokkaEventDocsUrl(eventName: String): String {
    val (packageName, functionName) = requireNotNull(EVENT_LINKS[eventName]) { "No Dokka package registered for event \"$eventName\"" }
    return dokkaUrl(packageName, functionName)
}
