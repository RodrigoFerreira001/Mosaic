package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object CheckIfHasInternetConnectionEventRunner : EventRunner<CheckIfHasInternetConnectionEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: CheckIfHasInternetConnectionEventSchema) {
        onTrigger(EventTriggers.onSuccess())
    }
}
