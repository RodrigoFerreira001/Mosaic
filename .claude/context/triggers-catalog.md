# Mosaic — EventTriggers Catalog

`EventTrigger` is a marker interface. Every `EventSchema` has one trigger that defines **when** it fires. Child events in the `events` list are dispatched using specific triggers after the parent event executes.

JSON serialization uses `@SerialName` as the `type` discriminator.

---

## Usage Pattern

```json
{
  "type": "Button",
  "id": "btn_confirm",
  "events": [
    {
      "type": "SendNetworkRequest",
      "id": "evt_submit",
      "trigger": { "type": "OnClick" },
      "url": "/api/submit",
      "method": "POST",
      "events": [
        {
          "type": "Navigate",
          "id": "evt_nav",
          "trigger": { "type": "OnSuccess" },
          "destination": "confirmation_screen",
          "navigatorId": "main"
        }
      ]
    }
  ]
}
```

---

## User Interaction Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnClickEventTrigger` | `"OnClick"` | — | Button, IconButton, FAB, Row, Column, Box, SuggestionChip |
| `OnLongPressEventTrigger` | `"OnLongPress"` | — | Button, Row, Column, Box |
| `OnCheckEventTrigger` | `"OnCheck"` | — | Checkbox, Switch (when checked = true) |
| `OnUncheckEventTrigger` | `"OnUncheck"` | — | Checkbox, Switch (when checked = false) |
| `OnCheckChangedEventTrigger` | `"OnCheckChanged"` | — | Checkbox, Switch (on any change) |
| `OnSelectEventTrigger` | `"OnSelected"` | — | RadioButton |
| `OnSelectChangedEventTrigger` | `"OnSelectChanged"` | — | Selection components |
| `OnTextChangedEventTrigger` | `"OnTextChanged"` | — | TextField, SearchBar |
| `OnQueryChangedEventTrigger` | `"OnQueryChanged"` | — | SearchBar |
| `OnQueryClearedEventTrigger` | `"OnQueryCleared"` | — | SearchBar |
| `OnLeadingIconClickEventTrigger` | `"OnLeadingIconClick"` | — | SearchBar |
| `OnTrailingIconClickEventTrigger` | `"OnTrailingIconClick"` | — | SearchBar |
| `OnPullEventTrigger` | `"OnPull"` | — | PullToRefresh |

---

## Keyboard Triggers

| Trigger | @SerialName | Fired When |
|---|---|---|
| `OnKeyboardDoneEventTrigger` | `"OnKeyboardDone"` | IME action = Done |
| `OnKeyboardGoEventTrigger` | `"OnKeyboardGo"` | IME action = Go |
| `OnKeyboardSearchEventTrigger` | `"OnKeyboardSearch"` | IME action = Search |
| `OnKeyboardSendEventTrigger` | `"OnKeyboardSend"` | IME action = Send |
| `OnKeyboardNextEventTrigger` | `"OnKeyboardNext"` | IME action = Next |
| `OnKeyboardPreviousEventTrigger` | `"OnKeyboardPrevious"` | IME action = Previous |

---

## Lifecycle / Display Triggers

| Trigger | @SerialName | Fired When |
|---|---|---|
| `OnDisplayEventTrigger` | `"OnDisplay"` | Tile becomes visible in Compose (DisposableEffect enter) |
| `InlineEventTrigger` | `"Inline"` | Executed immediately when the event is processed, regardless of context |
| `OnWidthBreakpointSatisfiedEventTrigger` | `"OnWidthBreakpointSatisfied"` | AdaptiveVisibility — width constraint becomes satisfied (first evaluation and on change) |
| `OnWidthBreakpointNotSatisfiedEventTrigger` | `"OnWidthBreakpointNotSatisfied"` | AdaptiveVisibility — width constraint becomes unsatisfied (first evaluation and on change) |
| `OnHeightBreakpointSatisfiedEventTrigger` | `"OnHeightBreakpointSatisfied"` | AdaptiveVisibility — height constraint becomes satisfied (first evaluation and on change) |
| `OnHeightBreakpointNotSatisfiedEventTrigger` | `"OnHeightBreakpointNotSatisfied"` | AdaptiveVisibility — height constraint becomes unsatisfied (first evaluation and on change) |

---

## Operation Lifecycle Triggers

Used as child event triggers after async operations (network, data, file, screen).

| Trigger | @SerialName | Fired When |
|---|---|---|
| `OnStartEventTrigger` | `"OnStart"` | Operation begins |
| `OnSuccessEventTrigger` | `"OnSuccess"` | Operation completes successfully |
| `OnFailureEventTrigger` | `"OnFailure"` | Operation fails |

---

## Tile State Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnTilesAddedEventTrigger` | `"OnTilesAdded"` | AddTilesEventSchema |
| `OnTilesRemovedEventTrigger` | `"OnTilesRemoved"` | RemoveTilesEventSchema |
| `OnTilesUpdatedEventTrigger` | `"OnTilesUpdated"` | UpdateTilesEventSchema, UpdateEventsEventSchema |
| `OnTilesReplacedEventTrigger` | `"OnTilesReplaced"` | ReplaceTilesEventSchema |
| `OnTilesWipedEventTrigger` | `"OnTilesWiped"` | WipeTilesEventSchema |

---

## Navigation Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnNavigationEventTrigger` | `"OnNavigation"` | — | NavigateEventSchema, NavigateUpEventSchema |
| `OnNavigationEntryChangedEventTrigger` | `"OnNavigationEntryChanged"` | — | NestedNavigationGraphTileSchema (on any entry change) |
| `OnNavigationEntrySetEventTrigger` | `"OnNavigationEntrySet"` | `screenId: String` | NestedNavigationGraphTileSchema (when set to a specific screen) |
| `OnNavigationBarItemClickEventTrigger` | `"OnNavigationBarItemClick"` | `itemId: String` | NavigationBarTileSchema |
| `OnNavigationRailItemClickEventTrigger` | `"OnNavigationRailItemClick"` | `itemId: String` | NavigationRailTileSchema |
| `OnTabItemClickEventTrigger` | `"OnTabItemClick"` | `itemId: String` | TabsTileSchema |

---

## Data Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnDataSentEventTrigger` | `"OnDataSent"` | SendDataEventSchema |
| `OnDataReceivedEventTrigger` | `"OnDataReceived"` | CheckForReceivedDataEventSchema |
| `OnDataUpdatedEventTrigger` | `"OnDataUpdated"` | UpdateDataEventSchema |
| `OnDataRemovedEventTrigger` | `"OnDataRemoved"` | RemoveDataEventSchema |

---

## Network Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnNetworkResponseTrigger` | `"OnNetworkResponse"` | `httpCode: Int` | SendNetworkRequestEventSchema (matches specific HTTP code) |
| `OnDownloadProgressEventTrigger` | `"OnDownloadProgress"` | — | DownloadFileEventSchema (each progress update) |
| `OnDownloadFinishEventTrigger` | `"OnDownloadFinish"` | — | DownloadFileEventSchema |
| `OnDownloadFailureEventTrigger` | `"OnDownloadFailure"` | — | DownloadFileEventSchema |
| `OnDownloadPartialEventTrigger` | `"OnDownloadPartial"` | — | DownloadFileEventSchema (partial progress) |
| `OnUploadProgressEventTrigger` | `"OnUploadProgress"` | — | SendFileEventSchema (incomingData = Int 0–100, fired only when the percent changes) |

---

## Image Loading Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnAsyncImageLoadStartEventTrigger` | `"OnAsyncImageLoadStart"` | AsyncImageTileSchema |
| `OnAsyncImageLoadSuccessEventTrigger` | `"OnAsyncImageLoadSuccess"` | AsyncImageTileSchema |
| `OnAsyncImageLoadFailureEventTrigger` | `"OnAsyncImageLoadFailure"` | AsyncImageTileSchema |

---

## Overlay Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnDialogDismissedEventTrigger` | `"OnDialogDismissed"` | DismissDialogEventSchema |
| `OnBottomSheetDismissedEventTrigger` | `"OnBottomSheetDismissed"` | DismissBottomSheetEventSchema |
| `OnNavigationDrawerDismissedEventTrigger` | `"OnNavigationDrawerDismissed"` | DismissNavigationDrawerEventSchema |

---

## Menu Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnMenuItemClickEventTrigger` | `"OnMenuItemClick"` | `itemId: String` | ToggleMenuEventSchema |

---

## Timer Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnCountdownTimerTickEventTrigger` | `"OnCountdownTimerTick"` | StartCountdownTimerEventSchema (each tick) |
| `OnCountdownTimerFinishEventTrigger` | `"OnCountdownTimerFinish"` | StartCountdownTimerEventSchema (end) |

---

## Permission Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnPermissionsAcquiredEventTrigger` | `"OnPermissionsAcquired"` | RequestPermissionEventSchema |
| `OnPermissionsDeniedEventTrigger` | `"OnPermissionsDenied"` | RequestPermissionEventSchema |

---

## Scroll / Page Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnScrolledEventTrigger` | `"OnScrolled"` | `direction: ScrollDirection` (Top, Bottom, Start, End) | ScrollColumnTileEventSchema, ScrollRowTileEventSchema, ScrollPagerTileEventSchema |
| `OnPageChangedEventTrigger` | `"OnPageChanged"` | `direction: Direction` (Start, End, Any, Index(index: Int)) | PagerTileSchema |

---

## Summary: Triggers by Category

| Category | Count |
|---|---|
| User Interaction | 13 |
| Keyboard | 6 |
| Lifecycle / Display | 2 |
| Operation Lifecycle (Start/Success/Failure) | 3 |
| Tile State | 5 |
| Navigation | 6 |
| Data | 4 |
| Network | 5 |
| Image Loading | 3 |
| Overlays | 3 |
| Menu | 1 |
| Timer | 2 |
| Permission | 2 |
| Scroll / Page | 2 |
| **Total** | **56** |
