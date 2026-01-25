package dev.catbit.mosaic.core.data.event_trigger

import dev.catbit.mosaic.core.data.event_trigger.triggers.OnBottomSheetDismissedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataRemovedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataSentEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesWipedEventTrigger

object EventTriggers {
    fun onBottomSheetDismissed() = OnBottomSheetDismissedEventTrigger
    fun onClick() = OnClickEventTrigger
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
}