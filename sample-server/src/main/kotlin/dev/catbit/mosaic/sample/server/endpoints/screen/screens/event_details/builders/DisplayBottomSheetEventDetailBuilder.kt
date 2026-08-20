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
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarShortDuration
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val PLAYER_SHEET_ID = "display_bs_player"
    private const val PARTIAL_SHEET_ID = "display_bs_partial"
    private const val BLOCKING_SHEET_ID = "display_bs_blocking"
    private const val STACK_SHEET_ID = "display_bs_stack"
    private const val STACK_MODAL_ID = "display_bs_stack_modal"

    override fun canBuild(eventName: String) = eventName == "DisplayBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Displays a non-modal bottom sheet with a server-defined tile tree. No scrim: the " +
                    "screen behind it stays visible and interactive. The difference from DisplayModalBottomSheet " +
                    "isn't visual, it's about intent. The modal opens in its own window and interrupts: while " +
                    "it's open, the user can only interact with it. The non-modal renders inside the screen's " +
                    "layout and coexists with it — it's meant for persistent panels like a media player, live " +
                    "filters, or a summary that follows navigation. If the user needs to resolve the sheet " +
                    "before continuing, use the modal instead."
            )

            ShowroomSectionTitle("Interactive demo")

            ShowroomDemoCard(title = "1. Player — sheet sized to its content") {
                ShowroomParagraph(
                    "Open the sheet and keep scrolling this page behind it: with no scrim, nothing is blocked. " +
                        "That's the behavior that sets the non-modal apart."
                )
                Button(
                    text = "Open player",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = PLAYER_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Now playing") {
                                    Paragraph(
                                        "Scroll the page behind this sheet — it keeps responding, and the " +
                                            "screen's buttons stay clickable."
                                    )
                                    Button(
                                        text = "Snackbar from inside the sheet",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar over the non-modal bottom sheet",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(PLAYER_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. fill + allowsPartialExpansion — opens halfway") {
                ShowroomParagraph(
                    "Same height mechanics as the modal: fill sends the sheet to the top, and " +
                        "allowsPartialExpansion creates the half-screen resting point where it opens."
                )
                Button(
                    text = "Open half-screen list",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = PARTIAL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            allowsPartialExpansion = true,
                            tiles = {
                                SheetBody(title = "Album tracks") {
                                    (1..20).forEach { index ->
                                        SimpleText(
                                            text = "$index. Sample track",
                                            typography = typographyBodyMedium()
                                        )
                                    }
                                    CloseSheetButton(PARTIAL_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. isCancellable = false — only the button gets you out") {
                ShowroomParagraph(
                    "No swipe-down and no back press. Since the non-modal has no scrim, those were the only " +
                        "gesture-based exits — only DismissBottomSheet is left. Note that the page behind it " +
                        "keeps scrolling normally: locking the sheet doesn't lock the screen."
                )
                Button(
                    text = "Open locked sheet",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = BLOCKING_SHEET_ID,
                            isCancellable = false,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Upload in progress") {
                                    Paragraph(
                                        "Try swiping this sheet down or pressing back — it doesn't budge. Only " +
                                            "the button below closes it."
                                    )
                                    Button(
                                        text = "Cancel upload",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                bottomSheetId = BLOCKING_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Upload cancelled",
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

            ShowroomDemoCard(title = "4. Coexisting with a modal") {
                ShowroomParagraph(
                    "Open the non-modal, and with it open, open a modal on top. The modal takes the window and " +
                        "the scrim; the non-modal stays on the stack underneath, and reappears intact once the " +
                        "modal closes."
                )
                Button(
                    text = "Open non-modal",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = STACK_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Non-modal layer") {
                                    Paragraph("id: $STACK_SHEET_ID")
                                    Button(
                                        text = "Open a modal on top",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = STACK_MODAL_ID,
                                                isCancellable = true,
                                                fill = false,
                                                tiles = {
                                                    SheetBody(title = "Modal layer") {
                                                        Paragraph(
                                                            "This one has a scrim and its own window. Close it " +
                                                                "and the non-modal is right where it was."
                                                        )
                                                        Button(
                                                            text = "Close this modal",
                                                            buttonType = textButton(),
                                                            events = {
                                                                DismissModalBottomSheet(
                                                                    trigger = EventTriggers.onClick(),
                                                                    modalBottomSheetId = STACK_MODAL_ID
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseSheetButton(STACK_SHEET_ID, text = "Close this sheet")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DisplayBottomSheet(
                    trigger = EventTriggers.onClick(),
                    bottomSheetId = "now_playing",
                    isCancellable = true,
                    fill = false,
                    tiles = {
                        Column(id = "player_root") {
                            SimpleText(text = "Now playing")
                        }
                    }
                )
                """
            )

            ShowroomNote(
                "With no scrim, the non-modal sheet doesn't intercept taps outside it — including the " +
                    "screen's own content, which can fire events while the sheet is open. Keep that in mind " +
                    "when choosing between the two."
            )

            ShowroomRelated(
                names = listOf("DismissBottomSheet", "DisplayModalBottomSheet", "DisplayDialog", "DisplaySnackbar"),
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

    private fun TileSchemaBuilderScope.CloseSheetButton(
        sheetId: String,
        text: String = "Close"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissBottomSheet(
                    trigger = EventTriggers.onClick(),
                    bottomSheetId = sheetId
                )
            }
        )
    }
}
