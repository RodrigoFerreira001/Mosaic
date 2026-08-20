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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarLongDuration
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarShortDuration
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DisplaySnackbarEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplaySnackbar"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Displays a Material 3 snackbar with a message, an optional action, and a " +
                    "configurable duration — brief feedback for confirmations, errors, and undo prompts. Use it " +
                    "for short feedback messages: success confirmation, error warning, \"undo\" prompt. With " +
                    "actionLabel set, the snackbar gets an action button that fires onSnackbarAction() when " +
                    "tapped; without an action, it only fires onSnackbarDismissed() when it disappears (by " +
                    "timeout, swipe, or programmatic dismissal)."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Fire snackbars with and without an action") {
                SimpleText(
                    id = "display_snackbar_feedback",
                    text = "(no action recorded yet)"
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Simple snackbar",
                        buttonType = outlinedButton(),
                        events = {
                            DisplaySnackbar(
                                trigger = EventTriggers.onClick(),
                                message = "Action completed successfully",
                                duration = snackbarShortDuration()
                            )
                        }
                    )
                    Button(
                        text = "Snackbar with \"Undo\" action",
                        buttonType = outlinedButton(),
                        events = {
                            DisplaySnackbar(
                                trigger = EventTriggers.onClick(),
                                message = "Item removed",
                                duration = snackbarLongDuration(),
                                actionLabel = "Undo",
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSnackbarAction(),
                                        updates = {
                                            update(
                                                "display_snackbar_feedback",
                                                inlineTileUpdateData("text" to "onSnackbarAction() fired — \"Undo\" was tapped ✓")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "Tap \"Undo\" inside the snackbar itself (not the button that opened it) to see the text " +
                        "above change — only onSnackbarAction() updates the tile; letting the snackbar disappear " +
                        "on its own fires onSnackbarDismissed(), which this example doesn't handle."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DisplaySnackbar(
                    trigger = EventTriggers.onFailure(),
                    message = "Something went wrong",
                    duration = snackbarLongDuration(),
                    actionLabel = "Retry",
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSnackbarAction(), url = "/api/retry", method = HttpMethod.POST)
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DismissSnackbar", "DisplayDialog", "DisplayModalBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
