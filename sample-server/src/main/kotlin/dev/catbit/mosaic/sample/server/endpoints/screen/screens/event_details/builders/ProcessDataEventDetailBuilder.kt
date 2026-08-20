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

object ProcessDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ProcessData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Hands incomingData off to a DataProcessor registered on the client, identified " +
                    "by processWith — opens the door to native logic that doesn't fit the DSL. Use it when the " +
                    "server needs to request processing that only exists on the client and can't be expressed " +
                    "by the event DSL — encrypting a value before persisting it locally, applying a platform-" +
                    "specific transform, or triggering a native capability registered under an id " +
                    "(processWith). The event itself does nothing on its own: it just delivers incomingData for " +
                    "the right processor to find and react to."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ProcessData(
                    trigger = EventTriggers.onSuccess(),
                    processWith = "EVENT_RUNNER"
                )
                """
            )

            SimpleText(
                text = "Why there's no interactive demo here: ProcessData depends on a DataProcessor with the " +
                    "id passed in processWith being registered on the client at runtime — that's app " +
                    "configuration, done in the Compose Multiplatform client's bootstrap, not something this " +
                    "sample-server (which only describes schemas) can demonstrate in isolation. Without a " +
                    "processor registered under the id used, the event is a silent no-op: no onSuccess/onFailure " +
                    "fires.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "A null incomingData also results in a complete no-op — no trigger fires. If you register " +
                    "your own DataProcessor in the client app, its onSuccess callback doesn't pass any data " +
                    "back: use UpdateData/SendData as a side channel to expose the processing result to other " +
                    "events."
            )

            ShowroomRelated(
                names = listOf("TransformData", "EvaluateData", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
