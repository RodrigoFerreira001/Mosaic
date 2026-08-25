# Mosaic — Event Catalog

Complete, field-by-field reference for every `EventSchema` shipped in `mosaic-core`. Every entry below was written directly from that schema's KDoc (the primary source of truth) and cross-checked against its `mosaic-server` DSL builder — not paraphrased from any other document. If the framework's actual behavior ever needs re-verifying, re-read the schema in `mosaic-core/.../data/schemas/event/events/` and the matching builder in `mosaic-server/.../builder/event/builders/`.

Every event shares 3 base fields, always available and not repeated per entry below:

| Field | Type | Default | What it's for |
|---|---|---|---|
| `id` | `String` | random id | Unique identifier — how `TriggerEvent`/`UpdateEvents` address this event by id. |
| `trigger` | `EventTrigger` | required | The condition that must occur for this event to run — see `architecture.md` §5, "Event chaining". |
| `events` | `List<EventSchema>?` | `null` | This event's children, each declaring which of *this* event's outgoing triggers runs it. |

For the underlying mechanics — trigger matching, `incomingData` propagation, the `field ?: incomingData` fallback convention, `GetScreen`/`ChangeScreenState`'s split — see [`architecture.md`](architecture.md). This catalog documents *what* each event does, *what parameters it takes*, and *what triggers it fires*, not the dispatch machinery itself.

---

## Data

### `CheckForReceivedData`

Reads a one-shot value from the client's `DataMailer` under `dataKey` and branches on whether it was there.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `dataKey` | `String` | required | Key looked up in `DataMailer`. |

**DSL example:**
```kotlin
CheckForReceivedData(
    trigger = EventTriggers.onDisplay(),
    dataKey = "upload_result"
)
```

**Triggers fired:** `OnDataReceived` (value exists, passed as incoming data) then `OnSuccess` (same value) — both fire together when found. `OnFailure` (no incoming data) when nothing is there.

**Notes:** `incomingData` not consumed. `DataMailer` is a one-shot channel — reading removes the entry, so a second `CheckForReceivedData` for the same key gets `OnFailure`.

### `EvaluateData`

Evaluates the boolean `expression` and branches on the result. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `expression` | `Expression` | required | Boolean expression tree — see below. |

`Expression` composes `not(expr)`, `expr1 and expr2`, `expr1 or expr2` over leaf comparisons. A leaf starts from a `Data` source — `incomingData()` or `dataSourceData(dataSource, accessMode)` — and calls a comparison function on it: `isEqualsTo`, `isSmallerThan`/`isBiggerThan` (+ `OrEquals` variants, for `Int`/`Long`/`Float`/`Double`), string ops (`matchesRegex`, `containsSubstring`, `startsWith`, `endsWith`, `equalsIgnoreCase`, `isBlank`/`isNotBlank`, `isLength*`), `isTrue`/`isFalse`, map ops (`containsKey`, `containsValue`, `isMapEmpty`/`isMapNotEmpty`, `isMapSize*`, `valueAtKeyEquals`, `valueAtKey(key)` — chainable, itself returns a `KeyedData` supporting most of the same operations, recursively), list ops (`listContains`, `inList`, `isListEmpty`/`isListNotEmpty`, `isListSize*`, `listContainsAll`, `listContainsAny`), and date ops (`isEqualTo`/`isBefore`/`isAfter`, `isWeekend`/`isWeekday`).

**DSL example:**
```kotlin
EvaluateData(
    trigger = EventTriggers.onDisplay(),
    expression = incomingData().valueAtKey("age").isBiggerThanOrEquals(18) and
        dataSourceData(screenPlainData(), singleAccessMode("consent")).isTrue()
)
```

**Triggers fired:** `OnSuccess` (expression is `true`, `incomingData` forwarded unchanged), `OnFailure` (expression is `false`, `incomingData` forwarded — or evaluation threw, in which case the `Throwable` is passed instead and the error is logged).

**Notes:** an operation applied to a value of the wrong runtime type (e.g. `isBiggerThan` on a `String`) evaluates to `false`, it never throws. `incomingData` is read by any `incomingData()` leaf and always forwarded downstream regardless of which branch fires.

### `GetData`

Reads one or more values from data sources and emits them downstream. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `readings` | block, one entry per `reading(dataSource, accessMode)` | required | Values to read — see below. |

Each reading pairs a `dataSource` (`inlineData(...)`, `applicationPlainData()`, `applicationSegmentedData(id)`, `screenPlainData()`, `screenSegmentedData(id)`, `screenNavigationData()`, `plainDataBase()`, `segmentedDataBase(id)`, or `tile(tileId, dataKey)`) with an `accessMode` (`singleAccessMode(id)`, `batchAccessMode(ids, allowMissingData, unwrapValuesToList)`, or `fullAccessMode()`). Readings are processed in order into a single accumulator — a later reading overwrites an earlier one on key collision.

**DSL example:**
```kotlin
GetData(
    trigger = EventTriggers.onDisplay(),
    readings = {
        reading(dataSource = screenNavigationData(), accessMode = singleAccessMode("userId"))
        reading(dataSource = plainDataBase(), accessMode = singleAccessMode("theme"))
    }
)
```

**Triggers fired:** `OnStart` (before any reading runs), `OnSuccess` (all readings resolved, assembled result as incoming data), `OnFailure` (aborts on the first problem — a `Single` reading resolving to `null`, a missing `Batch` id with `allowMissingData = false`, or a database error).

**Notes:** result shape depends on the mix of access modes — `Full`, and `Batch` without `unwrapValuesToList`, produce a map keyed by data id; `Batch` with `unwrapValuesToList` produces a list; with only `Single` readings, one reading emits the bare value and several emit a list. A map result wins over a list result when the two mix. `incomingData` not consumed.

### `ProcessData`

Hands the event's `incomingData` to the `DataProcessor` registered by the client under the id `processWith`. Processors are supplied by the host application — what the processing actually does is opaque to the framework.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `processWith` | `String` | required | Id of the registered `DataProcessor` to invoke. |

**DSL example:**
```kotlin
ProcessData(trigger = EventTriggers.onSuccess(), processWith = "EVENT_RUNNER")
```

**Triggers fired:** `OnSuccess` (processor returned successfully, no data forwarded), `OnFailure` (processor returned failure — `Throwable` as incoming data; or no processor registered under `processWith`; or `incomingData` was `null` — no data in either of those two cases).

**Notes:** `incomingData` is **required** — the event fails immediately if it's `null`, without even looking up the processor.

### `RemoveData`

Deletes values from data sources. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `deletions` | block, one entry per `addDeletion(dataSource, accessMode)` | required | Values to remove. |

Supported sources: application/screen plain and segmented holders, plus the plain and segmented local databases. Deletions targeting navigation data, a tile, or an inline source are silently ignored.

**DSL example:**
```kotlin
RemoveData(
    trigger = EventTriggers.onClick(),
    deletions = {
        addDeletion(dataSource = plainDataBase(), accessMode = singleAccessMode("draft"))
    }
)
```

**Triggers fired:** `OnSuccess` (every deletion completed, no data forwarded), `OnFailure` (at least one database deletion failed — fired once at the end, after every deletion was attempted).

**Notes:** all deletions are attempted regardless of earlier failures — there's no early abort. `accessMode = fullAccessMode()` wipes the entire source. `incomingData` not consumed.

### `SendData`

Posts a value into the client's `DataMailer` under `dataKey`, where a later `CheckForReceivedData` can pick it up — the way to hand a value to another screen.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `dataKey` | `String` | required | Key the value is stored under. |
| `data` | `AnySerializable?` | `null` | Value to send; falls back to `incomingData` when omitted. |

**DSL example:**
```kotlin
SendData(trigger = EventTriggers.onSuccess(), dataKey = "upload_result")
```

**Triggers fired:** `OnSuccess` (value posted, no data forwarded), `OnFailure` (both `data` and `incomingData` are `null` — nothing to send, error logged).

**Notes:** the value sent is `data` when it's non-null, otherwise `incomingData` — the `field ?: incomingData` fallback convention (see `architecture.md` §5).

### `TransformData`

Reshapes `incomingData` into a new value by applying `template` through the client's template engine — a payload rewritten mid-chain, no round trip.

**Parameters (2 overloads):**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `template` | `AnySerializable` | required (1st overload) | Template applied to `incomingData` — string, map or list, with `<\|path\|>`/`</path/>` placeholders. |
| `eventTemplate` | block | `{}` (2nd overload) | Alternate form — see Notes. |

**DSL example:**
```kotlin
TransformData(
    trigger = EventTriggers.onClick(),
    template = mapOf("event" to "<|name|>")
)
```

**Triggers fired:** `OnSuccess` (template applied, transformed value as incoming data), `OnFailure` (applying the template threw — `Throwable` as incoming data, error logged).

**Notes:** `incomingData` is the input the template is applied to — required for the placeholders to resolve against anything. There are two DSL overloads: one taking a literal `template` value, another taking `eventTemplate` (an `EventSchemaBuilderScope` block) — used when the template itself needs to be built from nested event-builder helpers rather than a plain literal.

### `UpdateData`

Writes values into data sources. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `updates` | block, one entry per `update(dataSource, updateData)` | required | Writes to perform. |

`updateData` is one of: `incomingUpdateData()` (spreads `incomingData` when it's a map), `inlineUpdateData(map)` / `inlineUpdateData(vararg pairs)` (a literal map), `explicitUpdateData(dataId, value)` (writes one known id to a literal value), `explicitIncomingUpdateData(dataId)` (writes one known id, value from `incomingData`), or `explicitNullUpdateData(dataId)` (writes one known id to `null`).

**DSL example:**
```kotlin
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(
            dataSource = applicationSegmentedData("auth"),
            updateData = explicitIncomingUpdateData(dataId = "session")
        )
    }
)
```

**Triggers fired:** `OnSuccess` (every write completed, no data forwarded), `OnFailure` (at least one database write failed — fired once at the end, after every write was attempted, each failure logged).

**Notes:** in-memory data holders accept `null` (clears the entry); the local databases **don't support `null` yet** — entries resolving to `null` are silently skipped there instead of written. Updates targeting navigation data, a tile, or an inline source are silently ignored. `incoming*` variants are the only ones that read `incomingData`. Explicit variants are the only way to write a value that is itself a map/record intact under one key — the other variants would spread its keys instead.

## Event orchestration

### `RunEvents`

Runs this event's own `events` list inline, in order, each receiving this event's `incomingData` — the way to fan a single trigger out into several events, or group events for reuse.

**Parameters:** none beyond the base fields — `events` here **is the payload**, not a downstream chain (see Notes).

**DSL example:**
```kotlin
RunEvents(trigger = EventTriggers.onClick()) {
    BroadcastToSystem(
        trigger = EventTriggers.inline(),
        broadcastId = "cart_updated",
        data = incomingBroadcastData()
    )
    UpdateData(trigger = EventTriggers.inline(), updates = { /* ... */ })
}
```

**Triggers fired:** `OnSuccess` (every event in the list ran without throwing, no data forwarded), `OnFailure` (at least one threw — fired once at the end, after all were attempted, each failure logged).

**Notes:** unlike every other event in this catalog, `RunEvents.events` is not filtered by trigger before running — every entry runs, unconditionally, in order. Each one is run guarded, so one failing event doesn't stop the rest.

### `RunCancellableEvents`

Runs this event's own `events` list inside a cancellable coroutine job, registered in the client's `CancellableEventsHolder` under `cancellableEventId`. A later `CancelEvents` carrying the same id stops the job mid-flight.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `cancellableEventId` | `String` | required | Key the job is registered under — a **global namespace**, not scoped per screen (see `architecture.md` §4). |

**DSL example:**
```kotlin
RunCancellableEvents(trigger = EventTriggers.onDisplay(), cancellableEventId = "poll_status") {
    StartTimeLoop(trigger = EventTriggers.inline(), timeData = seconds(delay = 5))
}
```

**Triggers fired:** `OnSuccess` (as soon as the job is **registered**, not when the events finish — fire-and-forget), `OnFailure` (`events` is `null`, nothing to run).

**Notes:** same "events is the payload, not a chain" behavior as `RunEvents`. Runs in its own coroutine scope, so this event returns immediately after registering the job — it never waits for the wrapped events to complete.

### `CancelEvents`

Cancels the coroutine job registered under `cancellableEventId`, stopping whatever `RunCancellableEvents` started with that id.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `cancellableEventId` | `String` | required | Key of the job to cancel. |

**DSL example:**
```kotlin
CancelEvents(trigger = EventTriggers.onClick(), cancellableEventId = "poll_status")
```

**Triggers fired:** `OnSuccess` (a running job was found and cancelled, no data forwarded), `OnFailure` (nothing registered under that id — either it never ran or already finished; a `NoSuchElementException` is passed as incoming data).

**Notes:** `incomingData` not consumed. Cancelling an id that already finished on its own is a normal `OnFailure`, not an exceptional case to guard against separately.

### `TriggerEvent`

Looks up the event registered on the screen under `eventId` and runs it inline — lets one event chain re-use another that lives on a different tile.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `eventId` | `String` | required | Id of the target event, looked up anywhere in the screen's tile tree. |

**DSL example:**
```kotlin
TriggerEvent(trigger = EventTriggers.onClick(), eventId = "shared_logout_flow")
```

**Triggers fired:** `OnSuccess` (target event ran, no data forwarded), `OnFailure` (no event registered under `eventId` — no data; or running it threw — `Throwable` as incoming data). Both failure cases are logged.

**Notes:** `incomingData` is forwarded unchanged to the event being run. The lookup searches the whole screen's tile tree (recursively, including nested navigation graphs' parent screens), not just the current tile.

### `UpdateEvents`

Patches events already registered on the screen — the event-level equivalent of `UpdateTiles`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `updates` | block, one entry per `update(eventId, data)` | required | Patches to apply. |

**DSL example:**
```kotlin
UpdateEvents(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(eventId = "retry_upload", data = mapOf("url" to "<|newUrl|>"))
    }
)
```

**Triggers fired:** `OnSuccess` (every update applied, no data forwarded), `OnFailure` (at least one update failed, typically because no event carries that id — fired once at the end, after all updates were attempted).

**Notes:** `data` is merged into the target event's own parameters as a shallow JSON patch, the same mechanism `UpdateTiles` uses on tiles. All updates are attempted regardless of earlier failures. `incomingData` not consumed. This is also how infinite-scroll pagination advances the page: a `SendNetworkRequest`'s own `url` gets rewritten to the next page from inside its own success response — see `skill/mosaic-server/SKILL.md` §9.

## File system

### `DeleteFile`

Deletes the file stored under `fileName` in the client's own file storage.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `fileName` | `String` | required | File to delete. |

**DSL example:**
```kotlin
DeleteFile(trigger = EventTriggers.onClick(), fileName = "draft.json")
```

**Triggers fired:** `OnSuccess` (deleted, no data forwarded), `OnFailure` (deletion failed — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

### `GetFile`

Reads the file stored under `fileName` and emits its content downstream, in the shape chosen by `outputType`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `fileName` | `String` | required | File to read. |
| `outputType` | `FileOutputType` | `arrayOfBytes()` | Shape of the emitted content — `arrayOfBytes()` (raw `ByteArray`), `flowOfBytes()` (streamed chunks, for large files), `platformFile()` (platform file handle), `mapObject()` (bytes decoded as JSON map), `base64()` (base64 string). |

**DSL example:**
```kotlin
GetFile(
    trigger = EventTriggers.onClick(),
    fileName = "avatar.png",
    outputType = platformFile()
)
```

**Triggers fired:** `OnSuccess` (content in the chosen shape as incoming data), `OnFailure` (read failed, no file exists under `fileName` — `NoSuchElementException` — or `MapObject` decoding failed; `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

### `OpenFilePicker`

Opens the platform's native file picker and emits the chosen file downstream.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `fileType` | `FileType` | required | What can be picked — `imageFileType()`, `videoFileType()`, `imageAndVideoFileType()`, or `fileFileType(vararg extensions)` for arbitrary files narrowed by extension. |
| `pickMode` | `PickMode` | required | Currently only `singlePickMode()` exists. |
| `outputType` | `FileOutputType` | `platformFile()` | Shape of the emitted content — same 5 options as `GetFile`, different default. |

**DSL example:**
```kotlin
OpenFilePicker(
    trigger = EventTriggers.onClick(),
    fileType = imageFileType(),
    pickMode = singlePickMode(),
    outputType = platformFile()
)
```

**Triggers fired:** `OnSuccess` (file picked and read, content as incoming data), `OnCancelled` (user dismissed the picker without choosing, no data), `OnFailure` (`MapObject` decoding failed, or the picker itself threw — `Throwable` as incoming data).

**Notes:** default `outputType` is `platformFile()` here, but `arrayOfBytes()` on `GetFile` — the two events share the `FileOutputType` enum but not its default. `incomingData` not consumed.

### `SaveFile`

Writes the event's `incomingData` to `fileName` in the client's own file storage.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `fileName` | `String` | required | File to write. |
| `overrideIfExists` | `Boolean` | required (no default) | When `false`, the event first checks whether `fileName` already exists and refuses to write if so. |

**DSL example:**
```kotlin
SaveFile(
    trigger = EventTriggers.onSuccess(),
    fileName = "cache.bin",
    overrideIfExists = true
)
```

**Triggers fired:** `OnSuccess` (written, no data forwarded), `OnFailure` — three distinct causes, all logged: `incomingData` missing or not a `ByteArray` (`IllegalArgumentException`), `overrideIfExists = false` and the file already exists (`IllegalStateException`), or the write itself failed (its own `Throwable`).

**Notes:** `incomingData` is **required** and must be a `ByteArray` — this is the only way content reaches the event, there's no literal-content parameter.

## Image

### `TakePicture`

Opens the camera through the client's `CameraManager` and emits the captured photo downstream. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `compression` | `CompressionScheme?` | `null` | `byQuality(percent)` or `byTargetSize(kb)` — when set, re-encodes the capture (treated as `image/png`). `null` emits the original bytes untouched. |
| `resize` | `ImageResizeOptions?` (`maxLongEdgePx`, `downscaleOnly`, `maintainAspectRatio`) | `null` | Only takes effect alongside a non-null `compression`; default resize options apply when left `null` there. |
| `outputType` | `OutputType` (`ArrayOfBytes`/`Base64`) | required | Shape of the emitted image — `pictureArrayOfBytes()`/`pictureBase64()`. |

**DSL example:**
```kotlin
TakePicture(
    trigger = EventTriggers.onClick(),
    compression = byQuality(qualityPercent = 80f),
    outputType = pictureArrayOfBytes()
)
```

**Triggers fired:** `OnSuccess` (image in the chosen shape as incoming data), `OnCancelled` (camera returned nothing, e.g. user backed out), `OnFailure` (capturing or compressing threw — `Throwable` as incoming data).

**Notes:** `incomingData` not consumed. `resize` is silently a no-op if `compression` is `null`.

### `GetImageFromGallery`

Opens the platform picker restricted to images and emits the chosen image downstream. Same compression/resize contract as `TakePicture`. Runs on the IO dispatcher.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `compression` | `CompressionScheme?` | `null` | Same as `TakePicture` — re-encodes when set. |
| `resize` | `ImageResizeOptions?` | `null` | Same as `TakePicture` — only effective alongside `compression`. |
| `outputType` | `OutputType` (`ArrayOfBytes`/`Base64`) | required | `galleryArrayOfBytes()`/`galleryBase64()`. |

**DSL example:**
```kotlin
GetImageFromGallery(
    trigger = EventTriggers.onClick(),
    resize = ImageResizeOptions(maxLongEdgePx = 1024),
    outputType = galleryArrayOfBytes()
)
```

**Triggers fired:** `OnSuccess` (image as incoming data), `OnCancelled` (picker dismissed without a choice), `OnFailure` (reading or compressing threw — `Throwable` as incoming data).

**Notes:** its `OutputType`/compression helpers are separate functions from `TakePicture`'s (`gallery*` vs. `picture*` prefix) even though the underlying types have the same shape — not shared between the two events. `incomingData` not consumed.

## Menu & popup

### `ToggleMenu`

Flips the open/closed state of the `Menu` tile identified by `menuId`. Since the tile only closes on dismissal by itself, this is how a menu is opened from the server side — and, wired onto a menu item's click, how it's closed after acting on a selection.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `menuId` | `String` | required | Id of the target `Menu` tile. |

**DSL example:**
```kotlin
ToggleMenu(trigger = EventTriggers.onClick(), menuId = "item_menu")
```

**Triggers fired:** `OnSuccess` (signal reached the tile, no data forwarded), `OnFailure` (no tile with `menuId` currently mounted — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

### `TogglePopup`

Flips the open/closed state of the `Popup` tile identified by `popupId` — the same mechanism as `ToggleMenu`, for popups.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `popupId` | `String` | required | Id of the target `Popup` tile. |

**DSL example:**
```kotlin
TogglePopup(trigger = EventTriggers.onClick(), popupId = "info_popup")
```

**Triggers fired:** `OnSuccess` (signal reached the tile, no data forwarded), `OnFailure` (no tile with `popupId` currently mounted — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

## Navigation

### `Navigate`

Navigates the graph registered under `navigatorId` to `destination`, pushing it onto the back stack.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `destination` | `String` | required | Screen id to navigate to. |
| `navigatorId` | `String` | required (no default anywhere in the framework — see `architecture.md` §4) | Which registered navigator's back stack to push onto (`"root"` is the app's own, by convention). |
| `popUpTo` | `PopUpTo?` — `poppingUpTo(destination, inclusive)` | `null` | Pops entries up to `destination` before pushing the new one. |
| `data` | `Map<String, AnySerializable>?` | `null` | Literal navigation arguments — wins over `incomingData` on key collision. |

**DSL example:**
```kotlin
Navigate(
    trigger = EventTriggers.onSuccess(),
    destination = "product_details",
    navigatorId = "root",
    data = mapOf("productId" to "<|id|>")
)
```

**Triggers fired:** `OnSuccess` (navigation performed, no data forwarded), `OnFailure` (no navigator registered under `navigatorId`, or the navigator refused the navigation — no data, logged).

**Notes:** navigation arguments the destination receives are `incomingData` merged with `data`, `data` winning on key collision — only map-shaped `incomingData` contributes, and `null` values are dropped from both sides (navigation arguments are never `null`).

### `NavigateUp`

Pops the back stack of the graph registered under `navigatorId`, going back one entry.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `navigatorId` | `String` | required | Which registered navigator to pop. |

**DSL example:**
```kotlin
NavigateUp(trigger = EventTriggers.onClick(), navigatorId = "root")
```

**Triggers fired:** `OnSuccess` (entry popped, no data forwarded), `OnFailure` (no navigator registered, or nothing to pop — no data, logged).

**Notes:** `incomingData` not consumed.

### `NavigateClearingStack`

Navigates the graph registered under `navigatorId` to `destination`, clearing the whole back stack so the destination becomes the only entry — the usual move after login or logout.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `destination` | `String` | required | Screen id to navigate to. |
| `navigatorId` | `String` | required | Which registered navigator's back stack to clear and push onto. |
| `launchSingleTop` | `Boolean` | `true` | Avoids stacking a second copy when the destination is already the current entry. |
| `data` | `Map<String, AnySerializable>?` | `null` | Same merge-with-`incomingData` semantics as `Navigate`. |

**DSL example:**
```kotlin
NavigateClearingStack(
    trigger = EventTriggers.onSuccess(),
    destination = "home",
    navigatorId = "root"
)
```

**Triggers fired:** `OnSuccess` (navigation performed, no data forwarded), `OnFailure` (no navigator registered, or refused — no data, logged).

**Notes:** the only one of the 3 navigation events with `launchSingleTop`. Same `incomingData`+`data` merge rule as `Navigate`.

## Networking

### `DownloadFile`

Downloads `url` and hands the result to the platform's own download destination — the user's Downloads folder or equivalent.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | Endpoint to download from. |
| `method` | `HttpMethod` | required | HTTP method. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `targetFileName` | `String` | required | Name the downloaded file is saved under. |
| `mimeType` | `String?` | `null` | Content type describing the saved file. |

**DSL example:**
```kotlin
DownloadFile(
    trigger = EventTriggers.onClick(),
    url = "/api/reports/latest",
    method = HttpMethod.GET,
    targetFileName = "report.pdf",
    mimeType = "application/pdf"
)
```

**Triggers fired:** `OnStart` (before the download begins), `OnDownloadProgress` (repeatedly, progress as incoming data), then either `OnDownloadFinish` + `OnSuccess` (both carrying `targetFileName`) or `OnDownloadFailure` + `OnFailure` (both carrying the `Throwable`, logged) — or `OnCancelled` alone (user cancelled; neither failure trigger fires in that case).

**Notes:** the only one of the 3 download events with an `OnCancelled` trigger — it's the only one whose destination (the platform's public download UI) can be interactively cancelled by the user. `incomingData` not consumed.

### `DownloadFileToDisk`

Downloads `url` into the client's own private file storage under `targetFileName` — `GetFile`/`DeleteFile` can reach it afterwards.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | Endpoint to download from. |
| `method` | `HttpMethod` | required | HTTP method. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `targetFileName` | `String` | required | Name the file is saved under, in the app's private storage. |

**DSL example:**
```kotlin
DownloadFileToDisk(
    trigger = EventTriggers.onClick(),
    url = "/api/offline-data",
    method = HttpMethod.GET,
    targetFileName = "offline_cache.json"
)
```

**Triggers fired:** `OnStart`, `OnDownloadProgress` (progress as incoming data), then `OnDownloadFinish` + `OnSuccess` (both carrying `targetFileName`) or `OnDownloadFailure` + `OnFailure` (both carrying the `Throwable`, logged). No `OnCancelled` — private storage writes aren't interactively cancellable.

**Notes:** `incomingData` not consumed.

### `DownloadFileToMemory`

Downloads `url` without touching the filesystem, keeping the content in memory.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | Endpoint to download from. |
| `method` | `HttpMethod` | required | HTTP method. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |

**DSL example:**
```kotlin
DownloadFileToMemory(
    trigger = EventTriggers.onClick(),
    url = "/api/thumbnail",
    method = HttpMethod.GET
)
```

**Triggers fired:** `OnStart`, `OnDownloadProgress` (progress as incoming data), then `OnDownloadFinish` + `OnSuccess` (both carrying the total byte count, not a file name) or `OnDownloadFailure` + `OnFailure` (both carrying the `Throwable`, logged).

**Notes:** the only download event that never touches the filesystem. `incomingData` not consumed.

### `SendNetworkRequest`

Sends an HTTP request to `url` and emits the response downstream — the general-purpose way to talk to a backend from inside an event chain.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | Endpoint. |
| `method` | `HttpMethod` | required | HTTP method. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `timeoutMillis` | `Long?` | `null` | Request timeout override. |

**DSL example:**
```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/login",
    method = HttpMethod.POST
)
```

**Triggers fired:** `OnStart` (before sending) — then exactly one outcome trigger per response: `OnNetworkResponse`/`OnNetworkFailure` (only if this event has a child wired to that **exact** HTTP status code — the first for 2xx, the second for the rest, parsed body as incoming data), otherwise `OnSuccess` (2xx) or `OnFailure` (non-2xx, parsed body as incoming data — or the request itself failed, e.g. timeout/no connectivity, `Throwable` as incoming data, logged).

**Notes:** response body is auto-parsed — JSON content types become plain maps/lists/primitives, everything else stays a raw `ByteArray`; a JSON body that fails to parse yields `null` rather than throwing. `incomingData` not consumed by this event, but its parsed response becomes the next event's `incomingData`.

### `SetIncomingDataToNetworkParamsHolderBody`

Stores the event's `incomingData` as the request body in the client's `NetworkParametersHolder`, so a later request in the chain picks it up instead of carrying the body on its own schema.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess())
```

**Triggers fired:** `OnSuccess` (stored, no data forwarded), `OnFailure` (`incomingData` is `null`, logged).

**Notes:** `incomingData` is **required** — any non-null value is accepted as-is, no type validation. See `architecture.md` §4 for `NetworkParametersHolder`'s "whoever consumes it first wins" behavior — this event only stages the value, it doesn't guarantee which subsequent network event reads it.

### `SetIncomingDataToNetworkParamsHolderHeaders`

Same staging mechanism as the Body variant, for request headers.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess())
```

**Triggers fired:** `OnSuccess` (stored, no data forwarded), `OnFailure` (`incomingData` missing, not a map, or holds no `String` value at all — logged).

**Notes:** `incomingData` must be a map holding **at least one** `String` value — entries whose value isn't a `String` are silently dropped rather than failing the whole event.

### `SetIncomingDataToNetworkParamsHolderQueryParameters`

Same staging mechanism, for query parameters.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
SetIncomingDataToNetworkParamsHolderQueryParameters(trigger = EventTriggers.onSuccess())
```

**Triggers fired:** `OnSuccess` (stored, no data forwarded), `OnFailure` (`incomingData` missing or not a map — logged).

**Notes:** unlike the Headers variant, there's no "at least one valid value" requirement here — any map keyed by `String` is accepted, values aren't validated.

### `SetIncomingDataToNetworkParamsHolderUrl`

Same staging mechanism, for the URL.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
SetIncomingDataToNetworkParamsHolderUrl(trigger = EventTriggers.onSuccess())
```

**Triggers fired:** `OnSuccess` (stored, no data forwarded), `OnFailure` (`incomingData` missing or not a `String` — logged).

**Notes:** `UploadFile` is the primary consumer of this one — it's the only network event whose `url` field is nullable, precisely so it can be fed from here instead of a schema literal.

### `UploadFile`

Uploads the file carried in the event's `incomingData` to `url`, reporting progress as it goes. Pair with `OpenFilePicker` or `GetFile` using their `PlatformFile` output.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String?` | `null` | Endpoint — falls back to `NetworkParametersHolder` (see `SetIncomingDataToNetworkParamsHolderUrl`) when omitted. |
| `method` | `HttpMethod` | `HttpMethod.PUT` | HTTP method — the only networking event with a non-required default here. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `contentType` | `String?` | `null` | Content type of the uploaded file. |

**DSL example:**
```kotlin
UploadFile(
    trigger = EventTriggers.onSuccess(),
    url = "<|signedUrl|>",
    contentType = "image/png"
)
```

**Triggers fired:** `OnStart` (after `incomingData` was validated, before the upload begins), `OnUploadProgress` (repeatedly, progress as incoming data), then `OnNetworkResponse`/`OnNetworkFailure` (only if a child is wired to the exact status) or `OnSuccess` (parsed body as incoming data) — or `OnFailure`, three distinct causes: `incomingData` not a `PlatformFile` (fires **before** `OnStart`, no data), non-2xx response with no status-specific child (parsed body as incoming data), or the upload itself failing (`Throwable`).

**Notes:** `incomingData` is **required** and must be a `PlatformFile`. Response parsing follows the same JSON-or-raw-bytes rule as `SendNetworkRequest`.

## Overlays

Two structurally different groups: `BottomSheet`/`ModalBottomSheet`/`Dialog` are addressed by an id you choose and can fail (`OnFailure` exists); `NavigationDrawer`/`Snackbar` are pure fire-and-forget broadcasts with no id (one drawer/snackbar per screen) and no `OnFailure` at all.

### `DisplayBottomSheet`

Shows a **non-modal** bottom sheet built from `tiles`, registered under `bottomSheetId` — doesn't dim or block the content behind it.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `bottomSheetId` | `String` | required | Id this sheet is registered under, for `DismissBottomSheet` to target later. |
| `tiles` | `List<TileSchema>` | required | Sheet content. |
| `isCancellable` | `Boolean` | required | Whether the user can dismiss it by gesture. |
| `fill` | `Boolean` | required | Whether the sheet takes the full height. |
| `allowsPartialExpansion` | `Boolean` | required | Whether it stops at a half-expanded state before reaching full height. |

**DSL example:**
```kotlin
DisplayBottomSheet(
    trigger = EventTriggers.onClick(),
    bottomSheetId = "filters",
    isCancellable = true,
    fill = false,
    allowsPartialExpansion = true,
    tiles = { Column { /* filter controls */ } }
)
```

**Triggers fired:** `OnSuccess` (added, no data forwarded), `OnFailure` (couldn't be added — typically `bottomSheetId` already in use — `Throwable` as incoming data), `OnDisplay` (fires once, when the sheet actually enters composition on screen — later than `OnSuccess`, which fires as soon as it's registered).

**Notes:** `incomingData` not consumed.

### `DismissBottomSheet`

Closes the bottom sheet registered under `bottomSheetId`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `bottomSheetId` | `String` | required | Id of the sheet to close. |

**DSL example:**
```kotlin
DismissBottomSheet(trigger = EventTriggers.onClick(), bottomSheetId = "filters")
```

**Triggers fired:** `OnSuccess` (dismissed, no data forwarded), `OnFailure` (no sheet showing under that id — `Throwable` as incoming data).

**Notes:** dismissal here starts the two-phase animation handshake described in `architecture.md` §5 — the overlay isn't actually gone from the tree the instant this event succeeds.

### `DisplayModalBottomSheet`

Shows a **modal** bottom sheet built from `tiles`, registered under `modalBottomSheetId` — unlike the plain variant, this one dims and blocks the content behind it.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `modalBottomSheetId` | `String` | required | Id this sheet is registered under. |
| `tiles` | `List<TileSchema>` | required | Sheet content. |
| `isCancellable` | `Boolean` | required | Whether the user can dismiss it by gesture or scrim tap. |
| `fill` | `Boolean` | required | Whether the sheet takes the full height. |
| `allowsPartialExpansion` | `Boolean` | required | Whether it stops at a half-expanded state. |

**DSL example:**
```kotlin
DisplayModalBottomSheet(
    trigger = EventTriggers.onClick(),
    modalBottomSheetId = "share_sheet",
    isCancellable = true,
    fill = false,
    allowsPartialExpansion = false,
    tiles = { /* share options */ }
)
```

**Triggers fired:** `OnSuccess` (added, no data forwarded), `OnFailure` (couldn't be added, typically `modalBottomSheetId` already in use — `Throwable` as incoming data), `OnDisplay` (fires once, when the sheet actually enters composition on screen — later than `OnSuccess`, which fires as soon as it's registered).

**Notes:** `incomingData` not consumed.

### `DismissModalBottomSheet`

Closes the modal bottom sheet registered under `modalBottomSheetId`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `modalBottomSheetId` | `String` | required | Id of the sheet to close. |

**DSL example:**
```kotlin
DismissModalBottomSheet(trigger = EventTriggers.onClick(), modalBottomSheetId = "share_sheet")
```

**Triggers fired:** `OnSuccess` (dismissed, no data forwarded), `OnFailure` (nothing showing under that id — `Throwable` as incoming data).

**Notes:** same two-phase dismissal handshake as `DismissBottomSheet`.

### `DisplayDialog`

Shows a dialog built from `tiles`, registered under `dialogId`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `dialogId` | `String` | required | Id this dialog is registered under. |
| `tiles` | `List<TileSchema>` | required | Dialog content. |
| `isCancellable` | `Boolean` | required | Whether the back gesture or a scrim tap dismisses it. |
| `usePlatformDefaultWidth` | `Boolean` | required | `true` keeps the platform's default width; `false` lets the dialog size itself from its content. |

**DSL example:**
```kotlin
DisplayDialog(
    trigger = EventTriggers.onClick(),
    dialogId = "confirm_delete",
    isCancellable = true,
    usePlatformDefaultWidth = true,
    tiles = { /* confirmation content */ }
)
```

**Triggers fired:** `OnSuccess` (added, no data forwarded), `OnFailure` (couldn't be added, typically `dialogId` already in use — `Throwable` as incoming data), `OnDisplay` (fires once, when the dialog actually enters composition on screen — later than `OnSuccess`, which fires as soon as it's registered).

**Notes:** `incomingData` not consumed. Unlike the two bottom sheet variants, `Dialog`'s dismissal has **no exit animation to wait for** — its two-phase handshake resolves instantly (see `architecture.md` §5).

### `DismissDialog`

Closes the dialog registered under `dialogId`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `dialogId` | `String` | required | Id of the dialog to close. |

**DSL example:**
```kotlin
DismissDialog(trigger = EventTriggers.onClick(), dialogId = "confirm_delete")
```

**Triggers fired:** `OnSuccess` (dismissed, no data forwarded), `OnFailure` (nothing showing under that id — `Throwable` as incoming data).

**Notes:** `incomingData` not consumed.

### `DisplayNavigationDrawer`

Opens the screen's navigation drawer, by broadcasting an open command on the screen's broadcast channel. The drawer's content is declared on the screen itself (`ScreenTileSchema.navigationDrawerTiles`), so this event carries no parameters — there's only ever one drawer per screen.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
DisplayNavigationDrawer(trigger = EventTriggers.onClick())
```

**Triggers fired:** `OnSuccess` — always, right after the broadcast, fire-and-forget. Fires even when the screen declares no drawer at all.

**Notes:** no `OnFailure` exists for this event — see the group note above. `incomingData` not consumed.

### `DismissNavigationDrawer`

Closes the screen's navigation drawer, by broadcasting a close command.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
DismissNavigationDrawer(trigger = EventTriggers.onClick())
```

**Triggers fired:** `OnSuccess` — always, fire-and-forget. Fires even when no drawer is open.

**Notes:** `incomingData` not consumed.

### `DisplaySnackbar`

Shows a snackbar with `message`, by broadcasting a display command on the screen's broadcast channel.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `message` | `String` | required | Snackbar text. |
| `duration` | `SnackbarDuration` (`Short`/`Long`/`Indefinite`) | `Short` — `snackbarShortDuration()`/`snackbarLongDuration()`/`snackbarIndefiniteDuration()` | How long it stays visible. |
| `actionLabel` | `String?` | `null` | Adds an action button when non-null. |

**DSL example:**
```kotlin
DisplaySnackbar(
    trigger = EventTriggers.onFailure(),
    message = "Login failed",
    duration = snackbarShortDuration()
)
```

**Triggers fired:** `OnSuccess` (right after the broadcast, before the snackbar has actually resolved — fire-and-forget), then, **later, asynchronously**: `OnSnackbarAction` (user pressed the action button) or `OnSnackbarDismissed` (it went away without the action being pressed).

**Notes:** `incomingData` not consumed. `OnSuccess` firing doesn't mean the snackbar was seen or resolved — it only confirms the broadcast was sent; `OnSnackbarAction`/`OnSnackbarDismissed` are the events that reflect what actually happened, and can fire well after `OnSuccess` already ran downstream events.

### `DismissSnackbar`

Hides the snackbar currently showing on the screen, by broadcasting a dismiss command.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
DismissSnackbar(trigger = EventTriggers.onClick())
```

**Triggers fired:** `OnSuccess` — always, fire-and-forget. Fires even when no snackbar is showing.

**Notes:** `incomingData` not consumed.

## Pull to refresh

### `StopRefreshing`

Stops the loading indicator of the `PullToRefresh` tile identified by `tileId`. The tile never hides its own spinner — this event has to close every refresh flow, on the success branch and the failure branch alike (see `tiles-catalog.md`'s `PullToRefresh` entry).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tileId` | `String` | required | Id of the target `PullToRefresh` tile. |

**DSL example:**
```kotlin
RefreshScreen(
    trigger = EventTriggers.onPull(),
    events = {
        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "list")
        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "list")
    }
)
```

**Triggers fired:** `OnSuccess` (signal reached the tile, no data forwarded), `OnFailure` (no tile with `tileId` currently mounted — `Throwable` as incoming data).

**Notes:** `incomingData` not consumed. Forgetting to chain this on the failure branch is the single most common way a `PullToRefresh` spinner gets stuck.

## Screen

### `GetScreen`

Fetches the payload of the screen this event lives in and emits it downstream **without applying it**. Pair with `ChangeScreenState` to decide when and how the fetched content is installed.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `method` | `HttpMethod` | `HttpMethod.GET` | HTTP method for the fetch. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `timeoutMillis` | `Long?` | `null` | Request timeout override. |

**DSL example:**
```kotlin
GetScreen(
    trigger = EventTriggers.onDisplay(),
    events = { ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState()) }
)
```

**Triggers fired:** `OnStart` (before the request), `OnSuccess` (the fetched `ScreenModel` as incoming data, ready for `ChangeScreenState`), `OnNetworkFailure` (only if this event has a child wired to the exact HTTP status of the failure), otherwise `OnFailure` (every other failure — `Throwable` as incoming data, logged).

**Notes:** the request always targets **this screen's own id** — there's no way to fetch a different screen's content through this event; fetching another screen is always navigation (`Navigate`/`NavigateClearingStack`). This is exactly the pair the `initialEvents` default of every `entry{}` uses (see `architecture.md` §5). `incomingData` not consumed.

### `RefreshScreen`

Refetches the screen this event lives in **and applies the result to it** — `GetScreen` + `ChangeScreenState` fused into one step. The screen moves to its initial (loading) state, then to success with the new content or to its failure state.

**Parameters:** identical to `GetScreen` — `method` (default `HttpMethod.GET`), `body`, `headers`, `timeoutMillis`.

**DSL example:**
```kotlin
RefreshScreen(trigger = EventTriggers.onPull())
```

**Triggers fired:** `OnSuccess` (fetched and applied, `ScreenModel` as incoming data), `OnNetworkFailure` (status-specific child exists), otherwise `OnFailure` (every other failure — `Throwable` as incoming data, logged; the screen is left in its failure state either way).

**Notes:** unlike `GetScreen`, no extra event is needed to install what came back — but that also means it always flashes the screen back to its loading state first, even if the previous content is still valid. `incomingData` not consumed.

### `ChangeScreenState`

Moves the screen this event lives in to another state, with no network call involved.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `state` | `State` — `initialState()`, `failureState()`, or `successState(data?)` | required | Target state. `Success.data` (tiles/navigationDrawerTiles/events) is optional. |

**DSL example:**
```kotlin
ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
```

**Triggers fired:** `OnSuccess` (state applied, no data forwarded), `OnFailure` (applying the state threw — including `Success` with no declared data and no `ScreenModel` in `incomingData` — no data, logged).

**Notes:** for `Success`, content comes from `state`'s own `data` when passed to `successState(data)`, otherwise from `incomingData`, which must then be a `ScreenModel` — typically the one produced by a preceding `GetScreen`. `successState()` with no argument (the default used by every `entry{}`) always relies on `incomingData`.

## Scroll

### `ScrollColumnTile`

Scrolls the `Column` or `LazyColumn` tile identified by `tileId`, by broadcasting a scroll command on the screen's broadcast channel.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tileId` | `String` | required | Target tile. |
| `where` | `Where` — `scrollColumnToTop()`, `scrollColumnTo(index)`, `scrollColumnToBottom()` | required | Scroll destination. |
| `smoothly` | `Boolean` | required | Animated vs. immediate jump. |

**DSL example:**
```kotlin
ScrollColumnTile(
    trigger = EventTriggers.onClick(),
    tileId = "list",
    where = scrollColumnToTop(),
    smoothly = true
)
```

**Triggers fired:** `OnSuccess` — always, fire-and-forget, fires even when no tile carries `tileId`.

**Notes:** `where`'s `To(index)` is read as a **pixel offset** by a plain `Column` and as a **child index** by a `LazyColumn` — same field, different meaning depending on which tile receives it. `incomingData` not consumed, no `OnFailure` exists.

### `ScrollRowTile`

Horizontal counterpart of `ScrollColumnTile`, for `Row`/`LazyRow`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tileId` | `String` | required | Target tile. |
| `where` | `Where` — `scrollRowToStart()`, `scrollRowTo(index)`, `scrollRowToEnd()` | required | Scroll destination. |
| `smoothly` | `Boolean` | required | Animated vs. immediate jump. |

**DSL example:**
```kotlin
ScrollRowTile(
    trigger = EventTriggers.onClick(),
    tileId = "tabs_row",
    where = scrollRowTo(index = 3),
    smoothly = true
)
```

**Triggers fired:** `OnSuccess` — always, fire-and-forget.

**Notes:** same pixel-offset-vs-index distinction as `ScrollColumnTile`, on the horizontal axis. No `OnFailure`.

### `ScrollPagerTile`

Moves the `Pager` or `Carousel` tile identified by `tileId` to another page/item.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tileId` | `String` | required | Target tile. |
| `where` | `Where` — `scrollPageToBegin()`, `scrollPageToPreviousPage()`, `scrollPageToNextPage()`, `scrollPageToEnd()` | required | Scroll destination. |
| `smoothly` | `Boolean` | required | Animated vs. immediate jump. |

**DSL example:**
```kotlin
ScrollPagerTile(
    trigger = EventTriggers.onClick(),
    tileId = "onboarding",
    where = scrollPageToNextPage(),
    smoothly = true
)
```

**Triggers fired:** `OnSuccess` — always, fire-and-forget.

**Notes:** unlike the other two, there's no `To(index)` variant — only relative navigation (begin/end/next/previous). The receiving tile clamps the result, so requesting the next page on the last one is a no-op, not an error. Shared by both `Pager` and `Carousel` (they use the same broadcast). No `OnFailure`.

## Security & system

### `RequestPermission`

Asks the platform for the runtime permissions listed in `permissions`, through the client's `PermissionManager`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `permissions` | `List<Permissions>` | required | Requested permissions — `cameraPermission()`, `galleryPermission()`, `storagePermission()`, `microphonePermission()`, `locationPermission()`, `notificationPermission()`, `contactsPermission()`. |

**DSL example:**
```kotlin
RequestPermission(
    trigger = EventTriggers.onClick(),
    permissions = listOf(cameraPermission(), microphonePermission())
)
```

**Triggers fired:** `OnPermissionsAcquired` then `OnSuccess` (every requested permission granted), `OnPermissionsDenied` then `OnFailure` (denied), `OnPermissionRationale` (platform wants a rationale shown before asking again — chain the explanation onto this and request once more). No data passed in any case.

**Notes:** `incomingData` not consumed. `Rationale` is Android-only in practice (per platform permission APIs — see `architecture.md` §7); other platforms only ever produce `Granted`/`Denied`.

### `BroadcastToSystem`

Publishes a value on the client's system broadcast channel under `broadcastId` — the outbound half of the bridge between server-declared flows and native app code (push notifications, connectivity changes, etc. can be the *inbound* half via `SystemBroadcastListener`).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `broadcastId` | `String` | required | Channel id — matched against `OnSystemBroadcast(broadcastId)` on the receiving end. |
| `data` | `BroadcastData` — `incomingBroadcastData()` or `inlineBroadcastData(value)` | required | Payload: `incomingBroadcastData()` publishes `incomingData`; `inlineBroadcastData(value)` publishes a literal. |

**DSL example:**
```kotlin
BroadcastToSystem(
    trigger = EventTriggers.onSuccess(),
    broadcastId = "cart_updated",
    data = incomingBroadcastData()
)
```

**Triggers fired:** `OnSuccess` (published, no data forwarded), `OnFailure` (only in the `incomingBroadcastData()` case, when `incomingData` is `null` — nothing to publish).

**Notes:** app-wide, not screen-scoped — reaches the host application and any mounted `SystemBroadcastListener` tile, on any screen.

### `CheckIfHasInternetConnection`

Asks the client's network layer whether the device currently has an internet connection, and branches on the answer. Runs on the IO dispatcher.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
CheckIfHasInternetConnection(trigger = EventTriggers.onDisplay())
```

**Triggers fired:** `OnStart` (before the check), `OnSuccess` (connected, no data), `OnFailure` (not connected, no data).

**Notes:** `incomingData` not consumed.

### `DropCaches`

Clears the client's local caches, selectively.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `dropScreensCache` | `Boolean` | required | Clears cached screen payloads. |
| `dropInitialGraphCache` | `Boolean` | required | Clears the cached initial navigation graph. |
| `dropVersionCache` | `Boolean` | required | Clears the cached version marker used to decide whether cached content is still valid. |

**DSL example:**
```kotlin
DropCaches(
    trigger = EventTriggers.onClick(),
    dropScreensCache = true,
    dropInitialGraphCache = false,
    dropVersionCache = false
)
```

**Triggers fired:** `OnSuccess` (dropped, no data forwarded), `OnFailure` (dropping failed — `Throwable` as incoming data, logged).

**Notes:** all 3 flags are required, no defaults — forces the author to decide each one explicitly rather than silently clearing everything.

### `OpenExternalLink`

Hands `url` to the platform so it opens outside the app — the system browser, or whichever app claims the scheme.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | URL or URI scheme to open. |

**DSL example:**
```kotlin
OpenExternalLink(trigger = EventTriggers.onClick(), url = "https://example.com/terms")
```

**Triggers fired:** `OnSuccess` (platform accepted the request, no data forwarded), `OnFailure` (opening threw, e.g. nothing handles the URL — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed. Accepting the request isn't proof the URL actually opened something meaningful — a scheme with no registered handler can still fail silently depending on the platform.

## Theme

### `SetTheme`

Overrides the app's Material color schemes at runtime — every Material 3 color role, from `primary` through the surface-container and fixed-accent families.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `colorsScheme` | `ColorsScheme` — `colorsScheme(light, dark)` | required | A full light **and** full dark `ColorScheme`, each built via `colorScheme(...)` (48 hex-string parameters, one per Material 3 role — no partial overrides, every role must be specified). |

**DSL example:**
```kotlin
SetTheme(
    trigger = EventTriggers.onSuccess(),
    colorsScheme = colorsScheme(
        light = colorScheme(primary = "#6750A4", onPrimary = "#FFFFFF", /* ...45 more roles */),
        dark = colorScheme(primary = "#D0BCFF", onPrimary = "#381E72", /* ...45 more roles */)
    )
)
```

**Triggers fired:** `OnSuccess` — always, after both schemes are applied. No `OnFailure` exists.

**Notes:** `incomingData` not consumed. Both light and dark are always applied together in one call — there's no way to override just one; the app keeps following the system's light/dark setting, only the values each mode resolves to change. There's no partial/incremental theming — every one of the 48 roles must be supplied on every call.

### `ResetTheme`

Drops any color scheme previously installed by `SetTheme`, putting the app back on the color scheme it was built with.

**Parameters:** none beyond the base fields.

**DSL example:**
```kotlin
ResetTheme(trigger = EventTriggers.onClick())
```

**Triggers fired:** `OnSuccess` — always, after the reset. No `OnFailure` exists.

**Notes:** `incomingData` not consumed.

## Tile management

### `AddTiles`

Appends `tiles` as children of the grouping tile identified by `groupingTileId`, without rebuilding the rest of the screen.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Target container tile. |
| `tiles` | `List<TileSchema>` | required | Tiles to insert. |
| `position` | `InsertionPosition` — `insertAtStart()`, `insertAtEnd()`, `insertBeforeTile(id)`, `insertAfterTile(id)`, `insertAtIndex(i)` | required | Where to insert. |

**DSL example:**
```kotlin
AddTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "results",
    position = insertAtEnd(),
    tiles = { items.forEach { Card(id = it.id) { SimpleText(text = it.title) } } }
)
```

**Triggers fired:** `OnSuccess` (added, no data forwarded), `OnFailure` (no grouping tile carries `groupingTileId`, or it can't hold children — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed. `tiles` is a DSL block, but it's evaluated exactly once, when the screen is built server-side — any `id`/`randomId()` calls inside it resolve to fixed values baked into the served schema. If this same `AddTiles` fires more than once (e.g. a repeatable trigger like `onScrollThresholdReached` firing again after the list grows), it re-inserts the *same* ids, which crashes list renderers that key by id (`LazyColumn`/`LazyRow`). For pagination that needs to fire repeatedly, fetch each page over the network instead (fresh ids generated per response) rather than relying on a single static `AddTiles`, or disable further firing after one page (e.g. patch the source tile's `scrollThreshold` to `null` via `UpdateTiles` once the list is exhausted).

### `CheckIfTileContainsChildren`

Tests whether the grouping tile identified by `groupingTileId` currently holds every child listed in `childrenIds`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile to inspect. |
| `childrenIds` | `List<String>` | required | Ids expected to be present. |

**DSL example:**
```kotlin
CheckIfTileContainsChildren(
    trigger = EventTriggers.onDisplay(),
    groupingTileId = "cart_items",
    childrenIds = listOf("item_1", "item_2")
)
```

**Triggers fired:** `OnSuccess` (all listed children present, no data), `OnFailure` (at least one missing, or no grouping tile carries `groupingTileId` — no data in either case, so the two failure causes are indistinguishable downstream).

**Notes:** `incomingData` not consumed.

### `GetTileChildrenCount`

Reads how many children the grouping tile identified by `groupingTileId` currently holds.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile to inspect. |

**DSL example:**
```kotlin
GetTileChildrenCount(trigger = EventTriggers.onDisplay(), groupingTileId = "cart_items")
```

**Triggers fired:** `OnSuccess` (count as incoming data, an `Int`), `OnFailure` (no grouping tile carries `groupingTileId`, or it can't hold children — no data).

**Notes:** `incomingData` not consumed.

### `ReloadLazyTiles`

Resets the `LazyTiles` tile identified by `lazyTileId` back to its loading state, making it fire its network request again — the way to retry after a failed load.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `lazyTileId` | `String` | required | Target `LazyTiles` tile. |

**DSL example:**
```kotlin
ReloadLazyTiles(trigger = EventTriggers.onClick(), lazyTileId = "recommendations")
```

**Triggers fired:** `OnSuccess` (signal reached the tile, no data forwarded), `OnFailure` (no tile with `lazyTileId` currently mounted — `Throwable` as incoming data).

**Notes:** `incomingData` not consumed. `OnSuccess` here reports the reset signal reaching the tile, **not** the outcome of the reload that follows — that arrives later through the `LazyTiles` tile's own `OnLoadTilesSuccess`/`OnLoadTilesFailure` triggers, not through this event.

### `RemoveTiles`

Removes the children listed in `tileIds` from the grouping tile identified by `groupingTileId`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Target container tile. |
| `tileIds` | `List<String>` | required | Ids to remove. |

**DSL example:**
```kotlin
RemoveTiles(
    trigger = EventTriggers.onClick(),
    groupingTileId = "cart_items",
    tileIds = listOf("item_2")
)
```

**Triggers fired:** `OnSuccess` (removal completed, no data forwarded), `OnFailure` (no grouping tile carries `groupingTileId` — `Throwable` as incoming data, logged).

**Notes:** ids in `tileIds` that aren't among the container's actual children are silently ignored — not a failure. `incomingData` not consumed.

### `ReplaceTiles`

Swaps the whole children list of the grouping tile identified by `groupingTileId` for `tiles`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Target container tile. |
| `tiles` | `List<TileSchema>` | required | New complete children list. |

**DSL example:**
```kotlin
ReplaceTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "results",
    tiles = { items.forEach { Card(id = it.id) { SimpleText(text = it.title) } } }
)
```

**Triggers fired:** `OnSuccess` (replaced, no data forwarded), `OnFailure` (no grouping tile carries `groupingTileId` — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

### `UpdateTiles`

Patches tiles in place — the primary way to change what's on screen without refetching it.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `updates` | block, one entry per `update(tileId, updateData)` | required | Patches to apply. |

`updateData` is one of: `incomingTileUpdateData()` (uses `incomingData` as-is when it's a map), `inlineTileUpdateData(map)` / `inlineTileUpdateData(vararg pairs)` (a literal map), or `mappedIncomingTileUpdateData(patterns)` (one template per field, each resolved against `incomingData` via the `<|path|>` template engine — see `architecture.md` §5).

**DSL example:**
```kotlin
UpdateTiles(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(tileId = "progress", updateData = mappedIncomingTileUpdateData("progress" to "<|percent|>"))
    }
)
```

**Triggers fired:** `OnSuccess` (every update applied, no data forwarded), `OnFailure` (at least one update failed — no tile carries that id, a `Mapped` template threw, or an `Incoming` update got non-map `incomingData` — fired once at the end, each failure logged).

**Notes:** the merge is per-field — only listed fields change, and `style` is merged (not replaced wholesale) the same way the rest of the tile is. All updates are attempted regardless of earlier failures.

### `WipeTiles`

Removes every child of the grouping tile identified by `groupingTileId`, leaving it empty.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `groupingTileId` | `String` | required | Container tile to empty. |

**DSL example:**
```kotlin
WipeTiles(trigger = EventTriggers.onClick(), groupingTileId = "cart_items")
```

**Triggers fired:** `OnSuccess` (children removed, no data forwarded), `OnFailure` (no grouping tile carries `groupingTileId` — `Throwable` as incoming data, logged).

**Notes:** `incomingData` not consumed.

## Time

### `StartCountdownTimer`

Starts a countdown in a coroutine launched from the running event's context, ticking down from `initial` to `step` in `step`-sized decrements, waiting one step between ticks.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `timerData` | `TimerData` — `milliseconds(initial, step)` or `seconds(initial, step)` | required | Countdown range and unit. |

**DSL example:**
```kotlin
StartCountdownTimer(trigger = EventTriggers.onDisplay(), timerData = seconds(initial = 60, step = 1))
```

**Triggers fired:** `OnCountdownTimerTick` (once per tick, remaining amount as incoming data), `OnTimeFinish` (once, after the last tick, no data). **Neither success nor failure is reported.**

**Notes:** the event returns immediately after launching the countdown — the chain continues while the timer runs in the background. It stops on its own when the countdown ends; to stop it early, launch it from inside `RunCancellableEvents` and cancel that id with `CancelEvents` (see `architecture.md` §4, `CancellableEventsHolder`). `incomingData` not consumed.

### `StartTimeLoop`

Starts an endless loop in a coroutine launched from the running event's context, firing once per period.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `timeData` | `TimeData` — `milliseconds(delay)` or `seconds(delay)` | required | Loop period and unit. |

**DSL example:**
```kotlin
StartTimeLoop(trigger = EventTriggers.onDisplay(), timeData = seconds(delay = 30))
```

**Triggers fired:** `OnTimeLoop` — once per period, indefinitely, no data. **Neither success nor failure is reported.**

**Notes:** the first fire happens **after** the first delay, not immediately. Runs forever by design — the only way to stop it is the same `RunCancellableEvents`/`CancelEvents` pairing as `StartCountdownTimer`. `incomingData` not consumed.
