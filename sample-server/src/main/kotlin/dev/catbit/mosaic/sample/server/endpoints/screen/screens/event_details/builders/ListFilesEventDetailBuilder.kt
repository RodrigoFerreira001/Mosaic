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
import dev.catbit.mosaic.server.builder.event.builders.data.ProcessData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.file.DeleteFiles
import dev.catbit.mosaic.server.builder.event.builders.file.ListFiles
import dev.catbit.mosaic.server.builder.event.builders.file.singleFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object ListFilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ListFiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Lists the files stored directly inside a directory of the app's private storage " +
                    "(not recursive — subdirectories are skipped). onSuccess carries the list of file paths, " +
                    "already in the form GetFile and DeleteFiles take as fileName; onEmptyDir fires when the " +
                    "directory exists but holds no files; onFailure fires when the directory doesn't exist. " +
                    "Use it to let the user review and clear local files, for example after a download " +
                    "fails with onInsufficientDiskSpace."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "List the app's private storage") {
                SimpleText(
                    id = "list_files_status",
                    text = "Tap a button to list a directory."
                )
                Button(
                    text = "List the storage root",
                    events = {
                        ListFiles(
                            trigger = EventTriggers.onClick(),
                            directory = "",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("list_files_status", inlineTileUpdateData("text" to "onSuccess · the directory holds files (the paths are the incomingData)"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onEmptyDir(),
                                    updates = {
                                        update("list_files_status", inlineTileUpdateData("text" to "onEmptyDir · the directory has no files"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("list_files_status", inlineTileUpdateData("text" to "onFailure · listing failed"))
                                    }
                                )
                            }
                        )
                    }
                )
                Button(
                    text = "List a directory that doesn't exist",
                    buttonType = outlinedButton(),
                    events = {
                        ListFiles(
                            trigger = EventTriggers.onClick(),
                            directory = "mosaic_missing_dir",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("list_files_status", inlineTileUpdateData("text" to "onFailure · DirNotFoundException — the directory doesn't exist"))
                                    }
                                )
                            }
                        )
                    }
                )
                Button(
                    text = "Delete every file in the storage root",
                    buttonType = outlinedButton(),
                    events = {
                        ListFiles(
                            trigger = EventTriggers.onClick(),
                            directory = "",
                            events = {
                                TransformData(
                                    trigger = EventTriggers.onSuccess(),
                                    applyPerItem = true,
                                    eventTemplate = {
                                        DeleteFiles(
                                            trigger = EventTriggers.inline(),
                                            target = singleFile("<||>")
                                        )
                                    },
                                    events = {
                                        ProcessData(
                                            trigger = EventTriggers.onSuccess(),
                                            processWith = "EVENT_RUNNER"
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onEmptyDir(),
                                    updates = {
                                        update("list_files_status", inlineTileUpdateData("text" to "onEmptyDir · nothing to delete"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "The last button is the cleanup flow: ListFiles hands the paths to TransformData, which with " +
                        "applyPerItem builds one DeleteFiles event per path, and ProcessData's EVENT_RUNNER runs " +
                        "them. It removes the files other demos saved too."
                )
                ShowroomNote(
                    "Save some files first (SaveFile, DownloadFileToDisk) to see onSuccess; a fresh install " +
                        "with an empty storage fires onEmptyDir instead."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ListFiles(
                    trigger = EventTriggers.onInsufficientDiskSpace(),
                    directory = "models",
                    events = {
                        // onSuccess carries List<String>, e.g. ["models/a.glb", "models/b.glb"]
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = { /* ... */ })
                        UpdateTiles(trigger = EventTriggers.onEmptyDir(), updates = { /* ... */ })
                    }
                )
                """
            )

            ShowroomNote(
                "Subdirectories and their contents are skipped, and paths are relative to the storage root — " +
                    "pass them straight to GetFile or DeleteFiles (singleFile) as the file name."
            )

            ShowroomRelated(
                names = listOf("DeleteFiles", "GetFile", "SaveFile", "DownloadFileToDisk"),
                destination = "eventDetails"
            )
        }
    }
}
