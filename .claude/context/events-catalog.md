# Mosaic — Events Catalog

All events implement `EventSchema`. Every event inherits these base fields:

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier for this event instance |
| `trigger` | `EventTrigger` | Condition that causes this event to fire |
| `events` | `List<EventSchema>?` | Child events executed after this one completes |

JSON serialization uses `@SerialName` as the `type` discriminator field.

The `events` field enables event chaining: child events are dispatched using specific triggers (e.g., `OnSuccess`, `OnFailure`) after the parent event executes.

---

## Tile Management

### AddTilesEventSchema
**JSON type:** `"AddTiles"`

Inserts one or more tiles into a container tile.

| Field | Type | Description |
|---|---|---|
| `groupingTileId` | `String` | ID of the target container (Column, Row, Box, etc.) |
| `tiles` | `List<TileSchema>` | Tiles to insert |
| `position` | `InsertionPosition` | Where to insert |

`InsertionPosition` (sealed interface):
- `Start` — prepend
- `End` — append
- `BeforeTile(tileId: String)` — before a specific tile
- `AfterTile(tileId: String)` — after a specific tile
- `AtIndex(index: Int)` — at a specific index

**Child trigger used:** `OnTilesAdded`

---

### RemoveTilesEventSchema
**JSON type:** `"RemoveTiles"`

Removes tiles by ID from a container.

| Field | Type | Description |
|---|---|---|
| `groupingTileId` | `String?` | Container scope (null = search globally) |
| `tileIds` | `List<String>` | IDs of tiles to remove |

**Child trigger used:** `OnTilesRemoved`

---

### UpdateTilesEventSchema
**JSON type:** `"UpdateTiles"`

Partially updates tile fields via JSON merge.

| Field | Type |
|---|---|
| `updates` | `List<Update>` |

`Update`: `{ tileId: String, data: Map<String, AnySerializable?> }`

The `data` map keys must match the field names of the target `TileSchema`. Only provided keys are updated; others remain unchanged.

**Child trigger used:** `OnTilesUpdated`

---

### ReplaceTilesEventSchema
**JSON type:** `"ReplaceTiles"`

Replaces all children of a container with a new list.

| Field | Type | Description |
|---|---|---|
| `groupingTileId` | `String?` | Target container (null = screen root) |
| `tiles` | `List<TileSchema>` | New tile list |

**Child trigger used:** `OnTilesReplaced`

---

### WipeTilesEventSchema
**JSON type:** `"WipeTiles"`

Removes all children from a container.

| Field | Type |
|---|---|
| `groupingTileId` | `String` |

**Child trigger used:** `OnTilesWiped`

---

### ReloadLazyTilesEventSchema
**JSON type:** `"ReloadLazyTiles"`

Forces a `LazyTilesTileSchema` to re-fetch its content.

| Field | Type |
|---|---|
| `lazyTileId` | `String` |

---

### UpdateEventsEventSchema
**JSON type:** `"UpdateEvents"`

Partially updates event fields via JSON merge.

| Field | Type |
|---|---|
| `updates` | `List<Update>` |

`Update`: `{ eventId: String, data: Map<String, AnySerializable?> }`

**Child trigger used:** `OnTilesUpdated`

---

## Navigation

### NavigateEventSchema
**JSON type:** `"Navigate"`

Navigates to a new screen within a named navigator.

| Field | Type | Default |
|---|---|---|
| `destination` | `String` | required — target screen ID |
| `navigatorId` | `String` | required — must match a registered NavigationController |
| `popUpTo` | `PopUpTo?` | `null` |
| `data` | `Map<String, AnySerializable>?` | `null` — passed as navigation data |

`PopUpTo`: `{ destination: String, inclusive: Boolean }`

**Child trigger used:** `OnNavigation`

---

### NavigateUpEventSchema
**JSON type:** `"NavigateUp"`

Pops the back stack by one entry.

| Field | Type |
|---|---|
| `navigatorId` | `String` |

**Child trigger used:** `OnNavigation`

---

## Screen

### GetScreenEventSchema
**JSON type:** `"GetScreen"`

Fetches screen content from the server for the current screen ID.

No additional fields.

**Child triggers used:** `OnSuccess` (with `ScreenModel`), `OnFailure`

---

### RefreshScreenEventSchema
**JSON type:** `"RefreshScreen"`

Re-fetches the current screen from the server.

No additional fields.

**Child triggers used:** `OnSuccess`, `OnFailure`

---

### ChangeScreenStateEventSchema
**JSON type:** `"ChangeScreenState"`

Transitions the screen to a given state directly (without a network call).

| Field | Type |
|---|---|
| `state` | `State` (sealed interface) |

`State`:
- `Success(data: ScreenData?)` — `ScreenData` contains `tiles`, `navigationDrawerTiles?`, `events?`
- `Failure`
- `Initial`

---

## Data

### SendDataEventSchema
**JSON type:** `"SendData"`

Stores data in `DataMailer` under a key for cross-screen or cross-event retrieval.

| Field | Type |
|---|---|
| `dataKey` | `String` |
| `data` | `AnySerializable?` |

**Child trigger used:** `OnDataSent`

---

### CheckForReceivedDataEventSchema
**JSON type:** `"CheckForReceivedData"`

Reads data from `DataMailer` by key. Fires `OnDataReceived` if data exists.

| Field | Type |
|---|---|
| `dataKey` | `String` |

**Child trigger used:** `OnDataReceived`

---

### GetDataEventSchema
**JSON type:** `"GetData"`

Reads data from a `DataSourceSchema` using an `AccessModeSchema`.

| Field | Type |
|---|---|
| `readings` | `List<Reading>` |

`Reading`: `{ dataSource: DataSourceSchema, accessMode: AccessModeSchema }`

`DataSourceSchema` (sealed interface):
- `PlainDataBase` — flat persistent DB
- `SegmentedDataBase(segmentId: String)` — segmented persistent DB
- `ScreenPlainData` — screen-scoped plain memory
- `ScreenSegmentedData(segmentId: String)` — screen-scoped segmented memory
- `ScreenNavigationData` — data passed via navigation
- `Tile(tileId: String, dataKey: String)` — reads a specific key from a tile's exposed value map via `TilesValueProducer.getValueWithKey`; returns `Map<String, Any>?`

`AccessModeSchema` (sealed interface):
- `Full` — returns all data as a map (for `Tile`: spreads the returned map into the accumulator)
- `Single(dataId: String)` — returns one value (for `Tile`: stores the returned map under `dataId`)
- `Batch(dataIds: List<String>, allowMissingData: Boolean, unwrapValuesToList: Boolean)` — returns multiple values (for `Tile`: returns the map from `getValueWithKey`; `dataIds` are applied as keys to filter the result)

**Note:** This is a complex builder scenario. Always study `GetDataEventBuilder` before modifying.

**Child triggers used:** `OnStart`, `OnSuccess`, `OnFailure`

---

### UpdateDataEventSchema
**JSON type:** `"UpdateData"`

Writes or updates data in a `DataSourceSchema`.

| Field | Type |
|---|---|
| `updates` | `List<Update>` |

`Update`: `{ dataSource: DataSourceSchema, accessMode: AccessModeSchema }`

**Child trigger used:** `OnDataUpdated`

---

### RemoveDataEventSchema
**JSON type:** `"RemoveData"`

Deletes data from a `DataSourceSchema`.

| Field | Type |
|---|---|
| `deletions` | `List<Deletion>` |

`Deletion`: `{ dataSource: DataSourceSchema, accessMode: AccessModeSchema }`

**Child trigger used:** `OnDataRemoved`

---

### ProcessDataEventSchema
**JSON type:** `"ProcessData"`

Passes the current `incomingData` to a registered `DataProcessor` by ID.

| Field | Type |
|---|---|
| `processWith` | `String` — ID of a registered `DataProcessor` |

Built-in processor: `"EVENT_RUNNER"` — deserializes incoming data as events and executes them inline.

---

### EvaluateDataEventSchema
**JSON type:** `"EvaluateData"`

Evaluates a boolean expression against available data and fires `OnSuccess` or `OnFailure`.

| Field | Type |
|---|---|
| `expression` | `Expression` (sealed interface, recursive) |

`Expression`:
- `NotExpression(expression: Expression)`
- `OrExpression(leftExpression, rightExpression)`
- `AndExpression(leftExpression, rightExpression)`
- `DataExpression(data: Data, operation: Operation)` — compares a data value using typed operations (String, Int, Long, Float, Double, Boolean, Map, List, LocalDateTime)

`DataExpression.Data`:
- `IncomingData` — uses the current `incomingData` from the event scope
- `DataSourceData(reading: Reading)` — reads from a `DataSourceSchema` using an `AccessModeSchema`; supports all `DataSourceSchema` variants including `Tile(tileId, dataKey)`

**Note:** Complex nested structure. Study the existing builder and schema thoroughly before generating code involving `EvaluateData`.

**Child triggers used:** `OnSuccess`, `OnFailure`

---

## Networking

### SendNetworkRequestEventSchema
**JSON type:** `"SendNetworkRequest"`

Sends an HTTP request.

| Field | Type | Default |
|---|---|---|
| `url` | `String` | required |
| `method` | `HttpMethod` | required |
| `body` | `AnySerializable?` | `null` |
| `headers` | `Map<String, String>?` | `null` |

**Child triggers used:** `OnStart`, `OnSuccess` (with response body), `OnFailure`, `OnNetworkResponse(httpCode)`

---

### DownloadFileEventSchema
**JSON type:** `"DownloadFile"`

Downloads a file with progress reporting.

| Field | Type | Default |
|---|---|---|
| `url` | `String` | required |
| `method` | `HttpMethod` | required |
| `body` | `AnySerializable?` | `null` |
| `headers` | `Map<String, String>?` | `null` |

**Child triggers used:** `OnStart`, `OnDownloadProgress`, `OnDownloadFinish`, `OnDownloadFailure`

---

## File System

### SaveFileEventSchema
**JSON type:** `"SaveFile"`

Saves the `incomingData` (as `ByteArray`) to the file system.

| Field | Type | Default |
|---|---|---|
| `fileName` | `String` | required |
| `overrideIfExists` | `Boolean` | required |

**Child triggers used:** `OnSuccess`, `OnFailure`

---

### GetFileEventSchema
**JSON type:** `"GetFile"`

Reads a file from the file system. Passes `ByteArray` as data to child events on success.

| Field | Type |
|---|---|
| `fileName` | `String` |

**Child triggers used:** `OnSuccess` (with `ByteArray`), `OnFailure`

---

### DeleteFileEventSchema
**JSON type:** `"DeleteFile"`

Deletes a file from the file system.

No additional fields.

**Child triggers used:** `OnSuccess`, `OnFailure`

---

## Overlays

### DisplayDialogEventSchema
**JSON type:** `"DisplayDialog"`

Shows a dialog with the provided tile tree as content.

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `isCancellable` | `Boolean` |
| `usePlatformDefaultWidth` | `Boolean` |

---

### DismissDialogEventSchema
**JSON type:** `"CloseBottomSheet"` *(note: JSON name is inconsistent, verify in MosaicSerializer)*

Dismisses the currently displayed dialog.

No additional fields.

**Child trigger used:** `OnDialogDismissed`

---

### DisplayBottomSheetEventSchema
**JSON type:** `"DisplayBottomSheet"`

Shows a modal bottom sheet.

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `isCancellable` | `Boolean` |
| `fill` | `Boolean` |

---

### DismissBottomSheetEventSchema
**JSON type:** `"DismissBottomSheet"`

Dismisses the currently displayed bottom sheet.

No additional fields.

**Child trigger used:** `OnBottomSheetDismissed`

---

### DisplayNavigationDrawerEventSchema
**JSON type:** `"DisplayNavigationDrawer"`

Opens the navigation drawer.

No additional fields.

---

### DismissNavigationDrawerEventSchema
**JSON type:** `"DismissNavigationDrawer"`

Closes the navigation drawer.

No additional fields.

**Child trigger used:** `OnNavigationDrawerDismissed`

---

## Events / Meta

### TriggerEventEventSchema
**JSON type:** `"TriggerEvent"`

Manually fires another event by its ID.

| Field | Type |
|---|---|
| `eventId` | `String` |

---

## Menu

### ToggleMenuEventSchema
**JSON type:** `"ToggleMenu"`

Toggles the expanded state of a `MenuTileSchema`.

| Field | Type |
|---|---|
| `menuId` | `String` |

**Child trigger used:** `OnMenuItemClick`

---

## Time

### StartCountdownTimerEventSchema
**JSON type:** `"StartCountdownTimer"`

Starts a countdown timer. Fires tick and finish events.

| Field | Type |
|---|---|
| `setupTimeInSeconds` | `Long` |

**Child triggers used:** `OnCountdownTimerTick` (each second), `OnCountdownTimerFinish`

---

## Scroll

### ScrollColumnTileEventSchema
**JSON type:** `"ScrollColumn"`

| Field | Type |
|---|---|
| `tileId` | `String` |
| `where` | `Where`: `Top`, `Bottom`, `To(index: Int)` |
| `smoothly` | `Boolean` |

**Child trigger used:** `OnScrolled`

---

### ScrollRowTileEventSchema
**JSON type:** `"ScrollRow"`

| Field | Type |
|---|---|
| `tileId` | `String` |
| `where` | `Where`: `Start`, `End`, `To(index: Int)` |
| `smoothly` | `Boolean` |

**Child trigger used:** `OnScrolled`

---

### ScrollPagerTileEventSchema
**JSON type:** `"ScrollPager"`

| Field | Type |
|---|---|
| `tileId` | `String` |
| `where` | `Where`: `Begin`, `PreviousPage`, `NextPage`, `End` |

**Child trigger used:** `OnScrolled`

---

## Pull to Refresh

### StopRefreshingEventSchema
**JSON type:** `"StopRefreshing"`

Stops the pull-to-refresh animation on a `PullToRefreshTileSchema`.

| Field | Type |
|---|---|
| `tileId` | `String` |

---

## Security

### RequestPermissionEventSchema
**JSON type:** `"RequestPermission"`

Requests one or more system permissions from the user.

| Field | Type |
|---|---|
| `permissions` | `List<Permissions>` |

`Permissions`: `CAMERA`, `GALLERY`, `STORAGE`, `MICROPHONE`, `LOCATION`, `NOTIFICATION`, `CONTACTS`

**Child triggers used:** `OnPermissionsAcquired`, `OnPermissionsDenied`

---

## System

### CheckIfHasInternetConnectionEventSchema
**JSON type:** `"CheckIfHasInternetConnection"`

Checks whether the device has an active internet connection.

No additional fields.

**Child triggers used:** `OnSuccess`, `OnFailure`
