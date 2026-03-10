package dev.catbit.mosaic.core.data.schemas.event.trigger

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnBottomSheetDismissedEventTrigger
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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesWipedEventTrigger

object EventTriggers {
    fun onBottomSheetDismissed() = OnBottomSheetDismissedEventTrigger
    fun onClick() = OnClickEventTrigger
    fun onOnCountdownTimerTick() = OnCountdownTimerTickEventTrigger
    fun onOnCountdownTimerFinish() = OnCountdownTimerFinishEventTrigger
    fun onDisplay() = OnDisplayEventTrigger
    fun onDataReceived() = OnDataReceivedEventTrigger
    fun onDataRemoved() = OnDataRemovedEventTrigger
    fun onDataSent() = OnDataSentEventTrigger
    fun onDataUpdated() = OnDataUpdatedEventTrigger
    fun onDialogDismissed() = OnDialogDismissedEventTrigger
    fun onFailure() = OnFailureEventTrigger
    fun onLongPress() = OnLongPressEventTrigger
    fun onMenuItemClick(itemId: String) = OnMenuItemClickEventTrigger(itemId)
    fun onNavigationDrawerDismissed() = OnNavigationDrawerDismissedEventTrigger
    fun onNavigation() = OnNavigationEventTrigger
    fun onNetworkResponse(httpCode: Int) = OnNetworkResponseTrigger(httpCode)
    fun onPermissionsAcquired() = OnPermissionsAcquiredEventTrigger
    fun onPermissionsDenied() = OnPermissionsDeniedEventTrigger
    fun onScrolled() = OnScrolledEventTrigger
    fun onStart() = OnStartEventTrigger
    fun onSuccess() = OnSuccessEventTrigger
    fun onTextChanged() = OnTextChangedEventTrigger
    fun onTilesAdded() = OnTilesAddedEventTrigger
    fun onTilesRemoved() = OnTilesRemovedEventTrigger
    fun onTilesReplaced() = OnTilesReplacedEventTrigger
    fun onTilesUpdated() = OnTilesUpdatedEventTrigger
    fun onTilesWiped() = OnTilesWipedEventTrigger
    fun onDownloadFailureEventTrigger() = OnDownloadFailureEventTrigger
    fun onDownloadFinishEventTrigger() = OnDownloadFinishEventTrigger
    fun onDownloadProgressEventTrigger() = OnDownloadProgressEventTrigger
    fun onDownloadPartialEventTrigger() = OnDownloadPartialEventTrigger
}