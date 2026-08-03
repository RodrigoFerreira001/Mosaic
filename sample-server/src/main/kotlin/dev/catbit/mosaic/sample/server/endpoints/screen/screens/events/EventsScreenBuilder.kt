package dev.catbit.mosaic.sample.server.endpoints.screen.screens.events

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

private val eventCatalogEntries = listOf(
    CatalogEntry("AddTiles", "add_box", "Tile Management", "Insere um ou mais tiles novos na lista de filhos de um container, numa posição específica, sem recarregar a tela."),
    CatalogEntry("RemoveTiles", "delete", "Tile Management", "Remove tiles específicos da lista de filhos de um container pelas suas ids."),
    CatalogEntry("UpdateTiles", "sync", "Tile Management", "Aplica patches de dados a tiles existentes sem alterar a estrutura da árvore."),
    CatalogEntry("ReplaceTiles", "swap_horiz", "Tile Management", "Substitui atomicamente toda a lista de filhos de um container por um novo conjunto."),
    CatalogEntry("WipeTiles", "delete_sweep", "Tile Management", "Remove todos os filhos de um container numa única operação, deixando-o vazio."),
    CatalogEntry("ReloadLazyTiles", "autorenew", "Tile Management", "Sinaliza um LazyColumn/LazyRow para descartar o conteúdo e buscar tiles de novo, resetando a paginação."),
    CatalogEntry("CheckIfTileContainsChildren", "checklist", "Tile Management", "Verifica de forma síncrona se um container possui todos os filhos indicados."),
    CatalogEntry("GetTileChildrenCount", "pin", "Tile Management", "Retorna o número atual de filhos diretos de um container."),
    CatalogEntry("RunEvents", "playlist_play", "Tile Management", "Executa todos os eventos filhos incondicionalmente, agrupando uma cadeia sob um único trigger."),
    CatalogEntry("UpdateEvents", "edit_note", "Tile Management", "Corrige o incomingData de eventos já registrados sem round-trip de rede."),
    CatalogEntry("Navigate", "arrow_forward", "Navigation", "Empilha um destino na pilha de um navigator, podendo remover entradas anteriores e levar dados."),
    CatalogEntry("NavigateClearingStack", "restart_alt", "Navigation", "Navega para um destino limpando toda a pilha de navegação atual."),
    CatalogEntry("NavigateUp", "arrow_back", "Navigation", "Remove o destino atual da pilha, equivalente ao botão de voltar do sistema."),
    CatalogEntry("GetScreen", "download", "Screen", "Busca a definição de uma tela no servidor e expõe o resultado como incomingData."),
    CatalogEntry("RefreshScreen", "refresh", "Screen", "Recarrega a tela atual do zero, buscando e aplicando a nova definição automaticamente."),
    CatalogEntry("ChangeScreenState", "tune", "Screen", "Transiciona a tela imediatamente para Success, Failure ou Initial sem chamada de rede."),
    CatalogEntry("SendData", "outbox", "Data", "Publica um valor no barramento DataMailer, disponível para outras telas via CheckForReceivedData."),
    CatalogEntry("CheckForReceivedData", "inbox", "Data", "Lê o barramento DataMailer por um valor e o encaminha como incomingData."),
    CatalogEntry("GetData", "download_for_offline", "Data", "Lê dados de uma ou mais fontes — tile, memória da tela, banco persistente — e agrega o resultado."),
    CatalogEntry("UpdateData", "save", "Data", "Grava dados chave-valor num ou mais data stores, em memória ou persistentes."),
    CatalogEntry("RemoveData", "delete_forever", "Data", "Apaga dados de um data store — uma chave, um lote de chaves ou o store inteiro."),
    CatalogEntry("ProcessData", "memory", "Data", "Delega o incomingData a um DataProcessor registrado no cliente para lógica nativa da plataforma."),
    CatalogEntry("TransformData", "transform", "Data", "Remodela o incomingData substituindo placeholders por valores resolvidos a partir dele mesmo."),
    CatalogEntry("EvaluateData", "rule", "Data", "Avalia uma árvore de expressão booleana e ramifica a cadeia entre onSuccess e onFailure."),
    CatalogEntry("SendNetworkRequest", "cloud", "Networking", "Realiza uma requisição HTTP e propaga a resposta pelos eventos filhos."),
    CatalogEntry("DownloadFile", "cloud_download", "Networking", "Baixa um arquivo de uma URL direto para o armazenamento público do dispositivo."),
    CatalogEntry("SendFile", "cloud_upload", "Networking", "Envia um arquivo como binário bruto para uma URL, sem multipart."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderBody", "data_object", "Networking", "Guarda o incomingData como corpo da próxima requisição de rede da cadeia."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderHeaders", "list_alt", "Networking", "Guarda o incomingData como headers da próxima requisição de rede da cadeia."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderUrl", "link", "Networking", "Guarda o incomingData como URL da próxima requisição de rede da cadeia."),
    CatalogEntry("SetIncomingDataToNetworkParamsHolderQueryParameters", "filter_list", "Networking", "Guarda o incomingData como query parameters da próxima requisição de rede da cadeia."),
    CatalogEntry("SaveFile", "save", "File System", "Salva dados num arquivo local do dispositivo."),
    CatalogEntry("GetFile", "folder_open", "File System", "Lê um arquivo armazenado localmente, moldado pelo outputType configurado."),
    CatalogEntry("DeleteFile", "delete", "File System", "Apaga um arquivo armazenado localmente identificado pelo nome."),
    CatalogEntry("OpenFilePicker", "attach_file", "File System", "Abre o seletor de arquivos do sistema para o usuário escolher um arquivo."),
    CatalogEntry("TakePicture", "photo_camera", "File System", "Abre a câmera do dispositivo para o usuário tirar uma foto."),
    CatalogEntry("GetImageFromGallery", "photo_library", "File System", "Abre a galeria do dispositivo para o usuário escolher uma imagem."),
    CatalogEntry("DisplayDialog", "open_in_new", "Overlays", "Exibe um diálogo modal com uma árvore de tiles definida pelo servidor."),
    CatalogEntry("DismissDialog", "close", "Overlays", "Fecha programaticamente o diálogo exibido no momento."),
    CatalogEntry("DisplayBottomSheet", "vertical_align_bottom", "Overlays", "Exibe um bottom sheet modal com uma árvore de tiles definida pelo servidor."),
    CatalogEntry("DismissBottomSheet", "expand_more", "Overlays", "Fecha programaticamente o bottom sheet exibido no momento."),
    CatalogEntry("DisplayNavigationDrawer", "menu_open", "Overlays", "Abre o menu de navegação lateral da tela."),
    CatalogEntry("DismissNavigationDrawer", "menu", "Overlays", "Fecha o menu de navegação lateral da tela."),
    CatalogEntry("DisplaySnackbar", "chat_bubble", "Overlays", "Exibe um snackbar Material 3 com mensagem, ação opcional e duração configurável."),
    CatalogEntry("DismissSnackbar", "cancel", "Overlays", "Fecha programaticamente o snackbar exibido no momento."),
    CatalogEntry("TriggerEvent", "bolt", "Events / Meta", "Localiza outro evento registrado pela id e o executa inline, como uma sub-rotina reutilizável."),
    CatalogEntry("RunCancellableEvents", "play_circle", "Events / Meta", "Executa uma cadeia de eventos que pode ser interrompida por um CancelEvents correspondente."),
    CatalogEntry("CancelEvents", "cancel", "Events / Meta", "Interrompe a execução de uma cadeia de eventos cancelável iniciada por RunCancellableEvents."),
    CatalogEntry("ToggleMenu", "more_vert", "Menu", "Alterna o estado aberto/fechado de um tile de Menu."),
    CatalogEntry("TogglePopup", "picture_in_picture", "Popup", "Alterna o estado aberto/fechado de um tile de Popup."),
    CatalogEntry("StartCountdownTimer", "timer", "Time", "Inicia uma contagem regressiva no cliente, disparando triggers a cada segundo e ao zerar."),
    CatalogEntry("StartTimeLoop", "loop", "Time", "Inicia um laço de tempo recorrente no cliente, disparando triggers em intervalos configurados."),
    CatalogEntry("ScrollColumnTile", "swap_vert", "Scroll", "Rola imperativamente um Column ou LazyColumn até uma posição via broadcast."),
    CatalogEntry("ScrollRowTile", "swap_horiz", "Scroll", "Rola imperativamente um Row ou LazyRow até uma posição via broadcast."),
    CatalogEntry("ScrollPagerTile", "view_carousel", "Scroll", "Navega imperativamente um Pager até uma página alvo via broadcast."),
    CatalogEntry("StopRefreshing", "check_circle", "Pull to Refresh", "Sinaliza um PullToRefresh para parar o indicador de carregamento e voltar ao estado ocioso."),
    CatalogEntry("RequestPermission", "lock", "Security", "Solicita uma ou mais permissões em tempo de execução usando o mecanismo nativo de cada plataforma."),
    CatalogEntry("BroadcastToSystem", "podcasts", "System", "Emite um broadcast nomeado, notificando qualquer assinante do app pelo broadcastId."),
    CatalogEntry("CheckIfHasInternetConnection", "wifi", "System", "Verifica se o dispositivo tem conexão ativa com a internet."),
    CatalogEntry("DropCaches", "delete_sweep", "System", "Limpa caches internos do Mosaic — dados, imagens ou telas — mantidos em memória ou disco."),
    CatalogEntry("OpenExternalLink", "open_in_new", "System", "Abre uma URL no navegador ou app externo do sistema, fora do contexto do Mosaic."),
    CatalogEntry("SetTheme", "palette", "Theme", "Sobrescreve o color scheme Material 3 do app em tempo de execução, até ser revertido."),
    CatalogEntry("ResetTheme", "format_color_reset", "Theme", "Reverte um SetTheme anterior, restaurando o color scheme padrão do app."),
)

// Preserves first-seen order, same grouping m3.material.io uses to label its component grid.
private val eventCatalogEntriesByCategory = eventCatalogEntries.groupBy { it.category }

// Cycled per card thumbnail — same role as the varied blob/illustration colors on m3.material.io cards.
private val catalogThumbAccents = listOf(
    themeColorPrimaryContainer() to themeColorOnPrimaryContainer(),
    themeColorTertiaryContainer() to themeColorOnTertiaryContainer(),
    themeColorSecondaryContainer() to themeColorOnSecondaryContainer(),
    themeColorErrorContainer() to themeColorOnErrorContainer(),
)

/**
 * A single catalog card in the "Components"-grid style: a full-width colored thumbnail, a title,
 * and the event's actual one-line purpose below — mirrors m3.material.io's `.thumb-container` +
 * `.title`/`.description` card anatomy. Mirrors [dev.catbit.mosaic.sample.server.endpoints.screen.screens.tiles.TilesScreenBuilder]'s
 * TileCatalogCard but navigates to "eventDetails".
 */
private fun TileSchemaBuilderScope.EventCatalogCard(
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
                                destination = "eventDetails"
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

object EventsScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "events"

    override suspend fun RoutingCall.build() = Screen(id = "events") {
        Column(
            id = "events_screen_root",
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
                id = "events_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorSecondaryContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(90), height = fixedVertically(90))
                            clip(circleShape())
                            background(color(themeColorTertiaryContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(120), height = fixedVertically(120))
                            clip(circleShape())
                            background(color(themeColorPrimaryContainer()))
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
                        text = "Events",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Events são o que acontece quando o usuário interage: ler dado, transformar, " +
                            "chamar uma API, navegar, atualizar outros tiles. Eles se encadeiam em cadeias, com " +
                            "o incomingData fluindo de um evento pai para os filhos a cada passo.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            SearchBar(
                id = "events_search",
                placeholder = "Buscar events",
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
                                    eventCatalogEntriesByCategory.keys.forEach { category ->
                                        update(
                                            tileId = "events_category_$category",
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
                id = "events_catalog_list",
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                },
                arrangement = arrangeVerticallySpacedBy(24),
                scrollable = true
            ) {
                eventCatalogEntriesByCategory.forEach { (category, entries) ->
                    Column(
                        id = "events_category_$category",
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
                                    EventCatalogCard(
                                        entry = entry,
                                        accentIndex = eventCatalogEntries.indexOf(entry) + pairIndex
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
