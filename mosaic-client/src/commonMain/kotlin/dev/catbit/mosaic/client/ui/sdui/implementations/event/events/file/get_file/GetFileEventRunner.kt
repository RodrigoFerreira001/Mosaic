package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file

import dev.catbit.mosaic.client.domain.file.GetFilePlatformFileUseCase
import dev.catbit.mosaic.client.domain.file.GetFileStreamingUseCase
import dev.catbit.mosaic.client.domain.file.GetFileUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.decodeAsJsonMap
import dev.catbit.mosaic.core.data.schemas.event.events.file.FileOutputType
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object GetFileEventRunner : EventRunner<GetFileEventSchema> {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun EventRunningScope.runEvent(event: GetFileEventSchema) {
        with(event) {
            when (outputType) {
                FileOutputType.ArrayOfBytes ->
                    get<GetFileUseCase>()(GetFileUseCase.Params(fileName))
                        .onSuccess { bytes ->
                            if (bytes != null) {
                                onTrigger(EventTriggers.onSuccess(), data = bytes)
                            } else {
                                onNotFound(fileName)
                            }
                        }
                        .onFailure { onReadFailure(it) }

                FileOutputType.FlowOfBytes ->
                    get<GetFileStreamingUseCase>()(GetFileStreamingUseCase.Params(fileName))
                        .onSuccess { flow ->
                            if (flow != null) {
                                onTrigger(EventTriggers.onSuccess(), data = flow)
                            } else {
                                onNotFound(fileName)
                            }
                        }
                        .onFailure { onReadFailure(it) }

                FileOutputType.PlatformFile ->
                    get<GetFilePlatformFileUseCase>()(GetFilePlatformFileUseCase.Params(fileName))
                        .onSuccess { platformFile ->
                            if (platformFile != null) {
                                onTrigger(EventTriggers.onSuccess(), data = platformFile)
                            } else {
                                onNotFound(fileName)
                            }
                        }
                        .onFailure { onReadFailure(it) }

                FileOutputType.MapObject ->
                    get<GetFileUseCase>()(GetFileUseCase.Params(fileName))
                        .onSuccess { bytes ->
                            if (bytes == null) {
                                onNotFound(fileName)
                                return@onSuccess
                            }
                            runCatching { bytes.decodeAsJsonMap() }
                                .onSuccess { map -> onTrigger(EventTriggers.onSuccess(), data = map) }
                                .onFailure { onReadFailure(it) }
                        }
                        .onFailure { onReadFailure(it) }

                FileOutputType.Base64 ->
                    get<GetFileUseCase>()(GetFileUseCase.Params(fileName))
                        .onSuccess { bytes ->
                            if (bytes != null) {
                                onTrigger(EventTriggers.onSuccess(), data = Base64.Default.encode(bytes))
                            } else {
                                onNotFound(fileName)
                            }
                        }
                        .onFailure { onReadFailure(it) }
            }
        }
    }

    private suspend fun EventRunningScope.onNotFound(fileName: String) {
        onReadFailure(NoSuchElementException("File not found: '$fileName'"))
    }

    private suspend fun EventRunningScope.onReadFailure(throwable: Throwable) {
        onTrigger(EventTriggers.onFailure(), data = throwable)
        logError(tag = "GetFileEventRunner", throwable = throwable)
    }
}
