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
                description = "Reads a file stored locally in the app — the format delivered on onSuccess " +
                    "depends on the chosen outputType. Use it to load back a file previously saved with " +
                    "SaveFile or DownloadFileToDisk. arrayOfBytes() reads everything into memory; " +
                    "flowOfBytes() delivers a Flow<ByteArray> in chunks, without loading the whole file; " +
                    "platformFile() delivers only the reference without reading anything; mapObject() decodes " +
                    "the content as JSON; base64() delivers an encoded String. A missing file or I/O error " +
                    "fires onFailure."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Read the file saved by the TakePicture/GetImageFromGallery/SaveFile demos") {
                SimpleText(
                    id = "get_file_status",
                    text = "Tap the button to try reading mosaic_demo_photo.webp."
                )
                Button(
                    text = "Read mosaic_demo_photo.webp",
                    events = {
                        GetFile(
                            trigger = EventTriggers.onClick(),
                            fileName = "mosaic_demo_photo.webp",
                            outputType = arrayOfBytes(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("get_file_status", inlineTileUpdateData("text" to "File found and read ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "get_file_status",
                                            inlineTileUpdateData("text" to "Not found — save an image first (TakePicture, GetImageFromGallery, or SaveFile)")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "If you haven't saved anything yet in the TakePicture/GetImageFromGallery/SaveFile demos, " +
                        "the onFailure here is the correct, expected behavior — not a bug."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                GetFile(trigger = EventTriggers.onClick(), fileName = "config.json", outputType = mapObject())
                """
            )

            ShowroomRelated(
                names = listOf("SaveFile", "DeleteFile", "DownloadFileToDisk"),
                destination = "eventDetails"
            )
        }
    }
}
