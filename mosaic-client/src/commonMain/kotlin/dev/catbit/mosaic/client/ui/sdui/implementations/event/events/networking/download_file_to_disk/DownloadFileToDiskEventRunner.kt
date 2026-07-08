package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file_to_disk

import dev.catbit.mosaic.client.domain.download.DownloadFileToDiskUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToDiskEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DownloadFileToDiskEventRunner : EventRunner<DownloadFileToDiskEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: DownloadFileToDiskEventSchema) {
        with(event) {
            onTrigger(EventTriggers.onStart())

            get<DownloadFileToDiskUseCase>()(
                DownloadFileToDiskUseCase.Params(
                    url = url,
                    headers = headers,
                    body = body,
                    httpMethod = method.toKtorHttpMethod(),
                    targetFileName = targetFileName,
                    onProgress = { progress ->
                        onTrigger(
                            eventTrigger = EventTriggers.onDownloadProgress(),
                            data = progress
                        )
                    },
                    onDownloadFinished = {
                        onTrigger(
                            eventTrigger = EventTriggers.onDownloadFinish(),
                            data = targetFileName
                        )
                        onTrigger(
                            eventTrigger = EventTriggers.onSuccess(),
                            data = targetFileName
                        )
                    },
                    onDownloadFailure = { failure ->
                        onTrigger(
                            eventTrigger = EventTriggers.onDownloadFailure(),
                            data = failure
                        )
                        onTrigger(
                            eventTrigger = EventTriggers.onFailure(),
                            data = failure
                        )
                        logError(
                            tag = "DownloadFileToDiskEventRunner",
                            throwable = failure
                        )
                    }
                )
            )
        }
    }
}
