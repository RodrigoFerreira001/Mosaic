package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer.DisplayNavigationDrawer
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button

object DisplayNavigationDrawerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplayNavigationDrawer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Opens the screen's side navigation drawer — the drawer's content is defined by " +
                    "the screen's navigationDrawerTiles tile, not by this event. Use it, fired from a hamburger " +
                    "button or equivalent. This event has no visible effect if the current screen doesn't " +
                    "configure navigationDrawerTiles in its Screen(...) — the drawer needs to exist before it " +
                    "can be opened. This very \"eventDetails\" page configures a minimal drawer just for this " +
                    "demonstration (no other screen in this app opens one)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Open the drawer configured on this very page") {
                Button(
                    text = "Open with DisplayNavigationDrawer",
                    events = {
                        DisplayNavigationDrawer(trigger = EventTriggers.onClick())
                    }
                )
                ShowroomNote(
                    "The drawer that opens contains a \"Close with DismissNavigationDrawer\" button — the " +
                        "demonstration of the paired event, at the bottom of the drawer."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Configured once, at the screen level:
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
