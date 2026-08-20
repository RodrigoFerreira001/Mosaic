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
                description = "Saves data to a local file, in the app's private storage — usually chained " +
                    "after an event that produces a ByteArray, such as TakePicture or GetImageFromGallery. " +
                    "Use it to persist downloaded or captured content (a photo, a document, an asset cache) " +
                    "in the app's private storage. incomingData must be a ByteArray — any other type fails " +
                    "silently without writing anything. With overrideIfExists = false, trying to save over an " +
                    "existing file fails instead of overwriting it."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick an image and save it with SaveFile") {
                SimpleText(
                    id = "save_file_status",
                    text = "Tap the button to pick and save an image."
                )
                Button(
                    text = "Pick image and save",
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
                                                update("save_file_status", inlineTileUpdateData("text" to "Saved successfully ✓"))
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update("save_file_status", inlineTileUpdateData("text" to "Failed to save (check overrideIfExists)"))
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "SaveFile alone doesn't make sense without a byte source earlier in the chain — that's " +
                        "why this demo uses GetImageFromGallery to produce the ByteArray that SaveFile " +
                        "consumes. See GetFile and DeleteFile to read or remove the file saved here."
                )
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("GetFile", "DeleteFile", "GetImageFromGallery"),
                destination = "eventDetails"
            )
        }
    }
}
