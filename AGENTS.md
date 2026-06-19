# Mosaic — Project Rules & Context

> Este arquivo substitui `CLAUDE.md`. É a fonte de verdade para AI agents trabalhando neste repositório.

## Project Overview

**Mosaic** é um framework de **Server-Driven UI (SDUI)** construído com **Kotlin Multiplatform (KMP)**. Seu propósito central é permitir que o backend controle completamente a estrutura da UI, navegação e lógica de eventos em runtime.

O backend descreve uma tela como uma árvore de componentes tipados (Tiles) e interações (Events), serializa para JSON e envia via HTTP. O cliente deserializa esse JSON e renderiza a UI dinamicamente usando **Compose Multiplatform** — sem necessitar de uma nova release.

---

## Module Structure

| Module | Role |
|---|---|
| `mosaic-core` | Foundation compartilhada. Define todos os Schemas (contratos), modelos base, respostas e o serializer polimórfico (`MosaicSerializer`). Usado por server e client. |
| `mosaic-server` | DSL Kotlin type-safe para construir árvores de UI no backend. Contém Builders para Tiles, Events, Screens e Graphs. |
| `mosaic-client` | Engine de renderização de UI para apps cliente. Deserializa JSON em árvores `TileSchema` e renderiza como Composables. Executa lógica `EventSchema` via `EventRunner`s. |
| `sample-server` | Implementação Ktor de referência. Mostra como servir telas SDUI usando `mosaic-server`. |
| `sample-client` | App cliente de referência (Android + Desktop). Bootstrap via `MosaicApplication(baseUrl, colorScheme)`. |

---

## Technology Stack

- **Language:** Kotlin 2.x, Kotlin Multiplatform
- **UI:** Compose Multiplatform (Android, iOS, Desktop/JVM, Web/WasmJs)
- **Networking:** Ktor Client (client) / Ktor Server (server)
- **Serialization:** Kotlinx Serialization (JSON), configuração polimórfica
- **Dependency Injection:** Koin
- **Concurrency:** Kotlin Coroutines
- **Database:** SQLDelight (multiplatform local storage)
- **Images:** Coil 3
- **Navigation:** Navigation 3 (Adaptive + ViewModel)
- **Android:** minSdk 26, compileSdk 36, JVM target 11

---

## Core Architecture: Schema → Builder → Renderer

Todo componente visual ou interação no Mosaic segue um padrão de três camadas.

### Em `mosaic-core`
- **Schema**: O contrato tipado. Anotado com `@Serializable` e `@SerialName`. Registrado em `MosaicSerializer` para encoding/decoding polimórfico de JSON.

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
Antes de implementar um novo Tile ou Event, abra e leia todos os arquivos correspondentes de uma implementação existente. Para Events, estude `TriggerEventEventRunner`. Para Tiles, estude `ButtonTileRenderer` e `ButtonTileHolder`. O arquivo `.claude/context/boilerplate-templates.md` contém templates prontos — use-o como ponto de partida.

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

**Regra 8. Após criar ou modificar Schema, EventRunner, TileRenderer ou Builder: atualizar a documentação.**
- Novo TileSchema → adicionar entrada em `.claude/context/tiles-catalog.md`
- Novo EventSchema → adicionar entrada em `.claude/context/events-catalog.md`
- Novo EventTrigger → adicionar entrada em `.claude/context/triggers-catalog.md`
- Modificação de campos → atualizar a entrada correspondente no catalog
- O formato de cada entrada: nome do schema, `@SerialName` / JSON type, tabela de campos, triggers suportados (extraídos do `@Triggers` do schema e do KDoc)
- **Nunca inventar campos** — ler o schema para extrair as informações corretas

---

## Workflow: Adding a New Event

1. **`mosaic-core`** — Criar o `EventSchema` implementando a interface base. Adicionar `@Serializable` e `@SerialName`. Registrar em `MosaicSerializer`.
2. **`mosaic-server`** — Criar o `EventSchemaBuilder` e a função DSL de extensão correspondente.
3. **`mosaic-client`** — Em `ui/sdui/implementations/event/events/`, criar:
   - `[Name]EventDefinition` (object)
   - `[Name]EventRunner` (object, com placeholder `TODO()`)
   - `[Name]EventHolder` (class)
   - `[Name]EventHolderBuilder` (object)
4. Registrar o `EventDefinition` na lista `baseEventsDefinitions` em `MosaicModules.kt`.
5. Atualizar `events-catalog.md` com a nova entrada.

## Workflow: Adding a New Tile

1. **`mosaic-core`** — Criar o `TileSchema` implementando a interface base. Adicionar `@Serializable` e `@SerialName`. Registrar em `MosaicSerializer`.
2. **`mosaic-server`** — Criar o `TileSchemaBuilder` e a função DSL de extensão correspondente.
3. **`mosaic-client`** — Em `ui/sdui/implementations/tile/tiles/`, criar:
   - `[Name]TileDefinition` (object)
   - `[Name]TileRenderer` (object, com placeholder `TODO()`)
   - `[Name]TileHolder` (class)
   - `[Name]TileHolderBuilder` (object)
4. Registrar o `TileDefinition` na lista `baseTilesDefinitions` em `MosaicModules.kt`.
5. Atualizar `tiles-catalog.md` com a nova entrada.

---

## Tiles e Events com Implementação Pendente

Todos os `EventRunner`s e `TileRenderer`s atuais têm implementação completa (nenhum `TODO()` em runtime). Se futuros tiles ou events forem adicionados com placeholder, eles devem ser explicitamente marcados no catalog correspondente com `> **STATUS: TODO — implementação pendente. Aguarda instrução do desenvolvedor.**`.

Uma LLM **não deve tentar inferir ou inventar** a implementação de um Renderer ou Runner marcado como TODO. Aguardar instrução explícita.

---

## Extensão por Bibliotecas Externas

Para times que adicionam tiles/events **customizados** em um projeto que depende do Mosaic (sem modificar o core do framework), use a skill `skill/mosaic-extension/`. A skill documenta o fluxo completo de extensão via `MosaicDependencyInjectionConfig.tileDefinitions` / `eventDefinitions` e `MosaicSerializer(tileSerializers=..., eventSerializers=...)`.

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
| `architecture.md` | Diagramas de fluxo de dados, mapa do sistema, padrões arquiteturais |
| `tiles-catalog.md` | Catálogo completo de 41 TileSchemas: campos, triggers, notas de comportamento |
| `events-catalog.md` | Catálogo completo de 49 EventSchemas: campos, triggers filhos, semântica |
| `triggers-catalog.md` | Todos os 72 EventTriggers: @SerialName, campos, quem dispara |
| `mechanisms.md` | API detalhada de cada mecanismo de runtime: EventRunningScope, TileRenderingScope, BuilderScope, DataHolder, TilesEditor, BroadcastChannel, etc. |
| `boilerplate-templates.md` | **Ler este arquivo PRIMEIRO ao gerar boilerplate.** Templates completos para Event e Tile (5 arquivos + 2 registros cada), API rápida de EventRunningScope e TileRenderingScope. |
