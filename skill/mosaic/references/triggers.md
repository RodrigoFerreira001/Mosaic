# Mosaic — Triggers Reference

All triggers are accessed via `EventTriggers.*` (object `dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers`).

JSON serialization uses `@SerialName` as the `type` discriminator on the `trigger` field.

---

## User Interaction

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onClick()` | `"OnClick"` | Button, IconButton, FAB, Row, Column, Box, Card, AssistChip, SuggestionChip, FilterChip, InputChip |
| `EventTriggers.onLongPress()` | `"OnLongPress"` | Button, Row, Column, Box |
| `EventTriggers.onCheck()` | `"OnCheck"` | Checkbox, Switch, FilterChip, InputChip (checked = true) |
| `EventTriggers.onUncheck()` | `"OnUncheck"` | Checkbox, Switch, FilterChip, InputChip (checked = false) |
| `EventTriggers.onCheckChanged()` | `"OnCheckChanged"` | Checkbox, Switch, FilterChip, InputChip (any change) |
| `EventTriggers.onSelect()` | `"OnSelected"` | RadioButton (tapped) |
| `EventTriggers.onUnselect()` | `"OnUnselected"` | RadioButton (auto-deselected when sibling selected) |
| `EventTriggers.onSelectChanged()` | `"OnSelectChanged"` | Selection components |
| `EventTriggers.onTextChanged()` | `"OnTextChanged"` | TextField, SearchBar (each keystroke) |
| `EventTriggers.onSearch()` | `"OnSearch"` | SearchBar (IME Search action) |
| `EventTriggers.onQueryChanged()` | `"OnQueryChanged"` | SearchBar |
| `EventTriggers.onQueryCleared()` | `"OnQueryCleared"` | SearchBar (clear button tapped) |
| `EventTriggers.onLeadingIconClick()` | `"OnLeadingIconClick"` | TextField (if `clickableLeadingIcon=true`), SearchBar |
| `EventTriggers.onTrailingIconClick()` | `"OnTrailingIconClick"` | TextField (if `clickableTrailingIcon=true`) |
| `EventTriggers.onPull()` | `"OnPull"` | PullToRefresh |

---

## Keyboard

| DSL | @SerialName | Fired When |
|---|---|---|
| `EventTriggers.onKeyboardDone()` | `"OnKeyboardDone"` | IME action = Done |
| `EventTriggers.onKeyboardGo()` | `"OnKeyboardGo"` | IME action = Go |
| `EventTriggers.onKeyboardSearch()` | `"OnKeyboardSearch"` | IME action = Search |
| `EventTriggers.onKeyboardSend()` | `"OnKeyboardSend"` | IME action = Send |
| `EventTriggers.onKeyboardNext()` | `"OnKeyboardNext"` | IME action = Next |
| `EventTriggers.onKeyboardPrevious()` | `"OnKeyboardPrevious"` | IME action = Previous |

---

## Lifecycle / Display

| DSL | @SerialName | Fired When |
|---|---|---|
| `EventTriggers.onDisplay()` | `"OnDisplay"` | Tile enters composition — fired by all grouping tiles (Column, LazyColumn, Row, LazyRow, Box, Card, Grid, FlexBox, FlowRow, Pager, Carousel, Shimmer, PullToRefresh, AdaptiveVisibility, LazyTiles) |
| `EventTriggers.inline()` | `"Inline"` | Executed immediately when event is processed |
| `EventTriggers.onWidthBreakpointSatisfied()` | `"OnWidthBreakpointSatisfied"` | AdaptiveVisibility — width constraint satisfied |
| `EventTriggers.onWidthBreakpointNotSatisfied()` | `"OnWidthBreakpointNotSatisfied"` | AdaptiveVisibility — width constraint not satisfied |
| `EventTriggers.onHeightBreakpointSatisfied()` | `"OnHeightBreakpointSatisfied"` | AdaptiveVisibility — height constraint satisfied |
| `EventTriggers.onHeightBreakpointNotSatisfied()` | `"OnHeightBreakpointNotSatisfied"` | AdaptiveVisibility — height constraint not satisfied |

---

## Operation Lifecycle (child event triggers)

| DSL | @SerialName | Fired When |
|---|---|---|
| `EventTriggers.onStart()` | `"OnStart"` | Async operation begins |
| `EventTriggers.onSuccess()` | `"OnSuccess"` | Operation completed successfully |
| `EventTriggers.onFailure()` | `"OnFailure"` | Operation failed; incomingData = Throwable |

---

## Tile State

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onTilesAdded()` | `"OnTilesAdded"` | AddTilesEventSchema |
| `EventTriggers.onTilesRemoved()` | `"OnTilesRemoved"` | RemoveTilesEventSchema |
| `EventTriggers.onTilesUpdated()` | `"OnTilesUpdated"` | UpdateTilesEventSchema, UpdateEventsEventSchema |
| `EventTriggers.onTilesReplaced()` | `"OnTilesReplaced"` | ReplaceTilesEventSchema |
| `EventTriggers.onTilesWiped()` | `"OnTilesWiped"` | WipeTilesEventSchema |

---

## Navigation

| DSL | @SerialName | Fields | Fired By |
|---|---|---|---|
| `EventTriggers.onNavigation()` | `"OnNavigation"` | — | NavigateEventSchema, NavigateUpEventSchema |
| `EventTriggers.onNavigationEntryChanged()` | `"OnNavigationEntryChanged"` | — | NestedNavigationGraphTileSchema |
| `EventTriggers.onNavigationEntrySet(screenId)` | `"OnNavigationEntrySet"` | `screenId: String` | NestedNavigationGraphTileSchema |
| `EventTriggers.onNavigationBarItemClick(itemId)` | `"OnNavigationBarItemClick"` | `itemId: String` | NavigationBarTileSchema |
| `EventTriggers.onNavigationRailItemClick(itemId)` | `"OnNavigationRailItemClick"` | `itemId: String` | NavigationRailTileSchema |
| `EventTriggers.onTabItemClick(itemId)` | `"OnTabItemClick"` | `itemId: String` | TabsTileSchema |

---

## Data

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onDataSent()` | `"OnDataSent"` | SendDataEventSchema |
| `EventTriggers.onDataReceived()` | `"OnDataReceived"` | CheckForReceivedDataEventSchema |
| `EventTriggers.onDataUpdated()` | `"OnDataUpdated"` | UpdateDataEventSchema |
| `EventTriggers.onDataRemoved()` | `"OnDataRemoved"` | RemoveDataEventSchema |

---

## Network

| DSL | @SerialName | Fields | Notes |
|---|---|---|---|
| `EventTriggers.onNetworkResponse(httpCode)` | `"OnNetworkResponse"` | `httpCode: Int` | Overrides `OnSuccess` for that code |
| `EventTriggers.onNetworkFailure(httpCode)` | `"OnNetworkFailure"` | `httpCode: Int` | Overrides `OnFailure` for that code; never fires for network exceptions |
| `EventTriggers.onDownloadProgress()` | `"OnDownloadProgress"` | — | DownloadFile |
| `EventTriggers.onDownloadFinish()` | `"OnDownloadFinish"` | — | DownloadFile |
| `EventTriggers.onDownloadFailure()` | `"OnDownloadFailure"` | — | DownloadFile |
| `EventTriggers.onUploadProgress()` | `"OnUploadProgress"` | — | SendFile; incomingData = Int 0–100, fires only when % changes |

---

## Image Loading

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onAsyncImageLoadStart()` | `"OnAsyncImageLoadStart"` | AsyncImageTileSchema |
| `EventTriggers.onAsyncImageLoadSuccess()` | `"OnAsyncImageLoadSuccess"` | AsyncImageTileSchema |
| `EventTriggers.onAsyncImageLoadFailure()` | `"OnAsyncImageLoadFailure"` | AsyncImageTileSchema |

---

## Overlay

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onDialogDismissed()` | `"OnDialogDismissed"` | DismissDialogEventSchema |
| `EventTriggers.onBottomSheetDismissed()` | `"OnBottomSheetDismissed"` | DismissBottomSheetEventSchema |
| `EventTriggers.onNavigationDrawerDismissed()` | `"OnNavigationDrawerDismissed"` | DismissNavigationDrawerEventSchema |
| `EventTriggers.onSnackbarAction()` | `"OnSnackbarAction"` | DisplaySnackbarEventSchema |
| `EventTriggers.onSnackbarDismissed()` | `"OnSnackbarDismissed"` | DisplaySnackbarEventSchema |

---

## Menu

| DSL | @SerialName | Fields | Fired By |
|---|---|---|---|
| `EventTriggers.onMenuItemClick(itemId)` | `"OnMenuItemClick"` | `itemId: String` | MenuTileSchema |

---

## Timer

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onCountdownTimerTick()` | `"OnCountdownTimerTick"` | StartCountdownTimerEventSchema |
| `EventTriggers.onCountdownTimerFinish()` | `"OnCountdownTimerFinish"` | StartCountdownTimerEventSchema |

---

## Permission

| DSL | @SerialName | Fired By |
|---|---|---|
| `EventTriggers.onPermissionsAcquired()` | `"OnPermissionsAcquired"` | RequestPermissionEventSchema |
| `EventTriggers.onPermissionsDenied()` | `"OnPermissionsDenied"` | RequestPermissionEventSchema |

---

## Scroll / Page

| DSL | @SerialName | Fields | Fired By |
|---|---|---|---|
| `EventTriggers.onScrolled(direction)` | `"OnScrolled"` | `direction: Top/Bottom/Start/End` | Column (scrollable), Row, LazyColumn, LazyRow |
| `EventTriggers.onScrollThresholdReached()` | `"OnScrollThresholdReached"` | — | LazyColumn, LazyRow |
| `EventTriggers.onPageChanged(direction)` | `"OnPageChanged"` | `direction: Start/End/Any/Index(n)` | PagerTileSchema |

---

## LazyTiles Loading

| DSL | @SerialName | Fired When |
|---|---|---|
| `EventTriggers.onLoadTilesStart()` | `"OnLoadTilesStart"` | LazyTiles fetch begins |
| `EventTriggers.onLoadTilesSuccess()` | `"OnLoadTilesSuccess"` | LazyTiles loaded successfully |
| `EventTriggers.onLoadTilesFailure()` | `"OnLoadTilesFailure"` | LazyTiles fetch failed |

---

## System

| DSL | @SerialName | Fields | Notes |
|---|---|---|---|
| `EventTriggers.onSystemBroadcast(broadcastId)` | `"OnSystemBroadcast"` | `broadcastId: String` | Fires when `BroadcastToSystemEventSchema` emits matching broadcastId |

---

## Dropdown List

| DSL | @SerialName | Fields | Fired By |
|---|---|---|---|
| `EventTriggers.onDropdownListItemSelected(id)` | `"OnDropdownListItemSelected"` | `id: String` | `DropdownList` — user taps an option; incomingData = selected option `id` |
| `EventTriggers.onDropdownListOpen()` | `"OnDropdownListOpen"` | — | `DropdownList` — menu opens |
| `EventTriggers.onDropdownListClose()` | `"OnDropdownListClose"` | — | `DropdownList` — menu closes (toggle or outside dismiss) |

**Note:** `onDropdownListItemSelected(id)` is a parameterized trigger — pass the specific option `id` you want to react to. Each option can have its own event chain.
