# Catálogo de Componentes Material 3 para Tiles

Este documento descreve os componentes do Google Material 3 que devem ser mapeados para `TileSchema` neste projeto. Cada componente inclui suas variações, uma proposta de contrato para o `TileSchema` e a respectiva função Composable de referência.

## 1. Buttons (Investimento: Alto)
Os botões permitem que os usuários tomem ações e façam escolhas com um único toque.

### Variações
1.  **Filled Button (Preenchido):** Alto impacto visual. Usado para ações finais e primárias (ex: "Salvar", "Confirmar").
2.  **Filled Tonal Button:** Prioridade média-alta. Menor que o Filled, mas maior que o Outlined. Bom para ações secundárias em contextos onde o Filled seria muito pesado.
3.  **Outlined Button (Contorno):** Ênfase média. Importante, mas não primário. Ideal para ações secundárias pareadas com um botão Filled.
4.  **Elevated Button:** Essencialmente um Filled Button com sombra (elevação) em vez de apenas cor para separação. Usado quando é necessário separar o botão de um fundo complexo.
5.  **Text Button:** Baixa prioridade. Usado em dialogs, cards e snackbars para não distrair do conteúdo principal.

### Proposta de TileSchema (`ButtonTileSchema`)
Já existente no projeto, mas pode ser expandido para suportar as variações explicitamente através de um enum `variant`.

```kotlin
@SerialName("Button")
data class ButtonTileSchema(
    // Propriedades padrão de TileSchema
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    // Propriedades Específicas
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: String? = null, // Nome do ícone (opcional)
    @SerialName("loading") val loading: Boolean = false,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("variant") val variant: ButtonVariant = ButtonVariant.FILLED
) : TileSchema

enum class ButtonVariant {
    FILLED, FILLED_TONAL, OUTLINED, ELEVATED, TEXT
}
```

### Contrato Composable (Referência)
```kotlin
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
)
```
*A função muda ligeiramente para outras variações (ex: `OutlinedButton`, `TextButton`), mas os parâmetros principais são os mesmos.*

---

## 2. Icon Buttons
Botões compactos usados para ações em barras de ferramentas, listas ou especificamente onde o espaço é limitado.

### Variações
1.  **Standard:** Apenas o ícone.
2.  **Filled:** Ícone com background preenchido (contraste alto).
3.  **Filled Tonal:** Ícone com background de tom médio.
4.  **Outlined:** Ícone com borda.

### Proposta de TileSchema (`IconButtonTileSchema`)
```kotlin
@SerialName("IconButton")
data class IconButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("icon") val icon: String, // Identificador do ícone
    @SerialName("contentDescription") val contentDescription: String?,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("variant") val variant: IconButtonVariant = IconButtonVariant.STANDARD,
    @SerialName("toggleable") val toggleable: Boolean = false, // Se true, funciona como IconToggleButton
    @SerialName("checked") val checked: Boolean = false // Apenas se toggleable=true
) : TileSchema

enum class IconButtonVariant {
    STANDARD, FILLED, FILLED_TONAL, OUTLINED
}
```

### Contrato Composable
```kotlin
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
)
```

---

## 3. Floating Action Button (FAB)
O botão de ação primária da tela. Normalmente circular ou retangular arredondado.

### Variações
1.  **FAB:** Tamanho padrão.
2.  **Small FAB:** Versão menor e compacta.
3.  **Large FAB:** Versão maior.
4.  **Extended FAB:** Contém ícone e texto (rótulo).

### Proposta de TileSchema (`FabTileSchema`)
```kotlin
@SerialName("Fab")
data class FabTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("icon") val icon: String?,
    @SerialName("text") val text: String? = null, // Obrigatório se variant == EXTENDED
    @SerialName("expanded") val expanded: Boolean = true, // Apenas para Extended FAB
    @SerialName("variant") val variant: FabVariant = FabVariant.PRIMARY
) : TileSchema

enum class FabVariant {
    PRIMARY, SECONDARY, TERTIARY, SURFACE, SMALL, LARGE, EXTENDED
}
```

### Contrato Composable (Extended)
```kotlin
@Composable
fun ExtendedFloatingActionButton(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    shape: Shape = FloatingActionButtonDefaults.extendedFabShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource? = null,
)
```

---

## 4. Cards
Containers que agrupam informações e ações relacionadas sobre um único assunto.

### Variações
1.  **Elevated Card:** Usa sombra para separação. Bom para separar do fundo.
2.  **Filled Card:** Usa cor de fundo contrastante (flat). Menor ênfase que o Elevated.
3.  **Outlined Card:** Usa borda. A ênfase mais baixa, bom para agrupar elementos sem "peso" visual.

### Proposta de TileSchema (`CardTileSchema`)
Já existe parcialmente como `CardTileSchema`, mas deve suportar o conteúdo interno (children).

```kotlin
@SerialName("Card")
data class CardTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("children") val children: List<TileSchema>, // O Card é um container
    @SerialName("variant") val variant: CardVariant = CardVariant.FILLED,
    @SerialName("enabled") val enabled: Boolean = true // Cards podem ser clicáveis
) : TileSchema

enum class CardVariant {
    ELEVATED, FILLED, OUTLINED
}
```

### Contrato Composable
```kotlin
@Composable
fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null, // Usado no Outlined
    content: @Composable ColumnScope.() -> Unit
)
```

---

## 5. Text Fields
Campos para entrada de texto do usuário.

### Variações
1.  **Filled Text Field:** Alta ênfase visual. O campo tem um background preenchido.
2.  **Outlined Text Field:** Ênfase média. O campo tem apenas borda. Reduz ruído visual em formulários longos.

### Proposta de TileSchema (`TextFieldTileSchema`)
```kotlin
@SerialName("TextField")
data class TextFieldTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?, // OnChange deve estar aqui
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("value") val value: String,
    @SerialName("label") val label: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("supportingText") val supportingText: String? = null,
    @SerialName("errorText") val errorText: String? = null, // Se presente, isError = true
    @SerialName("leadingIcon") val leadingIcon: String? = null,
    @SerialName("trailingIcon") val trailingIcon: String? = null,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("readOnly") val readOnly: Boolean = false,
    @SerialName("singleLine") val singleLine: Boolean = true,
    @SerialName("maxLines") val maxLines: Int = 1,
    @SerialName("inputType") val inputType: InputType = InputType.TEXT, // Password, Number, Email...
    @SerialName("variant") val variant: TextFieldVariant = TextFieldVariant.OUTLINED
) : TileSchema

enum class TextFieldVariant {
    FILLED, OUTLINED
}
```

### Contrato Composable (Ex: Outlined)
```kotlin
@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
)
```

---

## 6. Checkbox
Permite selecionar um ou mais itens de um conjunto.

### Variações
1.  **Unselected/Selected:** Estados binários.
2.  **Indeterminate:** Estado visual (traço em vez de check) usado geralmente em "Selecionar Tudo" quando apenas alguns filhos estão selecionados.
3.  **Error state:** Estado de erro.

### Proposta de TileSchema (`CheckboxTileSchema`)
```kotlin
@SerialName("Checkbox")
data class CheckboxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("checked") val checked: Boolean, // Ou um enum para incluir INDETERMINATE
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("label") val label: String? // Checkbox geralmente vem com texto ao lado
) : TileSchema
```

### Contrato Composable
```kotlin
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
)
```

---

## 7. Switch
Alterna o estado de uma única configuração (On/Off).

### Variações
1.  **Standard:** Switch padrão M3 (pílula maior, track mais alto).
2.  **Icon:** Pode conter ícone no "thumb" (handle).

### Proposta de TileSchema (`SwitchTileSchema`)
```kotlin
@SerialName("Switch")
data class SwitchTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("checked") val checked: Boolean,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("showIcons") val showIcons: Boolean = false, // Ícones de check/x no thumb
    @SerialName("label") val label: String? // Texto descritivo associado
) : TileSchema
```

### Contrato Composable
```kotlin
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null, // Para ícones
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
)
```

---

## 8. Sliders
Permitem aos usuários fazer seleções a partir de uma faixa de valores.

### Variações
1.  **Continuous:** Valor fluido.
2.  **Discrete:** Salta entre valores pré-definidos (com marcações).

### Proposta de TileSchema (`SliderTileSchema`)
```kotlin
@SerialName("Slider")
data class SliderTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("value") val value: Float,
    @SerialName("valueRangeStart") val valueRangeStart: Float = 0f,
    @SerialName("valueRangeEnd") val valueRangeEnd: Float = 1f,
    @SerialName("steps") val steps: Int = 0, // 0 = contínuo, >0 = discreto
    @SerialName("enabled") val enabled: Boolean = true
) : TileSchema
```

### Contrato Composable
```kotlin
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
)
```

---

## 9. Progress Indicators
Informam ao usuário que uma operação está em andamento.

### Variações
1.  **Circular:** Um anel giratório.
2.  **Linear:** Uma barra de preenchimento.
3.  **Indeterminate vs Determinate:** Indeterminado (animação infinita) ou Determinado (0 a 100%).

### Proposta de TileSchema (`ProgressTileSchema`)
```kotlin
@SerialName("Progress")
data class ProgressTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("progress") val progress: Float? = null, // null = indeterminate
    @SerialName("type") val type: ProgressType = ProgressType.CIRCULAR
) : TileSchema

enum class ProgressType {
    CIRCULAR, LINEAR
}
```

### Contrato Composable (Ex: Circular)
```kotlin
@Composable
fun CircularProgressIndicator(
    progress: () -> Float, // Para determinate
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circularColor,
    trackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
)
```

---

## 10. Chips
Permitem que usuários insiram informações, façam seleções, filtrem conteúdo ou acionem ações.

### Variações
1.  **Assist Chip:** Ações inteligentes ou sugestões (ex: ícone de calendário para criar evento).
2.  **Filter Chip:** Seleciona opções de um conjunto (pode marcar/desmarcar).
3.  **Input Chip:** Representa informação inserida (ex: nome em campo "para" de email).
4.  **Suggestion Chip:** Sugestões dinâmicas.

### Proposta de TileSchema (`ChipTileSchema`)
```kotlin
@SerialName("Chip")
data class ChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("label") val label: String,
    @SerialName("icon") val icon: String? = null,
    @SerialName("selected") val selected: Boolean = false, // Para Filter/Input
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("onClose") val canClose: Boolean = false, // Para Input, mostra botão X
    @SerialName("variant") val variant: ChipVariant = ChipVariant.ASSIST
) : TileSchema

enum class ChipVariant {
    ASSIST, FILTER, INPUT, SUGGESTION
}
```

### Contrato Composable (Ex: FilterChip)
```kotlin
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = FilterChipDefaults.shape,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
    elevation: SelectableChipElevation? = FilterChipDefaults.filterChipElevation(),
    border: SelectableChipBorder? = FilterChipDefaults.filterChipBorder(),
    interactionSource: MutableInteractionSource? = null
)
```

---

## 11. Navigation Bar (Bottom Navigation)
Navegação primária entre destinos de nível superior (3-5 itens).

### Proposta de TileSchema (`NavBarTileSchema`)
Geralmente contém uma lista de itens de navegação.

```kotlin
@SerialName("NavigationBar")
data class NavigationBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("selectedId") val selectedId: String, // ID do item ativo
    @SerialName("items") val items: List<NavBarItemSchema>
) : TileSchema

@Serializable
data class NavBarItemSchema(
    val id: String,
    val icon: String,
    val label: String?,
    val badgeCount: Int? = null
)
```

### Contrato Composable
```kotlin
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit // Recebe NavigationBarItem
)
```

---

## 12. Top App Bar
Exibe informações e ações na parte superior da tela.

### Variações
1.  **Small:** Comum.
2.  **Center Aligned:** Título centralizado.
3.  **Medium:** Título maior (colapsável).
4.  **Large:** Título muito grande (colapsável).

### Proposta de TileSchema (`TopAppBarTileSchema`)
```kotlin
@SerialName("TopAppBar")
data class TopAppBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,

    @SerialName("title") val title: String,
    @SerialName("navigationIcon") val navigationIcon: String? = null, // Ex: Menu ou Back
    @SerialName("actions") val actions: List<IconButtonTileSchema> = emptyList(),
    @SerialName("variant") val variant: TopAppBarVariant = TopAppBarVariant.SMALL,
    @SerialName("scrollBehavior") val scrollBehavior: String? = null // Pinned, EnterAlways, ExitUntilCollapsed
) : TileSchema

enum class TopAppBarVariant {
    SMALL, CENTER_ALIGNED, MEDIUM, LARGE
}
```

### Contrato Composable (Ex: Small)
```kotlin
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
)
```
