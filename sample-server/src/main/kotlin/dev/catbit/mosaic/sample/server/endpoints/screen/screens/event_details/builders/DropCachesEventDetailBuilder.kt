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
import dev.catbit.mosaic.server.builder.event.builders.system.DropCaches
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DropCachesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DropCaches"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Discards local caches persisted by Mosaic — screens, the initial navigation " +
                    "graph, and/or the cache version — each controlled by an independent boolean flag. Useful " +
                    "in logout flows, environment/tenant switching, or \"force refresh\" buttons on " +
                    "settings/debug screens. Each flag is independent: you can discard just the screens cache " +
                    "without touching the initial graph, for example."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Each button fires DropCaches with a different flag combination") {
                SimpleText(
                    id = "drop_caches_status",
                    text = "Status: no cache discarded yet"
                )
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Only dropScreensCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropScreensCache = true", dropScreensCache = true, dropInitialGraphCache = false, dropVersionCache = false) }
                    )
                    Button(
                        text = "Only dropInitialGraphCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropInitialGraphCache = true", dropScreensCache = false, dropInitialGraphCache = true, dropVersionCache = false) }
                    )
                    Button(
                        text = "Only dropVersionCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropVersionCache = true", dropScreensCache = false, dropInitialGraphCache = false, dropVersionCache = true) }
                    )
                    Button(
                        text = "Clear everything",
                        events = { dropCachesDemo("all 3 flags = true", dropScreensCache = true, dropInitialGraphCache = true, dropVersionCache = true) }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DropCaches(
                    trigger = EventTriggers.onClick(),
                    dropScreensCache = true,
                    dropInitialGraphCache = true,
                    dropVersionCache = true,
                    events = {
                        RefreshScreen(trigger = EventTriggers.onSuccess())
                    }
                )
                """
            )

            ShowroomNote(
                "DropCaches is destructive: force a reload of the affected screens (RefreshScreen/GetScreen) " +
                    "after discarding, otherwise the UI may keep showing stale data until the next navigation."
            )

            ShowroomRelated(
                names = listOf("RefreshScreen", "GetScreen", "BroadcastToSystem"),
                destination = "eventDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.dropCachesDemo(
    label: String,
    dropScreensCache: Boolean,
    dropInitialGraphCache: Boolean,
    dropVersionCache: Boolean,
) {
    DropCaches(
        trigger = EventTriggers.onClick(),
        dropScreensCache = dropScreensCache,
        dropInitialGraphCache = dropInitialGraphCache,
        dropVersionCache = dropVersionCache,
        events = {
            UpdateTiles(
                trigger = EventTriggers.onSuccess(),
                updates = {
                    update(
                        tileId = "drop_caches_status",
                        updateData = inlineTileUpdateData("text" to "Status: caches discarded ($label)")
                    )
                }
            )
        }
    )
}
