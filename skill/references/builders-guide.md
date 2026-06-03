# Mosaic — Server Builder Guide

The server DSL lives in `mosaic-server/.../builder/`. Use it to compose entire screens and navigation graphs.

---

## Entry Point: Graph

```kotlin
// Returns GraphResponse — serialize and send as HTTP response
Graph(
    startEntryId = "splash",
    entries = {                         // named parameter, NOT trailing lambda
        SplashScreenEntry()
        LoginScreenEntry()
        HomeScreenEntry()
    }
)
```

Each entry is typically an extension function on `GraphEntryBuilderScope`, defined in its own file:

```kotlin
fun GraphEntryBuilderScope.HomeScreenEntry() {
    entry(
        screenId = "home",
        initialTiles = {
            // tiles shown while GetScreen loads (or if no GetScreen)
            Column(...) { ... }
        },
        initialEvents = {
            // Default if omitted: GetScreen + ChangeScreenState(Success)
            // Override to customize loading behavior
        },
        failureTiles = {
            Column { SimpleText("Something went wrong") }
        },
        failureEvents = {
            Button("Retry", events = {
                RefreshScreen(trigger = EventTriggers.onClick())
            })
        },
        transition = slideHorizontal(),
        popTransition = slideHorizontal()
    )
}
```

---

## StyleSchemaBuilder — Complete API

Every tile accepts `style: StyleSchemaBuilderScope.() -> Unit`.

### size() — always use named parameters
```kotlin
style = {
    // Width options:
    size(width = fillHorizontally())              // fill parent width
    size(width = fillHorizontally(max = 400))     // fill with max constraint
    size(width = wrapHorizontally())              // wrap content
    size(width = fixedHorizontally(200))          // fixed 200dp
    size(width = weightHorizontally(1f))          // flex weight in Row
    size(width = spanHorizontally(2))             // grid span

    // Height options:
    size(height = fillVertically())
    size(height = wrapVertically())               // DEFAULT
    size(height = fixedVertically(56))
    size(height = weightVertically(1f))
    size(height = fillRowHeight())                // fill the Row's height (fraction: Float = 1f)
    size(height = fillRowHeight(0.5f))            // 50% of row height

    // Combined:
    size(width = fillHorizontally(), height = fillVertically())
    size(width = fillHorizontally(max = 400), height = fixedVertically(56))
}
```

### margin() and padding()

**There is no single-argument overload.** Use the named parameter forms:

```kotlin
style = {
    // All sides equal:
    padding(horizontal = 16, vertical = 16)     // equivalent to "all = 16"
    margin(horizontal = 24, vertical = 24)

    // Horizontal/vertical split:
    padding(horizontal = 16, vertical = 8)
    margin(horizontal = 24, vertical = 0)

    // Horizontal + individual top/bottom:
    padding(horizontal = 16, top = 8, bottom = 24)
    margin(horizontal = 24, top = 0, bottom = 8)

    // All four sides:
    padding(top = 8, end = 16, bottom = 8, start = 16)
    margin(top = 8, end = 0, bottom = 16, start = 0)
}
```

### background, border, clip

```kotlin
style = {
    background(color(themeColorSurface()))

    // border: radius takes RadiusSchema, not an Int
    border(color = color(themeColorOutline()), thickness = 1)
    border(
        color = color(themeColorOutline()),
        thickness = 1,
        radius = radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)
    )

    // clip shapes:
    clip(roundedCornerShape(8))                          // all corners same radius (Int overload)
    clip(roundedCornerShape(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0))
    clip(roundedCornerShape(radius = radius(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0)))
    clip(circleShape())
    clip(rectangleShape())
}
```

`radius()` helper:
```kotlin
radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)
```

### windowInsets
```kotlin
style = {
    windowInsets(windowInsetsSystemBars())         // status bar + nav bar
    windowInsets(windowInsetsStatusBar())
    windowInsets(windowInsetsNavigationBar())
    windowInsets(windowInsetsIme())                // keyboard inset
    windowInsets(windowInsetsCaptionBar())
    windowInsets(windowInsetsDisplayCutout())
    windowInsets(windowInsetsWaterfall())
}
```

---

## Color & Typography Helpers

### Colors
```kotlin
// Theme colors (adapts to light/dark — prefer these):
color(themeColorPrimary())              color(themeColorOnPrimary())
color(themeColorPrimaryContainer())     color(themeColorOnPrimaryContainer())
color(themeColorSecondary())            color(themeColorOnSecondary())
color(themeColorSecondaryContainer())   color(themeColorOnSecondaryContainer())
color(themeColorTertiary())             color(themeColorOnTertiary())
color(themeColorTertiaryContainer())    color(themeColorOnTertiaryContainer())
color(themeColorError())                color(themeColorOnError())
color(themeColorErrorContainer())       color(themeColorOnErrorContainer())
color(themeColorBackground())           color(themeColorOnBackground())
color(themeColorSurface())              color(themeColorOnSurface())
color(themeColorSurfaceVariant())       color(themeColorOnSurfaceVariant())
color(themeColorOutline())              color(themeColorOutlineVariant())
color(themeColorScrim())
color(themeColorInverseSurface())       color(themeColorInverseOnSurface())
color(themeColorInversePrimary())
color(themeColorSurfaceDim())           color(themeColorSurfaceBright())
color(themeColorSurfaceContainerLowest()) color(themeColorSurfaceContainerLow())
color(themeColorSurfaceContainer())
color(themeColorSurfaceContainerHigh()) color(themeColorSurfaceContainerHighest())

// Explicit (use only for brand/custom colors):
ColorSchema.Hex("#AABBCC")      // 6-char — opaque
ColorSchema.Hex("#FFAABBCC")    // 8-char — AARRGGBB with alpha; invalid → Color.Unspecified (silent)
ColorSchema.Rgba(r = 1f, g = 0f, b = 0f, alpha = 1f)  // components in 0f..1f
```

### Typography
```kotlin
typographyDisplayLarge()      typographyDisplayMedium()      typographyDisplaySmall()
typographyHeadlineLarge()     typographyHeadlineMedium()     typographyHeadlineSmall()
typographyTitleLarge()        typographyTitleMedium()        typographyTitleSmall()
typographyBodyLarge()         typographyBodyMedium()         typographyBodySmall()
typographyLabelLarge()        typographyLabelMedium()        typographyLabelSmall()
```

**⚠ Client quirk:** `LABEL_LARGE/MEDIUM/SMALL` are mapped to `bodyLarge/bodyMedium/bodySmall` in the
client (`TypographySchemaExtensions`). They do NOT produce the Material label styles.

### Icons
```kotlin
icon("home")                                                   // 24dp, outlined, no tint
icon("visibility", color = color(themeColorPrimary()), size = 20)
icon("star", style = outlinedIcon())   // explicit OUTLINED style
icon("star", style = roundedIcon())    // ROUNDED
icon("star", style = sharpIcon())      // SHARP
```

---

## Data Source Helpers

```kotlin
// Persistent across sessions:
segmentedDataBase(segmentId = "auth")      // segmented by key
plainDataBase()                            // flat store

// Screen-scoped (cleared when screen is popped):
screenPlainData()                          // flat, keyed values
screenSegmentedData(segmentId = "form")    // segmented
screenNavigationData()                     // args passed via Navigate(data = ...)

// Tile value — dataKey is the field name the tile exposes:
tile(tileId = "email-field", dataKey = "email")
```

## Access Mode Helpers

```kotlin
fullAccessMode()                           // returns full data as map; use for tile readings to merge into map
singleAccessMode(dataId = "key")           // returns value at key
batchAccessMode(
    dataIds = listOf("key1", "key2"),
    allowMissingData = true,
    unwrapValuesToList = false
)
```

## UpdateData Helpers

```kotlin
inlineUpdateData("key" to value, "key2" to value2)   // vararg pairs
inlineUpdateData(mapOf("key" to value))               // from map
incomingUpdateData()                                  // use incomingData as the update
```

---

## StyleSchema — Application Order

The client applies style modifiers in this exact order (from `Modifier.styledWith()`):
1. `windowInsets` — padding for system bars/IME (outermost)
2. `margin` — outer spacing
3. `size` — width/height constraints
4. `clip` — shape clip (clips background too)
5. `background` — fill color (already clipped by step 4)
6. onClick (injected by the tile, not in StyleSchema)
7. `border` — drawn over background
8. `padding` — inner spacing (innermost)

**Implication:** To get a rounded background, use `clip(roundedCornerShape(8))` — the background
will be clipped to that shape. Setting `border.radius` does NOT clip the background.

---

## Transition Presets

```kotlin
slideHorizontal()           // SlideInHorizontally(Full) enter + SlideOutHorizontally(NegativeFull) exit (most common)
slideVertical()
fade()
fadeAndSlideHorizontal()
slideOver()
slideInFromLeft()    slideInFromRight()    slideInFromTop()    slideInFromBottom()
slideOutToLeft()     slideOutToRight()     slideOutToTop()     slideOutToBottom()
```

Used as `transition` and `popTransition` in `entry(...)`. Multiple enter or exit transitions
combine simultaneously (e.g. `fade()` + `slideHorizontal()` would layer both).

**Animation spec (for custom transitions):**
```kotlin
// Tween: fixed duration + easing
AnimationSpecSchema.Tween(durationMillis = 400, delayMillis = 0, easing = EasingType.EASE_IN_OUT)

// Spring: physics-based, no fixed duration
AnimationSpecSchema.Spring(dampingRatio = 0.7f, stiffness = 400f)  // slight overshoot
AnimationSpecSchema.Spring(dampingRatio = 1f, stiffness = 1500f)   // default, no overshoot
```

**Easing types:** `LINEAR`, `EASE_IN`, `EASE_OUT`, `EASE_IN_OUT`, `FAST_OUT_SLOW_IN` (default, Material),
`FAST_OUT_LINEAR_IN` (for exit), `LINEAR_OUT_SLOW_IN` (for enter).

**OffsetType for slide transitions:**
```kotlin
OffsetType.Full           // +fullSize (from right/bottom) — default for enter
OffsetType.NegativeFull   // -fullSize (from left/top)     — default for exit
OffsetType.Fixed(px = 200)
OffsetType.Fraction(factor = 0.3f)  // 30% of composable size
```

**⚠ Known limitation:** `ExitTransitionSchema.KeepUntilTransitionsFinished` is currently mapped
to `ExitTransition.None` on the client (awaiting stable Compose API). Avoid using it.

---

## Placement Helpers

```kotlin
// Alignment:
alignHorizontallyToStart()    alignHorizontallyToCenter()    alignHorizontallyToEnd()
alignVerticallyToTop()        alignVerticallyToCenter()      alignVerticallyToBottom()

// Arrangement:
arrangeVerticallyToTop()
arrangeVerticallySpacedBy(space = 16, alignment = alignVerticallyToCenter())
arrangeToCenter()
arrangeHorizontallyToStart()
arrangeHorizontallyToEnd()
arrangeHorizontallySpacedBy(space = 8)
```

---

## Full Real-World Example (Login Screen)

```kotlin
fun GraphEntryBuilderScope.LoginScreenEntry() {
    entry(
        screenId = "login",
        initialTiles = {
            Column(
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                    windowInsets(windowInsetsSystemBars())
                },
                alignment = alignHorizontallyToCenter(),
                arrangement = arrangeToCenter()
            ) {
                Image(
                    resourceName = "ic_logo",
                    style = {
                        size(width = fixedHorizontally(100), height = fixedVertically(100))
                        margin(horizontal = 0, vertical = 0, top = 0, bottom = 40)
                    }
                )
                TextField(
                    id = "email",
                    placeholder = "E-mail",
                    maxLines = 1,
                    style = {
                        size(width = fillHorizontally(max = 400))
                        margin(horizontal = 24, top = 0, bottom = 8)
                    }
                )
                TextField(
                    id = "password",
                    placeholder = "Password",
                    trailingIcon = icon("visibility"),
                    maxLines = 1,
                    visualTransformation = keyboardVisualTransformationPassword(),
                    style = {
                        size(width = fillHorizontally(max = 400))
                        margin(horizontal = 24, top = 0, bottom = 8)
                    },
                    events = {
                        GetData(
                            trigger = EventTriggers.onTrailingIconClick(),
                            readings = {
                                reading(screenPlainData(), singleAccessMode("showPassword"))
                            },
                            events = {
                                EvaluateData(
                                    trigger = EventTriggers.onSuccess(),
                                    expression = incomingData().isFalse(),
                                    events = {
                                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                                            update("password", mapOf(
                                                "trailingIcon" to icon("visibility_off"),
                                                "visualTransformation" to keyboardVisualTransformationNone()
                                            ))
                                        })
                                        UpdateData(trigger = EventTriggers.onSuccess(), updates = {
                                            addUpdate(screenPlainData(), inlineUpdateData("showPassword" to true))
                                        })
                                        UpdateTiles(trigger = EventTriggers.onFailure(), updates = {
                                            update("password", mapOf(
                                                "trailingIcon" to icon("visibility"),
                                                "visualTransformation" to keyboardVisualTransformationPassword()
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
                Button(
                    text = "Sign in",
                    buttonType = filledButton(),
                    style = {
                        size(width = fillHorizontally(max = 400), height = fixedVertically(56))
                        margin(horizontal = 24, top = 0, bottom = 8)
                    },
                    events = {
                        // Sign-in logic here
                    }
                )
            }
        },
        initialEvents = {
            UpdateData(
                trigger = EventTriggers.onDisplay(),
                updates = {
                    addUpdate(screenPlainData(), inlineUpdateData("showPassword" to false))
                }
            )
        }
    )
}
```
