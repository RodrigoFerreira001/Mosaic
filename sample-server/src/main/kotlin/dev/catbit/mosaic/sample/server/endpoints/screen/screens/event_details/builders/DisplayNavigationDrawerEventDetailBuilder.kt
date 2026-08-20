package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object DisplayNavigationDrawerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplayNavigationDrawer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Opens the screen's side navigation drawer — the drawer's content is defined by " +
                    "the screen's own navigationDrawerTiles, not by this event. Fire it from a hamburger " +
                    "button or equivalent. This event has no visible effect if the current screen doesn't " +
                    "configure navigationDrawerTiles in its Screen(...) — the drawer needs to exist before it " +
                    "can be opened."
            )

            ShowroomSectionTitle("Why there's no interactive demo here")
            ShowroomNote(
                "This showroom's own screens (Tiles, Events, eventDetails, tileDetails) don't configure a " +
                    "navigation drawer — the side navigation you see in this app is a NavigationRail/" +
                    "AdaptiveNavigation, a different mechanism. Demonstrating this event live would mean " +
                    "adding a drawer to every screen sharing this eventDetails route, which isn't worth the " +
                    "trade-off just for one event's demo. The code sample below is the real, correct pattern."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Screen(
                    id = "home",
                    navigationDrawerTiles = {
                        Column(id = "drawer_content") { /* menu items */ }
                    }
                ) { /* screen tiles */ }

                // Fired from anywhere inside that screen:
                IconButton(icon = icon("menu"), events = {
                    DisplayNavigationDrawer(trigger = EventTriggers.onClick())
                })
                """
            )

            ShowroomRelated(
                names = listOf("DismissNavigationDrawer", "ToggleMenu", "DisplayModalBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
