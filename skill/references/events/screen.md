# Events: Screen Lifecycle

**All triggers use `EventTriggers.xxx()`.**

---

## GetScreenEventSchema

**@SerialName:** `"GetScreen"`

Fetches the current screen's content from the server. Used automatically as the default `initialEvents` on each `entry` in a `Graph`.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.GetScreen(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()` (screen response as `incomingData`), `EventTriggers.onFailure()`

### Notes
The `GraphEntryBuilder` adds `GetScreen(trigger = onDisplay()) + ChangeScreenState(Success)` as the default `initialEvents` unless you override. Override when you need custom loading behavior (e.g., auth check before fetching):

```kotlin
entry(
    screenId = "home",
    initialEvents = {
        // Custom: check session before fetching screen
        GetData(
            trigger = EventTriggers.onDisplay(),
            readings = { reading(segmentedDataBase("auth"), singleAccessMode("token")) },
            events = {
                GetScreen(
                    trigger = EventTriggers.onSuccess(),
                    events = {
                        ChangeScreenState(
                            state = ChangeScreenStateEventSchema.State.Success(),
                            trigger = EventTriggers.onSuccess()
                        )
                        ChangeScreenState(
                            state = ChangeScreenStateEventSchema.State.Failure,
                            trigger = EventTriggers.onFailure()
                        )
                    }
                )
                Navigate(
                    trigger = EventTriggers.onFailure(),
                    destination = "login",
                    navigatorId = "root",
                    popUpTo = poppingUpTo("home", inclusive = true)
                )
            }
        )
    }
)
```

---

## ChangeScreenStateEventSchema

**@SerialName:** `"ChangeScreenState"`

Switches the screen between its three UI states.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ChangeScreenState(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    state: ChangeScreenStateEventSchema.State
)
```

`State`:
- `Initial` — shows initial skeleton/loading tiles + events
- `Failure` — shows failure tiles + events
- `Success(data: ScreenData?)` — shows content. If `data` is null, uses the server-fetched screen response.

`ScreenData`:
- `tiles: List<TileSchema>`
- `navigationDrawerTiles: List<TileSchema>?`
- `events: List<EventSchema>?`

### Child Triggers
`EventTriggers.onSuccess()` — screen state changed successfully
`EventTriggers.onFailure()` — invalid state or missing screen model

### Examples
```kotlin
// After GetScreen fails:
ChangeScreenState(
    state = ChangeScreenStateEventSchema.State.Failure,
    trigger = EventTriggers.onFailure()
)

// Show content after server fetch succeeds:
ChangeScreenState(
    state = ChangeScreenStateEventSchema.State.Success(),
    trigger = EventTriggers.onSuccess()
)
```

---

## RefreshScreenEventSchema

**@SerialName:** `"RefreshScreen"`

Re-fetches the current screen from the server and replaces all tiles.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.RefreshScreen(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()`, `EventTriggers.onFailure()`

### Examples
```kotlin
// Retry button on failure tiles:
Button("Retry", events = {
    RefreshScreen(trigger = EventTriggers.onClick())
})

// Pull-to-refresh:
RefreshScreen(
    trigger = EventTriggers.onPull(),
    events = {
        StopRefreshing(tileId = "pull-refresh", trigger = EventTriggers.onSuccess())
        StopRefreshing(tileId = "pull-refresh", trigger = EventTriggers.onFailure())
    }
)
```
