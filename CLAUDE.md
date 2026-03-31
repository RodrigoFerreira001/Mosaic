# Mosaic — Project Rules & Context

## Project Overview

**Mosaic** is a **Server-Driven UI (SDUI)** framework built with **Kotlin Multiplatform (KMP)**. Its core purpose is to allow the backend to fully control an application's UI structure, navigation, and event logic at runtime.

The backend describes a screen as a tree of typed components (Tiles) and interactions (Events), serializes it to JSON, and sends it over HTTP. The client deserializes that JSON and renders the UI dynamically using **Compose Multiplatform** — without requiring a new app release.

---

## Module Structure

| Module | Role |
|---|---|
| `mosaic-core` | Shared foundation. Defines all Schemas (contracts), base models, responses, and the polymorphic serializer (`MosaicSerializer`). Used by both server and client. |
| `mosaic-server` | Type-safe Kotlin DSL for building UI trees on the backend. Contains Builders for Tiles, Events, Screens, and Graphs. |
| `mosaic-client` | UI rendering engine for client applications. Deserializes JSON payloads into `TileSchema` trees and renders them as Composables. Executes `EventSchema` logic via `EventRunner`s. |
| `sample-server` | Reference Ktor server implementation. Shows how to serve SDUI screens using `mosaic-server`. |
| `sample-client` | Reference client app (Android + Desktop). Bootstraps the engine via `MosaicApplication(baseUrl, colorScheme)`. |

---

## Technology Stack

- **Language:** Kotlin 2.x, Kotlin Multiplatform
- **UI:** Compose Multiplatform (Android, iOS, Desktop/JVM, Web/WasmJs)
- **Networking:** Ktor Client (client) / Ktor Server (server)
- **Serialization:** Kotlinx Serialization (JSON), polymorphic configuration
- **Dependency Injection:** Koin
- **Concurrency:** Kotlin Coroutines
- **Database:** SQLDelight (multiplatform local storage)
- **Images:** Coil 3
- **Navigation:** Navigation 3 (Adaptive + ViewModel)
- **Android:** minSdk 26, compileSdk 36, JVM target 11

---

## Core Architecture: Schema → Builder → Renderer

Every visual component or interaction in Mosaic follows a strict three-layer pattern across the three core modules.

### In `mosaic-core`
- **Schema**: The typed data contract. Annotated with `@Serializable` and `@SerialName`. Registered in `MosaicSerializer` for polymorphic JSON encoding/decoding.

### In `mosaic-server`
- **Builder**: A DSL class that constructs a Schema with type safety. Exposed as an extension function on a scope (e.g., `TileSchemaBuilderScope`).

### In `mosaic-client`
- **Definition**: Registers the Renderer, HolderBuilder, and KClass together as a single unit.
- **Renderer**: A `@Composable` that receives the Schema and renders it using Compose.
- **Holder**: A stateful object that wraps a Schema instance and its associated event/tile children.
- **HolderBuilder**: A factory that produces a Holder from a Schema.

---

## Critical Rules for Code Generation

These rules apply to every code change in this project, regardless of scope.

**1. Always study existing examples before writing any code.**
Before implementing a new Tile or Event, open and read all corresponding files of an existing implementation. For Events, study `TriggerEventEventDefinition`, `TriggerEventEventHolder`, `TriggerEventEventHolderBuilder`, and `TriggerEventEventRunner`. For Tiles, study `ButtonTileDefinition`, `ButtonTileHolder`, `ButtonTileHolderBuilder`, and `ButtonTileRenderer`.

**2. Never invent implementations from scratch.**
The architecture is rigid and fully established. All patterns, type choices (`object` vs `class`), inheritance, and package organization must mirror existing implementations.

**3. Follow package and type conventions exactly.**
- `Definition`, `Runner`, and `HolderBuilder` are typically `object` (stateless singletons).
- `Holder` is a `class` (stateful instance).
- Do not create sub-packages that do not already exist.

**4. In `EventRunner`, use a placeholder in the implementation body.**
When generating a new `EventRunner`, leave a `println()` or `TODO()` in the execution logic. Do not attempt to write final business logic.

**5. In `TileRenderer`, use a placeholder in the `Render` method.**
When generating a new `TileRenderer`, leave a `println()` or `TODO()` in the composable body. Do not attempt to render final UI.

**6. For non-obvious scenarios, always consult the developer before proceeding.**
Some Schemas have complex builders (e.g., `GetDataEventBuilder` with a list of readings; `MenuTileSchemaBuilder` with nested `MenuItem` builders). When a scenario is not straightforward, present the proposed approach and wait for approval before generating code.

**7. Register every new Schema in `MosaicSerializer`.**
Both `TileSchema` and `EventSchema` subclasses must be added to the polymorphic block in `mosaic-core/.../MosaicSerializer.kt`. Omitting this will cause runtime deserialization failures.

---

## Workflow: Adding a New Event

1. **`mosaic-core`** — Create the `EventSchema` implementing the base interface. Add `@Serializable` and `@SerialName`. Register it in `MosaicSerializer`.
2. **`mosaic-server`** — Create the `EventSchemaBuilder` and the corresponding DSL extension function.
3. **`mosaic-client`** — Under `ui/sdui/implementations/event/events/`, create:
   - `[Name]EventDefinition` (object)
   - `[Name]EventRunner` (object, with placeholder logic)
   - `[Name]EventHolder` (class)
   - `[Name]EventHolderBuilder` (object)

## Workflow: Adding a New Tile

1. **`mosaic-core`** — Create the `TileSchema` implementing the base interface. Add `@Serializable` and `@SerialName`. Register it in `MosaicSerializer`.
2. **`mosaic-server`** — Create the `TileSchemaBuilder` and the corresponding DSL extension function.
3. **`mosaic-client`** — Under `ui/sdui/implementations/tile/tiles/`, create:
   - `[Name]TileDefinition` (object)
   - `[Name]TileRenderer` (object, with placeholder `Render`)
   - `[Name]TileHolder` (class)
   - `[Name]TileHolderBuilder` (object)

---

## Common Commands

```bash
# Run the sample server (Ktor, port 8080)
./gradlew sample-server:run

# Run the sample client (JVM Desktop)
./gradlew sample-client:run

# Build all modules
./gradlew build

# Build a specific module
./gradlew mosaic-core:build
./gradlew mosaic-client:build
./gradlew mosaic-server:build
```

---

## Extended Reference

Detailed catalogs and architecture documentation are located in `.claude/context/`:

- `architecture.md` — Full data flow diagrams, module dependency graph, rendering pipeline
- `tiles-catalog.md` — All Tile schemas, fields, variants, and supported triggers
- `events-catalog.md` — All Event schemas, fields, and execution behavior
- `triggers-catalog.md` — All EventTriggers and their usage context