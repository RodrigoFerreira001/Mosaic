# Events: Networking

**All triggers use `EventTriggers.xxx()`.**

---

## SendNetworkRequestEventSchema

**@SerialName:** `"SendNetworkRequest"`

Makes an HTTP request. Child events react to different outcomes.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.SendNetworkRequest(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
)
```

`HttpMethod`: `HttpMethod.GET`, `HttpMethod.POST`, `HttpMethod.PUT`, `HttpMethod.DELETE`, `HttpMethod.PATCH`

### Child Triggers

| Trigger | When | incomingData |
|---|---|---|
| `EventTriggers.onStart()` | Request dispatched | null |
| `EventTriggers.onSuccess()` | 2xx response | response body |
| `EventTriggers.onFailure()` | Non-2xx or network error | error info |
| `EventTriggers.onNetworkResponse(httpCode)` | Specific HTTP code | response body |

### How body and headers are resolved

`incomingData` is NOT consumed by this event. To pass data from a previous event as the request body
or headers, use `SetIncomingDataToNetworkParamsHolderBody` / `SetIncomingDataToNetworkParamsHolderHeaders`
before this event in the chain. The holder is always consumed (reset to null) on execution.

**Body priority:**
1. `body` parameter (if non-null)
2. `NetworkParametersHolder.body` (set by `SetIncomingDataToNetworkParamsHolderBody`)
3. `null`

**Headers:** `holder.headers + schema.headers` — schema wins on key collision.

```kotlin
// Form submit: read email+password, send as JSON body via holder
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        reading(tile("email", "email"), fullAccessMode())
        reading(tile("password", "password"), fullAccessMode())
    },
    events = {
        SetIncomingDataToNetworkParamsHolderBody(
            trigger = EventTriggers.onSuccess(),
            events = {
                SendNetworkRequest(
                    trigger = EventTriggers.onSuccess(),
                    url = "/api/auth/login",
                    method = HttpMethod.POST
                    // body comes from holder (the map {"email":"..","password":".."})
                )
            }
        )
    }
)
```

### Full Example (Login Flow)
```kotlin
Button("Sign in", events = {
    GetData(
        trigger = EventTriggers.onClick(),
        readings = {
            reading(tile("email", "email"), fullAccessMode())
            reading(tile("password", "password"), fullAccessMode())
        },
        events = {
            // Disable UI immediately
            UpdateTiles(trigger = EventTriggers.onStart(), updates = {
                update("login-btn", mapOf("loading" to true, "enabled" to false))
                update("email", mapOf("enabled" to false))
                update("password", mapOf("enabled" to false))
            })
            // Validate email before sending
            EvaluateData(
                trigger = EventTriggers.onSuccess(),
                expression = incomingData().containsKey("email") and
                    incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w-]+\\.[\\w.]+\$"),
                events = {
                    // Put incomingData into the holder so SendNetworkRequest can use it as body
                    SetIncomingDataToNetworkParamsHolderBody(
                        trigger = EventTriggers.onSuccess(),
                        events = {
                            SendNetworkRequest(
                                trigger = EventTriggers.onSuccess(),
                                url = "/api/auth/login",
                                method = HttpMethod.POST,
                                events = {
                                    // 2xx → navigate to home:
                                    Navigate(
                                        trigger = EventTriggers.onSuccess(),
                                        destination = "home",
                                        navigatorId = "root",
                                        popUpTo = poppingUpTo("login", inclusive = true)
                                    )
                                    // 401 → wrong credentials:
                                    DisplayDialog(
                                        trigger = EventTriggers.onNetworkResponse(401),
                                        isCancellable = true
                                    ) {
                                        Column(style = { size(width = fillHorizontally()); padding(horizontal = 24, vertical = 24) }) {
                                            SimpleText("Wrong email or password", typography = typographyBodyLarge())
                                            Button("Ok", buttonType = filledButton(), events = {
                                                DismissDialog(trigger = EventTriggers.onClick())
                                            })
                                        }
                                    }
                                    // Any failure → restore UI:
                                    UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                                        update("login-btn", mapOf("loading" to false, "enabled" to true))
                                        update("email", mapOf("enabled" to true))
                                        update("password", mapOf("enabled" to true))
                                    })
                                }
                            )
                        }
                    )
                    // Validation failed → mark email field:
                    UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                        update("email", mapOf("state" to "ERROR", "supportingText" to "Invalid email"))
                        update("login-btn", mapOf("loading" to false, "enabled" to true))
                        update("email", mapOf("enabled" to true))
                        update("password", mapOf("enabled" to true))
                    })
                }
            )
        }
    )
})
```

---

## SetIncomingDataToNetworkParamsHolderBodyEventSchema

**@SerialName:** `"SetIncomingDataToNetworkParamsHolderBody"`

Stores `incomingData` as the request body in the `NetworkParametersHolder`. The holder is consumed
by the next network event (`SendNetworkRequest`, `GetScreen`, `RefreshScreen`, `DownloadFile`) in
the same chain.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderBody(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
)
```

### Child Triggers

| Trigger | When | incomingData |
|---|---|---|
| `EventTriggers.onSuccess()` | Body stored | forwarded unchanged |
| `EventTriggers.onFailure()` | incomingData is null | — |

### Notes
- If `incomingData` is null → `onFailure()` fires, nothing is stored.
- The schema's explicit `body` parameter always takes precedence over the holder. Only use this when `body = null` in the network event.

---

## SetIncomingDataToNetworkParamsHolderHeadersEventSchema

**@SerialName:** `"SetIncomingDataToNetworkParamsHolderHeaders"`

Stores `incomingData` (must be `Map<String, String>`) as request headers in the
`NetworkParametersHolder`. Schema-level headers take precedence on key collision:
`finalHeaders = holder.headers + schema.headers`.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderHeaders(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
)
```

### Child Triggers

| Trigger | When | incomingData |
|---|---|---|
| `EventTriggers.onSuccess()` | Headers stored | forwarded unchanged |
| `EventTriggers.onFailure()` | Cast to `Map<String, String>` failed | — |

### Typical Usage (Auth Header)
```kotlin
GetData(trigger = EventTriggers.onClick(), readings = {
    reading(segmentedDataBase("auth"), singleAccessMode("token"))
    // incomingData = {"Authorization": "Bearer xyz"}
}, events = {
    SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess(), events = {
        SendNetworkRequest(
            trigger = EventTriggers.onSuccess(),
            url = "/api/protected",
            method = HttpMethod.GET
            // request goes out with Authorization header from holder
        )
    })
})
```

---

## DownloadFileEventSchema

**@SerialName:** `"DownloadFile"`

Downloads a file and fires streaming progress triggers throughout the transfer lifecycle.
`incomingData` is NOT consumed directly — use `SetIncomingDataToNetworkParamsHolderBody` or
`SetIncomingDataToNetworkParamsHolderHeaders` before this event to pass body/headers.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.DownloadFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null
)
```

### Child Triggers

| Trigger | When | incomingData |
|---|---|---|
| `EventTriggers.onStart()` | Download begins | null |
| `EventTriggers.onDownloadProgress()` | Per chunk (Content-Length available) | `Int` 0–100 (%) |
| `EventTriggers.onDownloadPartial()` | Per chunk | `ByteArray` of that chunk |
| `EventTriggers.onDownloadFinish()` | Transfer complete | full `ByteArray` |
| `EventTriggers.onDownloadFailure()` | Any failure | `Throwable` |

### Example
```kotlin
Button("Download Report", events = {
    DownloadFile(
        trigger = EventTriggers.onClick(),
        url = "/api/report.pdf",
        method = HttpMethod.GET,
        events = {
            UpdateTiles(trigger = EventTriggers.onStart(), updates = {
                update("download-btn", mapOf("loading" to true, "enabled" to false))
                update("progress-bar", mapOf("visibility" to "VISIBLE", "progress" to 0f))
            })
            UpdateTiles(trigger = EventTriggers.onDownloadProgress(), updates = {
                update("progress-bar", mapOf("progress" to null))  // null passes incomingData (progress %) through
            })
            UpdateTiles(trigger = EventTriggers.onDownloadFinish(), updates = {
                update("download-btn", mapOf("loading" to false, "enabled" to true))
                update("progress-bar", mapOf("visibility" to "GONE"))
            })
            DisplaySnackbar(trigger = EventTriggers.onDownloadFinish(), message = "Download complete!")
            UpdateTiles(trigger = EventTriggers.onDownloadFailure(), updates = {
                update("download-btn", mapOf("loading" to false, "enabled" to true))
                update("progress-bar", mapOf("visibility" to "GONE"))
            })
            DisplaySnackbar(trigger = EventTriggers.onDownloadFailure(), message = "Download failed")
        }
    )
})
```
