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
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DismissDialog
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DisplayDialog
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissDialogEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_DIALOG_ID = "dismiss_dialog_demo"

    override fun canBuild(eventName: String) = eventName == "DismissDialog"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Programmatically closes an open dialog — the \"reaction\" side of the " +
                    "DisplayDialog / DismissDialog pair. Use it from inside the dialog's own content (a " +
                    "\"Cancel\"/\"Close\" button) or after an async action completes inside it, such as the " +
                    "success response of a SendNetworkRequest fired by a dialog button. Pass the same dialogId " +
                    "used in the DisplayDialog that opened the dialog."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Open the dialog and close it with DismissDialog") {
                Button(
                    text = "Open dialog",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = DEMO_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                Column(
                                    style = { padding(horizontal = 24, vertical = 24) },
                                    arrangement = arrangeVerticallySpacedBy(16)
                                ) {
                                    SimpleText(
                                        text = "Example dialog",
                                        typography = typographyTitleLarge()
                                    )
                                    SimpleText(
                                        text = "The button below is the DismissDialog event itself being demonstrated.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Close with DismissDialog",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = DEMO_DIALOG_ID
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
                Button(id = "confirm_btn", text = "Delete") {
                    events = {
                        DismissDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = "delete_confirmation"
                        )
                    }
                }
                """
            )

            ShowroomNote(
                "Since overlays are stackable, dialogId is what guarantees the right dialog gets closed when " +
                    "more than one is open — keep the same id on both sides of the pair."
            )

            ShowroomRelated(
                names = listOf("DisplayDialog", "DismissModalBottomSheet", "DismissNavigationDrawer", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
