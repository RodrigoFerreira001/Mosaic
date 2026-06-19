# Boilerplate Templates

Arquivo de referência para geração de código. Usar estes templates adapta muito mais rápido do que explorar o projeto.
Substitua `[Name]` pelo nome do componente (ex: `TransformData`), `[name]` pela variante snake_case do pacote (ex: `transform_data`), e `[package]` pelo subpacote (`data`, `event`, `navigation`, `grouping`, etc.).

> Templates validados contra `TriggerEventEvent*` e `ColumnTile*`/`ButtonTile*` (jun/2026). Se algo não compilar, confira essas implementações reais primeiro.
> Para a API completa de `EventRunningScope`, `TileRenderingScope` e `BuilderScope`, veja [`mechanisms.md`](mechanisms.md).
> Para criar tiles/events em uma **biblioteca externa** que depende do Mosaic, use a skill em `skill/mosaic-extension/`.

## Regras de uso

1. **Sempre ler o Schema antes de gerar.** O schema define os campos específicos que entram no Builder e no Holder.
2. **Sempre se basear nos templates abaixo** — nunca inventar estrutura nova.
3. **Sempre usar `EventSchemaBuilderScope()` / `TileSchemaBuilderScope()` sem argumentos dentro de `build()`** — nunca construir o scope diretamente. O invoke operator em `GenericBuilder` injeta os `compositionLocals` automaticamente.
4. **Cenários simples:** schema com campos primitivos/strings/AnySerializable → gerar direto com os templates.
5. **Cenários complexos → perguntar ao desenvolvedor antes de gerar.** Exemplos de complexidade:
   - Builder com escopo aninhado (ex: `GetData` tem `GetDataReadingBuilderScope` separado)
   - Holder que precisa guardar estado adicional além do schema
   - Schema com subtipos sealed (ex: `EvaluateDataEventSchema.Expression`)
   - Qualquer campo do schema que não seja primitivo, String, AnySerializable, List simples, EventSchema ou sealed simples já existente

   Nesses casos: apresentar a abordagem proposta e aguardar aprovação.

6. **Nunca usar `snapshotLocals()` dentro de um lambda lazy.** Se um builder precisa avaliar um sub-scope fora de `build()`, capturar o snapshot **antes** de `addBuilder()` — ver seção "Server DSL: CompositionLocal System" em `architecture.md`.

7. **Sempre atualizar o catalog correspondente após gerar boilerplate.**
   - Novo Event → adicionar entrada em `.claude/context/events-catalog.md`
   - Novo Tile → adicionar entrada em `.claude/context/tiles-catalog.md`
   - Novo Trigger → adicionar entrada em `.claude/context/triggers-catalog.md`
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

`EventSchemaBuilder` é uma **classe abstrata** (herdar com parênteses). Ids usam `randomId()`.

```kotlin
package dev.catbit.mosaic.server.builder.event.builders.[package]

import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class [Name]EventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    // ↑ adicione aqui os campos específicos do schema
) : EventSchemaBuilder<[Name]EventSchema>() {

    override fun build() = [Name]EventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        // ↑ mapeie os campos específicos
    )
}

fun EventSchemaBuilderScope.[Name](
    id: String = randomId(),
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
    override suspend fun EventRunningScope.runEvent(event: [Name]EventSchema) {
        TODO("[Name]EventRunner not yet implemented")
    }
}
```

> Padrão síncrono com tratamento de erro (ver `TriggerEventEventRunner`):
> ```kotlin
> override suspend fun EventRunningScope.runEvent(event: [Name]EventSchema) {
>     runSafely(
>         onError = {
>             onTrigger(EventTriggers.onFailure(), data = it)
>             logError(tag = "[Name]EventRunner", throwable = it)
>         }
>     ) {
>         /* lógica */
>         onTrigger(EventTriggers.onSuccess())
>     }
> }
> ```
> `runSafely` vem de `dev.catbit.mosaic.core.extensions.runSafely`.
>
> Padrão com coroutine (eventos assíncronos como GetData, EvaluateData):
> ```kotlin
> override suspend fun EventRunningScope.runEvent(event: [Name]EventSchema) {
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

---

### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

Arquivo: `mosaic-core/.../serialization/MosaicSerializer.kt`

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.core.data.schemas.event.events.[package].[Name]EventSchema

// 2. Adicionar na propriedade defaultEventSerializers (manter agrupado por categoria)
private val defaultEventSerializers
    get() = mapOf(
        // ...
        [Name]EventSchema::class to [Name]EventSchema.serializer(),
        // ...
    )
```

> **Trigger novo?** Registrar também em `defaultEventTriggerSerializers` (mesmo arquivo, ordem alfabética) **e** criar a factory function em `EventTriggers.kt` (`fun on[X]() = On[X]EventTrigger`).

---

### Registro 2 — `MosaicModules.kt` (mosaic-client)

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.[package].[name].[Name]EventDefinition

// 2. Adicionar na lista baseEventsDefinitions (manter agrupado por categoria)
private val baseEventsDefinitions = listOf(
    // ...
    [Name]EventDefinition,
)
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

Todo `TileSchema` tem os campos base `id`, `events`, `style`, `searchableTerms`, `visibility` — o Builder e a DSL sempre os expõem.

---

### `[Name]TileSchemaBuilder.kt` — mosaic-server

`TileSchemaBuilder` é uma **classe abstrata** (herdar com parênteses).

```kotlin
package dev.catbit.mosaic.server.builder.tile.builders.[package]

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class [Name]TileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    // ↑ adicione os campos específicos do schema
) : TileSchemaBuilder<[Name]TileSchema>() {

    override fun build() = [Name]TileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        // ↑ mapeie os campos
    )
}

fun TileSchemaBuilderScope.[Name](
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    // ↑ parâmetros específicos
) {
    addBuilder(
        [Name]TileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            // ↑ passe os parâmetros
        )
    )
}
```

> **Tile container (com `tiles: List<TileSchema>`):** adicionar `private val tiles: TileSchemaBuilderScope.() -> Unit` no builder, mapear com `tiles = TileSchemaBuilderScope().apply(tiles).build()`, e na DSL colocar `tiles: TileSchemaBuilderScope.() -> Unit` como **último** parâmetro (trailing lambda, sem default) — ver `ColumnTileSchemaBuilder`.
>
> **Helpers utilitários de valores do schema** (estilo `AligmentHelper`): quando o schema tiver sealed types próprios, criar funções `fun xxxYyy() = Schema.Subtipo.Valor` no próprio arquivo do builder — ver `AdaptiveVisibilityTileSchemaBuilder`.

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

Listas do holder são `MutableList`. Tile folha:

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

class [Name]TileHolder(
    override val id: String,
    override var tile: [Name]TileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<[Name]TileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
```

> **Tile container:** `override val tiles: MutableList<TileHolder<*>>` (não-nulo, sem default) e `get()` também copia os filhos:
> ```kotlin
> override fun get() = tile.copy(
>     tiles = tiles.map { it.get() },
>     events = events?.map { it.get() }
> )
> ```

---

### `[Name]TileHolderBuilder.kt` — mosaic-client

O parâmetro chama-se `tileModel` e o `BuilderScope` oferece `buildEventHolders()` / `buildTileHolders()`.

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

object [Name]TileHolderBuilder : TileHolderBuilder<[Name]TileSchema, [Name]TileHolder> {

    override fun BuilderScope.build(
        tileModel: [Name]TileSchema
    ) = with(tileModel) {
        [Name]TileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            // container: tiles = tiles.buildTileHolders()
        )
    }
}
```

---

### `[Name]TileRenderer.kt` — mosaic-client

`TileRenderer` tem **um único type param** (o schema) e o `Render` é extensão de `TileRenderingScope` recebendo o schema:

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

object [Name]TileRenderer : TileRenderer<[Name]TileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: [Name]TileSchema
    ) {
        TODO("[Name]TileRenderer not yet implemented")
    }
}
```

> Esqueleto típico de implementação real (ver `ColumnTileRenderer`):
> ```kotlin
> with(tileSchema) {
>     val modifier = Modifier
>         .visible(isVisible())                      // androidx.compose.foundation.layout.visible
>         .styledWith(style = style, onClick = onClick(events))
>
>     /* composable raiz */ {
>         RenderChildren(tiles)                      // containers
>     }
> }
> ```

---

### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

Arquivo: `mosaic-core/.../serialization/MosaicSerializer.kt`

```kotlin
// 1. Adicionar import
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

// 2. Adicionar na propriedade defaultTileSerializers
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

// 2. Adicionar na lista baseTilesDefinitions
private val baseTilesDefinitions = listOf(
    // ...
    [Name]TileDefinition,
)
```

---

## EventRunningScope — API rápida

```kotlin
incomingData: Any?                              // dado que chegou do evento anterior
onTrigger(EventTriggers.onSuccess(), data = x) // dispara eventos filhos com o trigger dado
runEventInline(eventSchema, data = x)          // executa um EventSchema arbitrário imediatamente
runSuspendOnScreenScope { }                     // lança coroutine no escopo da screen
runSuspendOnStateHolderScope { }                // lança coroutine no escopo do state holder
broadcastData(data)                             // envia BroadcastData para os tiles da screen
get<SomeUseCase>()                             // injeção via Koin (também getOrNull / getAll)
incomingData.asMapAny()                        // cast para Map<String, AnySerializable>?
incomingData.asMapString()                     // cast para Map<String, String>?
logError(throwable = t, tag = "...")           // loga erro via MosaicLogger
log(level, msg)                                // log genérico

// Propriedades para manipular a screen:
tilesEditor / tilesEventDispatcher / tilesOverlaysEditor / tilesValueProducer
dataHolder / screenBehaviorsHolder
```

## TileRenderingScope — API rápida

```kotlin
triggerEvent(EventTriggers.onClick(), data = x) // dispara os events do tile com o trigger dado
dispatchEvent(tileEvent)                        // TileEvent direcionado a este tile
dispatchGroupEvent(tileGroupEvent)              // TileGroupEvent para um grupo
RenderChild(tileSchema)                         // renderiza um filho
RenderChildren(tileSchemas)                     // renderiza lista de filhos
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
