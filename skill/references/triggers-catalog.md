# EventTriggers — Complete Reference

All event triggers are accessed via the `EventTriggers` object. **Never instantiate trigger classes directly.**

```kotlin
// CORRECT
trigger = EventTriggers.onClick()

// WRONG — do not construct directly
trigger = OnClickEventTrigger()
```

The only exception is `OnPullEventTrigger`, which is not exposed via `EventTriggers` and must be referenced directly as the object:
```kotlin
trigger = OnPullEventTrigger  // used in PullToRefresh tile events
```

---

## Complete EventTriggers API

```kotlin
object EventTriggers {
    // Generic lifecycle
    fun inline()                  // fires immediately when parent event executes
    fun onDisplay()               // screen or tile becomes visible
    fun onStart()                 // operation begins (network request, data read, etc.)
    fun onSuccess()               // operation succeeded
    fun onFailure()               // operation failed

    // User interaction
    fun onClick()
    fun onLongPress()
    fun onLeadingIconClick()      // leading icon of TextField
    fun onTrailingIconClick()     // trailing icon of TextField

    // Text / input
    fun onTextChanged()
    fun onKeyboardDone()
    fun onKeyboardGo()
    fun onKeyboardNext()
    fun onKeyboardPrevious()
    fun onKeyboardSearch()
    fun onKeyboardSend()

    // Checkbox / Switch / RadioButton
    fun onCheckEvent()            // checkbox/switch checked
    fun onCheckChanged()          // any check state change (checked or unchecked)
    fun onUncheck()               // unchecked
    fun onSelect()                // radio button selected
    fun onOnSelectChanged()       // any select state change  ← NOTE: double "On"
    fun onUnselect()              // deselected

    // Navigation
    fun onNavigation()            // Navigate/NavigateUp completed
    fun onNavigationBarItemClick(itemId: String)
    fun onNavigationRailItemClick(itemId: String)
    fun onTabItemClick(itemId: String)
    fun onAdaptiveNavigationItemClick(itemId: String)
    fun onNavigationEntryChanged()
    fun onNavigationEntrySet(screenId: String)

    // Network
    fun onNetworkResponse(httpCode: Int)   // specific HTTP status code

    // Download
    fun onDownloadProgress()
    fun onDownloadPartial()
    fun onDownloadFinish()
    fun onDownloadFailure()

    // Scroll / Pager — REQUIRE a direction parameter
    fun onScrolled(direction: OnScrolledEventTrigger.ScrollDirection)
    fun onPageChanged(direction: OnPageChangedEventTrigger.Direction)
    fun onScrollThresholdReached()

    // Lazy tiles (LazyTiles tile loading)
    fun onLoadTilesStart()
    fun onLoadTilesSuccess()
    fun onLoadTilesFailure()

    // Tile management events
    fun onTilesAdded()
    fun onTilesRemoved()
    fun onTilesReplaced()
    fun onTilesUpdated()
    fun onTilesWiped()

    // Data events
    fun onDataReceived()
    fun onDataSent()
    fun onDataUpdated()
    fun onDataRemoved()

    // Overlay dismissal
    fun onDialogDismissed()
    fun onBottomSheetDismissed()
    fun onNavigationDrawerDismissed()

    // Snackbar
    fun onSnackbarAction()        // user tapped the actionLabel
    fun onSnackbarDismissed()

    // Countdown timer  ← NOTE: double "On" prefix is intentional
    fun onOnCountdownTimerTick()   // fires each second; remaining seconds in incomingData
    fun onOnCountdownTimerFinish() // fires when countdown reaches 0

    // Async image loading
    fun onAsyncImageLoadStart()
    fun onAsyncImageLoadSuccess()
    fun onAsyncImageLoadFailure()

    // Permissions
    fun onPermissionsAcquired()
    fun onPermissionsDenied()

    // Search bar
    fun onSearch()                // search submitted
    fun onQueryChanged()
    fun onQueryCleared()

    // Menu
    fun onMenuItemClick(itemId: String)
}
```

---

## Triggers NOT in EventTriggers (direct object reference)

| Trigger | Usage |
|---|---|
| `OnPullEventTrigger` | PullToRefresh tile — `trigger = OnPullEventTrigger` |

---

## Parameterized triggers

```kotlin
// Navigation items — itemId must match the item's id
EventTriggers.onMenuItemClick("delete")
EventTriggers.onNavigationBarItemClick("home")
EventTriggers.onTabItemClick("overview")
EventTriggers.onAdaptiveNavigationItemClick("settings")
EventTriggers.onNavigationEntrySet("home")
EventTriggers.onNetworkResponse(401)

// Scroll direction
EventTriggers.onScrolled(OnScrolledEventTrigger.ScrollDirection.Up)
EventTriggers.onScrolled(OnScrolledEventTrigger.ScrollDirection.Down)

// Page change direction
EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Forward)
EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Backward)
```

---

## Child trigger context table

| Parent Event | Triggers it dispatches |
|---|---|
| `GetData` | `onStart()`, `onSuccess()` (data→incomingData), `onFailure()` |
| `EvaluateData` | `onSuccess()` (true), `onFailure()` (false) |
| `UpdateData` | `onDataUpdated()` |
| `RemoveData` | `onDataRemoved()` |
| `SendData` | `onDataSent()` |
| `CheckForReceivedData` | `onDataReceived()` (data→incomingData) |
| `SendNetworkRequest` | `onStart()`, `onSuccess()` (body→incomingData), `onFailure()`, `onNetworkResponse(code)` |
| `DownloadFile` | `onStart()`, `onDownloadProgress()`, `onDownloadFinish()`, `onDownloadFailure()`, `onDownloadPartial()` |
| `Navigate` / `NavigateUp` | `onNavigation()` |
| `AddTiles` | `onTilesAdded()` |
| `RemoveTiles` | `onTilesRemoved()` |
| `ReplaceTiles` | `onTilesReplaced()` |
| `UpdateTiles` | `onTilesUpdated()` |
| `WipeTiles` | `onTilesWiped()` |
| `DisplayDialog` / `DismissDialog` | `onDialogDismissed()` |
| `DisplayBottomSheet` / `DismissBottomSheet` | `onBottomSheetDismissed()` |
| `DisplaySnackbar` | `onSnackbarAction()`, `onSnackbarDismissed()` |
| `DisplayNavigationDrawer` | `onNavigationDrawerDismissed()` |
| `GetScreen` / `RefreshScreen` | `onSuccess()`, `onFailure()` |
| `StartCountdownTimer` | `onOnCountdownTimerTick()`, `onOnCountdownTimerFinish()` |
| `RequestPermission` | `onPermissionsAcquired()`, `onPermissionsDenied()` |
| `GetFile` | `onSuccess()` (content→incomingData), `onFailure()` |
| `SaveFile` / `DeleteFile` | `onSuccess()`, `onFailure()` |
| `ProcessData` | `onSuccess()`, `onFailure()` |
| `TransformData` | `onSuccess()` |
| `CheckIfHasInternetConnection` | `onSuccess()` (has internet), `onFailure()` (no internet) |
