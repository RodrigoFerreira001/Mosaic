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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SearchBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SearchBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 search bar with a placeholder, optional leading/trailing icon, and " +
                    "a built-in animated clear button. Fixed height of 56dp. query is server-controlled — you " +
                    "need to resend it via UpdateTiles on every onQueryChanged to stay in sync. It's only the " +
                    "input field — no expanded state, no built-in suggestion list. Pair it with a LazyColumn " +
                    "whose filterChildrenByTerm tracks the query, as shown below."
            )

            ShowroomSectionTitle("Real usage: filtering a real list live")
            ShowroomDemoCard(title = "Type a fruit name — the list below filters via LazyColumn's filterChildrenByTerm") {
                SearchBar(
                    id = "search_bar_filter_demo",
                    placeholder = "Search fruit...",
                    leadingIcon = { Icon(icon = icon("search")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = {
                                update(tileId = "search_bar_filter_demo", updateData = incomingTileUpdateData())
                                update(
                                    tileId = "search_bar_filter_results",
                                    updateData = mappedIncomingTileUpdateData("filterChildrenByTerm" to "<//>")
                                )
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = {
                                update(tileId = "search_bar_filter_demo", updateData = inlineTileUpdateData("query" to ""))
                                update(tileId = "search_bar_filter_results", updateData = inlineTileUpdateData("filterChildrenByTerm" to null))
                            }
                        )
                        DisplaySnackbar(
                            trigger = EventTriggers.onSearch(),
                            message = "OnSearch fired — the IME search key was pressed"
                        )
                    }
                )
                LazyColumn(
                    id = "search_bar_filter_results",
                    style = { size(width = fillHorizontally(), height = fixedVertically(220)) },
                    arrangement = arrangeVerticallySpacedBy(4)
                ) {
                    listOf("Apple", "Apricot", "Banana", "Blueberry", "Cherry", "Grape", "Mango", "Orange", "Papaya", "Peach")
                        .forEach { fruit ->
                            SimpleText(
                                text = fruit,
                                searchableTerms = listOf(fruit),
                                style = { padding(vertical = 8) }
                            )
                        }
                }
            }
            ShowroomNote(
                text = "filterChildrenByTerm matches each child's own searchableTerms (case-insensitive " +
                    "substring) — children without searchableTerms are filtered out entirely, which is why " +
                    "every fruit item above declares its name as its searchableTerms."
            )

            ShowroomSectionTitle("leadingIcon vs trailingIcon")
            ShowroomDemoCard(title = "trailingIcon only shows while query is empty — cross-fades to the built-in clear button once you type") {
                SearchBar(
                    id = "search_bar_leading_demo",
                    placeholder = "Leading icon only",
                    leadingIcon = { Icon(icon = icon("search")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = { update(tileId = "search_bar_leading_demo", updateData = incomingTileUpdateData()) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = { update(tileId = "search_bar_leading_demo", updateData = inlineTileUpdateData("query" to "")) }
                        )
                    }
                )
                SearchBar(
                    id = "search_bar_trailing_demo",
                    placeholder = "Leading + trailing icon",
                    leadingIcon = { Icon(icon = icon("search")) },
                    trailingIcon = { Icon(icon = icon("tune")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = { update(tileId = "search_bar_trailing_demo", updateData = incomingTileUpdateData()) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = { update(tileId = "search_bar_trailing_demo", updateData = inlineTileUpdateData("query" to "")) }
                        )
                    }
                )
                SimpleText(
                    text = "tune icon disappears as soon as you type — the clear button takes over the same slot",
                    color = color(themeColorOnSurfaceVariant())
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SearchBar(
                    id = "searchBar",
                    placeholder = "Search environments...",
                    leadingIcon = { Icon(icon = icon("search")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = {
                                update(tileId = "searchBar", updateData = incomingTileUpdateData())
                                update(
                                    tileId = "environmentList",
                                    updateData = mappedIncomingTileUpdateData("filterChildrenByTerm" to "<//>")
                                )
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = {
                                update(tileId = "searchBar", updateData = inlineTileUpdateData("query" to ""))
                                update(tileId = "environmentList", updateData = inlineTileUpdateData("filterChildrenByTerm" to null))
                            }
                        )
                        SendNetworkRequest(
                            trigger = EventTriggers.onSearch(),
                            request = get(url = "/search", queryParameters = { "q" to incomingData })
                        )
                    }
                )
                LazyColumn(id = "environmentList") {
                    environments.forEach { env -> SimpleText(text = env.name, searchableTerms = listOf(env.name)) }
                }
                """
            )

            ShowroomRelated(
                names = listOf("TextField", "SuggestionChip", "LazyColumn"),
                destination = "tileDetails"
            )
        }
    }
}
