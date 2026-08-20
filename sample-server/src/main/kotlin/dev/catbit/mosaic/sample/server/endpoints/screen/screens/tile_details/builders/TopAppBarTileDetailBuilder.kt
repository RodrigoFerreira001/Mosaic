package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TopAppBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TopAppBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 top bar in 4 styles: DEFAULT, CENTER_ALIGNED, MEDIUM (collapsible), " +
                    "and LARGE (collapsible). title, navigationIcon, and actions are independent slots — they " +
                    "aren't part of the normal RenderChildren tree. This is exactly the tile that builds the " +
                    "app bar on this very detail screen you're reading right now."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Title + navigationIcon + action, each one really clickable") {
                TopAppBar(
                    barStyle = TopAppBarTileSchema.TopAppBarStyle.CENTER_ALIGNED,
                    style = { size(width = fillHorizontally()) },
                    navigationIcon = {
                        IconButton(
                            icon = icon("menu"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "navigationIcon clicked") }
                        )
                    },
                    actions = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "action clicked") }
                        )
                    }
                ) {
                    SimpleText(text = "CENTER_ALIGNED")
                }
            }

            ShowroomSectionTitle("barStyle — the other 3 values")
            ShowroomDemoCard(title = "DEFAULT, MEDIUM, LARGE — no scroll-collapse behavior in any of them yet") {
                Column(arrangement = arrangeVerticallySpacedBy(8)) {
                    TopAppBar(
                        barStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT,
                        style = { size(width = fillHorizontally()) },
                        navigationIcon = { IconButton(icon = icon("menu")) }
                    ) { SimpleText(text = "DEFAULT") }
                    TopAppBar(
                        barStyle = TopAppBarTileSchema.TopAppBarStyle.MEDIUM,
                        style = { size(width = fillHorizontally()) },
                        navigationIcon = { IconButton(icon = icon("menu")) }
                    ) { SimpleText(text = "MEDIUM") }
                    TopAppBar(
                        barStyle = TopAppBarTileSchema.TopAppBarStyle.LARGE,
                        style = { size(width = fillHorizontally()) },
                        navigationIcon = { IconButton(icon = icon("menu")) }
                    ) { SimpleText(text = "LARGE") }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                TopAppBar(
                    id = "topBar",
                    barStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT,
                    navigationIcon = {
                        IconButton(icon = icon("arrow_back"), events = { NavigateUp(trigger = EventTriggers.onClick()) })
                    },
                    actions = {
                        IconButton(icon = icon("settings"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "settings") })
                    }
                ) {
                    SimpleText(text = "Environments", typography = typographyTitleLarge())
                }
                """
            )

            ShowroomRelated(
                names = listOf("BottomAppBar", "IconButton", "NavigationBar"),
                destination = "tileDetails"
            )
        }
    }
}
