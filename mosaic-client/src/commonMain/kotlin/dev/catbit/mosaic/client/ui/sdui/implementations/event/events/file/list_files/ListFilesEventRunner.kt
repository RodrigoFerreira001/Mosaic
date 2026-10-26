package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.list_files

import dev.catbit.mosaic.client.domain.file.ListFilesUseCase
import dev.catbit.mosaic.client.exceptions.DirNotFoundException
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.ListFilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ListFilesEventRunner : EventRunner<ListFilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ListFilesEventSchema) {
        with(event) {
            get<ListFilesUseCase>()(ListFilesUseCase.Params(directory))
                .onSuccess { files ->
                    if (files.isEmpty()) {
                        onTrigger(EventTriggers.onEmptyDir())
                    } else {
                        onTrigger(EventTriggers.onSuccess(), data = files)
                    }
                }
                .onFailure { failure ->
                    onTrigger(EventTriggers.onFailure(), data = failure)
                    logError(tag = "ListFilesEventRunner", throwable = failure)
                }
        }
    }
}
