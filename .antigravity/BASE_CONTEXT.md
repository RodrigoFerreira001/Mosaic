# Contexto Base do Projeto Mosaic

Este arquivo contém o contexto central e a arquitetura do projeto **Mosaic**. Ele foi criado para fornecer a qualquer IA ou LLM uma compreensão rápida e profunda sobre do que se trata o projeto, como ele está estruturado e quais as tecnologias envolvidas.

## Visão Geral do Projeto

O **Mosaic** é um framework de **Server-Driven UI (SDUI)** desenvolvido em **Kotlin Multiplatform (KMP)**. Seu objetivo principal é permitir que a interface do usuário (UI) e a lógica de navegação/eventos de aplicativos sejam controladas inteiramente pelo backend.
Isso significa que o backend envia um payload (geralmente em JSON) descrevendo a estrutura da tela, componentes (tiles) e ações (eventos), e o cliente (aplicativo) é responsável apenas por interpretar esse payload e renderizar a UI usando **Compose Multiplatform**.

## Stack Tecnológico Principal

- **Linguagem:** Kotlin, Kotlin Multiplatform
- **UI:** Compose Multiplatform (suporte a Android, iOS, Desktop/JVM e Web/WasmJs)
- **Rede / Backend:** Ktor (Client no frontend, Server no backend)
- **Injeção de Dependências:** Koin
- **Serialização:** Kotlinx Serialization (JSON)
- **Concorrência:** Kotlin Coroutines
- **Imagens:** Coil 3 (Network e Compose)
- **Navegação:** Navigation 3 (Adaptive e ViewModel Navigation)

---

## Módulos do Projeto

A arquitetura do projeto é dividida em 5 submódulos principais, separando claramente as responsabilidades de modelos, construtores (DSL) e renderizadores:

### 1. `mosaic-core`
É a fundação do projeto. Contém as definições de domínio compartilhadas entre o backend e o frontend.
- **Responsabilidades:** Define os **Schemas** (ex: `TileSchema`, `EventSchema`), os modelos de dados base, serializadores (`kotlinx.serialization`) genéricos e extensões comuns centrais.
- **Importância:** Qualquer novo componente visual ou evento lógico precisa ter seu "contrato" (Schema) definido aqui para que tanto o servidor possa construí-lo quanto o cliente possa entendê-lo.

### 2. `mosaic-server`
É o módulo utilizado pelos backends que desejam fornecer telas no padrão Mosaic.
- **Responsabilidades:** Fornece uma **Kotlin DSL (Domain Specific Language)** "Type-safe" para construir as árvores de UI e fluxos de eventos no servidor. Contém pacotes como `builder/event`, `builder/tile`, `builder/screen`.
- **Exemplo de Uso Prático:** Permite ao backend escrever códigos parecidos com `screen { column { text("Olá") } }` que são traduzidos usando os schemas do `mosaic-core` e serializados em JSON.

### 3. `mosaic-client`
É o motor de renderização (UI Engine) consumido pelos aplicativos front-end.
- **Responsabilidades:** Faz o parse do payload JSON (recebido via rede ou local) e dinamicamente renderiza os componentes utilizando **Compose Multiplatform**. Contém interpretação de schemas, transformando `TileSchema` em `@Composable`, executando lógicas de `EventSchema` através de `EventRunners`, e gerenciamento de estado e navegação.
- **Estrutura:** Possui pacotes para componentes base (`composables`), efeitos (`effects`), modificadores (`modifiers`), e processamento principal do SDUI (`sdui`).

### 4. `sample-server`
Um aplicativo backend de exemplo.
- **Responsabilidades:** Implementado usando **Ktor Server**, demonstra como instanciar um servidor que retorna as telas SDUI construídas com o `mosaic-server`.
- **Estrutura:** Possui um `Application.kt` e configurações para rotas que disparam o JSON do SDUI.

### 5. `sample-client`
Um aplicativo frontend de exemplo (suportando Android e JVM/Desktop).
- **Responsabilidades:** Inicializa o motor do Mosaic usando o composable `MosaicApplication`, apontando as chamadas de rede para a base URL do `sample-server` local (ex: localhost:8080).

---

## Fluxo de Trabalho e Padrão de Arquitetura (LLM, preste atenção)

**Regras de Ouro (CRÍTICAS PARA DEVs E LLMs):**
- **NUNCA tire implementações da sua própria cabeça.** A arquitetura do Mosaic é rígida e consolidada.
- **SEMPRE abra e estude TODOS os arquivos correspondentes de um exemplo existente** ANTES de criar equivalentes para uma nova feature.
  - Se for um **Evento**, estude um exemplo como `TriggerEventEventDefinition`, `TriggerEventEventHolder`, `TriggerEventEventHolderBuilder`, e `TriggerEventEventRunner`.
  - Se for um **Tile**, estude um exemplo como `ButtonTileDefinition`, `ButtonTileHolder`, `ButtonTileHolderBuilder`, e `ButtonTileRenderer` (busque exemplos na pasta `ui/.../implementations/tile/tiles/`).
- **Baseie-se FORTEMENTE na estrutura do exemplo lido para implementar sua solução:**
  - Estude os arquivos abertos para ter uma compreensão profunda de como os mecanismos funcionam e como a arquitetura está interligada.
  - Siga a organização dos pacotes e a divisão de responsabilidades.
  - Observe quais construtores, tipos Kotlin (`object` vs `class`) e padrões de herança estão sendo utilizados para guiar a sua implementação de forma coerente com o projeto.
- **Atenção a Cenários Complexos:** Alguns `TileSchemas` ou `EventSchemas` possuem uma definição mais complexa. Por exemplo, o `GetDataEventBuilder` possui uma lista de *readings*, justificando a criação de um builder específico para eles. Outro exemplo é o `MenuTileSchemaBuilder`, que possui uma lista de `MenuItem` com builders internos. **A Regra:** Sempre que houverem cenários não óbvios como estes, a LLM *nunca* deve agir por conta própria. Ela sempre deve analisar, sugerir a abordagem ao desenvolvedor, e apenas com a confirmação dele deve gerar código.

Sempre que precisar criar ou alterar funcionalidades no Mosaic (Tiles ou Events), siga rigorosamente o roteiro abaixo:

### Cadastro de um Novo Event (EventSchema)

1. **`mosaic-core`**:
   - Criar o `EventSchema` estendendo a interface base apropriada.
   - **Registrar o Schema** no `MosaicSerializer` conforme os outros Events.

2. **`mosaic-server`**:
   - Criar o `EventSchemaBuilder` e o método de extensão, conforme os outros events.

3. **`mosaic-client`**:
   - Procurar o diretório correto (ex: `ui/sdui/implementations/event/events/`).
   - Criar o `EventDefinition`, `EventRunner`, `EventHolder` e `EventHolderBuilder`, **SEGUINDO ESTRITAMENTE** a estrutura de outro evento existente (como `TriggerEventEvent`). Note que Definition, Runner e HolderBuilder frequentemente são `object` estáticos com base em extensões, enquanto apenas o Holder é uma `class`.
   - Adicionar os imports corretos (não invente sub-pacotes inexistentes).
   - No `EventRunner`, **deixar apenas um `println()` ou placeholder na implementação específica do receiver**. Nunca tentar implementar a lógica final na raiz.

### Cadastro de um Novo Tile (TileSchema)

1. **`mosaic-core`**:
   - Criar o `TileSchema` estendendo a interface base apropriada.
   - **Registrar o Schema** no `MosaicSerializer` conforme os outros Tiles.

2. **`mosaic-server`**:
   - Criar o `TileSchemaBuilder` e o método de extensão, conforme os outros tiles.

3. **`mosaic-client`**:
   - Procurar o diretório correto (ex: `ui/sdui/implementations/tile/tiles/`).
   - Criar o `TileDefinition`, `TileRenderer`, `TileHolder` e `TileHolderBuilder`, **SEGUINDO ESTRITAMENTE** a estrutura de outro tile existente. Assim como nos eventos, estude os arquivos base primeiro.
   - Adicionar os imports corretos (não invente sub-pacotes inexistentes).
   - No `TileRenderer`, **deixar apenas um `println()` ou placeholder no método de Render**. Nunca tentar implementar a UI visual final para o component.
