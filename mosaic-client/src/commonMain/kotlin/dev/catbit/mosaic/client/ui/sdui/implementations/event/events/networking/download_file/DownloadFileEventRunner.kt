package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file

import dev.catbit.mosaic.client.domain.download.DownloadFileUseCase
import dev.catbit.mosaic.client.exceptions.DownloadCancelledException
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DownloadFileEventRunner : EventRunner<DownloadFileEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: DownloadFileEventSchema) {
        with(event) {
            onTrigger(EventTriggers.onStart())

            get<DownloadFileUseCase>()(
                DownloadFileUseCase.Params(
                    url = url,
                    headers = headers,
                    body = body,
                    httpMethod = method.toKtorHttpMethod(),
                    targetFileName = targetFileName,
                    mimeType = mimeType,
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
                        if (failure is DownloadCancelledException) {
                            onTrigger(eventTrigger = EventTriggers.onCancelled())
                            return@Params
                        }

                        onTrigger(
                            eventTrigger = EventTriggers.onDownloadFailure(),
                            data = failure
                        )
                        onTrigger(
                            eventTrigger = EventTriggers.onFailure(),
                            data = failure
                        )
                        logError(
                            tag = "DownloadFileEventRunner",
                            throwable = failure
                        )
                    }
                )
            )
        }
    }
}
