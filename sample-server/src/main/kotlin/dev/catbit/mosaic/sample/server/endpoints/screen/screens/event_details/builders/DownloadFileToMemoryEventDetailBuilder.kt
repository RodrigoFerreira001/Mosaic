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
import dev.catbit.mosaic.server.builder.event.builders.networking.DownloadFileToMemory
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DownloadFileToMemoryEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DownloadFileToMemory"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Downloads a URL into memory only — never touches the filesystem. Use it when " +
                    "the content is only needed transiently in the current event chain (a small JSON/config " +
                    "fetched to be parsed and discarded, warming a cache without persisting it). onSuccess and " +
                    "onDownloadFinish carry the total byte count downloaded, not the raw bytes themselves — this " +
                    "event confirms and measures the download, it doesn't hand the content back to the chain. " +
                    "For that, use SendNetworkRequest instead."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Real download — nothing is written to disk") {
                SimpleText(
                    id = "download_to_memory_status",
                    text = "Tap the button to download a real file straight into memory."
                )
                Button(
                    text = "Download hello-world-readme.txt to memory",
                    events = {
                        DownloadFileToMemory(
                            trigger = EventTriggers.onClick(),
                            url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README",
                            method = HttpMethod.GET,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update("download_to_memory_status", inlineTileUpdateData("text" to "onStart · downloading to memory..."))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "download_to_memory_status",
                                            updateData = mappedIncomingTileUpdateData("text" to "onSuccess · <||> bytes downloaded, never written to disk")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update("download_to_memory_status", inlineTileUpdateData("text" to "onFailure · request failed"))
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Try GetFile with fileName = \"hello-world-readme.txt\" right after this demo — it fails, " +
                        "proving nothing was persisted. Compare with the DownloadFileToDisk demo, which does " +
                        "leave a file GetFile can find."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DownloadFileToMemory(
                    trigger = EventTriggers.onClick(),
                    url = "https://api.example.com/warmup",
                    method = HttpMethod.GET,
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("bytes_label", mappedIncomingTileUpdateData("text" to "Warmed up <||> bytes"))
                        })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DownloadFileToDisk", "DownloadFile", "SendNetworkRequest"),
                destination = "eventDetails"
            )
        }
    }
}
