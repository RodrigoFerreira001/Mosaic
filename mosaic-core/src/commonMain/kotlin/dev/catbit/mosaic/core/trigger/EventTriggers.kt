package dev.catbit.mosaic.core.trigger

import dev.catbit.mosaic.core.trigger.triggers.OnClickTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnFailureTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnLongPressTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnStartTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnSuccessTrigger

object EventTriggers {
    val OnClick = OnClickTrigger
    val OnFailure = OnFailureTrigger
    val OnLongPress = OnLongPressTrigger
    val OnStart = OnStartTrigger
    val OnSuccess = OnSuccessTrigger
}