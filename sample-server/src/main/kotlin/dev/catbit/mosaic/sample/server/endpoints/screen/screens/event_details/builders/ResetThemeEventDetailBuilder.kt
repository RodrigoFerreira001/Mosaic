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
import dev.catbit.mosaic.server.builder.event.builders.theme.ResetTheme
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object ResetThemeEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ResetTheme"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Reverts a previous SetTheme, restoring the app's default color scheme — " +
                    "light and dark. Use it for \"restore default theme\" actions, or when ending a " +
                    "white-labeled session that had applied custom colors with SetTheme. It takes no " +
                    "parameters: it always goes back to the app's original scheme, the same as before any " +
                    "SetTheme was ever fired."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Restore the default theme") {
                ShowroomNote(
                    "Go to the SetTheme event and apply the \"ocean\" theme first — then come back here and " +
                        "tap the button below to see the app's colors return to normal."
                )
                Button(
                    text = "Restore with ResetTheme",
                    buttonType = outlinedButton(),
                    events = {
                        ResetTheme(trigger = EventTriggers.onClick())
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ResetTheme(
                    trigger = EventTriggers.onClick()
                )
                """
            )

            ShowroomRelated(
                names = listOf("SetTheme"),
                destination = "eventDetails"
            )
        }
    }
}
