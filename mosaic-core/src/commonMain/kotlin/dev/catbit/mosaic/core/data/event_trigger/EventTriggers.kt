package dev.catbit.mosaic.core.data.event_trigger

import dev.catbit.mosaic.core.data.event_trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTextChangedEventTrigger

object EventTriggers {
    fun onClick() = OnClickEventTrigger
    fun onFailure() = OnFailureEventTrigger
    fun onLongPress() = OnLongPressEventTrigger
    fun onStart() = OnStartEventTrigger
    fun onSuccess() = OnSuccessEventTrigger
    fun onTextChanged() = OnTextChangedEventTrigger
    fun onMenuItemClick(itemId: String) = OnMenuItemClickEventTrigger(itemId)
}