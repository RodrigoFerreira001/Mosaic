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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.BottomAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton

object BottomAppBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "BottomAppBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 bottom bar with actions on the left and an optional FAB anchored " +
                    "on the right. actions runs inside a RowScope — children can use weight modifiers via " +
                    "LocalRowScope. floatingActionButton stays anchored on the right, separate from the " +
                    "normal actions flow."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "2 actions + FAB, all firing real events") {
                BottomAppBar(
                    style = { size(width = fillHorizontally()) },
                    actions = {
                        IconButton(
                            icon = icon("delete"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Delete clicked") }
                        )
                        IconButton(
                            icon = icon("share"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Share clicked") }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = icon("add"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "FAB clicked") }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                BottomAppBar(
                    id = "bottomBar",
                    actions = {
                        IconButton(icon = icon("delete"), events = { SendNetworkRequest(trigger = EventTriggers.onClick()) })
                        IconButton(icon = icon("share"), events = { TriggerEvent(eventId = "shareEvent", trigger = EventTriggers.onClick()) })
                    },
                    floatingActionButton = {
                        FloatingActionButton(icon = icon("add"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "create") })
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("TopAppBar", "FloatingActionButton", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
