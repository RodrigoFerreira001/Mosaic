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
import dev.catbit.mosaic.server.builder.event.builders.file.DeleteFiles
import dev.catbit.mosaic.server.builder.event.builders.file.multipleFiles
import dev.catbit.mosaic.server.builder.event.builders.file.singleFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DeleteFilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DeleteFiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Deletes one or several locally stored files, identified by name — the cleanup " +
                    "counterpart of SaveFile. target picks the files: singleFile(name) for one, " +
                    "multipleFiles(names) for a list, where every file is attempted even if one fails and " +
                    "onFailure fires once at the end. Use it to clean up local files after a successful " +
                    "upload, when the app's cache is cleared manually, or to free space after " +
                    "onInsufficientDiskSpace."
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
                        DeleteFiles(
                            trigger = EventTriggers.onClick(),
                            target = singleFile("mosaic_demo_photo.webp"),
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
                Button(
                    text = "Delete two files at once (multipleFiles)",
                    buttonType = outlinedButton(),
                    events = {
                        DeleteFiles(
                            trigger = EventTriggers.onClick(),
                            target = multipleFiles(listOf("mosaic_demo_photo.webp", "mosaic_demo_downloaded.txt")),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("delete_file_status", inlineTileUpdateData("text" to "Deleted both files ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("delete_file_status", inlineTileUpdateData("text" to "onFailure · at least one deletion failed"))
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
                DeleteFiles(
                    trigger = EventTriggers.onSuccess(),
                    target = singleFile("avatar.jpg"),
                )

                DeleteFiles(
                    trigger = EventTriggers.onSuccess(),
                    target = multipleFiles(listOf("a.glb", "b.glb")),
                )
                """
            )

            ShowroomRelated(
                names = listOf("SaveFile", "GetFile", "ListFiles"),
                destination = "eventDetails"
            )
        }
    }
}
