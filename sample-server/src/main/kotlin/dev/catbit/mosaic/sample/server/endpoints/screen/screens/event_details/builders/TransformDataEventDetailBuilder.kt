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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object TransformDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TransformData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Reshapes incomingData by applying a template with <|path|> placeholders, " +
                    "resolved from incomingData itself. Use it when you need to extract or restructure part of " +
                    "incomingData before passing it along — pulling a single field out of a network response " +
                    "map, or assembling a new map shape. <|path.to.value|> walks keys in dot notation; " +
                    "<|items[0].name|> accesses list indices; <||> alone preserves the whole incomingData with " +
                    "its native type (Int, Boolean, List...), but if the placeholder appears mixed with text in " +
                    "a string, the value is coerced to String."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Type your name and watch the template resolve live") {
                TextField(
                    id = "transform_data_name_input",
                    placeholder = "Your name",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    events = {
                        TransformData(
                            trigger = EventTriggers.onTextChanged(),
                            template = mapOf("text" to "Hello, <||>!"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "transform_data_greeting",
                                            updateData = incomingTileUpdateData()
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "transform_data_greeting",
                    text = "Hello, !",
                    typography = typographyTitleMedium()
                )
                ShowroomNote(
                    "Notice the template: mapOf(\"text\" to \"Hello, <||>!\") — the key has to match the " +
                        "target tile's own field name (\"text\" for SimpleText), since incomingTileUpdateData() " +
                        "forwards the template's output map as-is to UpdateTiles. <||> is mixed with other " +
                        "text here (\"Hello, \" + value + \"!\"), so even if the TextField delivered something " +
                        "that wasn't a String, the final result would be coerced to a String."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                TransformData(
                    trigger = EventTriggers.onSuccess(),
                    template = mapOf("greeting" to "Hello, <|user.name|>!", "userId" to "<|user.id|>"),
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("greeting_tile", incomingTileUpdateData())
                        })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("EvaluateData", "GetData", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
