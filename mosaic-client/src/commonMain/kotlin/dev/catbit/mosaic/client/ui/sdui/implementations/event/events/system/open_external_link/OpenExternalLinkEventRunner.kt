package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.open_external_link

import dev.catbit.mosaic.client.data.data_sources.system.openExternalLink
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.runSafely

object OpenExternalLinkEventRunner : EventRunner<OpenExternalLinkEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: OpenExternalLinkEventSchema) {
        runSafely(
            onError = { throwable ->
                onTrigger(EventTriggers.onFailure(), data = throwable)
                logError(tag = "OpenExternalLinkEventRunner", throwable = throwable)
            }
        ) {
            openExternalLink(event.url)
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
