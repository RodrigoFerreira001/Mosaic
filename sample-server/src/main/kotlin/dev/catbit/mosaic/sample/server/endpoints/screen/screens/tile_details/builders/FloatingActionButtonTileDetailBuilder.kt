package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.defaultFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.buttons.largeFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.buttons.mediumFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object FloatingActionButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FloatingActionButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 FAB showing a single icon, in three sizes — a screen's primary, " +
                    "most persistent action. Use it for the screen's most prominent action (e.g. compose, add, " +
                    "create), which should stay reachable even while the screen scrolls. Unlike Button, the " +
                    "FAB only has an icon — no text label — and it has no enabled/loading state: Material 3 " +
                    "FABs don't support being disabled in place."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "The 3 sizes, one DisplaySnackbar per click") {
                Row(arrangement = arrangeHorizontallySpacedBy(16), alignment = alignVerticallyToCenter()) {
                    FloatingActionButton(
                        icon = icon("add"),
                        size = defaultFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "defaultFloatingActionButon()")
                        }
                    )
                    FloatingActionButton(
                        icon = icon("edit"),
                        size = mediumFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "mediumFloatingActionButon()")
                        }
                    )
                    FloatingActionButton(
                        icon = icon("create"),
                        size = largeFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "largeFloatingActionButon()")
                        }
                    )
                }
            }

            ShowroomSectionTitle("Hiding a FAB whose action isn't available")
            ShowroomDemoCard(title = "No enabled — toggle visibility instead") {
                Box(style = { size(width = fillHorizontally(), height = fixedVertically(72)) }) {
                    FloatingActionButton(
                        id = "fab_visibility_demo",
                        icon = icon("add"),
                        size = mediumFloatingActionButon()
                    )
                }
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    Button(
                        text = "Hide FAB",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "fab_visibility_demo", updateData = inlineTileUpdateData("visibility" to "GONE"))
                                }
                            )
                        }
                    )
                    Button(
                        text = "Show FAB",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "fab_visibility_demo", updateData = inlineTileUpdateData("visibility" to "VISIBLE"))
                                }
                            )
                        }
                    )
                }
            }
            ShowroomNote(
                "Material 3 FABs intentionally have no enabled parameter — a disabled-but-visible FAB fights " +
                    "the emphasis it's supposed to carry. Hide it entirely with the visibility field every tile " +
                    "has (mirrors an AnimatedVisibility around the FAB in plain Compose) instead of trying to " +
                    "gray it out."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                FloatingActionButton(
                    id = "addFab",
                    icon = icon("add"),
                    size = mediumFloatingActionButon(),
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "create_item")
                    }
                )

                // Hiding it when the action isn't available right now:
                UpdateTiles(
                    trigger = EventTriggers.onSuccess(),
                    updates = { update(tileId = "addFab", updateData = inlineTileUpdateData("visibility" to "GONE")) }
                )
                """
            )

            ShowroomRelated(
                names = listOf("IconButton", "Button", "BottomAppBar"),
                destination = "tileDetails"
            )
        }
    }
}
