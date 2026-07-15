package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection

import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetwork
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CheckIfHasInternetConnectionEventRunner : EventRunner<CheckIfHasInternetConnectionEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: CheckIfHasInternetConnectionEventSchema) {
        withContext(Dispatchers.IO) {
            onTrigger(EventTriggers.onStart())
            val hasInternetConnection = get<MosaicNetwork>().hasInternetConnection()
            onTrigger(if (hasInternetConnection) EventTriggers.onSuccess() else EventTriggers.onFailure())
        }
    }
}
