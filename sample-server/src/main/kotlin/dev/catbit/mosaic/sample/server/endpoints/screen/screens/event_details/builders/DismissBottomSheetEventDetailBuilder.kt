package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DismissBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DisplayBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarShortDuration
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_SHEET_ID = "dismiss_bs_demo"
    private const val OUTSIDE_SHEET_ID = "dismiss_bs_outside"

    override fun canBuild(eventName: String) = eventName == "DismissBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Programmatically closes an open non-modal bottom sheet — the \"reaction\" side " +
                    "of the DisplayBottomSheet / DismissBottomSheet pair. Use it from inside the sheet's own " +
                    "content (a \"Close\" button) or from anywhere else on the screen — since the non-modal " +
                    "sheet doesn't block what's behind it, it's common for the button that closes it to live " +
                    "outside the sheet. Pass the same bottomSheetId used in the DisplayBottomSheet that opened it."
            )

            ShowroomSectionTitle("Interactive demo")

            ShowroomDemoCard(title = "1. Closing from inside the sheet") {
                Button(
                    text = "Open sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = DEMO_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Example sheet") {
                                    Paragraph("The button below is the DismissBottomSheet event itself being demonstrated.")
                                    Button(
                                        text = "Close with DismissBottomSheet",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                bottomSheetId = DEMO_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Sheet closed programmatically",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. Closing from outside the sheet") {
                ShowroomParagraph(
                    "Open the sheet and close it from this page's button, without touching the sheet itself. " +
                        "This is only possible because the non-modal sheet doesn't block the screen behind it — " +
                        "with a modal, this button would be unreachable."
                )
                Button(
                    text = "Open sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = OUTSIDE_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Close me from out here") {
                                    Paragraph(
                                        "There's no close button in here. Use the page's button, right below " +
                                            "this sheet."
                                    )
                                }
                            }
                        )
                    }
                )
                Button(
                    text = "Close the open sheet",
                    buttonType = outlinedButton(),
                    events = {
                        DismissBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = OUTSIDE_SHEET_ID
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Button(id = "close_btn", text = "Close") {
                    events = {
                        DismissBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = "now_playing"
                        )
                    }
                }
                """
            )

            ShowroomNote(
                "Closing an id that isn't on the stack fires onFailure() instead of being ignored — which " +
                    "also covers the case of closing the same sheet twice."
            )

            ShowroomRelated(
                names = listOf("DisplayBottomSheet", "DismissModalBottomSheet", "DismissDialog", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }

    private fun TileSchemaBuilderScope.SheetBody(
        title: String,
        content: TileSchemaBuilderScope.() -> Unit
    ) {
        Column(
            style = { padding(horizontal = 24, top = 8, bottom = 32) },
            arrangement = arrangeVerticallySpacedBy(16),
            scrollable = true
        ) {
            SimpleText(text = title, typography = typographyTitleLarge())
            content()
        }
    }

    private fun TileSchemaBuilderScope.Paragraph(text: String) {
        SimpleText(
            text = text,
            typography = typographyBodyMedium(),
            color = color(themeColorOnSurfaceVariant())
        )
    }
}
