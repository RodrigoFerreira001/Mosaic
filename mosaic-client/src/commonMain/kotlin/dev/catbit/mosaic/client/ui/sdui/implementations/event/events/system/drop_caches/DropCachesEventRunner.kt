package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.drop_caches

import dev.catbit.mosaic.client.domain.cache.DropCachesUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DropCachesEventRunner : EventRunner<DropCachesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DropCachesEventSchema) {
        get<DropCachesUseCase>()(
            DropCachesUseCase.Params(
                dropScreensCache = event.dropScreensCache,
                dropInitialGraphCache = event.dropInitialGraphCache,
                dropVersionCache = event.dropVersionCache
            )
        ).onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }.onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "DropCachesEventRunner", throwable = throwable)
        }
    }
}
