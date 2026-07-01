package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.upload_file

import dev.catbit.mosaic.client.data.data_sources.network.UploadResult
import dev.catbit.mosaic.client.domain.upload.UploadFileUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.toAny
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

object UploadFileEventRunner : EventRunner<UploadFileEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: UploadFileEventSchema) {
        with(event) {

            val platformFile = incomingData as? PlatformFile ?: run {
                onTrigger(EventTriggers.onFailure())
                logError(
                    tag = "UploadFileEventRunner",
                    throwable = Throwable("Incoming data is not a PlatformFile, nothing to upload")
                )
                return
            }

            onTrigger(EventTriggers.onStart())

            get<UploadFileUseCase>()(
                UploadFileUseCase.Params(
                    url = url,
                    headers = headers,
                    httpMethod = method.toKtorHttpMethod(),
                    contentType = contentType,
                    platformFile = platformFile,
                    onProgress = { progress ->
                        onTrigger(
                            eventTrigger = EventTriggers.onUploadProgress(),
                            data = progress
                        )
                    }
                )
            )
                .onSuccess { result ->

                    val data = when {
                        result.contentType?.contains("application/json") == true -> {
                            runCatching {
                                Json.parseToJsonElement(result.body.decodeToString())
                            }.getOrNull()?.toAny()
                        }

                        else -> result.body
                    }

                    val hasCustomResponseListener = triggerOwner.events?.any {
                        it.trigger == EventTriggers.onNetworkResponse(result.statusCode)
                                || it.trigger == EventTriggers.onNetworkFailure(result.statusCode)
                    } == true

                    if (hasCustomResponseListener && !result.isSuccess) {
                        onTrigger(
                            eventTrigger = EventTriggers.onNetworkFailure(result.statusCode),
                            data = data
                        )
                    } else if (hasCustomResponseListener && result.isSuccess) {
                        onTrigger(
                            eventTrigger = EventTriggers.onNetworkResponse(result.statusCode),
                            data = data
                        )
                    } else {
                        onTrigger(
                            eventTrigger = if (result.isSuccess) EventTriggers.onSuccess() else EventTriggers.onFailure(),
                            data = data
                        )
                    }
                }
                .onFailure { failure ->
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = failure
                    )
                    logError(
                        tag = "UploadFileEventRunner",
                        throwable = failure
                    )
                }
        }
    }
}
