package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.networking.DownloadFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DownloadFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DownloadFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Downloads a file from a URL directly into the device's public/general storage " +
                    "— the Downloads folder visible in the OS's file manager. Use it for any \"save this " +
                    "download somewhere the user can find it\" flow — reports, exports, media the user wants " +
                    "to keep outside the app. If you only need the bytes in memory, use " +
                    "DownloadFileToMemory; if the file just needs to live in the app's private sandbox, use " +
                    "DownloadFileToDisk. This event behaves differently per platform — a real OS constraint, " +
                    "not an implementation gap. Android: delegated to the system's DownloadManager — silent, " +
                    "no dialog, shows a download notification and lands in the public Downloads folder (only " +
                    "GET with no body is supported, a DownloadManager limitation). iOS: there's no public " +
                    "\"Downloads\" folder writable by the app — this event opens FileKit.openFileSaver() (an " +
                    "export UIDocumentPickerViewController), requiring a tap to choose the destination; " +
                    "cancelling fires onFailure() with no data, the same convention as OpenFilePicker's " +
                    "cancellation. JVM/Desktop: written silently to ~/Downloads. wasmJs/Web: triggers the " +
                    "browser's native download flow."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Real download — triggers the device platform's native behavior") {
                SimpleText(
                    id = "download_file_status_text",
                    text = "Tap the button to download a real file to the Downloads folder."
                )
                Button(
                    text = "Download hello-world-readme.txt",
                    events = {
                        DownloadFile(
                            trigger = EventTriggers.onClick(),
                            url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                            method = HttpMethod.GET,
                            targetFileName = "hello-world-readme.txt",
                            mimeType = "text/plain",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onStart · downloading..."))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onDownloadProgress(),
                                    updates = {
                                        update(
                                            tileId = "download_file_status_text",
                                            updateData = mappedIncomingTileUpdateData("text" to "onDownloadProgress · <//>%")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onSuccess · saved to Downloads"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("download_file_status_text", inlineTileUpdateData("text" to "onFailure · cancelled or error"))
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DownloadFile(
                    trigger = EventTriggers.onClick(),
                    url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                    method = HttpMethod.GET,
                    targetFileName = "hello-world-readme.txt",
                    mimeType = "text/plain",
                    events = {
                        UpdateTiles(trigger = EventTriggers.onDownloadProgress(), updates = {
                            update("progress_bar", incomingTileUpdateData())
                        })
                        DisplaySnackbar(trigger = EventTriggers.onSuccess(), message = "Saved to Downloads")
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Download cancelled or failed")
                    }
                )
                """
            )

            ShowroomNote(
                "The real interaction depends on the device — on Android the download happens silently via a " +
                    "system notification; on iOS it opens a native destination picker."
            )

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
