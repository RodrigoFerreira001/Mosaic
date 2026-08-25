# Mosaic — Project Rules & Context

> Este arquivo substitui `CLAUDE.md`. É a fonte de verdade para AI agents trabalhando neste repositório.

## Project Overview

**Mosaic** é um framework de **Server-Driven UI (SDUI)** construído com **Kotlin Multiplatform (KMP)**. Seu propósito central é permitir que o backend controle completamente a estrutura da UI, navegação e lógica de eventos em runtime.

O backend descreve uma tela como uma árvore de componentes tipados (Tiles) e interações (Events), serializa para JSON e envia via HTTP. O cliente deserializa esse JSON e renderiza a UI dinamicamente usando **Compose Multiplatform** — sem necessitar de uma nova release.

Para o mapa completo de arquitetura (mecanismos explícitos e implícitos, pipelines de Tile/Event, camada de dados), veja [`.claude/context/architecture.md`](.claude/context/architecture.md).

---

## Module Structure

| Module | Role |
|---|---|
| `mosaic-core` | Foundation compartilhada. Define todos os Schemas (contratos), modelos base, respostas e o serializer polimórfico (`MosaicSerializer`). Usado por server e client. |
| `mosaic-server` | DSL Kotlin type-safe para construir árvores de UI no backend. Contém Builders para Tiles, Events, Screens e Graphs. |
| `mosaic-client` | Engine de renderização de UI para apps cliente. Deserializa JSON em árvores `TileSchema` e renderiza como Composables. Executa lógica `EventSchema` via `EventRunner`s. |
| `sample-server` | Implementação Ktor de referência. Mostra como servir telas SDUI usando `mosaic-server` — também é o showroom interativo do framework. |
| `sample-client` | App cliente de referência (Android + Desktop). Bootstrap via `MosaicApplication(applicationId, baseUrl, ...)`. |

---

## Technology Stack

- **Language:** Kotlin 2.x, Kotlin Multiplatform
- **UI:** Compose Multiplatform (Android, iOS, Desktop/JVM, Web/WasmJs)
- **Networking:** Ktor Client (client) / Ktor Server (server)
- **Serialization:** Kotlinx Serialization (JSON), configuração polimórfica
- **Dependency Injection:** Koin
- **Concurrency:** Kotlin Coroutines
- **Database:** Room (multiplatform local storage) — driver SQLite nativo em Android/iOS/JVM, `WebWorkerSQLiteDriver` em WasmJS
- **Images:** Coil 3
- **Navigation:** Navigation 3 (Adaptive + ViewModel)
- **Android:** minSdk 26, compileSdk 36, JVM target 11

---

## Core Architecture: Schema → Builder → Renderer

Todo componente visual ou interação no Mosaic segue um padrão de três camadas.

### Em `mosaic-core`
- **Schema**: O contrato tipado. Anotado com `@Serializable` e `@SerialName`. Registrado em `MosaicSerializer` para encoding/decoding polimórfico de JSON. O KDoc do schema é a **fonte primária** de toda a documentação — os catálogos em `.claude/context/` são escritos a partir dele, nunca o contrário.

### Em `mosaic-server`
- **Builder**: Classe DSL que constrói um Schema com type safety. Exposta como função de extensão em um scope (ex: `TileSchemaBuilderScope`).

### Em `mosaic-client`
- **Definition**: Registra Renderer, HolderBuilder e KClass juntos como uma única unidade.
- **Renderer**: Um `@Composable` que recebe o Schema e renderiza usando Compose.
- **Holder**: Objeto stateful que envolve uma instância de Schema e seus eventos/tiles filhos.
- **HolderBuilder**: Factory que produz um Holder a partir de um Schema.
- **Runner** (apenas Events): Executa a lógica de um EventSchema dentro de um `EventRunningScope`.

---

## Critical Rules for Code Generation

Estas regras se aplicam a **toda** mudança de código neste projeto, independente do escopo.

**Regra 1. Sempre estudar exemplos existentes antes de escrever qualquer código.**
Antes de implementar um novo Tile ou Event, abra e leia todos os arquivos correspondentes de uma implementação existente. Para Events, estude `TriggerEventEventRunner`. Para Tiles, estude `ButtonTileRenderer` e `ButtonTileHolder`. A seção **"Boilerplate — templates prontos"** abaixo contém templates completos — use-os como ponto de partida.

**Regra 2. Nunca inventar implementações do zero.**
A arquitetura é rígida e completamente estabelecida. Todos os padrões, escolhas de tipo (`object` vs `class`), herança e organização de pacotes devem espelhar implementações existentes.

**Regra 3. Seguir convenções de pacote e tipo exatamente.**
- `Definition`, `Runner`, e `HolderBuilder` são tipicamente `object` (singletons sem estado).
- `Holder` é uma `class` (instância com estado).
- Não crie sub-pacotes que ainda não existam.

**Regra 4. Em `EventRunner`, use um placeholder no corpo.**
Ao gerar um novo `EventRunner`, deixe `TODO("[Name]EventRunner not yet implemented")` no corpo de execução. Não tente escrever lógica de negócio final — aguarde instrução explícita.

**Regra 5. Em `TileRenderer`, use um placeholder no `Render`.**
Ao gerar um novo `TileRenderer`, deixe `TODO("[Name]TileRenderer not yet implemented")` no body do composable. Não tente renderizar UI final — aguarde instrução explícita.

**Regra 6. Em cenários não-óbvios, sempre consulte o desenvolvedor antes de prosseguir.**
Alguns Schemas têm builders complexos (ex: `GetDataEventBuilder` com escopo aninhado de readings; `MenuTileSchemaBuilder` com nested `MenuItem` builders). Quando um cenário não for direto, apresente a abordagem proposta e aguarde aprovação antes de gerar código.

**Regra 7. Registrar todo novo Schema em `MosaicSerializer`.**
Subclasses de `TileSchema` e `EventSchema` devem ser adicionadas ao bloco polimórfico em `mosaic-core/.../MosaicSerializer.kt`. Omitir isso causa falhas de deserialização em runtime (sem exceção explícita, o campo `type` é ignorado silenciosamente).

**Regra 8. Toda alteração em um Tile ou Event exige atualização imediata e completa da documentação e dos exemplos — sem exceção.**

Isso vale para **qualquer** mudança relacionada a um Tile ou Event — schema, builder, holder, holderBuilder, definition, renderer, runner — seja criação, modificação de campo, mudança de comportamento/trigger disparado, renomeação, ou remoção. Nenhuma alteração, inserção ou deleção pode ficar sem reflexo em **todos** os quatro lugares abaixo, na mesma tarefa:

1. **O KDoc do schema** em `mosaic-core` — é a fonte primária, atualizado primeiro e com precisão (parâmetros, triggers disparados, comportamento). Os catálogos são escritos a partir dele, nunca o contrário — se o KDoc estiver desatualizado, tudo que vem depois também estará.
2. **A entrada correspondente no catálogo** — `.claude/context/tiles-catalog.md` (Tiles) ou `.claude/context/events-catalog.md` (Events). Formato de cada entrada: descrição, tabela de parâmetros (nome, tipo, default, para que serve), snippet de DSL real, triggers disparados, observações de comportamento não-óbvio. **Nunca inventar campos ou comportamento** — reler o schema e o builder reais para extrair as informações.
3. **A skill relevante** — `skill/mosaic-server/` (catálogo usado para autoria de telas via DSL), `skill/mosaic-client/` (se a mudança afeta o que um `TileRenderer`/`EventRunner` customizado pode fazer), `skill/mosaic/` (se a mudança afeta arquitetura ou um mecanismo explícito/implícito descrito em `architecture.md`).
4. **O exemplo correspondente no showroom** (`sample-server`/`sample-client`) — a página de detalhe do tile/event em `sample-server/.../screens/tile_details/builders/*.kt` ou `.../event_details/builders/*.kt` precisa demonstrar o comportamento real, atualizado: parâmetro novo ganha uma variação na demo, comportamento alterado corrige o texto/`ShowroomNote` que o descreve, campo removido é removido de qualquer exemplo que ainda o use. Um tile/event novo **precisa** de uma página de detalhe nova, registrada no manager correspondente e com um card no catálogo (`TilesScreenBuilder.kt`/`EventsScreenBuilder.kt`) — sem isso ele fica inacessível na UI mesmo já fazendo parte do framework.

Uma tarefa que cria, modifica ou remove um Tile/Event **só está concluída** quando KDoc, catálogo, skill e o exemplo no showroom estiverem sincronizados entre si — isso não é um passo opcional "se der tempo", é parte da definição de "terminado" para esse tipo de tarefa.

**Regra 9. Comentário de código (`//`) só quando explicitamente pedido — o padrão é código legível sem comentário nenhum.**

Nomes de variável/função/classe bons e uma estrutura clara substituem o comentário na maioria dos casos — se o comentário só repete o que o código já deixa óbvio, não escreva. Escreva um comentário apenas quando o usuário pedir explicitamente comentários naquela tarefa, ou quando, mesmo sem pedido, o motivo por trás do código não é dedutível pela leitura (uma constante que replica um valor externo específico, um workaround pra um bug de uma lib, uma ordem de operações não-óbvia que quebra se invertida). Nesse segundo caso, o comentário explica o *porquê*, nunca o *o quê*.

Isso **não** se aplica ao KDoc de `Schema`/`Builder`/mecanismos públicos exigido pelas Regras 7 e 8 — esse é contrato/documentação estrutural do framework, não um comentário explicativo opcional, e continua obrigatório como já descrito acima.

---

## Workflow: Adding a New Event

1. **`mosaic-core`** — Criar o `EventSchema` implementando a interface base. Adicionar `@Serializable`, `@SerialName` e um KDoc completo (descrição, campos, triggers disparados — ver padrão em qualquer `EventSchema` existente). Registrar em `MosaicSerializer`.
2. **`mosaic-server`** — Criar o `EventSchemaBuilder` e a função DSL de extensão correspondente (ver template abaixo).
3. **`mosaic-client`** — Em `ui/sdui/implementations/event/events/`, criar:
   - `[Name]EventDefinition` (object)
   - `[Name]EventRunner` (object, com placeholder `TODO()`)
   - `[Name]EventHolder` (class)
   - `[Name]EventHolderBuilder` (object)
4. Registrar o `EventDefinition` na lista `baseEventsDefinitions` em `MosaicModules.kt`.
5. Atualizar `events-catalog.md` com a nova entrada (Regra 8).
6. Atualizar a skill relevante (Regra 8).
7. Criar a página de detalhe em `sample-server/.../screens/event_details/builders/[Name]EventDetailBuilder.kt`, registrá-la no `eventDetailBuilderManager` e adicionar o card no catálogo em `EventsScreenBuilder.kt` (Regra 8).

## Workflow: Adding a New Tile

1. **`mosaic-core`** — Criar o `TileSchema` implementando a interface base. Adicionar `@Serializable`, `@SerialName` e um KDoc completo. Registrar em `MosaicSerializer`.
2. **`mosaic-server`** — Criar o `TileSchemaBuilder` e a função DSL de extensão correspondente (ver template abaixo).
3. **`mosaic-client`** — Em `ui/sdui/implementations/tile/tiles/`, criar:
   - `[Name]TileDefinition` (object)
   - `[Name]TileRenderer` (object, com placeholder `TODO()`)
   - `[Name]TileHolder` (class)
   - `[Name]TileHolderBuilder` (object)
4. Registrar o `TileDefinition` na lista `baseTilesDefinitions` em `MosaicModules.kt`.
5. Atualizar `tiles-catalog.md` com a nova entrada (Regra 8).
6. Atualizar a skill relevante (Regra 8).
7. Criar a página de detalhe em `sample-server/.../screens/tile_details/builders/[Name]TileDetailBuilder.kt`, registrá-la no `tileDetailBuilderManager` e adicionar o card no catálogo em `TilesScreenBuilder.kt` (Regra 8).

---

## Tiles e Events com Implementação Pendente

Todos os `EventRunner`s e `TileRenderer`s atuais têm implementação completa (nenhum `TODO()` em runtime). Se futuros tiles ou events forem adicionados com placeholder, eles devem ser explicitamente marcados no catalog correspondente com `> **STATUS: TODO — implementação pendente. Aguarda instrução do desenvolvedor.**`.

Uma LLM **não deve tentar inferir ou inventar** a implementação de um Renderer ou Runner marcado como TODO. Aguardar instrução explícita.

---

## Extensão por Bibliotecas Externas

Para times que adicionam tiles/events **customizados** em um projeto que depende do Mosaic (sem modificar o core do framework), use a skill `skill/mosaic-client/`. Ela documenta o fluxo completo de extensão via `MosaicDependencyInjectionConfig.tileDefinitions` / `eventDefinitions`, `MosaicSerializer(tileSerializers=..., eventSerializers=...)`, e quais mecanismos explícitos/implícitos do runtime (ver `architecture.md` §4/§5) um `TileRenderer`/`EventRunner` customizado realmente consegue usar.

---

## Boilerplate — templates prontos

Templates de referência para geração de código de novo Tile/Event. Usar estes templates é muito mais rápido do que explorar o projeto do zero.

Substitua `[Name]` pelo nome do componente (ex: `TransformData`), `[name]` pela variante snake_case do pacote (ex: `transform_data`), e `[package]` pelo subpacote (`data`, `event`, `navigation`, `grouping`, etc.).

> Templates validados contra `TriggerEventEvent*` e `ColumnTile*`/`ButtonTile*`. Se algo não compilar, confira essas implementações reais primeiro.
> Para a API completa de `EventRunningScope`, `TileRenderingScope` e `BuilderScope`, veja [`architecture.md`](.claude/context/architecture.md) §3–§4.

### Regras de uso

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

6. **Nunca usar `snapshotLocals()` dentro de um lambda lazy.** Se um builder precisa avaliar um sub-scope fora de `build()`, capturar o snapshot **antes** de `addBuilder()` — ver `BuildContext` em `architecture.md` §4.
7. **Sempre atualizar catálogo, KDoc e skill após gerar boilerplate — ver Regra 8.** Nenhuma exceção, mesmo para um `TODO()` inicial: o placeholder também precisa aparecer documentado como pendente (ver "Tiles e Events com Implementação Pendente" acima).

---

### Evento — 5 arquivos + 2 registros

#### Estrutura de pacotes

```
mosaic-core/.../schemas/event/events/[package]/[Name]EventSchema.kt         ← criar o schema aqui (ponto de partida)
mosaic-server/.../builder/event/builders/[package]/[Name]EventBuilder.kt    ← server DSL
mosaic-client/.../implementations/event/events/[package]/[name]/
    [Name]EventDefinition.kt
    [Name]EventHolder.kt
    [Name]EventHolderBuilder.kt
    [Name]EventRunner.kt
```

#### `[Name]EventBuilder.kt` — mosaic-server

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

#### `[Name]EventDefinition.kt` — mosaic-client

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

#### `[Name]EventHolder.kt` — mosaic-client

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

#### `[Name]EventHolderBuilder.kt` — mosaic-client

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
            events = events.buildEventHolders()
        )
    }
}
```

#### `[Name]EventRunner.kt` — mosaic-client

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
> Padrão assíncrono/IO (eventos como GetData, EvaluateData) — `runEvent` já é `suspend`, basta envolver o corpo em `withContext(Dispatchers.IO)`, sem wrapper especial nenhum:
> ```kotlin
> override suspend fun EventRunningScope.runEvent(event: [Name]EventSchema) {
>     withContext(Dispatchers.IO) {
>         val result = runCatching { /* lógica */ }
>             .getOrElse {
>                 onTrigger(EventTriggers.onFailure(), data = it)
>                 return@withContext
>             }
>         onTrigger(EventTriggers.onSuccess(), data = result)
>     }
> }
> ```

#### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

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

#### Registro 2 — `MosaicModules.kt` (mosaic-client)

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

### Tile — 5 arquivos + 2 registros

#### Estrutura de pacotes

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

#### `[Name]TileSchemaBuilder.kt` — mosaic-server

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

#### `[Name]TileDefinition.kt` — mosaic-client

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

#### `[Name]TileHolder.kt` — mosaic-client

Listas do holder são `MutableList`. Tile folha:

```kotlin
package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.[package].[name]

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.[package].[Name]TileSchema

class [Name]TileHolder(
    override val id: String,
    override var tile: [Name]TileSchema,
    override val events: MutableList<EventHolder<*>>,
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

#### `[Name]TileHolderBuilder.kt` — mosaic-client

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
            events = events.buildEventHolders(),
            // container: tiles = tiles.buildTileHolders()
        )
    }
}
```

#### `[Name]TileRenderer.kt` — mosaic-client

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

#### Registro 1 — `MosaicSerializer.kt` (mosaic-core)

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

#### Registro 2 — `MosaicModules.kt` (mosaic-client)

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

### `EventRunningScope` — API rápida

```kotlin
incomingData: Any?                              // dado que chegou do evento anterior
onTrigger(EventTriggers.onSuccess(), data = x) // dispara eventos filhos com o trigger dado
runEventInline(eventSchema, data = x)          // executa um EventSchema arbitrário imediatamente
runEventsInline(eventSchemas, data = x)        // idem, para uma lista
broadcastData(data)                             // envia BroadcastData para os tiles da screen
// runEvent já é suspend — para IO, envolva o corpo em withContext(Dispatchers.IO) { }, sem wrapper especial
get<SomeUseCase>()                             // injeção via Koin (também getOrNull / getAll)
incomingData.asMapAny()                        // cast para Map<String, AnySerializable>?
incomingData.asMapString()                     // cast para Map<String, String>?
logError(throwable = t, tag = "...")           // loga erro via MosaicLogger
log(level, msg)                                // log genérico

// Propriedades para manipular a screen:
tilesEditor / tilesEventDispatcher / tilesOverlaysEditor / tilesValueProducer
screenDataHolder / screenBehaviorsHolder
```

### `TileRenderingScope` — API rápida

```kotlin
triggerEvent(EventTriggers.onClick(), data = x) // dispara os events do tile com o trigger dado
dispatchEvent(tileEvent)                        // TileEvent direcionado a este tile
dispatchGroupEvent(tileGroupEvent)              // TileGroupEvent para um grupo
RenderChild(tileSchema)                         // renderiza um filho
RenderChildren(tileSchemas)                     // renderiza lista de filhos
```

### Tipos concretos de `AnySerializable` em runtime

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

---

## Common Commands

```bash
# Rodar o sample server (Ktor, porta 9090)
./gradlew sample-server:run

# Rodar o sample client (JVM Desktop)
./gradlew sample-client:run

# Build de todos os módulos
./gradlew build

# Build de um módulo específico
./gradlew mosaic-core:build
./gradlew mosaic-client:build
./gradlew mosaic-server:build
```

---

## Extended Reference

Documentação detalhada em `.claude/context/`:

| File | Content |
|---|---|
| `architecture.md` | O que é o Mosaic, pipelines de Tile/Event, mecanismos explícitos e implícitos do runtime, camada de dados, plataformas. |
| `tiles-catalog.md` | Catálogo completo dos 48 `TileSchema`s: parâmetros, snippet de DSL, triggers disparados, notas de comportamento. |
| `events-catalog.md` | Catálogo completo dos 67 `EventSchema`s: parâmetros, snippet de DSL, triggers disparados, notas de comportamento. |

E as 3 skills (`skill/mosaic/`, `skill/mosaic-client/`, `skill/mosaic-server/`) — ver "Extensão por Bibliotecas Externas" acima para quando usar cada uma.
