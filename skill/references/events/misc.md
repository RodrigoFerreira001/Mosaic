# Events: Miscellaneous

**All triggers use `EventTriggers.xxx()`.**

---

## RequestPermissionEventSchema

**@SerialName:** `"RequestPermission"`

Requests one or more OS-level permissions.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.RequestPermission(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    permissions: List<Permissions>
)
```

`Permissions`: `CAMERA`, `GALLERY`, `STORAGE`, `MICROPHONE`, `LOCATION`, `NOTIFICATION`, `CONTACTS`

### Child Triggers
`EventTriggers.onPermissionsAcquired()`, `EventTriggers.onPermissionsDenied()`

### Example
```kotlin
IconButton(icon = icon("camera_alt"), events = {
    RequestPermission(
        permissions = listOf(Permissions.CAMERA),
        trigger = EventTriggers.onClick(),
        events = {
            Navigate(
                destination = "camera",
                navigatorId = "main",
                trigger = EventTriggers.onPermissionsAcquired()
            )
            DisplaySnackbar(
                message = "Camera permission is required",
                trigger = EventTriggers.onPermissionsDenied()
            )
        }
    )
})
```

---

## CheckIfHasInternetConnectionEventSchema

**@SerialName:** `"CheckIfHasInternetConnection"`

Checks current connectivity. Fires `onSuccess()` if connected, `onFailure()` if not.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.CheckIfHasInternetConnection(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Example
```kotlin
Button("Sync", events = {
    CheckIfHasInternetConnection(
        trigger = EventTriggers.onClick(),
        events = {
            SendNetworkRequest(
                url = "/api/sync",
                method = HttpMethod.POST,
                trigger = EventTriggers.onSuccess()
            )
            DisplaySnackbar(
                message = "No internet connection",
                trigger = EventTriggers.onFailure()
            )
        }
    )
})
```

---

## ToggleMenuEventSchema

**@SerialName:** `"ToggleMenu"`

Opens or closes a `MenuTileSchema` by ID.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ToggleMenu(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    menuId: String
)
```

### Child Triggers
`EventTriggers.onSuccess()` — menu toggle dispatched successfully
`EventTriggers.onFailure()` — menu tile not found (`TileNotFoundException`)

> `EventTriggers.onMenuItemClick(itemId)` fires from the `MenuTile` renderer itself (not from this runner) when a menu item is tapped.

### Example
```kotlin
IconButton(icon = icon("more_vert"), events = {
    ToggleMenu(
        menuId = "options-menu",
        trigger = EventTriggers.onClick()
    )
})
```

---

## StartCountdownTimerEventSchema

**@SerialName:** `"StartCountdownTimer"`

Starts a countdown timer. Fires tick events periodically and a finish event when done.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.StartCountdownTimer(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    setupTimeInSeconds: Long
)
```

### Child Triggers
`EventTriggers.onCountdownTimerTick()` (remaining seconds as `incomingData`), `EventTriggers.onCountdownTimerFinish()`

### Example
```kotlin
// OTP resend timer — 60 second countdown:
StartCountdownTimer(
    setupTimeInSeconds = 60L,
    trigger = EventTriggers.onDisplay(),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onCountdownTimerTick(),
            updates = {
                // incomingData carries remaining seconds — use null to pass it through:
                update("timer-label", mapOf("text" to null))
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onCountdownTimerFinish(),
            updates = {
                update("resend-btn", mapOf("enabled" to true, "text" to "Resend"))
            }
        )
    }
)
```

---

## StopRefreshingEventSchema

**@SerialName:** `"StopRefreshing"`

Stops the loading animation of a `PullToRefresh` tile.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.StopRefreshing(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String
)
```

### Child Triggers
`EventTriggers.onSuccess()` — stop-refresh broadcast sent successfully
`EventTriggers.onFailure()` — PullToRefresh tile not found (`TileNotFoundException`)

### Example
```kotlin
RefreshScreen(
    trigger = EventTriggers.onPull(),
    events = {
        StopRefreshing(tileId = "pull-refresh", trigger = EventTriggers.onSuccess())
        StopRefreshing(tileId = "pull-refresh", trigger = EventTriggers.onFailure())
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Failed to refresh")
    }
)
```

---

## TriggerEventEventSchema

**@SerialName:** `"TriggerEvent"`
**Import:** `dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent`

Manually triggers another event by its ID. Useful for invoking shared/reusable event logic defined in `initialEvents` with `EventTriggers.inline()`.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.TriggerEvent(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    eventId: String
)
```

### Child Triggers
`EventTriggers.onSuccess()` — target event found and executed
`EventTriggers.onFailure()` — event ID not found or execution threw an exception

### Pattern: shared event in `initialEvents`

Define the shared logic once in `initialEvents` using `EventTriggers.inline()` (it won't auto-fire). Then call it from any tile event using `TriggerEvent`.

```kotlin
entry(
    screenId = "login",
    initialTiles = {
        TextField(
            id = "password",
            events = {
                // fires when user presses "Done" on keyboard — needs imeAction = keyboardImeActionDone()
                TriggerEvent(eventId = "loginEvent", trigger = EventTriggers.onKeyboardDone())
            }
        )
        Button(id = "loginButton", text = "Entrar", events = {
            TriggerEvent(eventId = "loginEvent", trigger = EventTriggers.onClick())
        })
    },
    initialEvents = {
        // Shared logic — inline() means it only fires when TriggerEvent calls it
        GetData(
            id = "loginEvent",
            trigger = EventTriggers.inline(),
            readings = { /* ... */ },
            events = { /* full validation + network chain */ }
        )
    }
)
```

---

## UpdateEventsEventSchema

**@SerialName:** `"UpdateEvents"`

Patches fields of one or more events at runtime (similar to `UpdateTiles` but for events).

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.UpdateEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateEventsBuilderScope.() -> Unit
)
```

### Child Triggers
`EventTriggers.onSuccess()` — all event updates applied without errors
`EventTriggers.onFailure()` — at least one event ID was not found (`EventNotFoundException`)

### Example
```kotlin
// Dynamically change where a navigate event points:
UpdateEvents(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update("nav-event", mapOf("destination" to "profile"))
    }
)
```
