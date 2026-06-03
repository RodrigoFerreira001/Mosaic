# Events: File Operations

**All triggers use `EventTriggers.xxx()`.**

---

## SaveFileEventSchema

**@SerialName:** `"SaveFile"`

Saves the current `incomingData` as a file in local storage.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.SaveFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    fileName: String,
    overrideIfExists: Boolean = false
)
```

### Child Triggers
`EventTriggers.onSuccess()`, `EventTriggers.onFailure()`

### Example
```kotlin
// Download PDF then save it:
Button("Export Report", events = {
    SendNetworkRequest(
        trigger = EventTriggers.onClick(),
        url = "/api/report/export",
        method = HttpMethod.GET,
        events = {
            SaveFile(
                trigger = EventTriggers.onSuccess(),
                fileName = "report.pdf",
                overrideIfExists = true,
                events = {
                    DisplaySnackbar(
                        trigger = EventTriggers.onSuccess(),
                        message = "Report saved!"
                    )
                    DisplaySnackbar(
                        trigger = EventTriggers.onFailure(),
                        message = "Could not save the file"
                    )
                }
            )
            DisplaySnackbar(
                trigger = EventTriggers.onFailure(),
                message = "Failed to download report"
            )
        }
    )
})
```

---

## GetFileEventSchema

**@SerialName:** `"GetFile"`

Reads a file from local storage and passes its content as `incomingData`.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.GetFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    fileName: String
)
```

### Child Triggers
`EventTriggers.onSuccess()` (file content as `incomingData`), `EventTriggers.onFailure()`

### Example
```kotlin
// Load local config on display:
GetFile(
    fileName = "config.json",
    trigger = EventTriggers.onDisplay(),
    events = {
        ProcessData(
            processWith = "parse-config",
            trigger = EventTriggers.onSuccess(),
            events = {
                UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                    update("settings-panel", mapOf("visible" to true))
                })
            }
        )
        ChangeScreenState(
            state = ChangeScreenStateEventSchema.State.Failure,
            trigger = EventTriggers.onFailure()
        )
    }
)
```

---

## DeleteFileEventSchema

**@SerialName:** `"DeleteFile"`

Deletes a file from local storage. The file name comes from `incomingData`.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.DeleteFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()`, `EventTriggers.onFailure()`

### Example
```kotlin
// Delete cached file:
DeleteFile(
    trigger = EventTriggers.onClick(),
    events = {
        DisplaySnackbar(
            trigger = EventTriggers.onSuccess(),
            message = "Cache cleared"
        )
        DisplaySnackbar(
            trigger = EventTriggers.onFailure(),
            message = "Could not clear cache"
        )
    }
)
```
