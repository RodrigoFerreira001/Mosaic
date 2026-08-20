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
import dev.catbit.mosaic.server.builder.event.builders.file.GetFile
import dev.catbit.mosaic.server.builder.event.builders.file.arrayOfBytes
import dev.catbit.mosaic.server.builder.event.builders.networking.DownloadFileToDisk
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DownloadFileToDiskEventDetailBuilder : EventDetailBuilder {

    private const val TARGET_FILE_NAME = "mosaic_demo_downloaded.txt"

    override fun canBuild(eventName: String) = eventName == "DownloadFileToDisk"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Downloads a URL directly into the app's own private storage under targetFileName " +
                    "— not the device's public Downloads folder (that's DownloadFile), the app's private sandbox, " +
                    "readable and deletable afterwards with GetFile/DeleteFile. Use it for content the app needs " +
                    "to keep around privately: a cached asset, an offline copy of a document, a downloaded " +
                    "attachment the app itself will open later."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Download to the app's private storage, then read it back") {
                SimpleText(
                    id = "download_to_disk_status",
                    text = "Tap the button to download a real file to the app's private storage."
                )
                Button(
                    text = "Download to $TARGET_FILE_NAME",
                    events = {
                        DownloadFileToDisk(
                            trigger = EventTriggers.onClick(),
                            url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                            method = HttpMethod.GET,
                            targetFileName = TARGET_FILE_NAME,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update("download_to_disk_status", inlineTileUpdateData("text" to "onStart · downloading..."))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("download_to_disk_status", inlineTileUpdateData("text" to "onSuccess · saved as $TARGET_FILE_NAME in app storage"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("download_to_disk_status", inlineTileUpdateData("text" to "onFailure · request failed"))
                                    }
                                )
                            }
                        )
                    }
                )
                Button(
                    text = "Confirm with GetFile",
                    buttonType = outlinedButton(),
                    events = {
                        GetFile(
                            trigger = EventTriggers.onClick(),
                            fileName = TARGET_FILE_NAME,
                            outputType = arrayOfBytes(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("download_to_disk_status", inlineTileUpdateData("text" to "GetFile found it ✓ — really persisted in app storage"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("download_to_disk_status", inlineTileUpdateData("text" to "GetFile failed — download it first"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Unlike DownloadFile (the public-Downloads-folder event), this one never involves the OS's " +
                        "download manager or a native picker — it's a plain private write, same storage GetFile/" +
                        "DeleteFile/SaveFile already operate on."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DownloadFileToDisk(
                    trigger = EventTriggers.onClick(),
                    url = "https://api.example.com/assets/manual.pdf",
                    method = HttpMethod.GET,
                    targetFileName = "manual.pdf",
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = { /* ... */ })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DownloadFile", "DownloadFileToMemory", "GetFile", "DeleteFile"),
                destination = "eventDetails"
            )
        }
    }
}
