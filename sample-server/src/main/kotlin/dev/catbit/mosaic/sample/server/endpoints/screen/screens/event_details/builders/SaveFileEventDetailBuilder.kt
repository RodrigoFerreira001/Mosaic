package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
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
import dev.catbit.mosaic.server.builder.event.builders.file.SaveFile
import dev.catbit.mosaic.server.builder.event.builders.image.GetImageFromGallery
import dev.catbit.mosaic.server.builder.event.builders.image.byQuality
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SaveFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SaveFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Salva dados num arquivo local, no armazenamento privado do app — normalmente " +
                    "encadeado depois de um evento que produz um ByteArray, como TakePicture ou GetImageFromGallery."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra persistir conteúdo baixado ou capturado (foto, documento, cache de asset) no " +
                    "armazenamento privado do app. O incomingData precisa ser um ByteArray — qualquer outro " +
                    "tipo falha silenciosamente sem escrever nada. Com overrideIfExists = false, tentar salvar " +
                    "sobre um arquivo já existente falha em vez de sobrescrever."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("fileName", "String", "Obrigatório. Nome do arquivo de destino no armazenamento privado do app."),
                    ShowroomParam("overrideIfExists", "Boolean", "Obrigatório. Sobrescreve um arquivo existente quando true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DownloadFileToMemory(
                    trigger = EventTriggers.onClick(),
                    url = "/files/report.pdf",
                    method = HttpMethod.GET,
                    events = {
                        SaveFile(trigger = EventTriggers.onDownloadFinish(), fileName = "report.pdf", overrideIfExists = true)
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Escolha uma imagem e salve-a com SaveFile") {
                SimpleText(
                    id = "save_file_status",
                    text = "Toque no botão para escolher e salvar uma imagem."
                )
                Button(
                    text = "Escolher imagem e salvar",
                    events = {
                        GetImageFromGallery(
                            trigger = EventTriggers.onClick(),
                            compression = byQuality(70f),
                            events = {
                                SaveFile(
                                    trigger = EventTriggers.onSuccess(),
                                    fileName = "mosaic_demo_photo.webp",
                                    overrideIfExists = true,
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update("save_file_status", inlineTileUpdateData("text" to "Salvo com sucesso ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("save_file_status", inlineTileUpdateData("text" to "Falha ao salvar (verifique overrideIfExists)"))
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "SaveFile sozinho não faz sentido sem uma fonte de bytes antes dele na cadeia — por isso " +
                        "esta demo usa GetImageFromGallery pra produzir o ByteArray que SaveFile consome. " +
                        "Veja GetFile e DeleteFile pra ler ou remover o arquivo salvo aqui."
                )
            }

            ShowroomRelated(
                names = listOf("GetFile", "DeleteFile", "GetImageFromGallery"),
                destination = "eventDetails"
            )
        }
    }
}
