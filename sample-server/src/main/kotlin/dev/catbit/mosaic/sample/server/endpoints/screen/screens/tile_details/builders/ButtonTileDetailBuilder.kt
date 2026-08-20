package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.elevatedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.iconAtEnd
import dev.catbit.mosaic.server.builder.tile.builders.buttons.iconAtStart
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.roundedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.squareButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow

object ButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Button"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 button in five visual styles (buttonType), with support for an " +
                    "icon, a loading state, and shape. Use it for a screen's primary action, form submission, " +
                    "or any labeled clickable action. loading = true swaps the text/icon for a spinner " +
                    "without disabling touch — only enabled = false actually blocks interaction."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "The 5 buttonType values, side by side — click any of them") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Filled",
                        buttonType = filledButton(),
                        events = { buttonClickSnackbar("Filled", "filledButton()") }
                    )
                    Button(
                        text = "Elevated",
                        buttonType = elevatedButton(),
                        events = { buttonClickSnackbar("Elevated", "elevatedButton()") }
                    )
                    Button(
                        text = "Tonal",
                        buttonType = filledTonalButton(),
                        events = { buttonClickSnackbar("Tonal", "filledTonalButton()") }
                    )
                    Button(
                        text = "Outlined",
                        buttonType = outlinedButton(),
                        events = { buttonClickSnackbar("Outlined", "outlinedButton()") }
                    )
                    Button(
                        text = "Text",
                        buttonType = textButton(),
                        events = { buttonClickSnackbar("Text", "textButton()") }
                    )
                }
                Button(
                    id = "button_loading_demo",
                    text = "Click to load",
                    icon = icon("hourglass_top"),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onClick(),
                            updates = {
                                update(
                                    tileId = "button_loading_demo",
                                    updateData = inlineTileUpdateData("loading" to true)
                                )
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("shape — squareButton() vs roundedButton()")
            ShowroomDemoCard(title = "SQUARE → medium theme shape, ROUNDED (default) → fully round") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(text = "squareButton()", buttonType = filledButton(), shape = squareButton())
                    Button(text = "roundedButton()", buttonType = filledButton(), shape = roundedButton())
                }
            }

            ShowroomSectionTitle("iconPosition — iconAtStart() vs iconAtEnd()")
            ShowroomDemoCard(title = "Same icon, opposite side of the text") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(text = "Download", icon = icon("download"), iconPosition = iconAtStart())
                    Button(text = "Download", icon = icon("download"), iconPosition = iconAtEnd())
                }
            }

            ShowroomSectionTitle("enabled = false")
            ShowroomDemoCard(title = "Blocks interaction for real, regardless of buttonType") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(text = "Disabled filled", buttonType = filledButton(), enabled = false)
                    Button(text = "Disabled outlined", buttonType = outlinedButton(), enabled = false)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Button(
                    id = "loginButton",
                    text = "Log in",
                    buttonType = filledButton(),
                    style = {
                        size(width = fillHorizontally(max = 400), height = fixedVertically(56))
                    },
                    events = {
                        TriggerEvent(eventId = "loginEvent", trigger = EventTriggers.onClick())
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("IconButton", "FloatingActionButton", "AssistChip"),
                destination = "tileDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.buttonClickSnackbar(
    label: String,
    code: String,
) {
    DisplaySnackbar(
        trigger = EventTriggers.onClick(),
        message = "$label → buttonType = $code"
    )
}
