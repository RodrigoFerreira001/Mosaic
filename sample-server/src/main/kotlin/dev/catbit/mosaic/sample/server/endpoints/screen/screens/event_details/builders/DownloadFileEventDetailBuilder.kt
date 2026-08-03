package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.networking.DownloadFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DownloadFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DownloadFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Baixa um arquivo de uma URL direto para o armazenamento público/geral do dispositivo — a pasta Downloads visível no gerenciador de arquivos do SO."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para qualquer fluxo de \"salvar este download em algum lugar que o usuário encontre\" " +
                    "— relatórios, exportações, mídia que o usuário quer manter fora do app. Se você precisa " +
                    "só dos bytes em memória, use DownloadFileToMemory; se o arquivo só precisa viver no " +
                    "sandbox privado do app, use DownloadFileToDisk."
            )
            ShowroomParagraph(
                "Este evento se comporta diferente por plataforma — uma restrição real do SO, não uma " +
                    "lacuna de implementação. Android: delegado ao DownloadManager do sistema — silencioso, " +
                    "sem diálogo, mostra notificação de download e cai na pasta Downloads pública (só GET " +
                    "sem body é suportado, limitação do DownloadManager). iOS: não existe pasta \"Downloads\" " +
                    "pública gravável pelo app — este evento abre o FileKit.openFileSaver() (um " +
                    "UIDocumentPickerViewController de exportação), exigindo um toque para escolher o " +
                    "destino; cancelar dispara onFailure() sem dado, mesma convenção do cancelamento do " +
                    "OpenFilePicker. JVM/Desktop: gravado silenciosamente em ~/Downloads. wasmJs/Web: aciona " +
                    "o fluxo nativo de download do navegador."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("url", "String", "Obrigatório. Mesma resolução de SendNetworkRequest."),
                    ShowroomParam("method", "HttpMethod", "Obrigatório."),
                    ShowroomParam("body", "AnySerializable?", "Padrão null. No Android, DownloadManager só suporta GET sem body."),
                    ShowroomParam("headers", "Map<String, String>?", "Padrão null."),
                    ShowroomParam("targetFileName", "String", "Obrigatório. Nome de exibição (com extensão) na pasta Downloads do usuário."),
                    ShowroomParam("mimeType", "String?", "Padrão null → inferido pela extensão de targetFileName."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DownloadFile(
                    trigger = EventTriggers.onClick(),
                    url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                    method = HttpMethod.GET,
                    targetFileName = "hello-world-readme.txt",
                    mimeType = "text/plain",
                    events = {
                        UpdateTiles(trigger = EventTriggers.onDownloadProgress(), updates = {
                            update("progress_bar", incomingTileUpdateData())
                        })
                        DisplaySnackbar(trigger = EventTriggers.onSuccess(), message = "Salvo em Downloads")
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Download cancelado ou falhou")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Download real — dispara o comportamento nativo da plataforma do dispositivo") {
                SimpleText(
                    id = "download_file_status_text",
                    text = "Toque no botão para baixar um arquivo real para a pasta Downloads."
                )
                Button(
                    text = "Baixar hello-world-readme.txt",
                    events = {
                        DownloadFile(
                            trigger = EventTriggers.onClick(),
                            url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                            method = HttpMethod.GET,
                            targetFileName = "hello-world-readme.txt",
                            mimeType = "text/plain",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onStart · baixando..."))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onDownloadProgress(),
                                    updates = {
                                        update(
                                            tileId = "download_file_status_text",
                                            updateData = mappedIncomingTileUpdateData("text" to "onDownloadProgress · <//>%")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onSuccess · salvo em Downloads"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onFailure · cancelado ou erro"))
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "A interação real depende do dispositivo — no Android o download acontece silenciosamente " +
                    "via notificação do sistema; no iOS abre um seletor de destino nativo."
            )

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
