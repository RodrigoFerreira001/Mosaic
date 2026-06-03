# Tiles: Navigation

---

## NavigationBarTileSchema

**@SerialName:** `"NavigationBar"`

Material3 bottom navigation bar.

### Fields
| Field | Type | Default |
|---|---|---|
| `items` | `List<NavigationBarItem>` | required |
| `selectedItemId` | `String` | required |

`NavigationBarItem`: `id: String`, `icon: IconSchema`, `label: String?`

### Supported Triggers
`OnNavigationBarItemClickEventTrigger(itemId: String)`

### Builder
```kotlin
NavigationBar(selectedItemId = "home") {
    item(id = "home", icon = IconSchema("home"), label = "Home")
    item(id = "search", icon = IconSchema("search"), label = "Search")
    item(id = "profile", icon = IconSchema("person"), label = "Profile")
}
```

Attach events to the tile:
```kotlin
NavigationBar(selectedItemId = "home") {
    item("home", IconSchema("home"), "Home")
    // ...
    on(OnNavigationBarItemClickEventTrigger(itemId = "search")) {
        Navigate(destination = "search", navigatorId = "main", trigger = InlineEventTrigger())
    }
}
```

---

## NavigationRailTileSchema

**@SerialName:** `"NavigationRail"`

Vertical rail navigation (tablet/desktop).

### Fields
| Field | Type | Default |
|---|---|---|
| `items` | `List<NavigationRailItem>` | required |
| `selectedItemId` | `String` | required |
| `header` | `TileSchema?` | null (tile at top of rail) |
| `footer` | `TileSchema?` | null (tile at bottom of rail) |

`NavigationRailItem`: `id: String`, `icon: IconSchema`, `label: String?`

### Supported Triggers
`OnNavigationRailItemClickEventTrigger(itemId: String)`

---

## TabsTileSchema

**@SerialName:** `"Tabs"`

Horizontal tab bar.

### Fields
| Field | Type | Default |
|---|---|---|
| `selectedTabId` | `String` | required |
| `tabItems` | `List<TabItem>` | required |
| `tabType` | `TabsTileSchema.Type` | `PRIMARY` |
| `scrollable` | `Boolean` | `false` |

`TabItem`: `id: String`, `label: String?`, `icon: IconSchema?`, `badgeText: String?`
`Type`: `PRIMARY` (fixed), `SECONDARY` (scrollable style)

### Supported Triggers
`OnTabItemClickEventTrigger(itemId: String)`

### Builder
```kotlin
Tabs(selectedTabId = "all", tabType = TabsTileSchema.Type.PRIMARY) {
    tab(id = "all", label = "All")
    tab(id = "active", label = "Active")
    tab(id = "done", label = "Done")
}
```

---

## NestedNavigationGraphTileSchema

**@SerialName:** `"NestedNavigationGraph"`

Embeds a full navigation graph inside another screen.

### Fields
| Field | Type | Default |
|---|---|---|
| `navigatorId` | `String` | required (unique ID for this navigator) |
| `entries` | `List<Entry>` | required |
| `startEntryId` | `String` | required |
| `defaultTransition` | `ContentTransitionSchema?` | null |
| `defaultPopTransition` | `ContentTransitionSchema?` | null |
| `defaultPredictivePopTransition` | `ContentTransitionSchema?` | null |

`Entry`: `screenId`, `initialTiles`, `initialEvents`, `failureTiles`, `failureEvents`, `transition?`, `popTransition?`, `predictivePopTransition?`

### Supported Triggers
`OnNavigationEntryChangedEventTrigger`, `OnNavigationEntrySetEventTrigger(screenId: String)`

### Builder
```kotlin
NestedNavigationGraph(
    navigatorId = "inner-nav",
    startEntryId = "tab-home"
) {
    entry("tab-home") {
        initialTiles { SimpleText("Home content") }
    }
    entry("tab-search") {
        initialTiles { SimpleText("Search content") }
    }
}
```

Use `Navigate(navigatorId = "inner-nav", destination = "tab-search", ...)` to navigate within it.

---

## AdaptiveNavigationTileSchema

**@SerialName:** `"AdaptiveNavigation"`

Adaptive layout: shows `NavigationBar` on phones, `NavigationRail` on tablets/desktop.

### Fields
| Field | Type | Default |
|---|---|---|
| `navigatorId` | `String` | required |
| `entries` | `List<NavigationEntry>` | required |
| `startEntryId` | `String` | required |
| `selectedEntryId` | `String` | required |
| `header` | `TileSchema?` | null |
| `footer` | `TileSchema?` | null |
| `defaultTransition` | `ContentTransitionSchema?` | null |
| `defaultPopTransition` | `ContentTransitionSchema?` | null |
| `defaultPredictivePopTransition` | `ContentTransitionSchema?` | null |

`NavigationEntry`: `id`, `icon: IconSchema`, `label: String?`, `initialTiles`, `initialEvents`, `failureTiles`, `failureEvents`, `transition?`, `popTransition?`, `predictivePopTransition?`

### Supported Triggers
`OnAdaptiveNavigationItemClickEventTrigger(itemId: String)`, `OnNavigationEntryChangedEventTrigger`, `OnNavigationEntrySetEventTrigger(screenId: String)`

### Builder
```kotlin
AdaptiveNavigation(
    navigatorId = "adaptive-nav",
    startEntryId = "home",
    selectedEntryId = "home"
) {
    navigationEntry(id = "home", icon = IconSchema("home"), label = "Home") {
        initialTiles { Column { SimpleText("Home") } }
    }
    navigationEntry(id = "settings", icon = IconSchema("settings"), label = "Settings") {
        initialTiles { Column { SimpleText("Settings") } }
    }
}
```
