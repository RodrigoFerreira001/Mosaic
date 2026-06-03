# Events: Data Operations

**All triggers use `EventTriggers.xxx()`.**

---

## GetDataEventSchema

**@SerialName:** `"GetData"`

Reads from one or more data sources. The result is passed as `incomingData` to child events.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.GetData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    readings: GetDataReadingBuilderScope.() -> Unit
)

class GetDataReadingBuilderScope {
    fun reading(dataSource: DataSourceSchema, accessMode: AccessModeSchema)
}
```

### How multiple readings merge into incomingData

The runner accumulates all readings into a single map. The final `incomingData` type depends on access modes:

| Access mode used | Result type | Behavior |
|---|---|---|
| `fullAccessMode()` | **Map** | The full data map is merged (putAll) into the accumulator. Use this when reading tile values to get a merged map. |
| `batchAccessMode(...)` | **Map** (or List if `unwrapValuesToList=true`) | Each requested key is merged by key name. |
| `singleAccessMode("key")` | scalar (or **List** if multiple single readings) | Stored at `accumulator["key"]`. With only single readings → result is a flat list of values, not a map. |

**Key rule — reading tile values into a merged map:**
Use `fullAccessMode()` with tile data sources. The tile returns a map `{dataKey: value}` which gets merged into the accumulator:

```kotlin
// incomingData = { "email": "user@example.com", "password": "secret" }
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        reading(tile("email-field", "email"), fullAccessMode())
        reading(tile("password-field", "password"), fullAccessMode())
    },
    events = {
        // incomingData is now a Map {"email": "...", "password": "..."}
        // This map is automatically used as the SendNetworkRequest body
        SendNetworkRequest(
            trigger = EventTriggers.onSuccess(),
            url = "/api/auth/login",
            method = HttpMethod.POST
        )
    }
)
```

If you used `singleAccessMode` for two readings instead, `incomingData` would be a **list** `["user@example.com", "secret"]`, not a map — which is rarely what you want.

### DataSource helpers
```kotlin
segmentedDataBase(segmentId = "auth")          // persistent, segmented by key
plainDataBase()                                // persistent flat store
screenPlainData()                              // screen-scoped flat store
screenSegmentedData(segmentId = "form")        // screen-scoped, segmented
screenNavigationData()                         // navigation args from Navigate(data = ...)
tile(tileId = "field-id", dataKey = "value")   // reads a tile's current value by key
```

### AccessMode helpers
```kotlin
fullAccessMode()                               // returns the full data as a map; merges into accumulator
singleAccessMode(dataId = "key")               // returns the single value at "key"
batchAccessMode(
    dataIds = listOf("k1", "k2"),
    allowMissingData = true,                   // if false, fires onFailure when a key is missing
    unwrapValuesToList = false                 // if true, result is a list of values
)
```

### Child Triggers
`EventTriggers.onStart()`, `EventTriggers.onSuccess()` (data as incomingData), `EventTriggers.onFailure()`

### Examples

**Read single stored value:**
```kotlin
GetData(
    trigger = EventTriggers.onDisplay(),
    readings = {
        reading(segmentedDataBase("auth"), singleAccessMode("sessionCookie"))
    },
    events = {
        Navigate(trigger = EventTriggers.onSuccess(), navigatorId = "root", destination = "home",
            popUpTo = poppingUpTo("splash", inclusive = true))
        Navigate(trigger = EventTriggers.onFailure(), navigatorId = "root", destination = "login",
            popUpTo = poppingUpTo("splash", inclusive = true))
    }
)
```

**Read and validate a single tile field:**
```kotlin
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        reading(tile("email-field", "email"), fullAccessMode())
    },
    events = {
        EvaluateData(
            trigger = EventTriggers.onSuccess(),
            expression = incomingData().containsKey("email") and
                incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w-]+\\.[\\w.]+\$"),
            events = {
                SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/...", method = HttpMethod.POST)
                UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                    update("email-field", mapOf("state" to "ERROR", "supportingText" to "Invalid email"))
                })
            }
        )
    }
)
```

---

## EvaluateDataEventSchema

**@SerialName:** `"EvaluateData"`

Evaluates a boolean expression against `incomingData` or a stored data source. Fires `onSuccess()` if true, `onFailure()` if false.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.EvaluateData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    expression: EvaluateDataEventSchema.Expression
)
```

### Expression DSL — Data sources

```kotlin
incomingData()                              // references the current incomingData
dataSourceData(dataSource, accessMode)      // references stored data directly
```

### Logical operators

```kotlin
not(expression)
expression1 and expression2
expression1 or expression2
```

### Operations on `incomingData()` or `dataSourceData()`

**Null checks:**
```kotlin
incomingData().isNull()
incomingData().isNotNull()
```

**Boolean:**
```kotlin
incomingData().isTrue()
incomingData().isFalse()
```

**String:**
```kotlin
incomingData().isBlank()
incomingData().isNotBlank()
incomingData().isEqualsTo("value")
incomingData().equalsIgnoreCase("value")
incomingData().matchesRegex("^pattern\$")
incomingData().containsSubstring("text")
incomingData().startsWith("prefix")
incomingData().endsWith("suffix")
incomingData().isLengthBiggerThan(5)
incomingData().isLengthBiggerThanOrEquals(5)
incomingData().isLengthSmallerThan(100)
incomingData().isLengthSmallerThanOrEquals(100)
incomingData().isLengthEqualsTo(10)
```

**Int / Long / Float / Double:**
```kotlin
incomingData().isBiggerThan(0)           // works for Int, Long, Float, Double
incomingData().isBiggerThanOrEquals(1)
incomingData().isSmallerThan(100)
incomingData().isSmallerThanOrEquals(99)
incomingData().isEqualsTo(42)
incomingData().isIntEven()
incomingData().isIntOdd()
```

**Map operations (when incomingData is a map):**
```kotlin
incomingData().containsKey("email")
incomingData().containsValue(AnySerializable("admin"))
incomingData().isMapEmpty()
incomingData().isMapNotEmpty()
incomingData().isMapSizeBiggerThan(0)
incomingData().isMapSizeEqualsTo(2)
incomingData().valueAtKeyEquals("role", AnySerializable("admin"))
```

**Map — validate a specific key's value via `valueAtKey(key)`:**
```kotlin
// valueAtKey(key) returns a KeyedData with all the same operations
incomingData().valueAtKey("email").isNotBlank()
incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w-]+\\.[\\w.]+\$")
incomingData().valueAtKey("count").isBiggerThan(0)
incomingData().valueAtKey("enabled").isTrue()
incomingData().valueAtKey("name").isNull()
```

**List operations (when incomingData is a list):**
```kotlin
incomingData().listContains(AnySerializable("admin"))
incomingData().inList(listOf(AnySerializable("a"), AnySerializable("b")))
incomingData().isListEmpty()
incomingData().isListNotEmpty()
incomingData().isListSizeBiggerThan(0)
incomingData().listContainsAll(listOf(AnySerializable("x"), AnySerializable("y")))
incomingData().listContainsAny(listOf(AnySerializable("x"), AnySerializable("y")))
```

**DateTime operations (when incomingData is LocalDateTime):**
```kotlin
incomingData().isBefore(LocalDateTime(...))
incomingData().isAfter(LocalDateTime(...))
incomingData().isEqualTo(LocalDateTime(...))
incomingData().isWeekend()
incomingData().isWeekday()
```

### Child Triggers
`EventTriggers.onSuccess()` (expression true), `EventTriggers.onFailure()` (expression false)

---

## UpdateDataEventSchema

**@SerialName:** `"UpdateData"`

Writes/updates values in a data source.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.UpdateData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateDataUpdateBuilderScope.() -> Unit
)

class UpdateDataUpdateBuilderScope {
    fun addUpdate(dataSource: DataSourceSchema, updateData: Update.UpdateDate)
}
```

### UpdateDate helpers
```kotlin
inlineUpdateData("key" to value, "key2" to value2)  // explicit key-value pairs (vararg)
inlineUpdateData(mapOf("key" to value))              // from a map
incomingUpdateData()                                 // use incomingData as the update
```

### Child Triggers
`EventTriggers.onDataUpdated()`

### Examples
```kotlin
// Initialize screen state on display:
UpdateData(
    trigger = EventTriggers.onDisplay(),
    updates = {
        addUpdate(
            dataSource = screenPlainData(),
            updateData = inlineUpdateData("showPassword" to false, "step" to 1)
        )
    }
)

// Store response from server:
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        addUpdate(
            dataSource = screenPlainData(),
            updateData = incomingUpdateData()   // stores the full response
        )
    }
)
```

---

## SendDataEventSchema

**@SerialName:** `"SendData"`

Sends data to another screen via `DataMailer` (cross-screen communication).

### Builder
```kotlin
SendData(
    trigger = EventTriggers.onClick(),
    dataKey = "selected-item",
    data = AnySerializable(mapOf("id" to "123", "name" to "Product A"))
    // data = null → uses incomingData
)
```

### Child Triggers
`EventTriggers.onSuccess()` — data sent successfully
`EventTriggers.onFailure()` — both `data` and `incomingData` are null; nothing to send

---

## CheckForReceivedDataEventSchema

**@SerialName:** `"CheckForReceivedData"`

Polls for data sent by another screen.

### Builder
```kotlin
CheckForReceivedData(
    trigger = EventTriggers.onDisplay(),
    dataKey = "selected-item",
    events = {
        UpdateTiles(
            trigger = EventTriggers.onDataReceived(),
            updates = {
                update("label", mapOf("text" to null))  // null passes incomingData value through
            }
        )
    }
)
```

### Child Triggers
`EventTriggers.onDataReceived()` — data found in `DataMailer`; received data as incomingData
`EventTriggers.onSuccess()` — same as `onDataReceived` (fires together)
`EventTriggers.onFailure()` — no data found in `DataMailer` for the given `dataKey`

---

## RemoveDataEventSchema

**@SerialName:** `"RemoveData"`

```kotlin
RemoveData(
    trigger = EventTriggers.onClick(),
    deletions = {
        addDeletion(dataSource = screenPlainData(), accessMode = fullAccessMode())
    }
)
```

### Child Triggers
`EventTriggers.onDataRemoved()`

---

## ProcessDataEventSchema

**@SerialName:** `"ProcessData"`

Runs a registered `DataProcessor` client-side on current `incomingData`.

### Child Triggers
`EventTriggers.onSuccess()` — processor ran and returned a result; result as incomingData
`EventTriggers.onFailure()` — incomingData was null, processor ID not found, or processor threw

```kotlin
ProcessData(
    trigger = EventTriggers.onSuccess(),
    processWith = "MY_PROCESSOR_ID",
    events = {
        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = { ... })
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Processing failed")
    }
)
```

---

## TransformDataEventSchema

**@SerialName:** `"TransformData"`

Transforms `incomingData` via a template map. Use `{{key}}` syntax to reference values.

### Child Triggers
`EventTriggers.onSuccess()` — transformation applied; result as incomingData
`EventTriggers.onFailure()` — template resolution threw an exception

```kotlin
TransformData(
    trigger = EventTriggers.onSuccess(),
    template = AnySerializable(mapOf("output" to "{{input.name}}")),
    events = {
        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = { ... })
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Transform failed")
    }
)
```
