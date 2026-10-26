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
import dev.catbit.mosaic.server.builder.event.builders.file.ListFiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
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

            ShowroomDemoCard(title = "applyPerItem: map each stored file name") {
                SimpleText(
                    id = "transform_data_per_item_result",
                    text = "Tap the button to list the app's storage and map each file name with the template."
                )
                Button(
                    text = "List files and map each one",
                    buttonType = outlinedButton(),
                    events = {
                        ListFiles(
                            trigger = EventTriggers.onClick(),
                            directory = "",
                            events = {
                                TransformData(
                                    trigger = EventTriggers.onSuccess(),
                                    template = mapOf("label" to "file: <||>"),
                                    applyPerItem = true,
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "transform_data_per_item_result",
                                                    updateData = mappedIncomingTileUpdateData("text" to "<//>")
                                                )
                                            }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onEmptyDir(),
                                    updates = {
                                        update(
                                            "transform_data_per_item_result",
                                            inlineTileUpdateData("text" to "The storage is empty — save a file first (SaveFile, DownloadFileToDisk)")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "ListFiles delivers a list of paths; with applyPerItem = true the template runs once per " +
                        "path (<||> is the path itself) and incomingData becomes the list of results — here " +
                        "[{label=file: a.txt}, ...], printed with <//>. Without the flag the template would run " +
                        "once against the whole list."
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

            ShowroomNote(
                "applyPerItem = true changes what happens when incomingData is a list: instead of applying " +
                    "the template once to the whole list, it is applied to each item and the result is the list " +
                    "of outcomes — inside the template, <||> is the item itself and <|name|> a field of it. " +
                    "An incomingData that is not a list ignores the flag. See the ListFiles page for a live " +
                    "ListFiles → TransformData → ProcessData cleanup flow."
            )
            ShowroomCode(
                """
                // incomingData = ["a.glb", "b.glb"]  →  [DeleteFiles(a.glb), DeleteFiles(b.glb)] as JSON
                TransformData(
                    trigger = EventTriggers.onSuccess(),
                    applyPerItem = true,
                    eventTemplate = {
                        DeleteFiles(trigger = EventTriggers.inline(), target = singleFile("<||>"))
                    },
                    events = {
                        ProcessData(trigger = EventTriggers.onSuccess(), processWith = "EVENT_RUNNER")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("EvaluateData", "GetData", "UpdateTiles", "ProcessData", "ListFiles"),
                destination = "eventDetails"
            )
        }
    }
}
