# Mosaic — EventTriggers Catalog

> Atualizado: jun/2026. Validado contra MosaicSerializer.defaultEventTriggerSerializers, anotações `@Triggers` nos Schemas, e implementações de TileRenderer/EventRunner.

`EventTrigger` é uma interface marcador. Cada `EventSchema` tem um trigger que define **quando** ele dispara. Eventos filhos no campo `events` são despachados usando triggers específicos após o evento pai executar.

JSON serialization usa `@SerialName` como discriminador de tipo (`"type"` field).

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
| `OnClickEventTrigger` | `"OnClick"` | — | Button, IconButton, FAB, Row, Column, Box, Card, SuggestionChip, AssistChip, FilterChip, InputChip |
| `OnLongPressEventTrigger` | `"OnLongPress"` | — | Button, Row, Column, Box |
| `OnCheckEventTrigger` | `"OnCheck"` | — | Checkbox, Switch, FilterChip, InputChip (quando checked = true) |
| `OnUncheckEventTrigger` | `"OnUncheck"` | — | Checkbox, Switch, FilterChip, InputChip (quando checked = false) |
| `OnCheckChangedEventTrigger` | `"OnCheckChanged"` | — | Checkbox, Switch, FilterChip, InputChip (qualquer mudança) |
| `OnSelectEventTrigger` | `"OnSelected"` | — | RadioButton (ao tocar) |
| `OnUnselectEventTrigger` | `"OnUnselected"` | — | RadioButton (auto-deselecionado quando outro do grupo é selecionado) |
| `OnSelectChangedEventTrigger` | `"OnSelectChanged"` | — | Componentes de seleção |
| `OnTextChangedEventTrigger` | `"OnTextChanged"` | — | TextField, SearchBar (a cada keystroke) |
| `OnSearchEventTrigger` | `"OnSearch"` | — | SearchBar (ação IME Search submetida) |
| `OnQueryChangedEventTrigger` | `"OnQueryChanged"` | — | SearchBar |
| `OnQueryClearedEventTrigger` | `"OnQueryCleared"` | — | SearchBar (botão limpar tapped) |
| `OnLeadingIconClickEventTrigger` | `"OnLeadingIconClick"` | — | TextField (se clickableLeadingIcon=true), SearchBar |
| `OnTrailingIconClickEventTrigger` | `"OnTrailingIconClick"` | — | TextField (se clickableTrailingIcon=true) |
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
| `OnDisplayEventTrigger` | `"OnDisplay"` | Tile entra na composição (DisposableEffect enter) |
| `InlineEventTrigger` | `"Inline"` | Executado imediatamente quando o evento é processado, independente de contexto |
| `OnWidthBreakpointSatisfiedEventTrigger` | `"OnWidthBreakpointSatisfied"` | AdaptiveVisibility — restrição de largura satisfeita (primeira avaliação e em mudanças) |
| `OnWidthBreakpointNotSatisfiedEventTrigger` | `"OnWidthBreakpointNotSatisfied"` | AdaptiveVisibility — restrição de largura não satisfeita |
| `OnHeightBreakpointSatisfiedEventTrigger` | `"OnHeightBreakpointSatisfied"` | AdaptiveVisibility — restrição de altura satisfeita |
| `OnHeightBreakpointNotSatisfiedEventTrigger` | `"OnHeightBreakpointNotSatisfied"` | AdaptiveVisibility — restrição de altura não satisfeita |

---

## Operation Lifecycle Triggers

Usados como child event triggers após operações assíncronas (network, data, file, screen).

| Trigger | @SerialName | Fired When |
|---|---|---|
| `OnStartEventTrigger` | `"OnStart"` | Operação inicia |
| `OnSuccessEventTrigger` | `"OnSuccess"` | Operação completa com sucesso |
| `OnFailureEventTrigger` | `"OnFailure"` | Operação falha; `incomingData` = a `Throwable` |

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
| `OnNavigationEntryChangedEventTrigger` | `"OnNavigationEntryChanged"` | — | NestedNavigationGraphTileSchema (em qualquer mudança de entry) |
| `OnNavigationEntrySetEventTrigger` | `"OnNavigationEntrySet"` | `screenId: String` | NestedNavigationGraphTileSchema (quando ativado em screen específica) |
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
| `OnNetworkResponseTrigger` | `"OnNetworkResponse"` | `httpCode: Int` | SendNetworkRequestEventSchema (match de HTTP code específico) |
| `OnNetworkFailureEventTrigger` | `"OnNetworkFailure"` | `httpCode: Int` | SendNetworkRequestEventSchema (falha com código HTTP específico) |
| `OnDownloadProgressEventTrigger` | `"OnDownloadProgress"` | — | DownloadFileEventSchema (cada update de progresso) |
| `OnDownloadFinishEventTrigger` | `"OnDownloadFinish"` | — | DownloadFileEventSchema |
| `OnDownloadFailureEventTrigger` | `"OnDownloadFailure"` | — | DownloadFileEventSchema |
| `OnUploadProgressEventTrigger` | `"OnUploadProgress"` | — | SendFileEventSchema (incomingData = Int 0–100, fired apenas quando o % muda) |

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
| `OnSnackbarActionEventTrigger` | `"OnSnackbarAction"` | DisplaySnackbarEventSchema (ação da snackbar clicada) |
| `OnSnackbarDismissedEventTrigger` | `"OnSnackbarDismissed"` | DisplaySnackbarEventSchema (snackbar dispensada) |

---

## Menu Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnMenuItemClickEventTrigger` | `"OnMenuItemClick"` | `itemId: String` | MenuTileSchema |

---

## Timer Triggers

| Trigger | @SerialName | Fired By |
|---|---|---|
| `OnCountdownTimerTickEventTrigger` | `"OnCountdownTimerTick"` | StartCountdownTimerEventSchema (cada tick) |
| `OnCountdownTimerFinishEventTrigger` | `"OnCountdownTimerFinish"` | StartCountdownTimerEventSchema (fim) |

---

## Permission Triggers

| Trigger | @SerialName | Fired By | Notes |
|---|---|---|---|
| `OnPermissionsAcquiredEventTrigger` | `"OnPermissionsAcquired"` | RequestPermissionEventSchema | Todas as permissões concedidas |
| `OnPermissionsDeniedEventTrigger` | `"OnPermissionsDenied"` | RequestPermissionEventSchema | Negado permanentemente (Android: `shouldShowRationale == false`) |
| `OnPermissionRationaleEventTrigger` | `"OnPermissionRationale"` | RequestPermissionEventSchema | **Android only** — usuário negou uma vez, pode pedir novamente com explicação (`shouldShowRationale == true`) |

---

## Scroll / Page Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnScrolledEventTrigger` | `"OnScrolled"` | `direction: ScrollDirection` (Top, Bottom, Start, End) | Column (scrollable), Row (scrollable), LazyColumn, LazyRow |
| `OnScrollThresholdReachedEventTrigger` | `"OnScrollThresholdReached"` | — | LazyColumnTileSchema, LazyRowTileSchema (quando fração `scrollThreshold` é atingida) |
| `OnPageChangedEventTrigger` | `"OnPageChanged"` | `direction: Direction` (Start, End, Any, Index(index: Int)) | PagerTileSchema |

---

## LazyTiles Loading Triggers

Disparados pelo `LazyTilesTileSchema` durante o ciclo de vida do fetch remoto de tiles.

| Trigger | @SerialName | Fired When |
|---|---|---|
| `OnLoadTilesStartEventTrigger` | `"OnLoadTilesStart"` | Fetch inicia |
| `OnLoadTilesSuccessEventTrigger` | `"OnLoadTilesSuccess"` | Tiles carregados com sucesso |
| `OnLoadTilesFailureEventTrigger` | `"OnLoadTilesFailure"` | Fetch falhou |

---

## System Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnSystemBroadcastEventTrigger` | `"OnSystemBroadcast"` | `broadcastId: String` | `BroadcastToSystemEventSchema` — despacha broadcast para a screen; eventos na screen com este trigger e matching `broadcastId` são executados |

---

## Summary: Triggers by Category

| Category | Count |
|---|---|
| User Interaction | 15 |
| Keyboard | 6 |
| Lifecycle / Display | 6 |
| Operation Lifecycle | 3 |
| Tile State | 5 |
| Navigation | 6 |
| Data | 4 |
| Network | 7 |
| Image Loading | 3 |
| Overlay | 5 |
| Menu | 1 |
| Timer | 2 |
| Permission | 3 |
| Scroll / Page | 3 |
| LazyTiles Loading | 3 |
| System | 1 |
| **Total** | **73** |

---

## Dropdown List Triggers

| Trigger | @SerialName | Fields | Fired By |
|---|---|---|---|
| `OnDropdownListItemSelectedEventTrigger` | `"OnDropdownListItemSelected"` | `id: String` | `DropdownListTileSchema` — fired when user selects an option; incomingData = selected option `id` |
| `OnDropdownListOpenEventTrigger` | `"OnDropdownListOpen"` | — | `DropdownListTileSchema` — fired when dropdown menu opens |
| `OnDropdownListCloseEventTrigger` | `"OnDropdownListClose"` | — | `DropdownListTileSchema` — fired when dropdown menu closes |
