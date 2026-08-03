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
import dev.catbit.mosaic.server.builder.event.builders.file.OpenFilePicker
import dev.catbit.mosaic.server.builder.event.builders.file.fileFileType
import dev.catbit.mosaic.server.builder.event.builders.file.platformFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object OpenFilePickerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "OpenFilePicker"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Abre o seletor de arquivos do sistema, permitindo ao usuário escolher um " +
                    "arquivo — upload, anexo, importação de qualquer tipo."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra qualquer fluxo que exija o usuário escolher um arquivo do dispositivo. fileType " +
                    "restringe as opções mostradas no seletor (imagem, vídeo, ambos, ou uma lista de " +
                    "extensões via fileFileType). outputType controla como o conteúdo chega no incomingData — " +
                    "platformFile() (padrão) só entrega a referência, sem ler nada, ideal pra encadear direto " +
                    "com SendFile/SaveFile sem carregar tudo na memória primeiro."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("fileType", "FileType", "Obrigatório. imageFileType(), videoFileType(), imageAndVideoFileType() ou fileFileType(vararg types)."),
                    ShowroomParam("pickMode", "PickMode", "singlePickMode() (padrão, única opção suportada hoje)."),
                    ShowroomParam("outputType", "FileOutputType", "platformFile() (padrão), arrayOfBytes(), flowOfBytes(), mapObject() ou base64()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                OpenFilePicker(
                    trigger = EventTriggers.onClick(),
                    fileType = imageFileType(),
                    events = {
                        SendFile(trigger = EventTriggers.onSuccess(), url = "/api/upload/avatar", method = HttpMethod.POST)
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Selecione um arquivo pdf, png ou txt") {
                SimpleText(
                    id = "open_file_picker_status",
                    text = "Toque no botão para abrir o seletor de arquivos."
                )
                Button(
                    text = "Selecionar arquivo (pdf, png, txt)",
                    events = {
                        OpenFilePicker(
                            trigger = EventTriggers.onClick(),
                            fileType = fileFileType("pdf", "png", "txt"),
                            outputType = platformFile(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("open_file_picker_status", inlineTileUpdateData("text" to "Arquivo selecionado ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("open_file_picker_status", inlineTileUpdateData("text" to "Seleção cancelada"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "outputType = platformFile() entrega só a referência do arquivo (PlatformFile), sem ler " +
                        "seu conteúdo — por isso este exemplo só confirma que algo foi selecionado, sem " +
                        "mostrar o conteúdo. Encadeie com SendFile ou troque para arrayOfBytes()/base64() " +
                        "quando precisar dos bytes de verdade."
                )
            }

            ShowroomRelated(
                names = listOf("SendFile", "GetImageFromGallery", "TakePicture"),
                destination = "eventDetails"
            )
        }
    }
}
