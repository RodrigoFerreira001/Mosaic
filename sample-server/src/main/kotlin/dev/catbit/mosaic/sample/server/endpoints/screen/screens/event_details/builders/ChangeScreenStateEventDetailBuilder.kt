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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.failureState
import dev.catbit.mosaic.server.builder.event.builders.screen.initialState
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ChangeScreenStateEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ChangeScreenState"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Transitions the screen immediately to Success, Failure, or Initial, without " +
                    "making any network call. Use it to manually drive the screen's state transitions: apply a " +
                    "ScreenModel already received from a previous GetScreen, go back to showing the loading " +
                    "state (initialState()), or force the error screen (failureState()) — the \"Try again\" " +
                    "button on this app's own failure screen uses initialState() to restart the loading cycle."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Force each screen state manually") {
                SimpleText(
                    text = "This screen itself won't change state (eventDetails has no dedicated visual handling " +
                        "for Failure/Initial) — instead, watch the result in the client's event console/log " +
                        "when clicking each button.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant())
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "successState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = successState())
                        }
                    )
                    Button(
                        text = "failureState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = failureState())
                        }
                    )
                    Button(
                        text = "initialState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = initialState())
                        }
                    )
                }
                ShowroomNote(
                    "successState(data = null) tries to use the current incomingData (here, null coming from " +
                        "onClick()) as the screen's new ScreenModel — that's why the successState() button " +
                        "doesn't change anything visible: there's no real ScreenModel to apply in this demo. In " +
                        "production, successState() is normally chained after a GetScreen(onSuccess()), whose " +
                        "incomingData IS the expected ScreenModel."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                ChangeScreenState(trigger = EventTriggers.onClick(), state = initialState(), events = {
                    GetScreen(trigger = EventTriggers.onSuccess(), events = {
                        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
                        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
                    })
                })
                """
            )

            ShowroomRelated(
                names = listOf("GetScreen", "RefreshScreen"),
                destination = "eventDetails"
            )
        }
    }
}
