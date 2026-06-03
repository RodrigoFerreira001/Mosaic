# Events: Overlays (Dialog, Bottom Sheet, Snackbar, Navigation Drawer)

**All triggers use `EventTriggers.xxx()`.**

---

## Dialog Events

### DisplayDialogEventSchema
**@SerialName:** `"DisplayDialog"`

```kotlin
fun EventSchemaBuilderScope.DisplayDialog(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    isCancellable: Boolean = true,
    usePlatformDefaultWidth: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
)
```

The `tiles` lambda defines the dialog content. No separate `@SerialName` for tiles — they're embedded.

### Example
```kotlin
Button("Delete", buttonType = textButton(), events = {
    DisplayDialog(
        trigger = EventTriggers.onClick(),
        isCancellable = true,
        usePlatformDefaultWidth = false
    ) {
        Column(style = { size(width = fillHorizontally()); padding(24) }) {
            SimpleText("Delete item?", typography = typographyTitleLarge(), color = color(themeColorOnSurface()),
                style = { size(width = fillHorizontally()); margin(bottom = 8) })
            SimpleText("This cannot be undone.", typography = typographyBodyMedium(), color = color(themeColorOnSurface()),
                style = { size(width = fillHorizontally()); margin(bottom = 24) })
            Row(arrangement = arrangeHorizontallyToEnd()) {
                Button("Cancel", buttonType = textButton(), events = {
                    DismissDialog(trigger = EventTriggers.onClick())
                })
                Button("Delete", buttonType = filledButton(), events = {
                    DismissDialog(trigger = EventTriggers.onClick())
                    SendNetworkRequest(
                        trigger = EventTriggers.onDialogDismissed(),
                        url = "/api/items/42",
                        method = HttpMethod.DELETE
                    )
                })
            }
        }
    }
})
```

### DismissDialogEventSchema
**@SerialName:** `"DismissDialog"`

```kotlin
DismissDialog(trigger = EventTriggers.onClick())
```

Child triggers: `EventTriggers.onDialogDismissed()`

---

## Bottom Sheet Events

### DisplayBottomSheetEventSchema
**@SerialName:** `"DisplayBottomSheet"`

```kotlin
fun EventSchemaBuilderScope.DisplayBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    isCancellable: Boolean = true,
    fill: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
)
```

### Example — Recover Password
```kotlin
Button("Forgot password?", buttonType = textButton(), events = {
    DisplayBottomSheet(
        trigger = EventTriggers.onClick(),
        isCancellable = true,
        fill = false
    ) {
        Column(
            style = {
                size(width = fillHorizontally())
                margin(horizontal = 24, bottom = 24)
            }
        ) {
            SimpleText("Forgot your password?", typography = typographyTitleLarge(),
                color = color(themeColorOnSurface()),
                style = { size(width = fillHorizontally()); margin(bottom = 8) })
            SimpleText("Enter your email to receive a reset link.",
                typography = typographyBodyLarge(), color = color(themeColorOnSurface()),
                style = { size(width = fillHorizontally()); margin(bottom = 24) })
            TextField(
                id = "recoverEmail",
                placeholder = "Email",
                maxLines = 1,
                style = { size(width = fillHorizontally()); margin(bottom = 24) },
                events = {
                    // Reset validation state on typing:
                    UpdateTiles(trigger = EventTriggers.onTextChanged(), updates = {
                        update("recoverEmail", mapOf("state" to "NORMAL", "supportingText" to null, "trailingIcon" to null))
                    })
                }
            )
            Button(
                id = "sendBtn",
                text = "Send",
                buttonType = filledButton(),
                style = { size(width = fillHorizontally(), height = fixedVertically(56)); margin(bottom = 8) },
                events = {
                    GetData(
                        trigger = EventTriggers.onClick(),
                        readings = { reading(tile("recoverEmail", "email"), fullAccessMode()) },
                        events = {
                            EvaluateData(
                                trigger = EventTriggers.onSuccess(),
                                expression = incomingData().containsKey("email") and
                                    incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w-]+\\.[\\w.]+\$"),
                                events = {
                                    SendNetworkRequest(
                                        trigger = EventTriggers.onSuccess(),
                                        url = "/api/auth/recover",
                                        method = HttpMethod.POST,
                                        events = {
                                            UpdateTiles(trigger = EventTriggers.onStart(), updates = {
                                                update("sendBtn", mapOf("loading" to true, "enabled" to false))
                                            })
                                            DismissBottomSheet(trigger = EventTriggers.onSuccess())
                                            DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Error sending email, try again later")
                                            UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                                                update("sendBtn", mapOf("loading" to false, "enabled" to true))
                                            })
                                        }
                                    )
                                    // Validation failure:
                                    UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                                        update("recoverEmail", mapOf("state" to "ERROR", "supportingText" to "Invalid email", "trailingIcon" to icon("error")))
                                    })
                                }
                            )
                        }
                    )
                }
            )
            Button("Cancel", buttonType = textButton(),
                style = { size(width = fillHorizontally(), height = fixedVertically(56)) },
                events = { DismissBottomSheet(trigger = EventTriggers.onClick()) }
            )
        }
    }
})
```

### DismissBottomSheetEventSchema
**@SerialName:** `"DismissBottomSheet"`

```kotlin
DismissBottomSheet(trigger = EventTriggers.onClick())
```

Child triggers: `EventTriggers.onBottomSheetDismissed()`

---

## Snackbar Events

### DisplaySnackbarEventSchema
**@SerialName:** `"DisplaySnackbar"`

```kotlin
fun EventSchemaBuilderScope.DisplaySnackbar(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    message: String,
    duration: SnackbarDurationSchema = SnackbarDurationSchema.Short,
    actionLabel: String? = null
)
```

`SnackbarDurationSchema`: `Short`, `Long`, `Indefinite`

### Example with Undo
```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/items/42",
    method = HttpMethod.DELETE,
    events = {
        DisplaySnackbar(
            trigger = EventTriggers.onSuccess(),
            message = "Item deleted",
            duration = SnackbarDurationSchema.Long,
            actionLabel = "Undo",
            events = {
                SendNetworkRequest(
                    trigger = EventTriggers.onSnackbarAction(),
                    url = "/api/items/42/restore",
                    method = HttpMethod.POST
                )
                DismissSnackbar(trigger = EventTriggers.onSnackbarDismissed())
            }
        )
    }
)
```

Child triggers: `EventTriggers.onSnackbarAction()`, `EventTriggers.onSnackbarDismissed()`

### DismissSnackbarEventSchema
```kotlin
DismissSnackbar(trigger = EventTriggers.onClick())
```

---

## Navigation Drawer Events

The drawer content is defined in `Screen(navigationDrawerTiles = { ... })`.

```kotlin
DisplayNavigationDrawer(trigger = EventTriggers.onClick())
DismissNavigationDrawer(trigger = EventTriggers.onClick())
```

Child triggers: `EventTriggers.onNavigationDrawerDismissed()`

### Example (hamburger menu)
```kotlin
IconButton(icon = icon("menu"), events = {
    DisplayNavigationDrawer(trigger = EventTriggers.onClick())
})
```
