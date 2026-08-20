package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object DismissNavigationDrawerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DismissNavigationDrawer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Closes the screen's side navigation drawer — fires onSuccess() as soon as the " +
                    "close signal is sent. Use it inside the drawer's own items, right after the user picks a " +
                    "navigation option — closing the menu before or alongside navigating to the chosen screen."
            )

            ShowroomSectionTitle("Why there's no interactive demo here")
            ShowroomNote(
                "Same reason as DisplayNavigationDrawer: no screen in this showroom configures a " +
                    "navigation drawer, so there's nothing here for this event to actually close. The code " +
                    "sample below is the real, correct pattern — see DisplayNavigationDrawer for the pairing."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Inside the drawer's content:
                Button(
                    text = "Settings",
                    events = {
                        DismissNavigationDrawer(trigger = EventTriggers.onClick(), events = {
                            Navigate(trigger = EventTriggers.onSuccess(), destination = "settings", navigatorId = "root")
                        })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DisplayNavigationDrawer", "NavigateUp"),
                destination = "eventDetails"
            )
        }
    }
}
