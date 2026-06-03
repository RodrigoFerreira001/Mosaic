# Events: Navigation

**All triggers use `EventTriggers.xxx()`.**

---

## NavigateEventSchema

**@SerialName:** `"Navigate"`

Pushes a new destination onto a navigator's back stack.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.Navigate(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    destination: String,
    navigatorId: String,
    popUpTo: PopUpToSchema? = null,
    data: Map<String, AnySerializable>? = null
)
```

`PopUpTo` helper: `poppingUpTo(destination = "login", inclusive = true)`

### Child Triggers
`EventTriggers.onSuccess()` — navigation dispatched to the navigator
`EventTriggers.onFailure()` — navigator not registered under `navigatorId`

### Examples

```kotlin
// Simple navigation:
Navigate(
    destination = "detail",
    navigatorId = "main",
    trigger = EventTriggers.onClick(),
    data = mapOf("itemId" to AnySerializable("123"))
)

// Clear back stack before navigating (login → home):
Navigate(
    destination = "home",
    navigatorId = "main",
    trigger = EventTriggers.onSuccess(),
    popUpTo = poppingUpTo(destination = "login", inclusive = true)
)

// Navigate after permission granted:
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
            trigger = EventTriggers.onPermissionsDenied(),
            message = "Camera permission is required"
        )
    }
)
```

### Navigator IDs
- `"root"` or the id defined in `MosaicApplication` — the root app navigator
- Any `navigatorId` defined in `NestedNavigationGraphTileSchema` or `AdaptiveNavigationTileSchema`

### Notes
- `incomingData` (as a map) is merged with `data` before passing to the destination screen
- Access navigation data on the destination via `screenNavigationData()` in a `GetData` event

---

## NavigateUpEventSchema

**@SerialName:** `"NavigateUp"`

Pops the current screen from the back stack.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.NavigateUp(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    navigatorId: String
)
```

### Child Triggers
`EventTriggers.onSuccess()` — pop dispatched to the navigator
`EventTriggers.onFailure()` — navigator not registered under `navigatorId`

### Example
```kotlin
// Back button:
IconButton(icon = icon("arrow_back"), events = {
    NavigateUp(
        navigatorId = "main",
        trigger = EventTriggers.onClick()
    )
})
```
