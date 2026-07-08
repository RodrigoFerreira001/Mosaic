package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file

import dev.catbit.mosaic.client.domain.file.GetFileUseCase
import dev.catbit.mosaic.client.domain.file.SaveFileUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SaveFileEventRunner : EventRunner<SaveFileEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: SaveFileEventSchema) {
        with(event) {
            val bytes = incomingData as? ByteArray
            if (bytes == null) {
                val failure = IllegalArgumentException("incomingData is not a ByteArray, nothing to save to '$fileName'")
                onTrigger(EventTriggers.onFailure(), data = failure)
                logError(tag = "SaveFileEventRunner", throwable = failure)
                return
            }

            if (!overrideIfExists) {
                val existing = get<GetFileUseCase>()(GetFileUseCase.Params(fileName)).getOrNull()
                if (existing != null) {
                    val failure = IllegalStateException("File '$fileName' already exists and overrideIfExists is false")
                    onTrigger(EventTriggers.onFailure(), data = failure)
                    logError(tag = "SaveFileEventRunner", throwable = failure)
                    return
                }
            }

            get<SaveFileUseCase>()(SaveFileUseCase.Params(fileName = fileName, data = bytes))
                .onSuccess {
                    onTrigger(EventTriggers.onSuccess())
                }
                .onFailure { failure ->
                    onTrigger(EventTriggers.onFailure(), data = failure)
                    logError(tag = "SaveFileEventRunner", throwable = failure)
                }
        }
    }
}
