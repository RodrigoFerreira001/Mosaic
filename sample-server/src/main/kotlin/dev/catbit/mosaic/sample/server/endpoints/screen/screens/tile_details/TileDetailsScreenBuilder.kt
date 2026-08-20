package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.AdaptiveVisibilityTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.AssistChipTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.AsyncImageTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.BadgeTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.BottomAppBarTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.BoxTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.ButtonTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.CardTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.CarouselTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.CheckboxTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.CircularProgressIndicatorTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.ColumnTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.DatePickerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.DropdownListTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.FilterChipTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.FlexBoxTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.FloatingActionButtonTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.FlowRowTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.GridTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.IconButtonTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.IconTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.ImageTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.InputChipTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.LazyColumnTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.LazyRowTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.LazyTilesTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.LinearProgressIndicatorTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.MenuTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.NavigationBarTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.NavigationRailTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.NestedNavigationGraphTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.PagerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.PopupTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.PullToRefreshTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.RadioButtonTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.RowTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SearchBarTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SelectionContainerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.ShimmerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SimpleTextTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SuggestionChipTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SwitchTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.SystemBroadcastListenerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.TabsTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.TextFieldTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.TimePickerTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.TooltipTileDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders.TopAppBarTileDetailBuilder
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.dokkaTileDocsUrl
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.event.builders.system.OpenExternalLink
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.Tooltip
import io.ktor.server.routing.RoutingCall

/**
 * Generic destination shared by every card in the Tiles catalog
 * ([dev.catbit.mosaic.sample.server.dsl.tiles.catalog.CatalogItem]) that reads the `event` query
 * parameter (staged by the card's tap via `SetIncomingDataToNetworkParamsHolderQueryParameters`,
 * carrying the tile name) and delegates the actual content to [tileDetailBuilderManager] — one
 * dedicated [TileDetailBuilder] per tile name.
 */
private val tileDetailBuilderManager = TileDetailBuilderManager(
    builders = listOf(
        AdaptiveVisibilityTileDetailBuilder,
                AssistChipTileDetailBuilder,
                AsyncImageTileDetailBuilder,
                BadgeTileDetailBuilder,
                BottomAppBarTileDetailBuilder,
                BoxTileDetailBuilder,
                ButtonTileDetailBuilder,
                CardTileDetailBuilder,
                CarouselTileDetailBuilder,
                CheckboxTileDetailBuilder,
                CircularProgressIndicatorTileDetailBuilder,
                ColumnTileDetailBuilder,
                DatePickerTileDetailBuilder,
                DropdownListTileDetailBuilder,
                FilterChipTileDetailBuilder,
                FlexBoxTileDetailBuilder,
                FloatingActionButtonTileDetailBuilder,
                FlowRowTileDetailBuilder,
                GridTileDetailBuilder,
                IconButtonTileDetailBuilder,
                IconTileDetailBuilder,
                ImageTileDetailBuilder,
                InputChipTileDetailBuilder,
                LazyColumnTileDetailBuilder,
                LazyRowTileDetailBuilder,
                LazyTilesTileDetailBuilder,
                LinearProgressIndicatorTileDetailBuilder,
                MenuTileDetailBuilder,
                NavigationBarTileDetailBuilder,
                NavigationRailTileDetailBuilder,
                NestedNavigationGraphTileDetailBuilder,
                PagerTileDetailBuilder,
                PopupTileDetailBuilder,
                PullToRefreshTileDetailBuilder,
                RadioButtonTileDetailBuilder,
                RowTileDetailBuilder,
                SearchBarTileDetailBuilder,
                SelectionContainerTileDetailBuilder,
                ShimmerTileDetailBuilder,
                SimpleTextTileDetailBuilder,
                SuggestionChipTileDetailBuilder,
                SwitchTileDetailBuilder,
                SystemBroadcastListenerTileDetailBuilder,
                TabsTileDetailBuilder,
                TextFieldTileDetailBuilder,
                TimePickerTileDetailBuilder,
                TooltipTileDetailBuilder,
                TopAppBarTileDetailBuilder,
    )
)

object TileDetailsScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "tileDetails"

    override suspend fun RoutingCall.build(): ScreenResponse {
        val tileName = request.queryParameters["event"] ?: "?"

        return Screen(id = "tileDetails") {
            Column(
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                    windowInsets(windowInsetsSystemBars())
                }
            ) {
                TopAppBar(
                    style = {
                        size(
                            width = fillHorizontally()
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            icon = icon("arrow_back"),
                            events = {
                                NavigateUp(
                                    trigger = EventTriggers.onClick(),
                                    navigatorId = "root"
                                )
                            }
                        )
                    },
                    title = {
                        SimpleText(
                            text = tileName
                        )
                    },
                    actions = {
                        Tooltip(text = "Open code reference") {
                            IconButton(
                                icon = icon("code"),
                                events = {
                                    OpenExternalLink(
                                        trigger = EventTriggers.onClick(),
                                        url = dokkaTileDocsUrl(tileName)
                                    )
                                }
                            )
                        }
                    }
                )

                with(tileDetailBuilderManager) {
                    buildDetail(tileName)
                }
            }
        }
    }
}
