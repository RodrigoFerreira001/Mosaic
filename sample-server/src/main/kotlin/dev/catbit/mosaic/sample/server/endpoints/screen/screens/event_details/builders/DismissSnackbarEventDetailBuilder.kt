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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DismissSnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarIndefiniteDuration
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object DismissSnackbarEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DismissSnackbar"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Programmatically closes the currently displayed snackbar — useful for dismissing " +
                    "an Indefinite-duration snackbar once an async operation finishes. It's mainly used with " +
                    "snackbars whose duration = Indefinite, which don't disappear on their own — for example, " +
                    "an \"Uploading...\" snackbar that needs to be closed manually as soon as the request " +
                    "finishes (success or failure). Important: DismissSnackbar does NOT fire " +
                    "onSnackbarDismissed() — that trigger is reserved for the natural dismissal (timeout, " +
                    "swipe, or tap outside), not for programmatic dismissal."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Open an indefinite snackbar and close it manually") {
                Button(
                    text = "Open Indefinite snackbar",
                    events = {
                        DisplaySnackbar(
                            trigger = EventTriggers.onClick(),
                            message = "This snackbar won't disappear on its own — close it below",
                            duration = snackbarIndefiniteDuration()
                        )
                    }
                )
                Button(
                    text = "Close with DismissSnackbar",
                    buttonType = outlinedButton(),
                    events = {
                        DismissSnackbar(trigger = EventTriggers.onClick())
                    }
                )
                ShowroomNote(
                    "Open the indefinite snackbar first; it stays on screen until you tap \"Close with " +
                        "DismissSnackbar\" — note that closing it this way isn't the same as letting it " +
                        "disappear naturally, since onSnackbarDismissed() only fires in that second case."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SendNetworkRequest(
                    trigger = EventTriggers.onClick(),
                    url = "/api/upload",
                    method = HttpMethod.POST,
                    events = {
                        DisplaySnackbar(trigger = EventTriggers.onStart(), message = "Uploading...", duration = snackbarIndefiniteDuration())
                        DismissSnackbar(trigger = EventTriggers.onSuccess())
                        DismissSnackbar(trigger = EventTriggers.onFailure())
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DisplaySnackbar", "DismissDialog", "DismissModalBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
