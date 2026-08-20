package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.defaultIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedIconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object IconButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "IconButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 button with no label, just an icon — for toolbar actions and " +
                    "compact controls (back, search, close). Like Button, but without text: only the icon " +
                    "and the buttonType (visual style) matter. Prefer IconButton when the icon's meaning is " +
                    "already obvious from context — otherwise use Button with icon + text."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "The 4 buttonType values increment the same counter") {
                SimpleText(id = "icon_button_counter", text = "Tap any button")
                Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = defaultIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = filledIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = filledTonalIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = outlinedIconButton(),
                        events = { iconButtonFeedback() }
                    )
                }
            }

            ShowroomSectionTitle("enabled / loading")
            ShowroomDemoCard(title = "enabled = false disables interactivity for real; loading = true forces disabled + spinner") {
                Row(arrangement = arrangeHorizontallySpacedBy(16), alignment = alignVerticallyToCenter()) {
                    IconButton(icon = icon("delete"), buttonType = filledIconButton(), enabled = true)
                    IconButton(icon = icon("delete"), buttonType = filledIconButton(), enabled = false)
                    IconButton(icon = icon("delete"), buttonType = filledIconButton(), enabled = true, loading = true)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                IconButton(
                    id = "backButton",
                    icon = icon("arrow_back"),
                    buttonType = defaultIconButton(),
                    events = {
                        NavigateUp(trigger = EventTriggers.onClick(), navigatorId = "root")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Button", "FloatingActionButton", "Tooltip"),
                destination = "tileDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.iconButtonFeedback() {
    UpdateTiles(
        trigger = EventTriggers.onClick(),
        updates = {
            update(
                tileId = "icon_button_counter",
                updateData = inlineTileUpdateData("text" to "UpdateTiles fired by IconButton")
            )
        }
    )
}
