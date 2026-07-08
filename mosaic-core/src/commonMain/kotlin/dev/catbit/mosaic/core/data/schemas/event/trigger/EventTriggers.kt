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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDateSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListItemSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointSatisfiedEventTrigger
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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationBarItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationRailItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionRationaleEventTrigger
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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSystemBroadcastEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesWipedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTrailingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUnselectEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUploadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointSatisfiedEventTrigger

object EventTriggers {
    fun inline() = InlineEventTrigger
    fun onAsyncImageLoadStart() = OnAsyncImageLoadStartEventTrigger
    fun onAsyncImageLoadFailure() = OnAsyncImageLoadFailureEventTrigger
    fun onAsyncImageLoadSuccess() = OnAsyncImageLoadSuccessEventTrigger
    fun onBottomSheetDismissed() = OnBottomSheetDismissedEventTrigger
    fun onClick() = OnClickEventTrigger
    fun onCheckChanged() = OnCheckChangedEventTrigger
    fun onCheck() = OnCheckEventTrigger
    fun onOnCountdownTimerTick() = OnCountdownTimerTickEventTrigger
    fun onOnCountdownTimerFinish() = OnCountdownTimerFinishEventTrigger
    fun onDisplay() = OnDisplayEventTrigger
    fun onDownloadFailure() = OnDownloadFailureEventTrigger
    fun onDownloadFinish() = OnDownloadFinishEventTrigger
    fun onDownloadProgress() = OnDownloadProgressEventTrigger
    fun onDataReceived() = OnDataReceivedEventTrigger
    fun onDataRemoved() = OnDataRemovedEventTrigger
    fun onDataSent() = OnDataSentEventTrigger
    fun onDataUpdated() = OnDataUpdatedEventTrigger
    fun onDatePickerOpen() = OnDatePickerOpenEventTrigger
    fun onDatePickerClose() = OnDatePickerCloseEventTrigger
    fun onDateSelected() = OnDateSelectedEventTrigger
    fun onDialogDismissed() = OnDialogDismissedEventTrigger
    fun onDropdownListClose() = OnDropdownListCloseEventTrigger
    fun onDropdownListItemSelected(id: String) = OnDropdownListItemSelectedEventTrigger(id)
    fun onDropdownListOpen() = OnDropdownListOpenEventTrigger
    fun onFailure() = OnFailureEventTrigger
    fun onHeightBreakpointSatisfied() = OnHeightBreakpointSatisfiedEventTrigger
    fun onHeightBreakpointNotSatisfied() = OnHeightBreakpointNotSatisfiedEventTrigger
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
    fun onNavigationBarItemClick(itemId: String) = OnNavigationBarItemClickEventTrigger(itemId)
    fun onNavigationDrawerDismissed() = OnNavigationDrawerDismissedEventTrigger
    fun onNavigationEntryChanged() = OnNavigationEntryChangedEventTrigger
    fun onNavigationEntrySet(screenId: String) = OnNavigationEntrySetEventTrigger(screenId)
    fun onNavigation() = OnNavigationEventTrigger
    fun onNavigationRailItemClick(itemId: String) = OnNavigationRailItemClickEventTrigger(itemId)
    fun onNetworkFailure(httpCode: Int) = OnNetworkFailureEventTrigger(httpCode)
    fun onNetworkResponse(httpCode: Int) = OnNetworkResponseTrigger(httpCode)
    fun onPermissionsAcquired() = OnPermissionsAcquiredEventTrigger
    fun onPermissionsDenied() = OnPermissionsDeniedEventTrigger
    fun onPermissionRationale() = OnPermissionRationaleEventTrigger
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
    fun onSystemBroadcastEventTrigger(broadcastId: String) = OnSystemBroadcastEventTrigger(broadcastId)
    fun onTabItemClick(itemId: String) = OnTabItemClickEventTrigger(itemId)
    fun onTextChanged() = OnTextChangedEventTrigger
    fun onTilesAdded() = OnTilesAddedEventTrigger
    fun onTilesRemoved() = OnTilesRemovedEventTrigger
    fun onTilesReplaced() = OnTilesReplacedEventTrigger
    fun onTilesUpdated() = OnTilesUpdatedEventTrigger
    fun onTilesWiped() = OnTilesWipedEventTrigger
    fun onTimePickerOpen() = OnTimePickerOpenEventTrigger
    fun onTimePickerClose() = OnTimePickerCloseEventTrigger
    fun onTimeSelected() = OnTimeSelectedEventTrigger
    fun onUncheck() = OnUncheckEventTrigger
    fun onUnselect() = OnUnselectEventTrigger
    fun onUploadProgress() = OnUploadProgressEventTrigger
    fun onWidthBreakpointSatisfied() = OnWidthBreakpointSatisfiedEventTrigger
    fun onWidthBreakpointNotSatisfied() = OnWidthBreakpointNotSatisfiedEventTrigger
}