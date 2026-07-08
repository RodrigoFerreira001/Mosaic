# Mosaic — Events Reference

All events implement `EventSchema`. For each event: purpose, fields, incomingData consumed, triggers fired, failure scenarios, and a real DSL example.

**Base fields (every event):** `id: String` (use `randomId()`), `trigger: EventTrigger`, `events: List<EventSchema>?` (chained events)

**Global data helpers** — used across data/networking events:
```kotlin
// DataSource — import dev.catbit.mosaic.server.builder.data.DataSourceHelper (or wildcard)
applicationPlainData()              // app-scoped flat map (all screens, runtime lifetime)
applicationSegmentedData(segmentId) // app-scoped segment (all screens, runtime lifetime)
screenPlainData()                   // screen-scoped flat map
screenSegmentedData(segmentId)      // screen-scoped segment
screenNavigationData()              // navigation arguments
segmentedDataBase(segmentId)        // persistent DB segment
plainDataBase()                     // persistent DB flat
tile(tileId, dataKey)               // value exposed by a tile

// AccessMode — import dev.catbit.mosaic.server.builder.data.AccessModeHelper (or wildcard)
fullAccessMode()                    // entire data source as Map
singleAccessMode(dataId)            // single key → scalar value
batchAccessMode(listOf("a","b"))    // subset of keys → Map
batchAccessMode(listOf("a","b"), allowMissingData = true)
batchAccessMode(listOf("a","b"), unwrapValuesToList = true)  // Map values as List
```

---

## Tile Management

### AddTiles
**Purpose:** Inserts one or more new tiles into a grouping tile's child list at a specified insertion position — without a full screen reload.
**When to use:** When you need to dynamically append, prepend, or splice tiles into an already-rendered container (e.g., append a new list item after a form submission).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd`, `insertAtStart`, `insertBeforeTile`, `insertAfterTile`, `insertAtIndex`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | ID of the container tile (Column, Row, etc.) |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Tiles to insert |
| `position` | `InsertionPosition` | `insertAtEnd()` | Start, End, BeforeTile(id), AfterTile(id), AtIndex(n) |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()` — tiles added. `onFailure(exception)` — target container not found (TileNotFoundException).
**Failure scenarios:** `groupingTileId` does not match any tile in the current tree.
**Notes:** Tile IDs must be unique and fully specified by the server. `BeforeTile`/`AfterTile` with an unknown sibling ID may silently append instead.

**Example:**
```kotlin
AddTiles(
    trigger = EventTriggers.onClick(),
    groupingTileId = "list_container",
    position = insertAtEnd(),
    tiles = {
        SimpleText(id = randomId(), text = "New item", trigger = EventTriggers.inline())
    },
    events = {
        // no chained events needed
    }
)
```

---

### RemoveTiles
**Purpose:** Removes specific tiles from a grouping tile's child list by their IDs, without a full screen reload.
**When to use:** When you know the exact IDs of tiles to remove (e.g., deleting a single list item by ID). For clearing all children, prefer `WipeTiles`.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile ID |
| `tileIds` | `List<String>` | required | IDs to remove; unknown IDs are silently ignored |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()` — removal complete. `onFailure(exception)` — container not found.
**Failure scenarios:** `groupingTileId` not found → TileNotFoundException.

**Example:**
```kotlin
RemoveTiles(
    trigger = EventTriggers.onClick(),
    groupingTileId = "item_list",
    tileIds = listOf("item_42")
)
```

---

### UpdateTiles
**Purpose:** Applies data-only patches to existing tiles without altering the tile tree structure. Each update targets one tile by ID and supplies either the current `incomingData` or a static inline map of key-value overrides.
**When to use:** When you need to change a tile's mutable properties (text, color, visibility, `filterChildrenByTerm`, etc.) in response to user action, without replacing the tile.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData`, `incomingTileUpdateData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `updates` | `UpdateTilesUpdateBuilderScope.() -> Unit` | required | One `update(tileId, updateData)` per target tile |

`updateData` variants:
- `incomingTileUpdateData()` — uses current `incomingData` (coerced to `Map<String, Any>`)
- `inlineTileUpdateData("key" to value, ...)` — static key-value map

**incomingData consumed:** Used when `incomingTileUpdateData()` is specified. Runner coerces to `Map<String, Any>`; silently skipped if cast fails.
**Triggers fired:** `onTilesUpdated()` then `onSuccess()` — updates applied. `onFailure(exception)` — a tile ID not found.
**Failure scenarios:** Any `tileId` in `updates` not found → TileNotFoundException.
**Notes:** Updates are applied in list order; same tile listed twice: second update wins on key collision.

**Example:**
```kotlin
UpdateTiles(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(
            tileId = "search_list",
            updateData = inlineTileUpdateData("filterChildrenByTerm" to query)
        )
        update(
            tileId = "result_count",
            updateData = inlineTileUpdateData("text" to "$count results")
        )
    }
)
```

---

### ReplaceTiles
**Purpose:** Atomically replaces the entire child list of a grouping tile with a new server-supplied set — wipe-then-add in one operation.
**When to use:** When the full content of a container needs to change (e.g., switching tab content, reloading a list with fresh data from the server).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.ReplaceTiles`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile ID |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | New tile list; empty list = wipe |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()` — replacement done. `onFailure(exception)` — container not found.
**Notes:** Unlike `AddTiles`, there is no position — new tiles always fully replace the existing list.

**Example:**
```kotlin
ReplaceTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "tab_content",
    tiles = {
        items.forEach { item ->
            Card(id = item.id) { /* ... */ }
        }
    }
)
```

---

### WipeTiles
**Purpose:** Removes all children from a grouping tile in a single operation, leaving it empty.
**When to use:** When clearing an entire container is the goal (more efficient than `RemoveTiles` because no IDs need to be enumerated).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.WipeTiles`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile ID |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()`. `onFailure(exception)` — container not found.
**Notes:** Wiping an already-empty container is a no-op. Follow with `AddTiles` or `ReplaceTiles` to repopulate.

**Example:**
```kotlin
WipeTiles(
    trigger = EventTriggers.onClick(),
    groupingTileId = "search_results"
)
```

---

### ReloadLazyTiles
**Purpose:** Sends a reload signal to a lazy tile (LazyColumn, LazyRow), causing it to discard its content and re-fetch tiles from scratch — including resetting pagination.
**When to use:** After a data mutation (create/delete/update) that should cause a lazy list to re-fetch from the beginning. Prefer this over `WipeTiles` for lazy tiles because it lets the tile manage its own reload lifecycle.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `lazyTileId` | `String` | required | ID of the lazy tile to reload |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()`. `onFailure(exception)` — lazy tile not found.

**Example:**
```kotlin
ReloadLazyTiles(
    trigger = EventTriggers.onSuccess(),
    lazyTileId = "environment_list"
)
```

---

### CheckIfTileContainsChildren
**Purpose:** Checks synchronously whether a container tile holds all of the specified child IDs as direct children.
**When to use:** When you need to conditionally branch based on whether certain tiles are present in a container (e.g., before adding a tile that shouldn't be added twice).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.CheckIfTileContainsChildren`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile ID |
| `childrenIds` | `List<String>` | required | IDs to check; empty list always succeeds |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()` — all IDs found. `onFailure()` — one or more IDs missing.
**Notes:** Checks only direct children, not deeper descendants.

**Example:**
```kotlin
CheckIfTileContainsChildren(
    trigger = EventTriggers.onClick(),
    groupingTileId = "pinned_list",
    childrenIds = listOf("item_42"),
    events = {
        // onSuccess = already pinned; onFailure = not yet pinned, add it
        AddTiles(trigger = EventTriggers.onFailure(), groupingTileId = "pinned_list", tiles = { /* ... */ })
    }
)
```

---

### GetTileChildrenCount
**Purpose:** Returns the current number of direct children of a container tile as an `Int` via `onSuccess` incomingData.
**When to use:** When event logic depends on knowing how many items are in a list (e.g., disabling a button when the list is empty).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.tiles.GetTileChildrenCount`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile ID |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess(Int)` — child count. `onFailure()` — container not found.
**Notes:** Empty container fires `onSuccess(0)`.

**Example:**
```kotlin
GetTileChildrenCount(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "selected_items",
    events = {
        EvaluateData(
            trigger = EventTriggers.onSuccess(),
            expression = incomingData().isEqualsTo(0),
            events = {
                UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                    update("confirm_btn", inlineTileUpdateData("visibility" to "GONE"))
                })
            }
        )
    }
)
```

---

## Event / Meta

### RunEvents
**Purpose:** Executes all child events unconditionally. Acts as a grouping container for event chains that should always run together from a single trigger.
**When to use:** When multiple independent events must fire on the same trigger and semantic chaining (`onSuccess`/`onFailure`) between them would be misleading.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents`

**incomingData consumed:** Not used. Passed as-is to child events.
**Triggers fired:** `onSuccess()` — after all child events dispatched.
**Notes:** Transparent dispatcher — does not transform incomingData.

**Example:**
```kotlin
RunEvents(
    trigger = EventTriggers.onClick(),
    events = {
        UpdateTiles(trigger = EventTriggers.inline(), updates = { /* ... */ })
        UpdateData(trigger = EventTriggers.inline(), updates = { /* ... */ })
        DisplaySnackbar(trigger = EventTriggers.inline(), message = "Saved")
    }
)
```

---

### TriggerEvent
**Purpose:** Looks up another registered event by its ID and runs it inline within the current execution context — acts as a reusable subroutine call.
**When to use:** When the same event logic must be invoked from multiple call sites without duplicating event definitions (e.g., a shared "show error snackbar" event).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `eventId` | `String` | required | ID of the event to invoke |

**incomingData consumed:** Not used by this event; the resolved target event reads from its own holder.
**Triggers fired:** `onSuccess()` — target found and executed. `onFailure(exception)` — event ID not found, or target throws.
**Notes:** Target executes synchronously (`runEventInline`) — its trigger chain completes before the caller continues. Avoid circular references.

**Example:**
```kotlin
val showErrorId = randomId()

// Define the reusable event elsewhere on the tile:
DisplaySnackbar(id = showErrorId, trigger = EventTriggers.onSystemBroadcast("show_error"), message = "Something went wrong")

// Call it from multiple places:
SendNetworkRequest(trigger = EventTriggers.onClick(), url = "/api/data", method = HttpMethod.GET, events = {
    TriggerEvent(trigger = EventTriggers.onFailure(), eventId = showErrorId)
})
```

---

### UpdateEvents
**Purpose:** Patches the `incomingData` map of one or more already-registered event holders without a network round-trip — used to pre-populate event data before it fires.
**When to use:** When you need to inject contextual data into an event at runtime (e.g., storing a selected item's ID into a delete-event holder when the user taps on a list item).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `updates` | `UpdateEventsUpdateBuilderScope.() -> Unit` | required | One `update(eventId, data)` per event to patch |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()`. `onFailure()` if an error occurs during update processing.
**Notes:** Unknown `eventId` is silently skipped. Updates applied in list order; no rollback on partial failure.

**Example:**
```kotlin
UpdateEvents(
    trigger = EventTriggers.onClick(),
    updates = {
        update(
            eventId = deleteEventId,
            data = mapOf("itemId" to item.id)
        )
    }
)
```

---

## Navigation

### Navigate
**Purpose:** Instructs a navigator to push a destination onto its back stack, optionally popping prior entries and carrying data to the new destination.
**When to use:** Any navigation between screens within a navigator scope.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate`

**Helper functions** (same import path):
- `poppingUpTo(destination: String, inclusive: Boolean)` — builds the `PopUpTo` argument

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `destination` | `String` | required | Route/ID of the destination |
| `navigatorId` | `String` | required | ID of the registered navigator |
| `popUpTo` | `PopUpTo?` | `null` | Optional: pop back stack to `destination` before navigating |
| `data` | `Map<String, Any>?` | `null` | Extra data merged with incomingData for the destination |

**incomingData consumed:** Cast to `Map<String, Any>` and merged with `data` (schema wins on key conflicts) → becomes `navigationData` of destination.
**Triggers fired:** `onSuccess()`. `onFailure()` — navigator not found.

**Example:**
```kotlin
Navigate(
    trigger = EventTriggers.onClick(),
    destination = "environment_detail",
    navigatorId = "main_navigator",
    popUpTo = poppingUpTo(destination = "home", inclusive = false),
    data = mapOf("environmentId" to env.id)
)
```

---

### NavigateUp
**Purpose:** Pops the current destination off a navigator's back stack — equivalent to pressing the system back button within that navigator's scope.
**When to use:** Close dialogs/sheets navigated to as destinations, or implement a back button in a custom top bar.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `navigatorId` | `String` | required | ID of the registered navigator |

**incomingData consumed:** Not used.
**Triggers fired:** `onSuccess()`. `onFailure()` — navigator not found.

**Example:**
```kotlin
NavigateUp(
    trigger = EventTriggers.onClick(),
    navigatorId = "main_navigator"
)
```

---

## Screen

### GetScreen
**Purpose:** Fetches a screen definition from the server using the current screen's ID and exposes the result as `incomingData` for downstream events. Does NOT automatically apply the result.
**When to use:** When you need the fetched `ScreenModel` for inspection or conditional logic before deciding how to apply it. For a simple "reload screen", prefer `RefreshScreen`.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `method` | `HttpMethod` | `HttpMethod.GET` | HTTP method |
| `body` | `AnySerializable?` | `null` | Request body (schema wins over holder) |
| `headers` | `Map<String, String>?` | `null` | Merged with holder headers |

**incomingData consumed:** Not consumed directly; use `SetIncomingDataToNetworkParamsHolderBody/Headers` before this event to pass incomingData as body/headers.
**Triggers fired:** `onSuccess(ScreenModel)` — valid screen fetched. `onNetworkFailure(statusCode)` — non-2xx with matching child trigger. `onFailure(Throwable)` — all other failures.
**Notes:** Chain `ChangeScreenState(successState())` on `onSuccess()` to apply the result.

**Example:**
```kotlin
GetScreen(
    trigger = EventTriggers.onDisplay(),
    method = HttpMethod.GET,
    events = {
        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
    }
)
```

---

### RefreshScreen
**Purpose:** Reloads the current screen — resets it to `Initial` state, fetches its definition from the server, and automatically applies the result. Fully self-contained.
**When to use:** Simple pull-to-refresh or "retry" pattern where you want to re-fetch and re-render the whole screen without manual state transitions.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen`

**Fields:** Same as `GetScreen` (`method`, `body`, `headers`).

**incomingData consumed:** Not consumed directly.
**Triggers fired:** `onSuccess(ScreenModel)` — fetched and applied (screen → `Success`). `onNetworkFailure(statusCode)`. `onFailure(Throwable)` — screen → `Failure`.
**Notes:** Screen resets to `Initial` before the request begins (implicit loading indicator). No `ChangeScreenState` needed.

**Example:**
```kotlin
RefreshScreen(
    trigger = EventTriggers.onPull(),
    events = {
        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "pull_refresh")
        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "pull_refresh")
    }
)
```

---

### ChangeScreenState
**Purpose:** Immediately transitions the screen to `Success`, `Failure`, or `Initial` without making a network call.
**When to use:** To manually drive screen state transitions — apply a `ScreenModel` received from `GetScreen`, show loading (`Initial`), or show an error screen (`Failure`).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.screen.successState`, `failureState`, `initialState`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `state` | `State` | required | `successState(data?)`, `failureState()`, `initialState()` |

**incomingData consumed:** When `successState(data = null)`, incomingData is cast to `ScreenModel` as the screen content. If cast fails → `onFailure(IllegalArgumentException)`.
**Triggers fired:** `onSuccess()`. `onFailure(exception)` — only when `successState(null)` and incomingData isn't a `ScreenModel`.

**Example:**
```kotlin
ChangeScreenState(trigger = EventTriggers.onClick(), state = initialState(), events = {
    GetScreen(trigger = EventTriggers.onSuccess(), events = {
        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
    })
})
```

---

## Data

### GetData
**Purpose:** Reads data from one or more sources (tile data, screen DataHolder, broadcast data, persistent DB) and forwards the aggregated result to downstream events as `incomingData`.
**When to use:** Any time an event chain needs to read stored state before acting — e.g., reading form field values before sending a network request, or checking session tokens before adding auth headers.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.GetData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `readings` | `GetDataReadingBuilderScope.() -> Unit` | required | One or more `reading(dataSource, accessMode)` calls |

`DataSource` helpers: `applicationPlainData()`, `applicationSegmentedData(segment)`, `screenPlainData()`, `screenSegmentedData(segment)`, `screenNavigationData()`, `tileData(tileId)`, `broadcastData()`, `plainDataBase()`, `segmentedDataBase(segment)`
`AccessMode` helpers: `fullAccessMode()`, `singleAccessMode(key)`, `batchAccessMode(listOf(...))`, `batchAccessMode(listOf(...), unwrapValuesToList = true)`

**incomingData consumed:** Not used — produces new data.
**Triggers fired:** `onStart()` — always before reading. `onSuccess(data)` — all readings succeed; data shape: `Map` if any Full/Batch reading, scalar if single `singleAccessMode`. `onFailure(exception)` — any reading fails; stops processing further readings.
**Failure scenarios:** `singleAccessMode` on missing key → `DataNotFoundException`. `batchAccessMode` with `allowMissingData = false` and missing key → `DataNotFoundException`.
**Notes:** Multiple readings: later overwrites earlier on key collision. All I/O on `Dispatchers.IO`.

**`null` values:** `fullAccessMode()` returns the whole map as-is, including keys whose value is explicitly `null` (e.g. `Map<String, Any?>` with a `null` entry from a prior `explicitNullUpdateData` write). `singleAccessMode`/`batchAccessMode` do **not** distinguish "key missing" from "key present but `null`" — either case is treated as not found (`DataNotFoundException`, or silently skipped if `allowMissingData = true`). If you need to read back a `null` value reliably, use `fullAccessMode()`.

**Example:**
```kotlin
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        reading(screenSegmentedData("form"), fullAccessMode())
    },
    events = {
        SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
            SendNetworkRequest(
                trigger = EventTriggers.onSuccess(),
                url = "/api/login",
                method = HttpMethod.POST,
                events = {
                    Navigate(trigger = EventTriggers.onSuccess(), destination = "home", navigatorId = "root")
                    DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Login failed")
                }
            )
        })
    }
)
```

---

### UpdateData
**Purpose:** Writes key-value data into one or more data stores (screen memory or persistent DB).
**When to use:** After receiving data you want to persist (session tokens, user preferences, form state) so it can be retrieved later with `GetData`.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.data.inlineUpdateData`, `incomingUpdateData`, `explicitUpdateData`, `explicitIncomingUpdateData`, `explicitNullUpdateData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `updates` | `UpdateDataUpdateBuilderScope.() -> Unit` | required | One `update(dataSource, updateData)` per write |

`updateData` variants:
- `incomingUpdateData()` — **legacy.** Coerces current `incomingData` to `Map<String, Any>` and writes it, **exploding every top-level key into its own dataId**. Only correct when the map genuinely represents multiple independent dataId → value pairs.
- `inlineUpdateData("key" to value, ...)` — **legacy.** Same explode-by-key behavior, with a static map.
- `explicitUpdateData(dataId, value)` — writes `value` as-is under the exact `dataId` given, with **no inference** on the value's shape. Use this whenever `value` is itself a map that must be stored as one intact record (e.g. a segmented-data row). `value` accepts `null`.
- `explicitIncomingUpdateData(dataId)` — same as `explicitUpdateData`, but writes the current `incomingData` verbatim (not coerced or exploded) under `dataId`.
- `explicitNullUpdateData(dataId)` — shorthand for `explicitUpdateData(dataId, null)`. Writes an explicit `null` under `dataId`.

**⚠️ Legacy pitfall:** `incomingUpdateData()`/`inlineUpdateData(...)` treat every key of the resolved map as a separate dataId. If `incomingData` is a single record like `{"action":"NONE","comment":null,"imageBlob":null,"asBuilt":true}` that should be stored as ONE value under one dataId (typical for segmented data), these legacy variants will incorrectly split it into 4 separate writes. Use `explicitIncomingUpdateData(dataId)` instead in that case.

**incomingData consumed:** When `incomingUpdateData()` — coerced to `Map<String, Any>`, silently skipped if cast fails. When `explicitIncomingUpdateData(dataId)` — used verbatim (any shape), silently skipped only if `incomingData` is null.

**Writing `null`:** Use `explicitUpdateData(dataId, null)` / `explicitNullUpdateData(dataId)` to write a literal `null` under a `dataId` — since the `dataId` is already known, the runner always writes the entry, even when the value is `null` (unlike `incomingUpdateData()`/`inlineUpdateData(...)`, whose map-value entries can legitimately be `null` too, but only for keys already present in the map). **This only works for in-memory data sources** (`screenPlainData()`, `screenSegmentedData(segment)`, `applicationPlainData()`, `applicationSegmentedData(segment)`). Persistent DB sources (`plainDataBase()`, `segmentedDataBase(segment)`) do **not** support `null` values yet — a `null`-valued write targeting one of those is silently skipped (no error, no-op).
**Triggers fired:** `onDataUpdated()` declared but **not fired** by runner. Runner completes without calling triggers.
**Failure scenarios:** None explicit; DB exceptions propagate unchecked.
**Notes:** `ScreenNavigationData`, `Tile`, and `Inline` (constant) data sources are ignored (no-op) as write targets. Updates grouped by source, applied in iteration order. Unlike `GetData`/`RemoveData`, `Update` has no `accessMode: AccessModeSchema` field — targeting is via `dataSource` (which carries `segmentId` for segmented sources) plus `updateData`.

**Example (legacy, safe here because the map keys genuinely are independent dataIds):**
```kotlin
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(
            dataSource = screenSegmentedData("auth"),
            updateData = inlineUpdateData("sessionCookie" to cookie)
        )
    }
)
```

**Example (explicit — writing one whole record under one dataId, e.g. a segmented-data row):**
```kotlin
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(
            dataSource = segmentedDataBase("currentActionPlanning_$planId"),
            updateData = explicitIncomingUpdateData(dataId = "TALA-T_01-8")
        )
    }
)
```

---

### RemoveData
**Purpose:** Deletes data from one or more data stores — single key, batch of keys, or entire store.
**When to use:** Clearing session data on logout, removing a cached value, or resetting a form segment.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.RemoveData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `deletions` | `DeleteDataDeletionBuilderScope.() -> Unit` | required | One `addDeletion(dataSource, accessMode)` per deletion |

**incomingData consumed:** Not used.
**Triggers fired:** `onDataRemoved()` declared but **not fired** by runner.
**Notes:** `ScreenNavigationData` and `Tile` sources ignored. `fullAccessMode()` wipes the entire segment — use with caution.

**Example:**
```kotlin
RemoveData(
    trigger = EventTriggers.onClick(),
    deletions = {
        addDeletion(
            dataSource = segmentedDataBase("auth"),
            accessMode = fullAccessMode()
        )
    }
)
```

---

### SendData
**Purpose:** Posts a value into the `DataMailer` in-process message bus under a named key, making it available for retrieval on any screen via `CheckForReceivedData`.
**When to use:** Passing data between screens when navigating — the sending screen posts data before navigating; the receiving screen polls for it with `CheckForReceivedData` on `onDisplay`.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.SendData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `dataKey` | `String` | required | Key under which data is stored in DataMailer |
| `data` | `AnySerializable?` | `null` | Static payload; if null, incomingData is used |

**incomingData consumed:** Used as payload when `data` is null. Both null → `onFailure()`, nothing posted.
**Triggers fired:** `onSuccess()` — posted. `onFailure()` — both `data` and incomingData null.
**Notes:** In-process only, not persistent. Value is overwritten if same key is posted again.

**Example:**
```kotlin
SendData(
    trigger = EventTriggers.onClick(),
    dataKey = "selected_environment",
    data = environment.id
)
```

---

### CheckForReceivedData
**Purpose:** Reads the `DataMailer` bus for a value under `dataKey` and forwards it as `incomingData` to downstream events. Receive-side counterpart to `SendData`.
**When to use:** On destination screen `onDisplay` to pick up data posted by the previous screen.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.CheckForReceivedData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `dataKey` | `String` | required | Key to look up in DataMailer |

**incomingData consumed:** Not used.
**Triggers fired:** `onDataReceived(value)` then `onSuccess(value)` — value found. `onFailure()` — no value for `dataKey`.

**Example:**
```kotlin
CheckForReceivedData(
    trigger = EventTriggers.onDisplay(),
    dataKey = "selected_environment",
    events = {
        UpdateTiles(trigger = EventTriggers.onDataReceived(), updates = {
            update("env_name_label", incomingTileUpdateData())
        })
    }
)
```

---

### ProcessData
**Purpose:** Delegates `incomingData` to a client-registered `DataProcessor` identified by `processWith`. Allows arbitrary platform-side logic (e.g., encryption, formatting) to be applied to data.
**When to use:** When you need platform-native processing that can't be expressed in the event DSL — e.g., encrypting a value, applying a custom transformation, or triggering a native capability.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `processWith` | `String` | required | ID of the registered `DataProcessor` |

**incomingData consumed:** Passed directly to `DataProcessor.process()`.
**Triggers fired:** `onSuccess()` — processor calls its `onSuccess` callback (incomingData is null downstream). `onFailure(value)` — processor calls its `onFailure` callback.
**Failure scenarios:** `incomingData` is null → complete no-op, no triggers. No matching processor → complete no-op.
**Notes:** Only the first matching processor is executed. Processor's `onSuccess` passes no data; use `UpdateData`/`SendData` side-channels for output.

**Example:**
```kotlin
ProcessData(
    trigger = EventTriggers.onSuccess(),
    processWith = "EVENT_RUNNER"
)
```

---

### TransformData
**Purpose:** Reshapes `incomingData` by applying a template structure, substituting `<|path.to.value|>` placeholders with values resolved from `incomingData`. Supports map, list, and scalar templates applied recursively.
**When to use:** When you need to extract or restructure part of `incomingData` before passing it to the next event — e.g., extracting a single field from a network response map, or building a new map shape.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.TransformData`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `template` | `AnySerializable` | required (overload 1) | String/Map/List with `<\|path\|>` placeholders |
| `eventTemplate` | `EventSchemaBuilderScope.() -> Unit` | required (overload 2) | Template is a serialized EventSchema |

**Placeholder syntax:**
- `<|user.address.city|>` — dot-notation key path
- `<|items[0].name|>` — array index access
- `<||>` — entire `incomingData` as-is (preserves native type)

**incomingData consumed:** Used as data source for all placeholder lookups. Forwarded unchanged on both success and failure.
**Triggers fired:** `onSuccess(result)` — template applied; result preserves structure of template with placeholders replaced. Native type preserved when entire template string is a single `<||>` placeholder. `onFailure(Throwable)` — path not found, type mismatch, or null in mixed-content string.
**Failure scenarios:** Missing key → `NoSuchElementException`. Path expects Map/List but got other type → `IllegalArgumentException`. Index out of bounds → `IndexOutOfBoundsException`. Null resolved in mixed-content string → `IllegalArgumentException`.
**Notes:** Non-string, non-Map, non-List template values are returned as-is. Synchronous — no background dispatcher.

**Example:**
```kotlin
// Overload 1: static template — extract and reshape fields
TransformData(
    trigger = EventTriggers.onSuccess(),
    template = mapOf("greeting" to "Hello, <|user.name|>!", "userId" to "<|user.id|>"),
    events = {
        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
            update("greeting_tile", incomingTileUpdateData())
        })
    }
)

// Use <||> to pass incomingData (e.g., a String) directly to the next event
TransformData(
    trigger = EventTriggers.onQueryChanged(),
    template = "<||>",
    events = {
        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
            update("list", inlineTileUpdateData("filterChildrenByTerm" to "<||>"))
        })
    }
)
```

---

### EvaluateData
**Purpose:** Evaluates a boolean expression tree against data and branches the event chain — `onSuccess` when `true`, `onFailure` when `false` or on evaluation error.
**When to use:** Any conditional branching: form validation, feature flags, role checks, null checks, regex matching, numeric comparisons.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.data.EvaluateData`

**Helper functions** (same import path — all expression builders):
- `incomingData()` — references the current `incomingData` as the data subject
- `dataSourceData(dataSource, accessMode)` — reads from a data store as subject
- `not(expression)` — logical NOT
- `expr and expr` / `expr or expr` — infix logical composition (short-circuits)
- On `Data`: `.isNull()`, `.isNotNull()`, `.isEqualsTo(value)`, `.isBlank()`, `.isNotBlank()`, `.matchesRegex(regex)`, `.containsSubstring()`, `.startsWith()`, `.endsWith()`, `.equalsIgnoreCase()`, `.isFalse()`, `.isTrue()`, `.containsKey(key)`, `.valueAtKeyEquals(key, value)`, `.isListEmpty()`, `.isListNotEmpty()`, `.listContains(value)`, `.inList(list)`, numeric comparisons (`.isBiggerThan()`, `.isSmallerThan()`, etc. for Int/Long/Float/Double), `.isEqualTo(LocalDateTime)`, `.isBefore()`, `.isAfter()`, `.isWeekend()`, `.isWeekday()`
- `data.valueAtKey(key).<operator>()` — chains any operator against the value at a map key (returns `KeyedData` with same operator set)

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `expression` | `EvaluateDataEventSchema.Expression` | required | Boolean expression tree |

**incomingData consumed:** Used for `incomingData()` nodes; forwarded unchanged on both triggers.
**Triggers fired:** `onSuccess(incomingData)` — expression true. `onFailure(incomingData or Throwable)` — expression false (incomingData) or evaluation error (Throwable).
**Notes:** `and`/`or` short-circuit. Type mismatches resolve as `false` (not an exception). `valueAtKey(key)` chains any operator on the value at that map key.

**Example:**
```kotlin
GetData(
    trigger = EventTriggers.onClick(),
    readings = { reading(screenSegmentedData("form"), batchAccessMode(listOf("email", "password"))) },
    events = {
        EvaluateData(
            trigger = EventTriggers.onSuccess(),
            expression = incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w]+\\.[a-z]{2,}$")
                and incomingData().valueAtKey("password").isLengthBiggerThanOrEquals(8),
            events = {
                SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/auth/login", method = HttpMethod.POST)
                DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Invalid email or password too short")
            }
        )
    }
)
```

---

## Networking

### SendNetworkRequest
**Purpose:** Performs an HTTP request to a URL and propagates the response through child events.
**When to use:** Any server API call — CRUD operations, authentication, data fetching.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `url` | `String` | required | Full URL or path |
| `method` | `HttpMethod` | required | GET, POST, PUT, DELETE, PATCH |
| `body` | `AnySerializable?` | `null` | Schema body wins over holder |
| `headers` | `Map<String, String>?` | `null` | Merged with holder headers; schema wins |

**Request body resolution:** `body` (schema) ?? `holder.body` (set by `SetIncomingDataToNetworkParamsHolderBody`). Holder consumed on execution.
**incomingData consumed:** Not directly — use `SetIncomingDataToNetworkParamsHolderBody` first.
**Triggers fired:** `onStart()` — before request. `onSuccess(responseBody)` — 2xx, no custom listener. `onNetworkResponse(statusCode, responseBody)` — 2xx with matching child trigger. `onNetworkFailure(statusCode, responseBody)` — non-2xx with matching child trigger. `onFailure(Throwable or responseBody)` — non-2xx without matching trigger, or network error.
**Notes:** Response parsed as `JsonElement → Any` for JSON, `ByteArray` for all other content types.

**Example:**
```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/environments",
    method = HttpMethod.GET,
    events = {
        ReplaceTiles(trigger = EventTriggers.onSuccess(), groupingTileId = "env_list", tiles = { /* ... */ })
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Failed to load environments")
        Navigate(
            trigger = EventTriggers.onNetworkFailure(401),
            destination = "login",
            navigatorId = "root"
        )
    }
)
```

---

### DownloadFile
**Purpose:** Downloads a file from a URL with streaming progress triggers, producing the full `ByteArray` on completion.
**When to use:** Downloading files (PDFs, images, documents) when you need progress feedback or need the raw bytes for further processing (e.g., display or upload).
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.DownloadFile`

**Fields:** `url`, `method`, `body?`, `headers?` — same resolution as `SendNetworkRequest`.

**Triggers fired:** `onStart()`. `onDownloadProgress(Int 0-100)` — per chunk (when Content-Length known). `onDownloadFinish(ByteArray)` — full file. `onSuccess()`. `onDownloadFailure(Throwable)` — download error. `onFailure(Throwable)` — pre-request error.

**Example:**
```kotlin
DownloadFile(
    trigger = EventTriggers.onClick(),
    url = "/files/report.pdf",
    method = HttpMethod.GET,
    events = {
        UpdateTiles(trigger = EventTriggers.onDownloadProgress(), updates = {
            update("progress_bar", incomingTileUpdateData())
        })
        SaveFile(trigger = EventTriggers.onDownloadFinish(), fileName = "report.pdf", overrideIfExists = true)
    }
)
```

---

### SendFile
**Purpose:** Uploads a file as raw binary (no multipart) to a URL. Designed for the signed URL pattern where large files go directly to storage (GCS/S3) without passing through the app server.
**When to use:** File uploads using a pre-signed URL obtained from your backend.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.SendFile`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `url` | `String?` | `null` | Schema URL ?? holder URL (set by `SetIncomingDataToNetworkParamsHolderUrl`) |
| `method` | `HttpMethod` | required | Usually PUT for signed URLs |
| `headers` | `Map<String, String>?` | `null` | |
| `contentType` | `String?` | `null` | e.g. `"image/jpeg"`. Must match the content type used when signing |

**incomingData consumed:** Must be `ByteArray` (e.g., from `DownloadFile`'s `onDownloadFinish`). Non-ByteArray/null → `onFailure`.
**Triggers fired:** `onStart()`. `onUploadProgress(Int 0-100)`. `onSuccess(responseBody)`. `onFailure(Throwable/responseBody)`. `onNetworkResponse(statusCode)`. `onNetworkFailure(statusCode)`.

**Example:**
```kotlin
SendFile(
    trigger = EventTriggers.onSuccess(), // incomingData = ByteArray from DownloadFile
    url = null, // taken from NetworkParamsHolder set by SetIncomingDataToNetworkParamsHolderUrl
    method = HttpMethod.PUT,
    contentType = "application/pdf"
)
```

---

### SetIncomingDataToNetworkParamsHolderBody
**Purpose:** Stores `incomingData` as the request body in `NetworkParametersHolder`, making it available to the next network event in the chain.
**When to use:** When you need to pass the output of a previous event (e.g., form data from `GetData`) as the body of the next `SendNetworkRequest`.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderBody`

**Fields:** None beyond base fields.

**incomingData consumed:** Any non-null value stored as body. Null → `onFailure()`, nothing stored.
**Triggers fired:** `onSuccess()` — stored (incomingData forwarded unchanged). `onFailure()` — incomingData null.
**Notes:** Holder consumed by the next network call, regardless.

**Example:**
```kotlin
GetData(trigger = EventTriggers.onClick(), readings = { reading(screenSegmentedData("form"), fullAccessMode()) }, events = {
    SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/create", method = HttpMethod.POST)
    })
})
```

---

### SetIncomingDataToNetworkParamsHolderHeaders
**Purpose:** Stores `incomingData` (must be `Map<String, String>`) as request headers in `NetworkParametersHolder`, making them available to the next network event.
**When to use:** Injecting auth headers (e.g., session cookies, Bearer tokens) fetched via `GetData` into the next network request.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderHeaders`

**Fields:** None beyond base fields.

**incomingData consumed:** Must be `Map<String, String>`. Cast failure → `onFailure()`.
**Triggers fired:** `onSuccess()` — stored. `onFailure()` — cast failed.
**Notes:** Holder headers merged with schema headers; schema wins on key collision: `finalHeaders = holder + schema`.

**Example:**
```kotlin
GetData(trigger = EventTriggers.onStart(), readings = {
    reading(segmentedDataBase("auth"), singleAccessMode("sessionCookie"))
}, events = {
    TransformData(trigger = EventTriggers.onSuccess(), template = mapOf("Cookie" to "<||>"), events = {
        SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess(), events = {
            SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/protected", method = HttpMethod.GET)
        })
    })
})
```

---

### SetIncomingDataToNetworkParamsHolderUrl
**Purpose:** Stores `incomingData` (must be a `String`) as the request URL in `NetworkParametersHolder`, making it available to `SendFile` when its `url` is null.
**When to use:** Feeding a runtime-generated signed URL (obtained from your backend) into an upload event.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderUrl`

**Fields:** None beyond base fields.

**incomingData consumed:** `String` → stored as URL. Null or non-String → `onFailure()`.
**Triggers fired:** `onSuccess()` — stored. `onFailure()` — not a String.

**Example:**
```kotlin
// incomingData = response from backend with {"uploadUrl": "https://storage.googleapis.com/..."}
TransformData(trigger = EventTriggers.onSuccess(), template = "<|uploadUrl|>", events = {
    SetIncomingDataToNetworkParamsHolderUrl(trigger = EventTriggers.onSuccess(), events = {
        SendFile(trigger = EventTriggers.onSuccess(), method = HttpMethod.PUT, contentType = "video/mp4")
    })
})
```

---

## Overlays

### DisplayDialog
**Purpose:** Displays a modal dialog overlay populated with a server-defined tile tree. Shown immediately — no network call.
**When to use:** Confirmation dialogs, custom alert dialogs, form dialogs.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DisplayDialog`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Dialog content |
| `isCancellable` | `Boolean` | required | Tap-outside or back dismisses when true |
| `usePlatformDefaultWidth` | `Boolean` | required | Let tile tree control width when false |

**Triggers fired:** None. Only pushes overlay state via broadcast.

**Example:**
```kotlin
DisplayDialog(
    trigger = EventTriggers.onClick(),
    isCancellable = true,
    usePlatformDefaultWidth = false,
    tiles = {
        Column(id = "dialog_root") {
            SimpleText(id = "msg", text = "Delete this item?")
            Button(id = "confirm_btn", text = "Delete") {
                DismissDialog(trigger = EventTriggers.onClick())
            }
        }
    }
)
```

---

### DismissDialog
**Purpose:** Programmatically dismisses the currently displayed dialog.
**When to use:** When dialog content contains a close/cancel button, or after an action inside the dialog completes.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DismissDialog`

**Triggers fired:** `onDialogDismissed()` — fired by the overlay container after fully dismissed.

**Example:**
```kotlin
DismissDialog(trigger = EventTriggers.onClick())
```

---

### DisplayBottomSheet
**Purpose:** Displays a modal bottom sheet populated with a server-defined tile tree. Shown immediately — no network call.
**When to use:** Contextual menus, action sheets, inline forms, filter panels.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DisplayBottomSheet`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Sheet content |
| `isCancellable` | `Boolean` | required | Swipe-down/scrim tap dismisses when true |
| `fill` | `Boolean` | required | Expands to full screen height when true |

**Triggers fired:** None. Only pushes overlay state.

**Example:**
```kotlin
DisplayBottomSheet(
    trigger = EventTriggers.onClick(),
    isCancellable = true,
    fill = false,
    tiles = {
        Column(id = "sheet_root") { /* sheet content */ }
    }
)
```

---

### DismissBottomSheet
**Purpose:** Programmatically dismisses the currently displayed bottom sheet.
**When to use:** When sheet content has a close/cancel button, or after an action inside completes.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DismissBottomSheet`

**Triggers fired:** `onBottomSheetDismissed()`.

**Example:**
```kotlin
DismissBottomSheet(trigger = EventTriggers.onClick())
```

---

### DisplayNavigationDrawer
**Purpose:** Opens the screen's navigation drawer. Drawer content is defined on the screen tile, not here.
**When to use:** Triggered from a hamburger menu button or equivalent.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer.DisplayNavigationDrawer`

**Triggers fired:** None. Only pushes broadcast signal.
**Notes:** Navigation drawer must be configured on the screen tile for this to have a visible effect.

**Example:**
```kotlin
DisplayNavigationDrawer(trigger = EventTriggers.onClick())
```

---

### DismissNavigationDrawer
**Purpose:** Closes the screen's navigation drawer.
**When to use:** After the user selects a navigation item inside the drawer.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer.DismissNavigationDrawer`

**Triggers fired:** `onNavigationDrawerDismissed()`.

**Example:**
```kotlin
DismissNavigationDrawer(trigger = EventTriggers.onClick())
```

---

### DisplaySnackbar
**Purpose:** Displays a Material 3 snackbar with a message, optional action label, and configurable duration.
**When to use:** Brief feedback messages — success confirmations, error notices, undo prompts.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `message` | `String` | required | Snackbar message text |
| `duration` | `SnackbarDurationSchema` | `Short` | `Short`, `Long`, `Indefinite` |
| `actionLabel` | `String?` | `null` | Action button label; null = no button |

**Triggers fired:** `onSnackbarAction()` — user taps action label. `onSnackbarDismissed()` — dismissed by timeout, swipe, or programmatic dismiss (not action button).

**Example:**
```kotlin
DisplaySnackbar(
    trigger = EventTriggers.onFailure(),
    message = "Something went wrong",
    duration = SnackbarDurationSchema.Long,
    actionLabel = "Retry",
    events = {
        SendNetworkRequest(trigger = EventTriggers.onSnackbarAction(), url = "/api/retry", method = HttpMethod.POST)
    }
)
```

---

### DismissSnackbar
**Purpose:** Programmatically dismisses the currently displayed snackbar.
**When to use:** Dismissing an `Indefinite` snackbar after an async operation completes.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DismissSnackbar`

**Triggers fired:** None. Does NOT fire `onSnackbarDismissed`.

**Example:**
```kotlin
DismissSnackbar(trigger = EventTriggers.onSuccess())
```

---

## File System

### SaveFile
**Purpose:** Saves data to a local file on the device. ⚠️ **Runner is currently a placeholder** — actual write not yet implemented.
**When to use:** Persisting downloaded content (e.g., from `DownloadFile`) to local storage.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.file.SaveFile`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `fileName` | `String` | required | Target filename in app's private storage |
| `overrideIfExists` | `Boolean` | required | Overwrite existing file when true |

**Example:**
```kotlin
SaveFile(trigger = EventTriggers.onDownloadFinish(), fileName = "report.pdf", overrideIfExists = true)
```

---

### GetFile
**Purpose:** Reads a locally stored file. Data delivered to `onSuccess` is shaped by `outputType`.
**When to use:** Loading previously saved local file contents.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.file.GetFile`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.file.*`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `fileName` | `String` | required | Filename in app's private storage |
| `outputType` | `FileOutputType` | `arrayOfBytes()` | Use helpers: `arrayOfBytes()`, `flowOfBytes()`, `platformFile()`, `mapObject()`, `base64()` |

**FileOutputType helpers:**
- `arrayOfBytes()` → whole file read into memory as `ByteArray` (default)
- `flowOfBytes()` → chunked `Flow<ByteArray>`, without loading the whole file into memory
- `platformFile()` → `PlatformFile` reference to the file, without reading its contents
- `mapObject()` → file decoded as JSON into `Map<String, AnySerializable?>`
- `base64()` → whole file read into memory and delivered as a base64-encoded `String`

**Triggers fired:**
- `onSuccess(...)` — data shaped per `outputType`
- `onFailure(Throwable)` — file not found, I/O error, or invalid JSON when `outputType` is `mapObject()`

**Example:**
```kotlin
GetFile(trigger = EventTriggers.onClick(), fileName = "config.json", outputType = mapObject())
```

---

### DeleteFile
**Purpose:** Deletes a locally stored file identified by `fileName`.
**When to use:** Cleaning up local files after upload or when cache is cleared.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.file.DeleteFile`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `fileName` | `String` | required | Identifies the target file within the app's private storage scope |

**incomingData consumed:** Not used.

**Example:**
```kotlin
DeleteFile(
    trigger = EventTriggers.onSuccess(),
    fileName = "avatar.jpg",
)
```

---

### OpenFilePicker
**Purpose:** Opens the system file picker, allowing the user to select a file. Delivers the file to `onSuccess` shaped by `outputType`.
**When to use:** Any flow requiring the user to select a file from their device — upload, attach, import.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.file.OpenFilePicker`
**Helper imports:** `import dev.catbit.mosaic.server.builder.event.builders.file.*`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `fileType` | `FileType` | required | Use helpers: `imageFileType()`, `videoFileType()`, `imageAndVideoFileType()`, `fileFileType(vararg types)` |
| `pickMode` | `PickMode` | `singlePickMode()` | Currently only single selection supported |
| `outputType` | `FileOutputType` | `platformFile()` | Use helpers: `platformFile()`, `arrayOfBytes()`, `flowOfBytes()`, `mapObject()`, `base64()` |

**FileType helpers:**
- `imageFileType()` → `FileType.Image`
- `videoFileType()` → `FileType.Video`
- `imageAndVideoFileType()` → `FileType.ImageAndVideo`
- `fileFileType("pdf", "png", "txt")` → `FileType.File(types)`

**PickMode helpers:**
- `singlePickMode()` → `PickMode.Single`

**FileOutputType helpers:**
- `platformFile()` → picked `PlatformFile` reference, no read (default); chain with `UploadFile` or `SaveFile`
- `arrayOfBytes()` → whole file read into memory as `ByteArray`
- `flowOfBytes()` → chunked `Flow<ByteArray>`, without loading the whole file into memory
- `mapObject()` → file decoded as JSON into `Map<String, AnySerializable?>`
- `base64()` → whole file read into memory and delivered as a base64-encoded `String`

**Triggers fired:**
- `onStart()` — file selected, contents being read (when `outputType` requires it)
- `onSuccess(...)` — data shaped per `outputType`
- `onFailure()` — user cancelled the picker, an exception occurred, or (when `outputType` is `mapObject()`) the file was not valid JSON

**Example:**
```kotlin
OpenFilePicker(
    trigger = EventTriggers.onClick(),
    fileType = imageFileType(),
    events = {
        SendFile(
            trigger = EventTriggers.onSuccess(),
            url = "/api/upload/avatar",
            method = HttpMethod.POST,
        )
        UpdateTiles(
            trigger = EventTriggers.onFailure(),
            updates = {
                update(tileId = "error_text", updateData = inlineTileUpdateData("visibility" to "VISIBLE"))
            }
        )
    }
)
```

---

### TakePicture
**Purpose:** Opens the device camera, allowing the user to take a picture. Backed by Mosaic's own `CameraManager` — native per-platform implementation (Android: `ActivityResultContracts.TakePicturePreview()`; iOS: `UIImagePickerController`; JVM: `webcam-capture` live-preview dialog; Wasm: `getUserMedia`/canvas overlay). No third-party picker library.
**When to use:** Any flow requiring the user to capture a photo with the camera — avatar capture, document scan, attachment.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.image.TakePicture`

`CameraManager` always returns raw PNG bytes (lossless, no quality/format baked in per platform). Compression is applied directly by the client runner via `io.github.aryapreetam:cmp-imgcompress` — single `commonMain` lib, no wrapper class. `compression == null` → raw PNG bytes returned as-is. `compression != null` → re-encoded as **WebP** (the library's only output format).

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `compression` | `CompressionScheme?` | `null` | `byQuality(qualityPercent: Float)` or `byTargetSize(targetSizeKb: Int)` helpers |
| `resize` | `ImageResizeOptions?` | `null` | Only applies when `compression` is set; null uses the library default (`maxLongEdgePx = 2560`) |
| `outputType` | `TakePictureEventSchema.OutputType` | `pictureArrayOfBytes()` | `pictureArrayOfBytes()` or `pictureBase64()` — shape of the captured image in `incomingData`. Nested enum, **owned by this schema** (not shared with `GetImageFromGallery` — see its own `outputType`) |

`ImageResizeOptions(maxLongEdgePx: Int? = 2560, downscaleOnly: Boolean = true, maintainAspectRatio: Boolean = true)` — mirrors the library's `ResizeOptions` 1:1.

**Triggers fired:**
- `onSuccess()` — picture captured; `incomingData` is a `ByteArray` (`pictureArrayOfBytes()`, default) or a base64 `String` (`pictureBase64()`) — original PNG if no `compression`, WebP otherwise; chain with `SaveFile`
- `onFailure()` — user cancelled the capture, an exception occurred, or no camera is available

**Example:**
```kotlin
TakePicture(
    trigger = EventTriggers.onClick(),
    compression = byQuality(70f),
    resize = ImageResizeOptions(maxLongEdgePx = 1280),
    outputType = pictureBase64(),
    events = {
        // incomingData is now a base64 String — inject it as the request body instead of SaveFile
        SendNetworkRequest(
            trigger = EventTriggers.onSuccess(),
            url = "/api/avatar",
            method = HttpMethod.POST,
            events = {
                SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onStart())
            }
        )
    }
)
```

---

### GetImageFromGallery
**Purpose:** Opens the device gallery, allowing the user to pick an image. Uses `FileKit.openFilePicker(type = FileKitType.Image)`, read into bytes — equivalent to `OpenFilePicker` pre-filtered to images, exposed as its own event for convenience. Works on all platforms.
**When to use:** Any flow requiring the user to select an image from their photo library — avatar, upload, import.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.image.GetImageFromGallery`

**No permission needed:** uses the system Photo Picker (Android `PickVisualMedia` / iOS `PHPickerViewController`), which grants access only to the picked item — no `RequestPermission(GALLERY)` or manifest entry required.

Same `compression`/`resize` contract as `TakePicture` (shared `CompressionScheme`/`ImageResizeOptions`). `compression == null` → the picked image's original bytes/format are returned as-is. `compression != null` → re-encoded as **WebP**.

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `compression` | `CompressionScheme?` | `null` | Same as `TakePicture` |
| `resize` | `ImageResizeOptions?` | `null` | Same as `TakePicture` |
| `outputType` | `GetImageFromGalleryEventSchema.OutputType` | `galleryArrayOfBytes()` | `galleryArrayOfBytes()` or `galleryBase64()` — shape of the picked image in `incomingData`. Own nested enum, duplicated from (not shared with) `TakePicture`'s `outputType` |

**Triggers fired:**
- `onSuccess()` — image selected; `incomingData` is a `ByteArray` (`galleryArrayOfBytes()`, default) or a base64 `String` (`galleryBase64()`) — original format if no `compression`, WebP otherwise; chain with `SaveFile`
- `onFailure()` — user cancelled the selection or an exception occurred

**Example:**
```kotlin
GetImageFromGallery(
    trigger = EventTriggers.onClick(),
    compression = byQuality(70f),
    resize = ImageResizeOptions(maxLongEdgePx = 1280),
    events = {
        SaveFile(
            trigger = EventTriggers.onSuccess(),
            fileName = "avatar.webp",
        )
    }
)
```

---

## Menu

### ToggleMenu
**Purpose:** Toggles the open/closed state of a `MenuTile`. If open → closes; if closed → opens.
**When to use:** Wiring a button or icon to open/close a contextual menu.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.menu.ToggleMenu`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `menuId` | `String` | required | ID of the MenuTile to toggle |

**Triggers fired:** `onSuccess()`. `onFailure(exception)` — MenuTile not found.

**Example:**
```kotlin
ToggleMenu(
    trigger = EventTriggers.onClick(),
    menuId = "actions_menu"
)
```

---

## Popup

### TogglePopup
**Purpose:** Toggles the open/closed state of a `PopupTile`. If open → closes; if closed → opens.
**When to use:** Wiring a button or icon to open/close a `Popup` tile.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.popup.TogglePopup`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `popupId` | `String` | required | ID of the PopupTile to toggle |

**Triggers fired:** `onSuccess()`. `onFailure(exception)` — PopupTile not found.

**Example:**
```kotlin
TogglePopup(
    trigger = EventTriggers.onClick(),
    popupId = "info_popup"
)
```

---

## Pull to Refresh

### StopRefreshing
**Purpose:** Signals a `PullToRefreshTile` to stop its loading indicator and return to idle.
**When to use:** Always chain this after a `RefreshScreen` or network call triggered by `onPull` — both on success and failure — to dismiss the spinner.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh.StopRefreshing`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tileId` | `String` | required | ID of the PullToRefreshTile |

**Triggers fired:** `onSuccess()`. `onFailure(exception)` — tile not found.
**Notes:** Failing to call this after a pull event leaves the refresh indicator spinning indefinitely.

**Example:**
```kotlin
RefreshScreen(
    trigger = EventTriggers.onPull(),
    events = {
        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "ptr_container")
        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "ptr_container")
    }
)
```

---

## Scroll

### ScrollColumn
**Purpose:** Imperatively scrolls a `ColumnTile` or `LazyColumnTile` to a position via broadcast.
**When to use:** Scrolling a list to top after data reload, or scrolling to a specific item programmatically.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.scroll.column.ScrollColumn`

**Helper functions** (same import path):
- `scrollColumnToTop()` — scroll to first item
- `scrollColumnTo(index: Int)` — scroll to item at index
- `scrollColumnToBottom()` — scroll to last item

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tileId` | `String` | required | Column/LazyColumn tile ID |
| `where` | `Where` | required | Use helper functions |
| `smoothly` | `Boolean` | `true` | Animated vs instant |

**Notes:** If tile not found, broadcast silently ignored — no error trigger.

**Example:**
```kotlin
ScrollColumn(
    trigger = EventTriggers.onSuccess(),
    tileId = "result_list",
    where = scrollColumnToTop(),
    smoothly = true
)
```

---

### ScrollRow
**Purpose:** Imperatively scrolls a `RowTile` or `LazyRowTile` to a position via broadcast.
**When to use:** Horizontal list navigation, carousel scroll control.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.scroll.row.ScrollRow`

**Helper functions** (same import path):
- `scrollRowToStart()` — scroll to first item
- `scrollRowTo(index: Int)` — scroll to item at index
- `scrollRowToEnd()` — scroll to last item

**Fields:** `tileId`, `where` (use helper functions), `smoothly` (default `true`).

**Example:**
```kotlin
ScrollRow(trigger = EventTriggers.onClick(), tileId = "chip_row", where = scrollRowToStart(), smoothly = false)
```

---

### ScrollPager
**Purpose:** Imperatively navigates a `PagerTile` to a target page via broadcast.
**When to use:** Programmatic tab/page switching — e.g., jumping to the next page on a button tap.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.ScrollPager`

**Helper functions** (same import path):
- `scrollPageToBegin()` — go to first page
- `scrollPageToEnd()` — go to last page
- `scrollPageToNextPage()` — advance one page
- `scrollPageToPreviousPage()` — go back one page

**Fields:** `tileId`, `where` (use helper functions), `smoothly` (default `true`).

**Example:**
```kotlin
ScrollPager(trigger = EventTriggers.onClick(), tileId = "onboarding_pager", where = scrollPageToNextPage(), smoothly = true)
```

---

## Security

### RequestPermission
**Purpose:** Requests one or more runtime permissions from the user using each platform's native mechanism (Android `ActivityResultRegistry`, iOS `AVFoundation`/`CoreLocation`/etc., browser Permissions API).
**When to use:** Camera, microphone, location, notification, contacts, gallery, or storage access before using those features.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.security.RequestPermission`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `permissions` | `List<Permissions>` | required | `CAMERA`, `GALLERY`, `STORAGE`, `MICROPHONE`, `LOCATION`, `NOTIFICATION`, `CONTACTS` |

**Triggers fired:**
| Trigger | Quando |
|---|---|
| `onPermissionsAcquired()` + `onSuccess()` | Todas as permissões concedidas |
| `onPermissionRationale()` | **Android only** — 1ª negação, `shouldShowRequestPermissionRationale == true`; use para exibir explicação antes de pedir novamente |
| `onPermissionsDenied()` + `onFailure()` | Negado permanentemente (Android 2ª negação / "não perguntar novamente"), ou negado em iOS/Web |

> ⚠️ **Requisito do app consumidor:**
>
> **Android `AndroidManifest.xml`** — declare apenas as permissões que usar:
> ```xml
> <uses-permission android:name="android.permission.CAMERA" />
> <uses-permission android:name="android.permission.RECORD_AUDIO" />
> <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
> <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
> <uses-permission android:name="android.permission.READ_CONTACTS" />
> <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />          <!-- API 33+ -->
> <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />           <!-- API 33+ -->
> <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />            <!-- API 33+ -->
> <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
> <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
> ```
>
> **iOS `Info.plist`** — a ausência da key crasha o app em runtime:
> ```xml
> <key>NSCameraUsageDescription</key><string>Motivo...</string>
> <key>NSMicrophoneUsageDescription</key><string>Motivo...</string>
> <key>NSPhotoLibraryUsageDescription</key><string>Motivo...</string>
> <key>NSLocationWhenInUseUsageDescription</key><string>Motivo...</string>
> <key>NSContactsUsageDescription</key><string>Motivo...</string>
> ```
> `NOTIFICATION` no iOS não requer key no `Info.plist`.

**Example:**
```kotlin
RequestPermission(
    trigger = EventTriggers.onClick(),
    permissions = listOf(RequestPermissionEventSchema.Permissions.CAMERA),
    events = listOf(
        Navigate(trigger = EventTriggers.onPermissionsAcquired(), ...),
        ShowRationale(trigger = EventTriggers.onPermissionRationale(), ...),
        ShowDeniedMessage(trigger = EventTriggers.onPermissionsDenied(), ...)
    )
)
```

---

## System

### BroadcastToSystem
**Purpose:** Emits a named broadcast via `SystemBroadcastChannel`, notifying any subscriber in the app with a `broadcastId`/payload pair — without direct coupling to the event chain.
**When to use:** Cross-screen communication — e.g., notifying all screens that the environment changed, or triggering a global action from any screen.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem`

**Helper functions** (same import path):
- `inlineBroadcastData(data: AnySerializable)` — broadcasts a static value
- `incomingBroadcastData()` — forwards current `incomingData` as the payload

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `broadcastId` | `String` | required | Named channel identifier |
| `data` | `BroadcastData` | required | Use `inlineBroadcastData()` or `incomingBroadcastData()` |

**incomingData consumed:** Only when `incomingBroadcastData()` — current incomingData forwarded as payload. Null incomingData → `onFailure()`.
**Triggers fired:** `onSuccess()`. `onFailure()` — `incomingBroadcastData()` with null incomingData.

**Receiver side:** Wire `EventTriggers.onSystemBroadcast("BROADCAST_ID")` on any event that should react.

**Example:**
```kotlin
// Sender
BroadcastToSystem(
    trigger = EventTriggers.onClick(),
    broadcastId = "ENVIRONMENT_CHANGE",
    data = inlineBroadcastData(environment.id)
)

// Receiver (on any screen/tile)
RefreshScreen(trigger = EventTriggers.onSystemBroadcast("ENVIRONMENT_CHANGE"))
```

---

### CheckIfHasInternetConnection
**Purpose:** Checks whether the device has an active internet connection. ⚠️ **Runner is currently a placeholder** — connectivity check not yet implemented.
**When to use:** Before attempting network operations in offline-first scenarios.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.system.CheckIfHasInternetConnection`

**Fields:** None beyond base fields.
**Triggers fired (intended):** `onSuccess()` — connected. `onFailure()` — not connected.

**Example:**
```kotlin
CheckIfHasInternetConnection(
    trigger = EventTriggers.onDisplay(),
    events = {
        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/data", method = HttpMethod.GET)
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "No internet connection")
    }
)
```

---

## Theme

### SetTheme
**Purpose:** Overrides the app's Material3 color scheme (light + dark) globally at runtime, regardless of the current screen. Persists until `ResetTheme` is dispatched or the app restarts.
**When to use:** Server-driven theming/branding, white-labeling, user-selected accent themes.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.theme.SetTheme`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `colorsScheme` | `ColorsScheme` | required | Build with `colorsScheme(lightColorScheme, darkColorScheme)` |

`ColorsScheme` = `{ lightColorScheme: ColorScheme, darkColorScheme: ColorScheme }`. `ColorScheme` mirrors every Compose Material3 `ColorScheme` role (`primary`, `onPrimary`, `surfaceContainerHighest`, etc.) as a **hex color String** (e.g. `"#FF5722"`), not `color(...)`/`ColorSchema` — build one with the `colorScheme(...)` DSL helper, which takes every role as a named `String` parameter (see `mosaic-server/.../builder/event/builders/theme/SetThemeEventBuilder.kt` for the full parameter list).

**Triggers fired:** `onSuccess()` — always, once applied.

**Example:**
```kotlin
SetTheme(
    trigger = EventTriggers.onClick(),
    colorsScheme = colorsScheme(
        lightColorScheme = colorScheme(
            primary = "#6750A4", onPrimary = "#FFFFFF",
            primaryContainer = "#EADDFF", onPrimaryContainer = "#21005D",
            // ...remaining Material3 roles
        ),
        darkColorScheme = colorScheme(
            primary = "#D0BCFF", onPrimary = "#381E72",
            primaryContainer = "#4F378B", onPrimaryContainer = "#EADDFF",
            // ...remaining Material3 roles
        )
    )
)
```

---

### ResetTheme
**Purpose:** Reverts a previous `SetTheme` override, restoring the app's default color scheme (light + dark).
**When to use:** "Reset to default theme" actions, logging out of a white-labeled session.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.theme.ResetTheme`

**Fields:** none.

**Triggers fired:** `onSuccess()` — always, once restored.

**Example:**
```kotlin
ResetTheme(
    trigger = EventTriggers.onClick()
)
```

---

## Time

### StartCountdownTimer
**Purpose:** Starts a client-side countdown timer. ⚠️ **Runner is currently a placeholder** — timer not yet implemented.
**When to use:** OTP expiry countdowns, session timeout warnings, timed actions.
**Import:** `import dev.catbit.mosaic.server.builder.event.builders.time.StartCountdownTimer`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `setupTimeInSeconds` | `Long` | required | Countdown duration; must be positive |

**Triggers fired (intended):** `onCountdownTimerTick(remaining)` — every second. `onCountdownTimerFinish()` — when zero.

**Example:**
```kotlin
StartCountdownTimer(
    trigger = EventTriggers.onDisplay(),
    setupTimeInSeconds = 120L,
    events = {
        UpdateTiles(trigger = EventTriggers.onCountdownTimerTick(), updates = {
            update("timer_label", incomingTileUpdateData())
        })
        DisplaySnackbar(trigger = EventTriggers.onCountdownTimerFinish(), message = "Session expired")
    }
)
```
