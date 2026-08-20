package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
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
                description = "Opens the device's gallery to pick an image — uses the system Photo Picker, so " +
                    "no storage permission is required. Use it for any gallery-based image selection flow: " +
                    "avatars, uploads, imports. Because it relies on the native picker (Android PickVisualMedia " +
                    "/ iOS PHPickerViewController), the app only gets access to the chosen image, without " +
                    "needing RequestPermission(GALLERY) or a manifest entry. compression/resize follow the " +
                    "same contract as TakePicture — without compression, the image comes back in its original " +
                    "format; with compression, it's re-encoded as WebP."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick an image from the gallery and save it") {
                SimpleText(
                    id = "gallery_image_status",
                    text = "Tap the button to open the gallery."
                )
                Button(
                    text = "Pick from gallery and save as mosaic_demo_photo.webp",
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
                                                update("gallery_image_status", inlineTileUpdateData("text" to "Image picked and saved ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("gallery_image_status", inlineTileUpdateData("text" to "Failed to save the image"))
                                            }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("gallery_image_status", inlineTileUpdateData("text" to "Selection cancelled"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Same mosaic_demo_photo.webp file used in the TakePicture demo — picking here overwrites " +
                        "whatever a camera photo saved before it, since overrideIfExists = true."
                )
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("TakePicture", "SaveFile", "OpenFilePicker"),
                destination = "eventDetails"
            )
        }
    }
}
