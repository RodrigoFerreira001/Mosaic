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
import dev.catbit.mosaic.server.builder.event.builders.image.galleryArrayOfBytes
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object GetImageFromGalleryEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetImageFromGallery"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Abre a galeria do dispositivo pra escolher uma imagem — usa o Photo Picker do " +
                    "sistema, sem exigir permissão de storage."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra qualquer fluxo de seleção de imagem da galeria — avatar, upload, importação. Como " +
                    "usa o Photo Picker nativo (Android PickVisualMedia / iOS PHPickerViewController), o app " +
                    "só recebe acesso à imagem escolhida, sem precisar de RequestPermission(GALLERY) nem " +
                    "entrada no manifest. compression/resize seguem o mesmo contrato de TakePicture — sem " +
                    "compression, a imagem volta no formato original; com compression, sai recodificada como " +
                    "WebP."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("compression", "CompressionScheme?", "null (padrão). byQuality(percentual) ou byTargetSize(kb)."),
                    ShowroomParam("resize", "ImageResizeOptions?", "null (padrão). Só se aplica com compression definido."),
                    ShowroomParam("outputType", "OutputType", "galleryArrayOfBytes() (padrão) ou galleryBase64()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                GetImageFromGallery(
                    trigger = EventTriggers.onClick(),
                    compression = byQuality(70f),
                    events = {
                        SaveFile(trigger = EventTriggers.onSuccess(), fileName = "avatar.webp")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Escolha uma imagem da galeria e salve-a") {
                SimpleText(
                    id = "gallery_image_status",
                    text = "Toque no botão para abrir a galeria."
                )
                Button(
                    text = "Escolher da galeria e salvar como mosaic_demo_photo.webp",
                    events = {
                        GetImageFromGallery(
                            trigger = EventTriggers.onClick(),
                            compression = byQuality(70f),
                            outputType = galleryArrayOfBytes(),
                            events = {
                                SaveFile(
                                    trigger = EventTriggers.onSuccess(),
                                    fileName = "mosaic_demo_photo.webp",
                                    overrideIfExists = true,
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update("gallery_image_status", inlineTileUpdateData("text" to "Imagem escolhida e salva ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("gallery_image_status", inlineTileUpdateData("text" to "Falha ao salvar a imagem"))
                                            }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("gallery_image_status", inlineTileUpdateData("text" to "Seleção cancelada"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Mesmo arquivo mosaic_demo_photo.webp usado na demo de TakePicture — escolher aqui " +
                        "sobrescreve o que uma foto de câmera tiver salvo antes, já que overrideIfExists = true."
                )
            }

            ShowroomRelated(
                names = listOf("TakePicture", "SaveFile", "OpenFilePicker"),
                destination = "eventDetails"
            )
        }
    }
}
