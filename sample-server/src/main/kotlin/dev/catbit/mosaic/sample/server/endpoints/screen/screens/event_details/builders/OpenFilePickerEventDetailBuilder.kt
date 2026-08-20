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
                description = "Opens the system's file picker, letting the user choose a file — for upload, " +
                    "attachment, or import of any type. Use it for any flow that requires the user to pick a " +
                    "file from the device. fileType restricts the options shown in the picker (image, video, " +
                    "both, or a list of extensions via fileFileType). outputType controls how the content " +
                    "arrives in incomingData — platformFile() (default) only delivers the reference, without " +
                    "reading anything, ideal for chaining directly into UploadFile/SaveFile without loading " +
                    "everything into memory first."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick a pdf, png, or txt file") {
                SimpleText(
                    id = "open_file_picker_status",
                    text = "Tap the button to open the file picker."
                )
                Button(
                    text = "Pick a file (pdf, png, txt)",
                    events = {
                        OpenFilePicker(
                            trigger = EventTriggers.onClick(),
                            fileType = fileFileType("pdf", "png", "txt"),
                            outputType = platformFile(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("open_file_picker_status", inlineTileUpdateData("text" to "File picked ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("open_file_picker_status", inlineTileUpdateData("text" to "Selection cancelled"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "outputType = platformFile() only delivers the file's reference (PlatformFile), without " +
                        "reading its content — that's why this example only confirms something was picked, " +
                        "without showing the content. Chain it with UploadFile, or switch to " +
                        "arrayOfBytes()/base64() when you need the actual bytes."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                OpenFilePicker(
                    trigger = EventTriggers.onClick(),
                    fileType = imageFileType(),
                    events = {
                        UploadFile(trigger = EventTriggers.onSuccess(), url = "/api/upload/avatar", method = HttpMethod.POST)
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("UploadFile", "GetImageFromGallery", "TakePicture"),
                destination = "eventDetails"
            )
        }
    }
}
