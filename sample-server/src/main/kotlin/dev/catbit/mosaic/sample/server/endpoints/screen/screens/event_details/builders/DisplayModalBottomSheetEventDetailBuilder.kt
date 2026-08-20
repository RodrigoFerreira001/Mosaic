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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayModalBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val SHARE_SHEET_ID = "display_mbs_share"
    private const val FILL_SHEET_ID = "display_mbs_fill"
    private const val PARTIAL_SHEET_ID = "display_mbs_partial"
    private const val BLOCKING_SHEET_ID = "display_mbs_blocking"
    private const val SNACKBAR_SHEET_ID = "display_mbs_snackbar"
    private const val STACK_SHEET_1_ID = "display_mbs_stack_1"
    private const val STACK_SHEET_2_ID = "display_mbs_stack_2"
    private const val STACK_DIALOG_ID = "display_mbs_stack_dialog"

    override fun canBuild(eventName: String) = eventName == "DisplayModalBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Displays a modal bottom sheet with a server-defined tile tree. It appears " +
                    "immediately — no network call involved. Use it for contextual menus, action sheets, short " +
                    "forms, or filter panels. Just like the dialog, the content is a full tile tree — include a " +
                    "DismissModalBottomSheet in some internal button to close it."
            )

            ShowroomSectionTitle("Interactive demo")

            ShowroomDemoCard(title = "1. Share — default sheet (cancellable, no fill)") {
                Button(
                    text = "Open share sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = SHARE_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Share via") {
                                    listOf(
                                        "link" to "Copy link",
                                        "chat" to "Message",
                                        "print" to "Print"
                                    ).forEach { (iconName, label) ->
                                        Row(
                                            style = { padding(vertical = 4) },
                                            arrangement = arrangeHorizontallySpacedBy(16),
                                            alignment = alignVerticallyToCenter(),
                                            events = {
                                                DismissModalBottomSheet(
                                                    trigger = EventTriggers.onClick(),
                                                    modalBottomSheetId = SHARE_SHEET_ID
                                                )
                                                DisplaySnackbar(
                                                    trigger = EventTriggers.onClick(),
                                                    message = "\"$label\" selected",
                                                    duration = snackbarShortDuration()
                                                )
                                            }
                                        ) {
                                            Icon(icon = icon(iconName))
                                            SimpleText(text = label, typography = typographyBodyMedium())
                                        }
                                    }
                                    CloseSheetButton(SHARE_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. fill = true — sheet filling the whole screen") {
                ShowroomParagraph(
                    "With fill = true the sheet goes all the way to the top of the screen and the form scrolls " +
                        "inside it. There's no partial state: swiping down closes the sheet directly. Compare " +
                        "with demo 1, where fill = false leaves the sheet sized to its own content."
                )
                Button(
                    text = "Open full-screen form",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = FILL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            tiles = {
                                SheetBody(title = "New address") {
                                    listOf("ZIP code", "Street", "Number", "Complement", "Neighborhood", "City").forEach { label ->
                                        SimpleText(
                                            text = label,
                                            typography = typographyBodyMedium(),
                                            color = color(themeColorOnSurfaceVariant())
                                        )
                                        TextField(style = { size(width = fillHorizontally()) })
                                    }
                                    Button(
                                        text = "Save",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = FILL_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Address saved",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(FILL_SHEET_ID, text = "Discard")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. allowsPartialExpansion = true — opens halfway") {
                ShowroomParagraph(
                    "The sheet gains a resting point at half the screen and opens there; swipe up to reach the " +
                        "top, and down to close. It needs to come together with fill (or with content that's " +
                        "provably tall): Material only creates that resting point for sheets taller than half " +
                        "the screen."
                )
                Button(
                    text = "Open half-screen list",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = PARTIAL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            allowsPartialExpansion = true,
                            tiles = {
                                SheetBody(title = "Select a country") {
                                    listOf(
                                        "South Africa", "Germany", "Argentina", "Australia", "Brazil",
                                        "Canada", "Chile", "China", "Colombia", "South Korea",
                                        "Spain", "United States", "France", "India", "Italy",
                                        "Japan", "Mexico", "Nigeria", "Portugal", "Uruguay"
                                    ).forEach { country ->
                                        Row(
                                            style = { padding(vertical = 8) },
                                            arrangement = arrangeHorizontallySpacedBy(16),
                                            alignment = alignVerticallyToCenter(),
                                            events = {
                                                DismissModalBottomSheet(
                                                    trigger = EventTriggers.onClick(),
                                                    modalBottomSheetId = PARTIAL_SHEET_ID
                                                )
                                                DisplaySnackbar(
                                                    trigger = EventTriggers.onClick(),
                                                    message = "$country selected",
                                                    duration = snackbarShortDuration()
                                                )
                                            }
                                        ) {
                                            Icon(icon = icon("public"))
                                            SimpleText(text = country, typography = typographyBodyMedium())
                                        }
                                    }
                                    CloseSheetButton(PARTIAL_SHEET_ID, text = "Cancel")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "4. isCancellable = false — only the button gets you out") {
                ShowroomParagraph(
                    "No drag handle, no swiping down, no closing by tapping the scrim, and no back press. The " +
                        "only way out is a DismissModalBottomSheet inside the sheet itself."
                )
                Button(
                    text = "Open mandatory sheet",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = BLOCKING_SHEET_ID,
                            isCancellable = false,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Accept the terms") {
                                    SimpleText(
                                        text = "Try swiping down or tapping outside — nothing happens. Use the " +
                                            "buttons below.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Accept",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = BLOCKING_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Terms accepted",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(BLOCKING_SHEET_ID, text = "Decline")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "5. Snackbar on top of the sheet") {
                ShowroomParagraph(
                    "The snackbar renders in a layer created after the sheet, so it should appear above it — " +
                        "even with the sheet in fill mode."
                )
                Button(
                    text = "Open sheet and fire snackbars from inside",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = SNACKBAR_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            tiles = {
                                SheetBody(title = "Z-order test") {
                                    SimpleText(
                                        text = "The sheet is in fill mode (full screen). The snackbar fired " +
                                            "below needs to appear above it, not behind.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Short snackbar",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar fired from inside the sheet",
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
                                                message = "This one stays longer — drag the sheet while it shows",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "OK"
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Two snackbars in sequence",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "First — should animate out before the second",
                                                duration = snackbarShortDuration()
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Second — came in after the first left",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(SNACKBAR_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "6. Stacking — sheet over sheet over dialog") {
                ShowroomParagraph(
                    "Each overlay is an independent layer with its own id. The middle sheet can close the one " +
                        "below it without closing itself, proving the stack isn't a single slot."
                )
                Button(
                    text = "Open overlay stack",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = STACK_SHEET_1_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Layer 1 — base sheet") {
                                    SimpleText(
                                        text = "id: $STACK_SHEET_1_ID",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Open layer 2 (another sheet)",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = STACK_SHEET_2_ID,
                                                isCancellable = true,
                                                fill = false,
                                                tiles = {
                                                    SheetBody(title = "Layer 2 — sheet over sheet") {
                                                        SimpleText(
                                                            text = "id: $STACK_SHEET_2_ID",
                                                            typography = typographyBodyMedium(),
                                                            color = color(themeColorOnSurfaceVariant())
                                                        )
                                                        Button(
                                                            text = "Open layer 3 (dialog)",
                                                            buttonType = filledTonalButton(),
                                                            events = {
                                                                DisplayDialog(
                                                                    trigger = EventTriggers.onClick(),
                                                                    dialogId = STACK_DIALOG_ID,
                                                                    isCancellable = true,
                                                                    usePlatformDefaultWidth = false,
                                                                    tiles = {
                                                                        DialogBody(title = "Layer 3 — dialog on top") {
                                                                            SimpleText(
                                                                                text = "Three overlays alive at the same time. " +
                                                                                    "Close them in whatever order you like.",
                                                                                typography = typographyBodyMedium(),
                                                                                color = color(themeColorOnSurfaceVariant())
                                                                            )
                                                                            Button(
                                                                                text = "Snackbar over all three",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DisplaySnackbar(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        message = "Snackbar above sheet + sheet + dialog",
                                                                                        duration = snackbarLongDuration()
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Close layer 1's sheet",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DismissModalBottomSheet(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        modalBottomSheetId = STACK_SHEET_1_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Close this dialog",
                                                                                buttonType = textButton(),
                                                                                events = {
                                                                                    DismissDialog(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        dialogId = STACK_DIALOG_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                        )
                                                        Button(
                                                            text = "Close only layer 1 (the bottom one)",
                                                            buttonType = outlinedButton(),
                                                            events = {
                                                                DismissModalBottomSheet(
                                                                    trigger = EventTriggers.onClick(),
                                                                    modalBottomSheetId = STACK_SHEET_1_ID
                                                                )
                                                            }
                                                        )
                                                        CloseSheetButton(STACK_SHEET_2_ID, text = "Close this layer")
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseSheetButton(STACK_SHEET_1_ID, text = "Close this layer")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DisplayModalBottomSheet(
                    trigger = EventTriggers.onClick(),
                    modalBottomSheetId = "share_sheet",
                    isCancellable = true,
                    fill = false,
                    allowsPartialExpansion = false,
                    tiles = {
                        Column(id = "sheet_root") {
                            SimpleText(text = "Share via")
                        }
                    }
                )
                """
            )

            ShowroomNote(
                "fill and allowsPartialExpansion are independent: fill decides the sheet's height, " +
                    "allowsPartialExpansion decides whether there's a resting point halfway. The classic " +
                    "Material combination (a long list that opens halfway) is both set to true."
            )

            ShowroomRelated(
                names = listOf("DismissModalBottomSheet", "DisplayDialog", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }

    /**
     * Standard body for this page's sheets: title + content, with the breathing room the sheet doesn't
     * give on its own. Scrollable because the sheet caps its height to what fits on screen — without
     * this, long content (the fill demo's form, for example) gets cut off and unreachable.
     */
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

    /** Same body, for the dialog used in the stacking demo. */
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

    private fun TileSchemaBuilderScope.CloseSheetButton(
        sheetId: String,
        text: String = "Close"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissModalBottomSheet(
                    trigger = EventTriggers.onClick(),
                    modalBottomSheetId = sheetId
                )
            }
        )
    }
}
