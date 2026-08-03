package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.AddTilesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.BroadcastToSystemEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.CancelEventsEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ChangeScreenStateEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.CheckForReceivedDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.CheckIfHasInternetConnectionEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.CheckIfTileContainsChildrenEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DeleteFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissBottomSheetEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissDialogEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissNavigationDrawerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissSnackbarEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayBottomSheetEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayDialogEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayNavigationDrawerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplaySnackbarEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DownloadFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DropCachesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.EvaluateDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.GetDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.GetFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.GetImageFromGalleryEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.GetScreenEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.GetTileChildrenCountEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.NavigateEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.NavigateClearingStackEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.NavigateUpEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.OpenExternalLinkEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.OpenFilePickerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ProcessDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RefreshScreenEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ReloadLazyTilesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RemoveDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RemoveTilesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ReplaceTilesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RequestPermissionEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ResetThemeEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RunCancellableEventsEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.RunEventsEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SaveFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ScrollColumnTileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ScrollPagerTileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ScrollRowTileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SendDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SendFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SendNetworkRequestEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SetIncomingDataToNetworkParamsHolderBodyEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SetIncomingDataToNetworkParamsHolderHeadersEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SetIncomingDataToNetworkParamsHolderQueryParametersEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SetIncomingDataToNetworkParamsHolderUrlEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.SetThemeEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.StartCountdownTimerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.StartTimeLoopEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.StopRefreshingEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.TakePictureEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.ToggleMenuEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.TogglePopupEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.TransformDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.TriggerEventEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.UpdateDataEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.UpdateEventsEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.UpdateTilesEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.WipeTilesEventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer.DismissNavigationDrawer
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import io.ktor.server.routing.RoutingCall

/**
 * Generic destination shared by every card in the Tiles/Events catalogs
 * ([dev.catbit.mosaic.sample.server.dsl.tiles.catalog.CatalogItem]) that reads the `event` query
 * parameter (staged by the card's tap via `SetIncomingDataToNetworkParamsHolderQueryParameters`)
 * and delegates the actual content to [eventDetailBuilderManager] — one dedicated
 * [dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder] per
 * event name. Tile catalog entries have no matching builder yet (no "tileDetails" screen exists),
 * so tapping one currently throws [NoSuchElementException] — a known gap, not covered here.
 */
private val eventDetailBuilderManager = EventDetailBuilderManager(
    builders = listOf(
        AddTilesEventDetailBuilder,
        RemoveTilesEventDetailBuilder,
        UpdateTilesEventDetailBuilder,
        ReplaceTilesEventDetailBuilder,
        WipeTilesEventDetailBuilder,
        ReloadLazyTilesEventDetailBuilder,
        CheckIfTileContainsChildrenEventDetailBuilder,
        GetTileChildrenCountEventDetailBuilder,
        RunEventsEventDetailBuilder,
        UpdateEventsEventDetailBuilder,
        NavigateEventDetailBuilder,
        NavigateClearingStackEventDetailBuilder,
        NavigateUpEventDetailBuilder,
        GetScreenEventDetailBuilder,
        RefreshScreenEventDetailBuilder,
        ChangeScreenStateEventDetailBuilder,
        SendDataEventDetailBuilder,
        CheckForReceivedDataEventDetailBuilder,
        GetDataEventDetailBuilder,
        UpdateDataEventDetailBuilder,
        RemoveDataEventDetailBuilder,
        ProcessDataEventDetailBuilder,
        TransformDataEventDetailBuilder,
        EvaluateDataEventDetailBuilder,
        SendNetworkRequestEventDetailBuilder,
        DownloadFileEventDetailBuilder,
        SendFileEventDetailBuilder,
        SetIncomingDataToNetworkParamsHolderBodyEventDetailBuilder,
        SetIncomingDataToNetworkParamsHolderHeadersEventDetailBuilder,
        SetIncomingDataToNetworkParamsHolderUrlEventDetailBuilder,
        SetIncomingDataToNetworkParamsHolderQueryParametersEventDetailBuilder,
        SaveFileEventDetailBuilder,
        GetFileEventDetailBuilder,
        DeleteFileEventDetailBuilder,
        OpenFilePickerEventDetailBuilder,
        TakePictureEventDetailBuilder,
        GetImageFromGalleryEventDetailBuilder,
        DisplayDialogEventDetailBuilder,
        DismissDialogEventDetailBuilder,
        DisplayBottomSheetEventDetailBuilder,
        DismissBottomSheetEventDetailBuilder,
        DisplayNavigationDrawerEventDetailBuilder,
        DismissNavigationDrawerEventDetailBuilder,
        DisplaySnackbarEventDetailBuilder,
        DismissSnackbarEventDetailBuilder,
        TriggerEventEventDetailBuilder,
        RunCancellableEventsEventDetailBuilder,
        CancelEventsEventDetailBuilder,
        ToggleMenuEventDetailBuilder,
        TogglePopupEventDetailBuilder,
        StartCountdownTimerEventDetailBuilder,
        StartTimeLoopEventDetailBuilder,
        ScrollColumnTileEventDetailBuilder,
        ScrollRowTileEventDetailBuilder,
        ScrollPagerTileEventDetailBuilder,
        StopRefreshingEventDetailBuilder,
        RequestPermissionEventDetailBuilder,
        BroadcastToSystemEventDetailBuilder,
        CheckIfHasInternetConnectionEventDetailBuilder,
        DropCachesEventDetailBuilder,
        OpenExternalLinkEventDetailBuilder,
        SetThemeEventDetailBuilder,
        ResetThemeEventDetailBuilder,
    )
)

object EventDetailsScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "eventDetails"

    override suspend fun RoutingCall.build(): ScreenResponse {
        val eventName = request.queryParameters["event"] ?: "?"

        return Screen(
            id = "eventDetails",
            // Nenhuma tela deste app configura navigationDrawerTiles ainda — este drawer mínimo existe só
            // para dar um demo real a DisplayNavigationDrawer/DismissNavigationDrawer (ele nunca abre sozinho;
            // só quando um desses eventos for disparado explicitamente).
            navigationDrawerTiles = {
                Column(
                    id = "event_details_drawer_content",
                    style = {
                        size(width = fillHorizontally(), height = fillVertically())
                        padding(horizontal = 24, vertical = 24)
                        windowInsets(windowInsetsSystemBars())
                    },
                    arrangement = arrangeVerticallySpacedBy(16)
                ) {
                    SimpleText(
                        text = "Navigation drawer",
                        typography = typographyHeadlineSmall()
                    )
                    SimpleText(
                        text = "Este drawer existe só para demonstrar DisplayNavigationDrawer/" +
                            "DismissNavigationDrawer — nenhuma outra tela deste app o abre.",
                        typography = typographyBodyMedium(),
                        color = color(themeColorOnSurfaceVariant())
                    )
                    Button(
                        text = "Fechar com DismissNavigationDrawer",
                        events = {
                            DismissNavigationDrawer(trigger = EventTriggers.onClick())
                        }
                    )
                }
            }
        ) {
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
                            text = eventName
                        )
                    }
                )

                with(eventDetailBuilderManager) {
                    buildDetail(eventName)
                }
            }
        }
    }
}
