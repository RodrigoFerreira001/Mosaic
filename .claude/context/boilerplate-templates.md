# Boilerplate Templates

Arquivo de referência para geração de código. Usar estes templates adapta muito mais rápido do que explorar o projeto.
Substitua `[Name]` pelo nome do componente (ex: `TransformData`), `[name]` pela variante camelCase (ex: `transformData`), e `[package]` pelo subpacote (`data`, `event`, `navigation`, etc.).

## Regras de uso

1. **Sempre ler o Schema antes de gerar.** O schema define os campos específicos que entram no Builder e no Holder.
2. **Sempre se basear nos templates abaixo** — nunca inventar estrutura nova.
3. **Sempre usar `EventSchemaBuilderScope()` sem argumentos dentro de `build()`** — nunca construir o scope diretamente. O invoke operator em `GenericBuilder` injeta os `compositionLocals` automaticamente.
4. **Cenários simples:** schema com campos primitivos/strings/AnySerializable → gerar direto com os templates.
5. **Cenários complexos → perguntar ao desenvolvedor antes de gerar.** Exemplos de complexidade:
   - Builder com escopo aninhado (ex: `GetData` tem `GetDataReadingBuilderScope` separado)
   - Holder que precisa guardar estado adicional além do schema
   - Schema com subtipos sealed (ex: `EvaluateDataEventSchema.Expression`)
   - Tile com filhos (`tiles: List<TileHolder<*>>` não-nulo por padrão)
   - Qualquer campo do schema que não seja primitivo, String, AnySerializable, List simples ou EventSchema

   Nesses casos: apresentar a abordagem proposta e aguardar aprovação.

6. **Nunca usar `snapshotLocals()` dentro de um lambda lazy.** Se um builder precisa avaliar um sub-scope fora de `build()`, capturar o snapshot **antes** de `addBuilder()` — ver seção "Server DSL: CompositionLocal System" em `architecture.md`.

7. **Sempre atualizar o catalog correspondente após gerar boilerplate.**
   - Novo Event → adicionar entrada em `.claude/context/events-catalog.md`
   - Novo Tile → adicionar entrada em `.claude/context/tiles-catalog.md`
   - Formato: nome do schema, `@SerialName`, tabela de campos, triggers suportados (do `@Triggers` do schema)
   - Ler o schema para extrair essas informações — não inventar.

---

## Evento — 5 arquivos + 2 registros

### Estrutura de pacotes

```
mosaic-core/.../schemas/event/events/[package]/[Name]EventSchema.kt         ← criar o schema aqui (ponto de partida)
mosaic-server/.../builder/event/builders/[package]/[Name]EventBuilder.kt    ← server DSL
mosaic-client/.../implementations/event/events/[package]/[name]/
    [Name]EventDefinition.kt
    [Name]EventHolder.kt
    [Name]EventHolderBuilder.kt
    [Name]EventRunner.kt
```

---

### `[Name]EventBuilder.kt` — mosaic-server

```kotlin
package dev.catbit.mosaic.server.builder.event.builders.[package]

import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class [Name]EventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    // ↑ adicione aqui os campos específicos do schema
) : EventSchemaBuilder<[Name]EventSchema> {

    override fun build() = [Name]EventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        // ↑ mapeie os campos específicos
    )
}

fun EventSchemaBuilderScope.[Name](
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    // ↑ adicione os parâmetros específicos
) {
    addBuilder(
        [Name]EventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            // ↑ passe os parâmetros
        )
    )
}
```

---

### `[Name]EventDefinition.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema

object [Name]EventDefinition : EventDefinition<[Name]EventSchema> {
    override val eventSchemaClass = [Name]EventSchema::class
    override val eventRunner = [Name]EventRunner
    override val eventHolderBuilder = [Name]EventHolderBuilder
}
```

---

### `[Name]EventHolder.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class [Name]EventHolder(
    override val id: String,
    override var event: [Name]EventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<[Name]EventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
```

---

### `[Name]EventHolderBuilder.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema

object [Name]EventHolderBuilder : EventHolderBuilder<[Name]EventSchema, [Name]EventHolder> {

    override fun BuilderScope.build(
        eventSchema: [Name]EventSchema
    ) = with(eventSchema) {
        [Name]EventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
```

---

### `[Name]EventRunner.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema

object [Name]EventRunner : EventRunner<[Name]EventSchema> {
    override fun EventRunningScope.runEvent(event: [Name]EventSchema) {
        TODO("[Name]EventRunner not yet implemented")
    }
}
```

> Padrão com coroutine (eventos assíncronos como GetData, EvaluateData):
> ```kotlin
> override fun EventRunningScope.runEvent(event: [Name]EventSchema) {
>     runSuspendOnScreenScope {
>         withContext(Dispatchers.IO) {
>             val result = runCatching { /* lógica */ }
>                 .getOrElse {
>                     onTrigger(EventTriggers.onFailure(), data = it)
>                     return@withContext
>                 }
>             onTrigger(EventTriggers.onSuccess(), data = result)
>         }
>     }
> }
> ```
>
> Padrão síncrono (eventos simples como SendData, TransformData):
> ```kotlin
> override fun EventRunningScope.runEvent(event: [Name]EventSchema) {
>     val result = runCatching { /* lógica */ }
>         .getOrElse {
>             onTrigger(EventTriggers.onFailure(), data = it)
>             return
>         }
>     onTrigger(EventTriggers.onSuccess(), data = result)
> }
> ```

---

### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

Arquivo: `mosaic-core/.../serialization/MosaicSerializer.kt`

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema

// 2. Adicionar na função defaultEventSerializers (manter ordem alfabética/agrupada por categoria)
private val defaultEventSerializers
    get() = mapOf(
        // ...
        [Name]EventSchema::class to [Name]EventSchema.serializer(),
        // ...
    )
```

---

### Registro 2 — `MosaicModules.kt` (mosaic-client)

Arquivo: `mosaic-client/.../di/MosaicModules.kt`

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name].[Name]EventDefinition

// 2. Adicionar na lista de definitions (manter agrupado por categoria)
[Name]EventDefinition,
```

---

## Tile — 5 arquivos + 2 registros

### Estrutura de pacotes

```
mosaic-core/.../schemas/tile/tiles/[package]/[Name]TileSchema.kt
mosaic-server/.../builder/tile/builders/[package]/[Name]TileSchemaBuilder.kt
mosaic-client/.../implementations/tile/tiles/[package]/[name]/
    [Name]TileDefinition.kt
    [Name]TileHolder.kt
    [Name]TileHolderBuilder.kt
    [Name]TileRenderer.kt
```

---

### `[Name]TileSchemaBuilder.kt` — mosaic-server

```kotlin
package dev.catbit.mosaic.server.builder.tile.builders.[package]

import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class [Name]TileSchemaBuilder(
    private val id: String,
    // ↑ adicione os campos específicos do schema
) : TileSchemaBuilder<[Name]TileSchema> {

    override fun build() = [Name]TileSchema(
        id = id,
        // ↑ mapeie os campos
    )
}

fun TileSchemaBuilderScope.[Name](
    id: String = randomUuid(),
    // ↑ parâmetros específicos
) {
    addBuilder(
        [Name]TileSchemaBuilder(
            id = id,
            // ↑ passe os parâmetros
        )
    )
}
```

---

### `[Name]TileDefinition.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

object [Name]TileDefinition : TileDefinition<[Name]TileSchema> {
    override val tileSchemaClass = [Name]TileSchema::class
    override val tileRenderer = [Name]TileRenderer
    override val tileHolderBuilder = [Name]TileHolderBuilder
}
```

---

### `[Name]TileHolder.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

class [Name]TileHolder(
    override val id: String,
    override var tile: [Name]TileSchema,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : TileHolder<[Name]TileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
```

---

### `[Name]TileHolderBuilder.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

object [Name]TileHolderBuilder : TileHolderBuilder<[Name]TileSchema, [Name]TileHolder> {

    override fun BuilderScope.build(
        tileSchema: [Name]TileSchema
    ) = with(tileSchema) {
        [Name]TileHolder(
            id = id,
            tile = tileSchema,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
```

---

### `[Name]TileRenderer.kt` — mosaic-client

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

object [Name]TileRenderer : TileRenderer<[Name]TileSchema, [Name]TileHolder> {

    @Composable
    override fun [Name]TileHolder.Render() {
        TODO("[Name]TileRenderer not yet implemented")
    }
}
```

---

### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

Arquivo: `mosaic-core/.../serialization/MosaicSerializer.kt`

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

// 2. Adicionar na função defaultTileSerializers
private val defaultTileSerializers
    get() = mapOf(
        // ...
        [Name]TileSchema::class to [Name]TileSchema.serializer(),
        // ...
    )
```

---

### Registro 2 — `MosaicModules.kt` (mosaic-client)

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name].[Name]TileDefinition

// 2. Adicionar na lista de definitions
[Name]TileDefinition,
```

---

## EventRunningScope — API rápida

```kotlin
incomingData: Any?                              // dado que chegou do evento anterior
onTrigger(EventTriggers.onSuccess(), data = x) // dispara eventos filhos com trigger OnSuccess
onTrigger(EventTriggers.onFailure(), data = x) // dispara eventos filhos com trigger OnFailure
onTrigger(EventTriggers.onStart(), data = x)   // dispara eventos filhos com trigger OnStart
runSuspendOnScreenScope { }                     // lança coroutine no escopo da screen
get<SomeUseCase>()                             // injeção via Koin
incomingData.asMapAny()                        // cast para Map<String, AnySerializable>?
logError(throwable)                            // loga erro via MosaicLogger
```

## Tipos concretos de AnySerializable em runtime

`AnySerializable` é `typealias` para `Any`. Após deserialização do JSON:

| JSON | Tipo Kotlin |
|------|-------------|
| `"string"` | `String` |
| `true` / `false` | `Boolean` |
| número inteiro | `Int` ou `Long` |
| número decimal | `Double` |
| `[...]` | `List<Any?>` |
| `{...}` | `Map<String, Any?>` |
| `null` | `null` |
