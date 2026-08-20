# Roadmap

Open work only. Completed items are tracked in the git history, not here.

## Compose parity

> Umbrella for the long-standing goal: *bring every Compose property to the tile*, so a
> screen author on the server can reach what a Compose developer would reach on the client.
> The entries below are what a tile-by-tile audit of the renderers turned up.

### Interaction

- [ ] **Make leaf tiles clickable** — `SimpleText`, `Image`, `AsyncImage`, `Icon` and `Badge`
  render no interaction at all, so any tap handling today means wrapping them in a `Box` or
  `Card`. All the plumbing already exists: pass `onClick(events)` / `onLongPress(events)`
  (`mosaic-client/.../extensions/TileRenderingScopeExtensions.kt`) into `styledWith`, the same
  way the container tiles do, and add `OnClick` / `OnLongPress` to each schema's `@Triggers`.
  Size: S.

- [ ] **Long press on the remaining containers** — long press is currently wired only on `Box`,
  `Column`, `Row` and `Grid`. Still missing: `FlowRow`, `FlexBox`, `LazyColumn`, `LazyRow`,
  `Card`, `Carousel`, `Pager`. `Modifier.styledWith` already takes an `onLongClick`, so this is
  wiring plus documentation. Size: S.

- [ ] **Hand pointer on clickable tiles** — `Modifier.handPointer()` exists in
  `mosaic-client/.../ui/modifiers/HandPointer.kt` but only the SearchBar uses it, so on desktop
  and web a clickable tile shows the default cursor. Now that `styledWith` owns the single
  `combinedClickable`, applying the pointer next to it covers every tile at once. Size: XS.

### Layout and scope

- [ ] **`LocalBoxScope`** — eight tiles are a `Box` under the hood (`Box`, `Shimmer`,
  `SelectionContainer`, `PullToRefresh`, `Menu`, `Popup`, `AdaptiveVisibility`,
  `SystemBroadcastListener`) and none of them publishes a scope, so children can never reach
  `BoxScope.align` or `matchParentSize` — while `Column`, `Row`, `FlowRow`, `Grid` and `FlexBox`
  all expose theirs. The mechanism is uniform and already in place: add `LocalBoxScope` next to
  the others in `mosaic-client/.../foundation/local_providers/`, provide it from the eight
  renderers, and resolve it in `mosaic-client/.../ui/modifiers/Size.kt`, which is where
  `SizeSchema.Behavior` turns into a scoped modifier (`Weight` → `RowScope.weight`, `Span` →
  `GridScope.gridItem`, `Flex` → `FlexBoxScope.flex`). Likely needs a new
  `SizeSchema.Behavior.MatchParent`. Size: M.

- [ ] **Per-child alignment inside `Box`** — `BoxTileSchema.alignment` is a single
  `contentAlignment` applied to every child; Compose allows each child its own `align`. Depends
  on `LocalBoxScope` above. Size: S.

### Appearance

- [ ] **Progress indicator colors** — `CircularProgressIndicator` and `LinearProgressIndicator`
  are stuck with the Material defaults. `StyleSchema` covers size, background and border but has
  no notion of content color, so this needs per-tile fields: add `color` and `trackColor`
  (`ColorSchema?`) to both schemas, mirroring how `TooltipTileSchema` already exposes
  `contentColor` / `containerColor`. Size: S.

- [ ] **Rich text in `SimpleText`** — the tile renders plain text only: no `AnnotatedString`,
  no inline links, no per-span styling, no markdown. This is the largest schema surface in the
  group, since it needs a serializable span model (ranges + style + optional link annotation)
  and a renderer that folds it into an `AnnotatedString`. Size: L.

### Components

- [ ] **`TopAppBar` scroll behavior** — the `MEDIUM` and `LARGE` variants never collapse because
  no `scrollBehavior` is passed. The hard part is architectural rather than local: the app bar
  and the scrollable content are sibling tiles, so a `TopAppBarScrollBehavior` created by the bar
  has to reach the scrollable tile's `nestedScroll` — probably through a CompositionLocal owned
  by the Screen. Size: L.

- [ ] **`SearchBar` expanded state and suggestions** — the tile is only the input field: no
  expanded state, no suggestion list. The current workaround is pairing it with a `LazyColumn`
  driven by `filterChildrenByTerm`. Size: L.

- [ ] **`Badge` as a slot** — `BadgeTileSchema` is a standalone badge, not a `BadgedBox`: it does
  not attach to a sibling and has to be positioned by hand inside a `Box`. The slot pattern is
  already proven by `TabsTileRenderer.badgedWith`, which attaches a badge to a tab's icon or
  label; generalising it would let any host tile carry a badge. Size: M.

## Platform

- [ ] **AnimatedTile** — a tile that animates its own enter/exit and content changes, so screens
  can express motion without the server pushing intermediate states. Needs a serializable
  animation spec (the `ContentTransitionSchema` used by `NestedNavigationGraph` is the closest
  existing model) and a decision on whether it wraps children or replaces them.

- [ ] **Compress traffic between client and server** — screen payloads are sent uncompressed.
  `mosaic-client/.../extensions/CompressionSchemeExtensions.kt` already exists as a starting
  point; the open work is negotiating the scheme and wiring it through the request/response path
  on both sides.

## Recorded decisions (won't do)

Kept here so the analysis is not repeated and these do not come back as bug reports.

- **Non-lazy containers compose every child eagerly** — `Column`, `Row`, `FlowRow`, `Grid`,
  `FlexBox`, `Card`, `Shimmer`. This is inherent to Compose, not a defect; anything needing
  virtualisation should use `LazyColumn` / `LazyRow`.

- **`AssistChip` and `SuggestionChip` are stateless** — no selected state, per the Material
  spec. Use `FilterChip` or `InputChip` when selection is required.
