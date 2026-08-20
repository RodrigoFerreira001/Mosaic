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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayBottomSheetEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissModalBottomSheetEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissDialogEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissNavigationDrawerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DismissSnackbarEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayModalBottomSheetEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayDialogEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplayNavigationDrawerEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DisplaySnackbarEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DownloadFileEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DownloadFileToDiskEventDetailBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.DownloadFileToMemoryEventDetailBuilder
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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders.UploadFileEventDetailBuilder
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
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.dokkaEventDocsUrl
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
 * Generic destination shared by every card in the Events catalog
 * ([dev.catbit.mosaic.sample.server.dsl.tiles.catalog.CatalogItem]) that reads the `event` query
 * parameter (staged by the card's tap via `SetIncomingDataToNetworkParamsHolderQueryParameters`)
 * and delegates the actual content to [eventDetailBuilderManager] — one dedicated
 * [dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder] per
 * event name. The tile equivalent lives in
 * [dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailsScreenBuilder]
 * (screen id `"tileDetails"`).
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
        DownloadFileToDiskEventDetailBuilder,
        DownloadFileToMemoryEventDetailBuilder,
        UploadFileEventDetailBuilder,
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
        DisplayModalBottomSheetEventDetailBuilder,
        DismissModalBottomSheetEventDetailBuilder,
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
                    },
                    actions = {
                        Tooltip(text = "Open code reference") {
                            IconButton(
                                icon = icon("code"),
                                events = {
                                    OpenExternalLink(
                                        trigger = EventTriggers.onClick(),
                                        url = dokkaEventDocsUrl(eventName)
                                    )
                                }
                            )
                        }
                    }
                )

                with(eventDetailBuilderManager) {
                    buildDetail(eventName)
                }
            }
        }
    }
}
