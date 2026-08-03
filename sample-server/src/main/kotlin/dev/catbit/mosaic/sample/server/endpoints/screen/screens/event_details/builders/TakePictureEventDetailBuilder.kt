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
import dev.catbit.mosaic.server.builder.event.builders.image.TakePicture
import dev.catbit.mosaic.server.builder.event.builders.image.byQuality
import dev.catbit.mosaic.server.builder.event.builders.image.pictureArrayOfBytes
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TakePictureEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TakePicture"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Abre a câmera do dispositivo pra capturar uma foto — implementação nativa " +
                    "própria do Mosaic (CameraManager) em cada plataforma, sem biblioteca de terceiros."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra qualquer fluxo que exija capturar uma foto com a câmera — avatar, digitalização de " +
                    "documento, anexo. A câmera sempre devolve PNG bruto (sem perdas); se compression for " +
                    "definido, o resultado é recodificado como WebP no tamanho/qualidade pedidos. incomingData " +
                    "vira um ByteArray (ou String base64, com pictureBase64()) pronto pra encadear com SaveFile " +
                    "ou enviar direto por rede."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("compression", "CompressionScheme?", "null (padrão). byQuality(percentual) ou byTargetSize(kb)."),
                    ShowroomParam("resize", "ImageResizeOptions?", "null (padrão, usa maxLongEdgePx=2560). Só se aplica com compression definido."),
                    ShowroomParam("outputType", "OutputType", "pictureArrayOfBytes() (padrão) ou pictureBase64()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                TakePicture(
                    trigger = EventTriggers.onClick(),
                    compression = byQuality(70f),
                    outputType = pictureArrayOfBytes(),
                    events = {
                        SaveFile(trigger = EventTriggers.onSuccess(), fileName = "avatar.webp", overrideIfExists = true)
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Tire uma foto e salve-a no armazenamento privado do app") {
                SimpleText(
                    id = "take_picture_status",
                    text = "Toque no botão para abrir a câmera."
                )
                Button(
                    text = "Tirar foto e salvar como mosaic_demo_photo.webp",
                    events = {
                        TakePicture(
                            trigger = EventTriggers.onClick(),
                            compression = byQuality(70f),
                            outputType = pictureArrayOfBytes(),
                            events = {
                                SaveFile(
                                    trigger = EventTriggers.onSuccess(),
                                    fileName = "mosaic_demo_photo.webp",
                                    overrideIfExists = true,
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update("take_picture_status", inlineTileUpdateData("text" to "Foto capturada e salva ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("take_picture_status", inlineTileUpdateData("text" to "Falha ao salvar a foto"))
                                            }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("take_picture_status", inlineTileUpdateData("text" to "Captura cancelada ou sem câmera disponível"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Use os eventos GetFile e DeleteFile pra ler ou apagar o arquivo mosaic_demo_photo.webp " +
                        "salvo aqui."
                )
            }

            ShowroomRelated(
                names = listOf("GetImageFromGallery", "SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
