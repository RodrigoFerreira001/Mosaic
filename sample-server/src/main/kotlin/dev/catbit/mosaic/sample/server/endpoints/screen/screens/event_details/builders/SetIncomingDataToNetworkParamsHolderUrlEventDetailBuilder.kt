package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object SetIncomingDataToNetworkParamsHolderUrlEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderUrl"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stores the incomingData (must be a String) as the request URL in the " +
                    "NetworkParametersHolder, available to UploadFile when its url is null. Use it to feed an " +
                    "upload event (UploadFile) with a signed URL generated dynamically by your backend — the " +
                    "typical pattern is: the server requests a signed URL (SendNetworkRequest), extracts the " +
                    "URL from the response (TransformData), stores it as the URL on the holder " +
                    "(SetIncomingDataToNetworkParamsHolderUrl), and then fires the actual UploadFile, which " +
                    "reads that stored URL instead of having a fixed one in the schema. A null incomingData " +
                    "or one that isn't a String fires onFailure."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // incomingData = backend response with {"uploadUrl": "https://storage.googleapis.com/..."}
                TransformData(trigger = EventTriggers.onSuccess(), template = "<|uploadUrl|>", events = {
                    SetIncomingDataToNetworkParamsHolderUrl(trigger = EventTriggers.onSuccess(), events = {
                        UploadFile(trigger = EventTriggers.onSuccess(), method = HttpMethod.PUT, contentType = "video/mp4")
                    })
                })
                """
            )

            ShowroomSectionTitle("Why there's no interactive demo here")
            SimpleText(
                text = "This event only makes sense within the \"signed URL\" pattern: a real backend that " +
                    "generates a temporary URL for a direct upload to storage (GCS/S3). This sample-server " +
                    "has no such backend, and simulating one with a third-party service would mean actually " +
                    "uploading a file somewhere — not appropriate for a showroom demo. The code above shows " +
                    "the real, correct chaining; see UploadFile and SetIncomingDataToNetworkParamsHolderBody " +
                    "for demos that actually run.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "Without this event, UploadFile requires a fixed url in its own schema — useful only when the " +
                    "destination URL is known at build time, not generated per-request at runtime."
            )

            ShowroomRelated(
                names = listOf("UploadFile", "TransformData", "DownloadFile"),
                destination = "eventDetails"
            )
        }
    }
}
