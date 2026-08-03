package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tiles

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.CatalogEntry
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.routing.RoutingCall

private val tileCatalogEntries = listOf(
    CatalogEntry("SimpleText", "text_fields", "Text", "Exibe um rótulo de texto não interativo, com tipografia e cor totalmente configuráveis pelo servidor."),
    CatalogEntry("Button", "smart_button", "Buttons", "Botão Material 3 em cinco estilos visuais — filled, elevated, tonal, outlined ou text."),
    CatalogEntry("IconButton", "touch_app", "Buttons", "Botão compacto com apenas um ícone, em quatro variações de estilo Material 3."),
    CatalogEntry("FloatingActionButton", "add_circle", "Buttons", "FAB circular com um único ícone, em tamanho padrão, médio ou grande, para a ação primária da tela."),
    CatalogEntry("AssistChip", "label", "Chips", "Chip para ações inteligentes e automatizadas que atravessam múltiplos apps, com ícones opcionais."),
    CatalogEntry("FilterChip", "filter_alt", "Chips", "Chip com estado selecionado alternável, usado para filtrar o conteúdo de uma lista."),
    CatalogEntry("InputChip", "input", "Chips", "Chip que representa uma informação discreta escolhida pelo usuário, como uma tag ou contato."),
    CatalogEntry("SuggestionChip", "lightbulb", "Chips", "Chip para exibir sugestões geradas dinamicamente a partir do contexto da tela."),
    CatalogEntry("TextField", "edit", "Inputs", "Campo de texto filled ou outlined, com decoração rica e configuração completa de teclado."),
    CatalogEntry("Checkbox", "check_box", "Inputs", "Caixa de seleção Material 3 cujo estado marcado é totalmente controlado pelo servidor."),
    CatalogEntry("RadioButton", "radio_button_checked", "Inputs", "Botão de opção único; vários com o mesmo groupId formam um grupo de seleção exclusiva."),
    CatalogEntry("Switch", "toggle_on", "Inputs", "Interruptor liga/desliga Material 3 com estado totalmente controlado pelo servidor."),
    CatalogEntry("Icon", "emoji_symbols", "Images and Icons", "Renderiza um único ícone Material Symbol a partir de um IconSchema."),
    CatalogEntry("Image", "image", "Images and Icons", "Exibe uma imagem estática empacotada no app, resolvida por nome de recurso."),
    CatalogEntry("AsyncImage", "cloud_download", "Images and Icons", "Carrega uma imagem via Coil a partir de URL, bytes brutos ou base64, com placeholders."),
    CatalogEntry("Column", "view_agenda", "Containers", "Empilha filhos verticalmente, com scroll opcional e filtragem de filhos por termo de busca."),
    CatalogEntry("LazyColumn", "list", "Containers", "Lista vertical preguiçosa que só compõe os itens visíveis; cada filho é mantido pela sua id."),
    CatalogEntry("Row", "view_column", "Containers", "Posiciona filhos lado a lado horizontalmente, como um Row do Compose."),
    CatalogEntry("LazyRow", "reorder", "Containers", "Lista horizontal preguiçosa que só compõe os itens visíveis no viewport."),
    CatalogEntry("Box", "layers", "Containers", "Sobrepõe filhos uns sobre os outros, útil para badges sobre ícones ou overlays centralizados."),
    CatalogEntry("Card", "dashboard", "Containers", "Agrupa conteúdo numa superfície Material 3 com elevação, forma e cor do tema."),
    CatalogEntry("Grid", "grid_view", "Containers", "Layout bidimensional ao estilo CSS grid, com colunas e linhas definidas como trilhas tipadas."),
    CatalogEntry("FlexBox", "space_dashboard", "Containers", "Container flexível ao estilo CSS flexbox, com controle de direção, alinhamento e quebra de linha."),
    CatalogEntry("FlowRow", "wrap_text", "Containers", "Linha que quebra automaticamente para novas linhas quando os filhos excedem a largura disponível."),
    CatalogEntry("Pager", "swipe", "Containers", "Pager horizontal deslizável em que cada filho ocupa uma página inteira da tela."),
    CatalogEntry("Carousel", "view_carousel", "Containers", "Carrossel horizontal de cards navegáveis, em modo multi-browse ou uncontained."),
    CatalogEntry("Shimmer", "blur_on", "Containers", "Aplica um efeito de brilho animado sobre os filhos enquanto o conteúdo real ainda carrega."),
    CatalogEntry("PullToRefresh", "refresh", "Containers", "Envolve os filhos com o gesto de puxar-para-atualizar Material 3 e seu indicador giratório."),
    CatalogEntry("AdaptiveVisibility", "visibility", "Containers", "Mostra os filhos somente quando a janela atende às restrições de breakpoint configuradas."),
    CatalogEntry("LazyTiles", "cloud_sync", "Containers", "Container autocarregável que busca seus próprios filhos de um endpoint remoto na primeira composição."),
    CatalogEntry("TopAppBar", "web_asset", "App Bars", "Barra superior Material 3 em quatro estilos: default, center-aligned, medium ou large colapsável."),
    CatalogEntry("BottomAppBar", "vertical_align_bottom", "App Bars", "Barra inferior com ações à esquerda e um FAB opcional ancorado à direita."),
    CatalogEntry("Tabs", "tab", "Navigation", "Linha de abas Material 3 em quatro variantes, com ou sem rolagem horizontal."),
    CatalogEntry("NavigationBar", "dock_to_bottom", "Navigation", "Barra de navegação inferior com um conjunto fixo de destinos, ideal para telas pequenas."),
    CatalogEntry("NavigationRail", "vertical_split", "Navigation", "Trilho de navegação vertical com cabeçalho e rodapé opcionais, ideal para telas médias."),
    CatalogEntry("NestedNavigationGraph", "account_tree", "Navigation", "Hospeda um back-stack completo de telas Mosaic embutido dentro da tela atual."),
    CatalogEntry("CircularProgressIndicator", "progress_activity", "Progress Indicators", "Indicador circular Material 3, determinado quando há progress ou giratório quando indeterminado."),
    CatalogEntry("LinearProgressIndicator", "linear_scale", "Progress Indicators", "Indicador linear Material 3, determinado ou animado conforme o valor de progress."),
    CatalogEntry("Badge", "notifications", "Badge", "Pequeno indicador colorido — ponto ou pílula com texto — para contagens e notificações."),
    CatalogEntry("SearchBar", "search", "Search", "Campo de busca com estilo de superfície Material 3, ícone líder e botão de limpar animado."),
    CatalogEntry("Menu", "menu", "Menu", "Sobrepõe um DropdownMenu ancorado ao conteúdo filho, mostrado ou ocultado via estado expanded."),
    CatalogEntry("Popup", "picture_in_picture", "Popup", "Conteúdo livre exibido sobre o anchor, sem a lista fixa de itens de um Menu."),
    CatalogEntry("DropdownList", "arrow_drop_down_circle", "Inputs", "Menu suspenso Material 3 com lista de opções selecionáveis, filled ou outlined."),
    CatalogEntry("DatePicker", "calendar_month", "Inputs", "Campo que abre um DatePickerDialog para selecionar uma data ao ser tocado."),
    CatalogEntry("TimePicker", "schedule", "Inputs", "Campo que abre um TimePickerDialog para selecionar um horário ao ser tocado."),
    CatalogEntry("SystemBroadcastListener", "sensors", "System", "Container transparente que escuta broadcasts do sistema e dispara eventos ao recebê-los."),
)

// Preserves first-seen order, same grouping m3.material.io uses to label its component grid
// ("Buttons", "Navigation", "All other components") above each row of cards.
private val tileCatalogEntriesByCategory = tileCatalogEntries.groupBy { it.category }

// Cycled per card thumbnail — same role as the varied blob/illustration colors on m3.material.io cards.
private val catalogThumbAccents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

/**
 * A single catalog card in the "Components"-grid style: a full-width colored thumbnail (not a
 * small inline icon), a title, and the tile's actual one-line purpose below — mirrors
 * m3.material.io's `.thumb-container` + `.title`/`.description` card anatomy.
 */
private fun TileSchemaBuilderScope.TileCatalogCard(
    entry: CatalogEntry,
    accentIndex: Int,
    id: String = randomId(),
) {
    val (thumbColor, onThumbColor) = catalogThumbAccents[accentIndex % catalogThumbAccents.size]
    Card(
        id = id,
        style = {
            size(width = weightHorizontally(1f), height = wrapVertically())
            clip(roundedCornerShape(all = 20))
            background(color(themeColorSurfaceContainer()))
        },
        events = {
            TransformData(
                trigger = EventTriggers.onClick(),
                template = mapOf("event" to entry.name),
                events = {
                    SetIncomingDataToNetworkParamsHolderQueryParameters(
                        trigger = EventTriggers.onSuccess(),
                        events = {
                            Navigate(
                                trigger = EventTriggers.onSuccess(),
                                navigatorId = "root",
                                destination = "tileDetails"
                            )
                        }
                    )
                }
            )
        }
    ) {
        Column(
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
            }
        ) {
            Box(
                alignment = alignToCenter(),
                style = {
                    size(width = fillHorizontally(), height = fixedVertically(72))
                    background(color(thumbColor))
                }
            ) {
                Icon(
                    icon = icon(
                        name = entry.icon,
                        size = 30,
                        color = color(onThumbColor)
                    )
                )
            }
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    padding(horizontal = 12, vertical = 10)
                },
                arrangement = arrangeVerticallySpacedBy(4)
            ) {
                SimpleText(
                    text = entry.name,
                    typography = typographyTitleMedium()
                )
                SimpleText(
                    text = entry.description,
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant())
                )
            }
        }
    }
}

object TilesScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "tiles"

    override suspend fun RoutingCall.build() = Screen(id = "tiles") {
        Column(
            id = "tiles_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 16)
            },
            arrangement = arrangeVerticallySpacedBy(16)
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as the
            // colorful collage that sits beside/behind every m3.material.io hero title.
            Column(
                id = "tiles_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorTertiaryContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(90), height = fixedVertically(90))
                            clip(circleShape())
                            background(color(themeColorPrimaryContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(120), height = fixedVertically(120))
                            clip(circleShape())
                            background(color(themeColorSecondaryContainer()))
                            margin(bottom = 8, end = 8)
                        }
                    ) {}
                }
                Column(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(horizontal = 24, top = 20, bottom = 24)
                    },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    SimpleText(
                        text = "Tiles",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Tiles são os blocos de construção interativos usados para compor qualquer tela: " +
                            "texto, botões, inputs, containers de layout, navegação e indicadores de progresso. " +
                            "Cada um é descrito no servidor como um schema tipado e renderizado no cliente com Compose Multiplatform.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            SearchBar(
                id = "tiles_search",
                placeholder = "Buscar tiles",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                },
                events = {
                    TransformData(
                        trigger = EventTriggers.onQueryChanged(),
                        template = mapOf("filterChildrenByTerm" to "<||>"),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onSuccess(),
                                updates = {
                                    // filterChildrenByTerm only filters DIRECT children, so it must be
                                    // pushed to every per-category column, not to the outer list.
                                    tileCatalogEntriesByCategory.keys.forEach { category ->
                                        update(
                                            tileId = "tiles_category_$category",
                                            updateData = incomingTileUpdateData()
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
            Column(
                id = "tiles_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                },
                arrangement = arrangeVerticallySpacedBy(24),
                scrollable = true
            ) {
                tileCatalogEntriesByCategory.forEach { (category, entries) ->
                    Column(
                        id = "tiles_category_$category",
                        style = {
                            size(width = fillHorizontally(), height = wrapVertically())
                        },
                        arrangement = arrangeVerticallySpacedBy(8)
                    ) {
                        SimpleText(
                            text = category,
                            typography = typographyHeadlineSmall(),
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                padding(start = 4, bottom = 4)
                            }
                        )
                        // Two cards per row, mirroring the m3.material.io component grid. Each
                        // row's searchableTerms combines both cards' terms since
                        // filterChildrenByTerm only inspects direct children of this column.
                        entries.chunked(2).forEach { pair ->
                            Row(
                                searchableTerms = pair.flatMap { listOfNotNull(it.name, it.category) },
                                style = {
                                    size(width = fillHorizontally(), height = wrapVertically())
                                },
                                arrangement = arrangeHorizontallySpacedBy(8)
                            ) {
                                pair.forEachIndexed { pairIndex, entry ->
                                    TileCatalogCard(
                                        entry = entry,
                                        accentIndex = tileCatalogEntries.indexOf(entry) + pairIndex
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
