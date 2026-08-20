package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

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

object UploadFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UploadFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Uploads the PlatformFile carried in incomingData to a URL, reporting progress as " +
                    "it goes — the natural pair for a pre-signed URL from your own backend, so the file lands " +
                    "straight in a storage bucket (GCS/S3) without passing through your servers."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                UploadFile(
                    trigger = EventTriggers.onSuccess(), // incomingData = PlatformFile, from OpenFilePicker or GetFile
                    url = null, // resolved from the NetworkParamsHolder, set via SetIncomingDataToNetworkParamsHolderUrl
                    method = HttpMethod.PUT,
                    contentType = "image/jpeg"
                )
                """
            )

            SimpleText(
                text = "incomingData must be a PlatformFile — pair this with OpenFilePicker or GetFile. A " +
                    "pre-signed URL usually comes from your own backend via SendNetworkRequest first, staged " +
                    "with SetIncomingDataToNetworkParamsHolderUrl before UploadFile runs. onStart fires once " +
                    "incomingData is validated; onUploadProgress repeatedly while the transfer is in flight; " +
                    "onSuccess (or a status-specific onNetworkResponse/onNetworkFailure) once it completes.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "No interactive demo here: a real upload needs a real pre-signed URL against a real storage " +
                    "bucket, which this sample server doesn't have a backend for. The code above is the real " +
                    "usage pattern end to end."
            )

            ShowroomRelated(
                names = listOf("SetIncomingDataToNetworkParamsHolderUrl", "OpenFilePicker", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
