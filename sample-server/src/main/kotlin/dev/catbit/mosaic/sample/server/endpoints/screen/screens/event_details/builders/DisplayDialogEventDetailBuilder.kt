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
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DismissDialog
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DisplayDialog
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarLongDuration
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

object DisplayDialogEventDetailBuilder : EventDetailBuilder {

    private const val CONFIRM_DIALOG_ID = "display_dialog_confirm"
    private const val BLOCKING_DIALOG_ID = "display_dialog_blocking"
    private const val PLATFORM_WIDTH_DIALOG_ID = "display_dialog_platform_width"
    private const val WIDE_DIALOG_ID = "display_dialog_wide"
    private const val SNACKBAR_DIALOG_ID = "display_dialog_snackbar"
    private const val STACK_DIALOG_1_ID = "display_dialog_stack_1"
    private const val STACK_DIALOG_2_ID = "display_dialog_stack_2"
    private const val STACK_DIALOG_3_ID = "display_dialog_stack_3"
    private const val STACK_SHEET_ID = "display_dialog_stack_sheet"
    private const val ON_DISPLAY_DIALOG_ID = "display_dialog_on_display"

    override fun canBuild(eventName: String) = eventName == "DisplayDialog"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Displays a modal dialog with a server-defined tile tree. It appears immediately " +
                    "— no network call involved. Use it for confirmations, custom alerts, or small forms that " +
                    "need to interrupt the current flow. The dialog's content is a full tile tree: any tile and " +
                    "any event can live inside it, including a DismissDialog to close it from an internal button."
            )

            ShowroomSectionTitle("Interactive demo")

            ShowroomDemoCard(title = "1. Confirmation — default, cancellable dialog") {
                Button(
                    text = "Delete item",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = CONFIRM_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Delete this item?") {
                                    Paragraph("Tap outside or use back to cancel — isCancellable = true.")
                                    Button(
                                        text = "Delete",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = CONFIRM_DIALOG_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Item deleted",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "Undo"
                                            )
                                        }
                                    )
                                    CloseDialogButton(CONFIRM_DIALOG_ID, text = "Cancel")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. isCancellable = false — a decision is required") {
                ShowroomParagraph(
                    "Tap outside and back press do nothing. The dialog only leaves via an internal DismissDialog."
                )
                Button(
                    text = "Open blocking dialog",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = BLOCKING_DIALOG_ID,
                            isCancellable = false,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Session expired") {
                                    Paragraph(
                                        "Try tapping outside or pressing back — the dialog doesn't close. Pick " +
                                            "one of the options."
                                    )
                                    Button(
                                        text = "Sign in again",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = BLOCKING_DIALOG_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Redirecting to login...",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseDialogButton(BLOCKING_DIALOG_ID, text = "Log out")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. usePlatformDefaultWidth — platform width vs. content width") {
                ShowroomParagraph(
                    "Open both in sequence and compare: with true, the platform decides the width and the " +
                        "content adapts; with false, the tile tree drives it."
                )
                Button(
                    text = "usePlatformDefaultWidth = true",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = PLATFORM_WIDTH_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = true,
                            tiles = {
                                DialogBody(title = "Platform width") {
                                    Paragraph(
                                        "This dialog's width came from the system. Long text wraps within the " +
                                            "limit it imposes, and the content can't stretch."
                                    )
                                    CloseDialogButton(PLATFORM_WIDTH_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
                Button(
                    text = "usePlatformDefaultWidth = false",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = WIDE_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Content width") {
                                    Paragraph(
                                        "Here the tile tree controls the width — the dialog's Surface follows " +
                                            "whatever the content asks for."
                                    )
                                    CloseDialogButton(WIDE_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "4. Snackbar on top of the dialog") {
                ShowroomParagraph(
                    "The dialog opens its own window; the snackbar needs to come from a layer created after " +
                        "it in order to appear on top."
                )
                Button(
                    text = "Open dialog and fire snackbars from inside",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = SNACKBAR_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Z-order test") {
                                    Button(
                                        text = "Short snackbar",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar fired from inside the dialog",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Long snackbar with action",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Tap the action while the dialog is still open",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "OK"
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Snackbar and close the dialog together",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "The dialog is gone, the snackbar remains",
                                                duration = snackbarLongDuration()
                                            )
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = SNACKBAR_DIALOG_ID
                                            )
                                        }
                                    )
                                    CloseDialogButton(SNACKBAR_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "5. Stacking — three dialogs and a sheet") {
                ShowroomParagraph(
                    "Each layer has its own id, so the middle one can be closed without touching the others. " +
                        "Layer 3 opens a bottom sheet on top of everything."
                )
                Button(
                    text = "Open dialog stack",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = STACK_DIALOG_1_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Layer 1") {
                                    Paragraph("id: $STACK_DIALOG_1_ID")
                                    Button(
                                        text = "Open layer 2",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = STACK_DIALOG_2_ID,
                                                isCancellable = true,
                                                usePlatformDefaultWidth = false,
                                                tiles = {
                                                    DialogBody(title = "Layer 2") {
                                                        Paragraph("id: $STACK_DIALOG_2_ID")
                                                        Button(
                                                            text = "Open layer 3",
                                                            buttonType = filledTonalButton(),
                                                            events = {
                                                                DisplayDialog(
                                                                    trigger = EventTriggers.onClick(),
                                                                    dialogId = STACK_DIALOG_3_ID,
                                                                    isCancellable = true,
                                                                    usePlatformDefaultWidth = false,
                                                                    tiles = {
                                                                        DialogBody(title = "Layer 3") {
                                                                            Paragraph(
                                                                                "Three stacked dialogs. Close " +
                                                                                    "the middle one and watch the other two stay alive."
                                                                            )
                                                                            Button(
                                                                                text = "Open a bottom sheet over all three",
                                                                                buttonType = filledTonalButton(),
                                                                                events = {
                                                                                    DisplayModalBottomSheet(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        modalBottomSheetId = STACK_SHEET_ID,
                                                                                        isCancellable = true,
                                                                                        fill = false,
                                                                                        tiles = {
                                                                                            Column(
                                                                                                style = { padding(horizontal = 24, top = 8, bottom = 32) },
                                                                                                arrangement = arrangeVerticallySpacedBy(16)
                                                                                            ) {
                                                                                                SimpleText(
                                                                                                    text = "Sheet over three dialogs",
                                                                                                    typography = typographyTitleLarge()
                                                                                                )
                                                                                                Paragraph(
                                                                                                    "Four simultaneous overlays. The snackbar below " +
                                                                                                        "needs to appear above all of them."
                                                                                                )
                                                                                                Button(
                                                                                                    text = "Fire snackbar",
                                                                                                    buttonType = outlinedButton(),
                                                                                                    events = {
                                                                                                        DisplaySnackbar(
                                                                                                            trigger = EventTriggers.onClick(),
                                                                                                            message = "Snackbar over 3 dialogs + 1 sheet",
                                                                                                            duration = snackbarLongDuration()
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                                Button(
                                                                                                    text = "Close this sheet",
                                                                                                    buttonType = textButton(),
                                                                                                    events = {
                                                                                                        DismissModalBottomSheet(
                                                                                                            trigger = EventTriggers.onClick(),
                                                                                                            modalBottomSheetId = STACK_SHEET_ID
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Close layer 2 (the middle one)",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DismissDialog(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        dialogId = STACK_DIALOG_2_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                            CloseDialogButton(STACK_DIALOG_3_ID, text = "Close this layer")
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                        )
                                                        CloseDialogButton(STACK_DIALOG_2_ID, text = "Close this layer")
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseDialogButton(STACK_DIALOG_1_ID, text = "Close this layer")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "6. onDisplay — fires once the dialog is actually on screen") {
                ShowroomParagraph(
                    "OnSuccess fires the instant the dialog is registered — before Compose has even started " +
                        "composing it. OnDisplay fires slightly later, once the dialog has actually entered " +
                        "composition on screen. The gap is smaller here than on the bottom sheets — a dialog has " +
                        "no entrance animation to wait for — but it's still a separate moment, driven by Compose " +
                        "recomposition instead of the event's own registration."
                )
                Button(
                    text = "Open dialog wired to onDisplay",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = ON_DISPLAY_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onDisplay(),
                                    message = "Dialog is on screen now — this is onDisplay, not onSuccess",
                                    duration = snackbarShortDuration()
                                )
                            },
                            tiles = {
                                DialogBody(title = "Wired to onDisplay") {
                                    Paragraph(
                                        "The snackbar you just saw was triggered by this dialog's own onDisplay, " +
                                            "fired from inside DisplayDialog's events block."
                                    )
                                    CloseDialogButton(ON_DISPLAY_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DisplayDialog(
                    trigger = EventTriggers.onClick(),
                    dialogId = "delete_confirmation",
                    isCancellable = true,
                    usePlatformDefaultWidth = false,
                    tiles = {
                        Column(id = "dialog_root") {
                            SimpleText(id = "msg", text = "Delete this item?")
                            Button(id = "confirm_btn", text = "Delete") {
                                DismissDialog(
                                    trigger = EventTriggers.onClick(),
                                    dialogId = "delete_confirmation"
                                )
                            }
                        }
                    }
                )
                """
            )

            ShowroomNote(
                "The DisplayDialog → DismissDialog action/reaction pair is the most common pattern: the " +
                    "dialog's own content carries the button that closes it."
            )

            ShowroomRelated(
                names = listOf("DismissDialog", "DisplayModalBottomSheet", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }

    /** Standard body for this page's dialogs: title + spaced content. */
    private fun TileSchemaBuilderScope.DialogBody(
        title: String,
        content: TileSchemaBuilderScope.() -> Unit
    ) {
        Column(
            arrangement = arrangeVerticallySpacedBy(16)
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

    private fun TileSchemaBuilderScope.CloseDialogButton(
        dialogId: String,
        text: String = "Close"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissDialog(
                    trigger = EventTriggers.onClick(),
                    dialogId = dialogId
                )
            }
        )
    }
}
