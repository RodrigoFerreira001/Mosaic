# Mosaic — Event Chaining: Mechanics & Patterns

Event chaining is the core mechanism for defining reactive, multi-step behavior in Mosaic. Understanding it deeply unlocks the full expressive power of the framework.

---

## Anatomy of an Event

Every event builder has the same structural signature:

```kotlin
SomeEvent(
    id = "optional-id",
    trigger = EventTriggers.xxx(),    // WHEN this event fires
    events = {                        // WHAT fires AFTER this event completes
        ChildEvent(trigger = EventTriggers.onSuccess(), ...)
        AnotherChild(trigger = EventTriggers.onFailure(), ...)
    }
)
```

- **`trigger`** — the condition that causes *this* event to execute
- **`events = { }`** — child events that *may* fire after this one, each gated by their own trigger

---

## How the Chain Executes

1. A user interaction fires (e.g., button click) → `EventTriggers.onClick()` is matched
2. The matching event runs its logic (e.g., sends an HTTP request)
3. When the event concludes, it calls `onTrigger(trigger, data)` with a result trigger (e.g., `EventTriggers.onSuccess()`)
4. The runtime scans the event's `events = { }` children for matching triggers
5. Each matching child runs in sequence, potentially chaining further

The `incomingData` value is passed along: `onSuccess` receives the response body, `onFailure` receives the error, `onStart` receives nothing, etc.

```
onClick() fires
  ↓
SendNetworkRequest runs → dispatches onStart(null)
  ↓ children scanned for onStart()
    UpdateTiles(trigger = onStart()) runs → sets loading = true
  ↓ request completes → dispatches onSuccess(responseBody)
    Navigate(trigger = onSuccess()) runs with responseBody as incomingData
  ↓ OR dispatches onFailure(error)
    DisplaySnackbar(trigger = onFailure()) runs
```

---

## The `events = { }` Parameter

**Every event builder** accepts an `events` lambda — not just "async" ones. This is how you sequence reactions:

```kotlin
// After tiles are added, scroll to bottom:
AddTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "list",
    tiles = { /* new items */ },
    events = {
        ScrollColumn(
            trigger = EventTriggers.onTilesAdded(),
            tileId = "list",
            where = scrollToBottom(),
            smoothly = true
        )
    }
)
```

---

## incomingData Flow

`incomingData` is the data passed from the parent event's trigger dispatch. It flows through the entire chain:

```kotlin
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        reading(dataSource = tile(tileId = "email-field", dataKey = "value"), accessMode = fullAccessMode())
    },
    events = {
        // incomingData here = { "value": "user@example.com" }
        // To send incomingData as the body, use SetIncomingDataToNetworkParamsHolderBody first:
        SetIncomingDataToNetworkParamsHolderBody(
            trigger = EventTriggers.onSuccess(),
            events = {
                SendNetworkRequest(
                    trigger = EventTriggers.onSuccess(),
                    url = "/api/register",
                    method = HttpMethod.POST,
                    // body comes from the holder
                )
            }
        )
    }
)
```

Key rule: **`incomingData` from a parent event's trigger becomes available to all children in that `events` block.**

For `SendNetworkRequest`: `onSuccess()` receives the raw response body. You can pass it directly to `UpdateTiles` to patch tile fields with server values.

---

## Multi-Level Chaining

Chains can be arbitrarily deep:

```kotlin
Button("Save", events = {
    GetData(
        trigger = EventTriggers.onClick(),
        readings = { reading(tile("form", "data"), fullAccessMode()) },
        events = {
            EvaluateData(
                trigger = EventTriggers.onSuccess(),
                expression = incomingData().isMapNotEmpty(),
                events = {
                    SendNetworkRequest(
                        trigger = EventTriggers.onSuccess(),
                        url = "/api/save",
                        method = HttpMethod.POST,
                        events = {
                            // Level 4: react to HTTP result
                            UpdateTiles(
                                trigger = EventTriggers.onStart(),
                                updates = { update("save-btn", mapOf("loading" to true, "enabled" to false)) }
                            )
                            Navigate(
                                trigger = EventTriggers.onSuccess(),
                                destination = "confirmation",
                                navigatorId = "main"
                            )
                            UpdateTiles(
                                trigger = EventTriggers.onFailure(),
                                updates = { update("save-btn", mapOf("loading" to false, "enabled" to true)) }
                            )
                            DisplaySnackbar(
                                trigger = EventTriggers.onFailure(),
                                message = "Save failed"
                            )
                        }
                    )
                    // Validation failure: EvaluateData.onFailure()
                    UpdateTiles(
                        trigger = EventTriggers.onFailure(),
                        updates = { update("error-text", mapOf("text" to "Form is empty")) }
                    )
                }
            )
        }
    )
})
```

---

## Multiple Children with Different Triggers

An event can have multiple children gated on different outcomes simultaneously:

```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/auth",
    method = HttpMethod.POST,
    events = {
        // Fires before request completes:
        UpdateTiles(trigger = EventTriggers.onStart(), updates = {
            update("btn", mapOf("loading" to true))
        })

        // Fires on any 2xx:
        Navigate(trigger = EventTriggers.onSuccess(), destination = "home", navigatorId = "root")

        // Fires specifically on 401:
        DisplayDialog(trigger = EventTriggers.onNetworkResponse(401), tiles = {
            SimpleText("Session expired")
        })

        // Fires specifically on 403:
        DisplaySnackbar(trigger = EventTriggers.onNetworkResponse(403), message = "Access denied")

        // Fires on network error:
        UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
            update("btn", mapOf("loading" to false))
        })
        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Network error")
    }
)
```

---

## Events Attached to Tiles vs. Screen-Level Events

Events can be attached at two levels:

### Tile-level (`events = { }` on a tile builder)
```kotlin
Button("Go", events = {
    Navigate(trigger = EventTriggers.onClick(), destination = "home", navigatorId = "main")
})
```
These are the most common. They fire when the user interacts with that specific tile.

### Screen-level (in `entry(initialEvents = { })` or `entry(...)` event scope)
```kotlin
entry(
    screenId = "home",
    initialTiles = { /* tiles */ },
    initialEvents = {
        // These fire when the screen first loads
        GetData(
            trigger = EventTriggers.onDisplay(),
            readings = { reading(segmentedDataBase("auth"), singleAccessMode("token")) },
            events = { /* handle auth check */ }
        )
    }
)
```
Screen-level events are ideal for:
- Loading data on screen open
- Auth checks
- Initialization logic

---

## The `inline()` Trigger

`EventTriggers.inline()` fires immediately when its parent scope is entered. Use it when you want to execute an event as part of `initialEvents` without waiting for any user interaction:

```kotlin
initialEvents = {
    UpdateData(
        trigger = EventTriggers.onDisplay(),    // fires when screen appears
        updates = { addUpdate(screenPlainData(), inlineUpdateData("init" to true)) }
    )
    // OR — fire multiple things at once in initialEvents:
    GetData(
        trigger = EventTriggers.inline(),       // fires immediately
        readings = { ... }
    )
}
```

---

## Common Patterns

### Pattern 1: Form Validation → Submit
```kotlin
Button("Submit", events = {
    GetData(
        trigger = EventTriggers.onClick(),
        readings = {
            reading(tile("email", "value"), fullAccessMode())
        },
        events = {
            EvaluateData(
                trigger = EventTriggers.onSuccess(),
                expression = incomingData().containsKey("value") and
                             incomingData().valueAtKey("value").matchesRegex("^[\\w.-]+@[\\w.-]+\\.\\w+$"),
                events = {
                    SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/login", method = HttpMethod.POST)
                    UpdateTiles(
                        trigger = EventTriggers.onFailure(),
                        updates = { update("email", mapOf("state" to "ERROR", "supportingText" to "Invalid email")) }
                    )
                }
            )
        }
    )
})
```

### Pattern 2: Loading State
```kotlin
Button("Load", events = {
    SendNetworkRequest(
        trigger = EventTriggers.onClick(),
        url = "/api/items",
        method = HttpMethod.GET,
        events = {
            UpdateTiles(trigger = EventTriggers.onStart(), updates = {
                update("load-btn", mapOf("loading" to true, "enabled" to false))
            })
            AddTiles(trigger = EventTriggers.onSuccess(), groupingTileId = "list", tiles = { /* items */ }, events = {
                UpdateTiles(trigger = EventTriggers.onTilesAdded(), updates = {
                    update("load-btn", mapOf("loading" to false, "enabled" to true))
                })
            })
            UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                update("load-btn", mapOf("loading" to false, "enabled" to true))
            })
            DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Failed to load")
        }
    )
})
```

### Pattern 3: Toggle Show/Hide Password
```kotlin
TextField(
    id = "password",
    visualTransformation = keyboardVisualTransformationPassword(),
    trailingIcon = icon("visibility"),
    events = {
        GetData(
            trigger = EventTriggers.onTrailingIconClick(),
            readings = {
                reading(screenPlainData(), singleAccessMode("showPassword"))
            },
            events = {
                EvaluateData(
                    trigger = EventTriggers.onSuccess(),
                    expression = incomingData().isFalse(),     // currently hidden → show
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("password", mapOf(
                                "visualTransformation" to keyboardVisualTransformationNone(),
                                "trailingIcon" to icon("visibility_off")
                            ))
                        })
                        UpdateData(trigger = EventTriggers.onSuccess(), updates = {
                            addUpdate(screenPlainData(), inlineUpdateData("showPassword" to true))
                        })
                        UpdateTiles(trigger = EventTriggers.onFailure(), updates = {   // was showing → hide
                            update("password", mapOf(
                                "visualTransformation" to keyboardVisualTransformationPassword(),
                                "trailingIcon" to icon("visibility")
                            ))
                        })
                        UpdateData(trigger = EventTriggers.onFailure(), updates = {
                            addUpdate(screenPlainData(), inlineUpdateData("showPassword" to false))
                        })
                    }
                )
            }
        )
    }
)
```

### Pattern 4: Pagination (Infinite Scroll)
```kotlin
LazyColumn(id = "items-list", scrollThreshold = 3, events = {
    SendNetworkRequest(
        trigger = EventTriggers.onScrollThresholdReached(),
        url = "/api/items?page=...",  // page tracked via screenPlainData
        method = HttpMethod.GET,
        events = {
            AddTiles(trigger = EventTriggers.onSuccess(), groupingTileId = "items-list", position = insertAtEnd(), tiles = {
                // new items
            })
        }
    )
}) {
    // initial items
}
```

### Pattern 5: Cross-Screen Data
Screen A sends data:
```kotlin
Button("Select", events = {
    SendData(
        trigger = EventTriggers.onClick(),
        dataKey = "selected-item",
        data = AnySerializable(mapOf("id" to "123", "name" to "Product A"))
    )
    NavigateUp(trigger = EventTriggers.onDataSent(), navigatorId = "main")
})
```

Screen B receives it:
```kotlin
initialEvents = {
    CheckForReceivedData(
        trigger = EventTriggers.onDisplay(),
        dataKey = "selected-item",
        events = {
            UpdateTiles(
                trigger = EventTriggers.onDataReceived(),
                updates = {
                    update("selected-label", mapOf("text" to "Selected item"))
                }
            )
        }
    )
}
```
