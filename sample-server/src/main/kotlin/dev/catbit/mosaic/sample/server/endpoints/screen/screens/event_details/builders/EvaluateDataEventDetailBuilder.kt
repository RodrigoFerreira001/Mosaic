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
import dev.catbit.mosaic.server.builder.event.builders.data.EvaluateData
import dev.catbit.mosaic.server.builder.event.builders.data.incomingData
import dev.catbit.mosaic.server.builder.event.builders.data.matchesRegex
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object EvaluateDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "EvaluateData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Evaluates a boolean expression tree and branches the chain — onSuccess when " +
                    "true, onFailure when false or on an evaluation error. Use it for any conditional branch: " +
                    "form validation, feature flags, user role checks, null checks, regex, numeric comparison. " +
                    "incomingData() references the current incomingData as the expression's subject; " +
                    "dataSourceData(source, mode) reads from a data store as the subject instead. and/or " +
                    "short-circuit; a type mismatch resolves to false (it doesn't throw). The main operators on " +
                    "a Data are .isNull(), .isNotNull(), .isEqualsTo(value), .isBlank(), .matchesRegex(regex), " +
                    ".isBiggerThan()/.isSmallerThan() (numeric), .containsKey(key), and " +
                    ".valueAtKey(key).<operator>() to chain an operator onto a map key's value."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Type an email and watch it validate in real time") {
                TextField(
                    id = "evaluate_data_email_input",
                    placeholder = "your@email.com",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    events = {
                        EvaluateData(
                            trigger = EventTriggers.onTextChanged(),
                            expression = incomingData().matchesRegex("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            "evaluate_data_feedback",
                                            inlineTileUpdateData(
                                                "text" to "Valid email ✓",
                                                "color" to "#1B5E20"
                                            )
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "evaluate_data_feedback",
                                            inlineTileUpdateData(
                                                "text" to "Invalid email format",
                                                "color" to "#B3261E"
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "evaluate_data_feedback",
                    text = "Type something above",
                    typography = typographyBodyMedium()
                )
                ShowroomNote(
                    "incomingData() here references the TextField's current text (delivered as incomingData " +
                        "by the onTextChanged() trigger), evaluated directly against the regex — no need for " +
                        "an intermediate GetData, since the trigger itself already carries the value."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                EvaluateData(
                    trigger = EventTriggers.onSuccess(),
                    expression = incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w]+\\.[a-z]{2,}$")
                        and incomingData().valueAtKey("password").isLengthBiggerThanOrEquals(8),
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/auth/login", method = HttpMethod.POST)
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Invalid email or password too short")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("TransformData", "GetData", "TextField"),
                destination = "eventDetails"
            )
        }
    }
}
