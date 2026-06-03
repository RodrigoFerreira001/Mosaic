package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file

import dev.catbit.mosaic.client.domain.download.DownloadFileUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DownloadFileEventRunner : EventRunner<DownloadFileEventSchema> {

    override fun EventRunningScope.runEvent(event: DownloadFileEventSchema) {
        with(event) {
            runSuspendOnStateHolderScope {

                onTrigger(EventTriggers.onStart())

                get<DownloadFileUseCase>()(
                    DownloadFileUseCase.Params(
                        url = url,
                        headers = headers,
                        body = body,
                        httpMethod = method.toKtorHttpMethod(),
                        onProgress = { progress ->
                            onTrigger(
                                eventTrigger = EventTriggers.onDownloadProgress(),
                                data = progress
                            )
                        },
                        onBytesReceived = { bytes ->
                            onTrigger(
                                eventTrigger = EventTriggers.onDownloadPartial(),
                                data = bytes
                            )
                        },
                        onDownloadFinished = { totalBytes ->
                            onTrigger(
                                eventTrigger = EventTriggers.onDownloadFinish(),
                                data = totalBytes
                            )
                            onTrigger(
                                eventTrigger = EventTriggers.onSuccess(),
                                data = totalBytes
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
                                tag = "DownloadFileEventRunner",
                                throwable = failure
                            )
                        }
                    )
                )
            }
        }
    }
}
