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
                description = "Opens the device camera to capture a photo — Mosaic's own native " +
                    "implementation (CameraManager) on each platform, no third-party library. Use it for " +
                    "any flow that requires capturing a photo with the camera — an avatar, document " +
                    "scanning, an attachment. The camera always returns raw (lossless) PNG; if compression " +
                    "is set, the result is re-encoded as WebP at the requested size/quality. incomingData " +
                    "becomes a ByteArray (or a base64 String, with pictureBase64()) ready to chain with " +
                    "SaveFile or send directly over the network."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Take a photo and save it to the app's private storage") {
                SimpleText(
                    id = "take_picture_status",
                    text = "Tap the button to open the camera."
                )
                Button(
                    text = "Take photo and save as mosaic_demo_photo.webp",
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
                                                update("take_picture_status", inlineTileUpdateData("text" to "Photo captured and saved ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("take_picture_status", inlineTileUpdateData("text" to "Failed to save the photo"))
                                            }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("take_picture_status", inlineTileUpdateData("text" to "Capture canceled or no camera available"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Use the GetFile and DeleteFile events to read or delete the mosaic_demo_photo.webp file " +
                        "saved here."
                )
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("GetImageFromGallery", "SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
