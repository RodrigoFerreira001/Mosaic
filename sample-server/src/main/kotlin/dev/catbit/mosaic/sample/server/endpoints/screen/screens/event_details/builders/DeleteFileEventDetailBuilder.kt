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
import dev.catbit.mosaic.server.builder.event.builders.file.DeleteFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DeleteFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DeleteFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Deletes a locally stored file, identified by name — the cleanup counterpart of " +
                    "SaveFile. Use it to clean up local files after a successful upload, or when the app's " +
                    "cache is cleared manually. Deleting a file that doesn't exist fires onFailure."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Delete the file saved by the TakePicture/GetImageFromGallery/SaveFile demos") {
                SimpleText(
                    id = "delete_file_status",
                    text = "Tap the button to try deleting mosaic_demo_photo.webp."
                )
                Button(
                    text = "Delete mosaic_demo_photo.webp",
                    buttonType = outlinedButton(),
                    events = {
                        DeleteFile(
                            trigger = EventTriggers.onClick(),
                            fileName = "mosaic_demo_photo.webp",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("delete_file_status", inlineTileUpdateData("text" to "Deleted successfully ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "delete_file_status",
                                            inlineTileUpdateData("text" to "Failed to delete — the file didn't exist")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "After deleting, try the GetFile event to confirm: the read will fail because the file no " +
                        "longer exists."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DeleteFile(
                    trigger = EventTriggers.onSuccess(),
                    fileName = "avatar.jpg",
                )
                """
            )

            ShowroomRelated(
                names = listOf("SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
