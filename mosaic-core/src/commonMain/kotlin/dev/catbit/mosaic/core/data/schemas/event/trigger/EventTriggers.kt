package dev.catbit.mosaic.core.data.schemas.event.trigger

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.InlineEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnBottomSheetDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataSentEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadPartialEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardDoneEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardGoEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardNextEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardPreviousEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSendEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLeadingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAdaptiveNavigationItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationBarItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationRailItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryClearedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrollThresholdReachedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarActionEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesWipedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTrailingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUnselectEventTrigger

object EventTriggers {
    fun inline() = InlineEventTrigger
    fun onAsyncImageLoadStart() = OnAsyncImageLoadStartEventTrigger
    fun onAsyncImageLoadFailure() = OnAsyncImageLoadFailureEventTrigger
    fun onAsyncImageLoadSuccess() = OnAsyncImageLoadSuccessEventTrigger
    fun onBottomSheetDismissed() = OnBottomSheetDismissedEventTrigger
    fun onClick() = OnClickEventTrigger
    fun onCheckChanged() = OnCheckChangedEventTrigger
    fun onCheckEvent() = OnCheckEventTrigger
    fun onOnCountdownTimerTick() = OnCountdownTimerTickEventTrigger
    fun onOnCountdownTimerFinish() = OnCountdownTimerFinishEventTrigger
    fun onDisplay() = OnDisplayEventTrigger
    fun onDownloadFailure() = OnDownloadFailureEventTrigger
    fun onDownloadFinish() = OnDownloadFinishEventTrigger
    fun onDownloadProgress() = OnDownloadProgressEventTrigger
    fun onDownloadPartial() = OnDownloadPartialEventTrigger
    fun onDataReceived() = OnDataReceivedEventTrigger
    fun onDataRemoved() = OnDataRemovedEventTrigger
    fun onDataSent() = OnDataSentEventTrigger
    fun onDataUpdated() = OnDataUpdatedEventTrigger
    fun onDialogDismissed() = OnDialogDismissedEventTrigger
    fun onFailure() = OnFailureEventTrigger
    fun onKeyboardDone() = OnKeyboardDoneEventTrigger
    fun onKeyboardGo() = OnKeyboardGoEventTrigger
    fun onKeyboardNext() = OnKeyboardNextEventTrigger
    fun onKeyboardPrevious() = OnKeyboardPreviousEventTrigger
    fun onKeyboardSearch() = OnKeyboardSearchEventTrigger
    fun onKeyboardSend() = OnKeyboardSendEventTrigger
    fun onLoadTilesFailure() = OnLoadTilesFailureEventTrigger
    fun onLoadTilesStart() = OnLoadTilesStartEventTrigger
    fun onLoadTilesSuccess() = OnLoadTilesSuccessEventTrigger
    fun onLeadingIconClick() = OnLeadingIconClickEventTrigger
    fun onTrailingIconClick() = OnTrailingIconClickEventTrigger
    fun onLongPress() = OnLongPressEventTrigger
    fun onMenuItemClick(itemId: String) = OnMenuItemClickEventTrigger(itemId)
    fun onAdaptiveNavigationItemClick(itemId: String) = OnAdaptiveNavigationItemClickEventTrigger(itemId)
    fun onNavigationBarItemClick(itemId: String) = OnNavigationBarItemClickEventTrigger(itemId)
    fun onNavigationDrawerDismissed() = OnNavigationDrawerDismissedEventTrigger
    fun onNavigationEntryChanged() = OnNavigationEntryChangedEventTrigger
    fun onNavigationEntrySet(screenId: String) = OnNavigationEntrySetEventTrigger(screenId)
    fun onNavigation() = OnNavigationEventTrigger
    fun onNavigationRailItemClick(itemId: String) = OnNavigationRailItemClickEventTrigger(itemId)
    fun onNetworkResponse(httpCode: Int) = OnNetworkResponseTrigger(httpCode)
    fun onPermissionsAcquired() = OnPermissionsAcquiredEventTrigger
    fun onPermissionsDenied() = OnPermissionsDeniedEventTrigger
    fun onQueryChanged() = OnQueryChangedEventTrigger
    fun onQueryCleared() = OnQueryClearedEventTrigger
    fun onSearch() = OnSearchEventTrigger
    fun onSnackbarAction() = OnSnackbarActionEventTrigger
    fun onSnackbarDismissed() = OnSnackbarDismissedEventTrigger
    fun onPageChanged(direction: OnPageChangedEventTrigger.Direction) = OnPageChangedEventTrigger(direction)
    fun onScrolled(direction: OnScrolledEventTrigger.ScrollDirection) = OnScrolledEventTrigger(direction)
    fun onScrollThresholdReached() = OnScrollThresholdReachedEventTrigger
    fun onOnSelectChanged() = OnSelectChangedEventTrigger
    fun onSelect() = OnSelectEventTrigger
    fun onStart() = OnStartEventTrigger
    fun onSuccess() = OnSuccessEventTrigger
    fun onTabItemClick(itemId: String) = OnTabItemClickEventTrigger(itemId)
    fun onTextChanged() = OnTextChangedEventTrigger
    fun onTilesAdded() = OnTilesAddedEventTrigger
    fun onTilesRemoved() = OnTilesRemovedEventTrigger
    fun onTilesReplaced() = OnTilesReplacedEventTrigger
    fun onTilesUpdated() = OnTilesUpdatedEventTrigger
    fun onTilesWiped() = OnTilesWipedEventTrigger
    fun onUncheck() = OnUncheckEventTrigger
    fun onUnselect() = OnUnselectEventTrigger
}