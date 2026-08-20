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
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateClearingStack
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateClearingStackEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "NavigateClearingStack"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Navigates to a destination while clearing the entire current navigation stack " +
                    "— the destination becomes the only entry on the back stack, with no prior history. Use it " +
                    "when the destination shouldn't stack on top of the previous navigation — switching a top-" +
                    "level tab in an app with top-level navigation (this is exactly how this sample app itself " +
                    "switches between About/Style/Tiles/Events/Get started/Extend/Mechanisms in the side rail), " +
                    "or finishing a flow (checkout, onboarding) without leaving the intermediate screens on the " +
                    "back stack. launchSingleTop = true (default) avoids stacking the same destination twice if " +
                    "it's already the current one."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Navigate for real, clearing the stack, to a tile's detail page") {
                ShowroomParagraph(
                    "The button below fires a real NavigateClearingStack to the \"tileDetails\" screen, " +
                        "documenting the Button tile — going back afterwards doesn't return to this " +
                        "\"eventDetails\" page, since its entry was cleared from the stack, but to wherever you " +
                        "were before it (e.g. the Events catalog). Like Navigate, the destination has to differ " +
                        "from this screen's own id: navigateClearingStack() also hardcodes launchSingleTop = " +
                        "true, so a same-id destination is a silent no-op — the stack wouldn't even clear."
                )
                Button(
                    text = "View the Button tile (clearing the stack)",
                    buttonType = outlinedButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "Button"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        NavigateClearingStack(
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
                // Real usage in this sample app: HomeScreenBuilder.kt switches tabs this way,
                // once per side-rail item (About, Style, Tiles, Events...)
                HomeEntries.entries.forEach { homeEntry ->
                    NavigateClearingStack(
                        trigger = EventTriggers.onAdaptiveNavigationItemClick(homeEntry.id),
                        navigatorId = "home",
                        destination = homeEntry.id
                    )
                }
                """
            )

            ShowroomNote(
                "Difference from Navigate: Navigate stacks (optionally with a partial popUpTo); " +
                    "NavigateClearingStack always removes the navigator's ENTIRE stack before pushing the new " +
                    "destination — there's no partial popUpTo here because nothing of the previous stack remains."
            )

            ShowroomRelated(
                names = listOf("Navigate", "NavigateUp", "GetScreen"),
                destination = "eventDetails"
            )
        }
    }
}
