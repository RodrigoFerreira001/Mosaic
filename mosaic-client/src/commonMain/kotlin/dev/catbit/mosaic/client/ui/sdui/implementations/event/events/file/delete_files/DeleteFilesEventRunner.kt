package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_files

import dev.catbit.mosaic.client.domain.file.DeleteFilesUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema.DeleteTarget
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DeleteFilesEventRunner : EventRunner<DeleteFilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DeleteFilesEventSchema) {
        with(event) {
            get<DeleteFilesUseCase>()(DeleteFilesUseCase.Params(target.fileNames()))
                .onSuccess {
                    onTrigger(EventTriggers.onSuccess())
                }
                .onFailure { failure ->
                    onTrigger(EventTriggers.onFailure(), data = failure)
                    logError(tag = "DeleteFilesEventRunner", throwable = failure)
                }
        }
    }
}

private fun DeleteTarget.fileNames() = when (this) {
    is DeleteTarget.Single -> listOf(fileName)
    is DeleteTarget.Multiple -> fileNames
}
