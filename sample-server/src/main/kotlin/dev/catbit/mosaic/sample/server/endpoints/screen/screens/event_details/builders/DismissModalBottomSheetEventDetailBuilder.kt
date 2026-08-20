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
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissModalBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_SHEET_ID = "dismiss_modal_bottom_sheet_demo"

    override fun canBuild(eventName: String) = eventName == "DismissModalBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Programmatically closes an open modal bottom sheet — the \"reaction\" side of " +
                    "the DisplayModalBottomSheet / DismissModalBottomSheet pair. Use it on any button/row inside " +
                    "the sheet that represents a selection or cancellation, or as a child of onSuccess for an " +
                    "async event fired from within it. Pass the same modalBottomSheetId used in the " +
                    "DisplayModalBottomSheet that opened the sheet."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Open the sheet and close it with DismissModalBottomSheet") {
                Button(
                    text = "Open bottom sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = DEMO_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                Column(
                                    style = { padding(horizontal = 24, top = 24, bottom = 32) },
                                    arrangement = arrangeVerticallySpacedBy(16)
                                ) {
                                    SimpleText(
                                        text = "Example bottom sheet",
                                        typography = typographyTitleLarge()
                                    )
                                    SimpleText(
                                        text = "The button below is the DismissModalBottomSheet event itself being demonstrated.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Close with DismissModalBottomSheet",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = DEMO_SHEET_ID
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Row(events = {
                    DismissModalBottomSheet(
                        trigger = EventTriggers.onClick(),
                        modalBottomSheetId = "share_sheet"
                    )
                }) {
                    SimpleText(text = "Cancel")
                }
                """
            )

            ShowroomNote(
                "DismissModalBottomSheet fires onSuccess() as soon as it marks the sheet to close, and " +
                    "onFailure() if no sheet with that id is on the stack — which also covers the case of " +
                    "closing the same sheet twice."
            )

            ShowroomRelated(
                names = listOf("DisplayModalBottomSheet", "DismissDialog", "DismissNavigationDrawer", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
