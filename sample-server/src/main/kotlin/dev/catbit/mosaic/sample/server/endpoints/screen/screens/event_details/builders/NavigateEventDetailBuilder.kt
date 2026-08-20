package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "Navigate"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Pushes a new destination onto a navigator's stack, optionally removing prior " +
                    "entries (popUpTo) and carrying data along to the destination screen. Use it for any " +
                    "navigation between screens within a navigator's scope — opening a detail page, starting a " +
                    "flow, going to a settings screen. destination is the id of the target Screen/Graph.Entry; " +
                    "navigatorId identifies which navigator (several nested ones can exist) should handle the " +
                    "navigation. incomingData is converted to Map<String, Any> and merged with data (data wins " +
                    "on key conflicts), becoming the destination screen's navigationData, read via " +
                    "screenNavigationData()."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Navigate for real to a tile's detail page") {
                ShowroomParagraph(
                    "The button below fires a real Navigate (not staged) to the \"tileDetails\" screen, " +
                        "documenting the Button tile. It has to target a different destination id than " +
                        "this very screen (\"eventDetails\") — the client's navigate() call hardcodes " +
                        "launchSingleTop = true, and when destination already matches the back stack's top " +
                        "entry id, the whole call (push and any popUpTo alike) is a silent no-op, by design."
                )
                Button(
                    text = "View the Button tile",
                    buttonType = outlinedButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "Button"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        Navigate(
                                            trigger = EventTriggers.onSuccess(),
                                            destination = "tileDetails",
                                            navigatorId = "root"
                                        )
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
                Navigate(
                    trigger = EventTriggers.onClick(),
                    destination = "environment_detail",
                    navigatorId = "main_navigator",
                    popUpTo = poppingUpTo(destination = "home", inclusive = false),
                    data = mapOf("environmentId" to env.id)
                )
                """
            )

            ShowroomNote(
                "Why the data parameter isn't used here: this screen reads the event to document via the HTTP " +
                    "request's query parameter, set by the entry's automatic GetScreen (request." +
                    "queryParameters[\"event\"]), not via navigationData. That's why the demo uses " +
                    "SetIncomingDataToNetworkParamsHolderQueryParameters before Navigate instead of the data " +
                    "field — which is ideal when the destination screen reads with screenNavigationData()."
            )

            ShowroomRelated(
                names = listOf("NavigateUp", "NavigateClearingStack", "GetScreen"),
                destination = "eventDetails"
            )
        }
    }
}
