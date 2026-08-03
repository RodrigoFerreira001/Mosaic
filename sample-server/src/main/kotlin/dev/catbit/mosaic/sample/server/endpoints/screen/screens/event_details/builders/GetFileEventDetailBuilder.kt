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
import dev.catbit.mosaic.server.builder.event.builders.file.GetFile
import dev.catbit.mosaic.server.builder.event.builders.file.arrayOfBytes
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object GetFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Lê um arquivo armazenado localmente no app — o formato entregue no onSuccess " +
                    "depende do outputType escolhido."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra carregar de volta um arquivo salvo anteriormente com SaveFile ou DownloadFileToDisk. " +
                    "arrayOfBytes() lê tudo pra memória; flowOfBytes() entrega um Flow<ByteArray> em chunks, " +
                    "sem carregar o arquivo inteiro; platformFile() entrega só a referência sem ler nada; " +
                    "mapObject() decodifica o conteúdo como JSON; base64() entrega uma String codificada. " +
                    "Arquivo inexistente ou erro de I/O dispara onFailure."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("fileName", "String", "Obrigatório. Nome do arquivo no armazenamento privado do app."),
                    ShowroomParam("outputType", "FileOutputType", "arrayOfBytes() (padrão), flowOfBytes(), platformFile(), mapObject() ou base64()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                GetFile(trigger = EventTriggers.onClick(), fileName = "config.json", outputType = mapObject())
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Leia o arquivo salvo pelas demos de TakePicture/GetImageFromGallery/SaveFile") {
                SimpleText(
                    id = "get_file_status",
                    text = "Toque no botão para tentar ler mosaic_demo_photo.webp."
                )
                Button(
                    text = "Ler mosaic_demo_photo.webp",
                    events = {
                        GetFile(
                            trigger = EventTriggers.onClick(),
                            fileName = "mosaic_demo_photo.webp",
                            outputType = arrayOfBytes(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("get_file_status", inlineTileUpdateData("text" to "Arquivo encontrado e lido ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "get_file_status",
                                            inlineTileUpdateData("text" to "Não encontrado — salve uma imagem primeiro (TakePicture, GetImageFromGallery ou SaveFile)")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Se você ainda não salvou nada nas demos de TakePicture/GetImageFromGallery/SaveFile, o " +
                        "onFailure aqui é o comportamento correto e esperado — não um bug."
                )
            }

            ShowroomRelated(
                names = listOf("SaveFile", "DeleteFile", "DownloadFileToDisk"),
                destination = "eventDetails"
            )
        }
    }
}
