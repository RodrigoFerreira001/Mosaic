package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.take_picture

import dev.catbit.mosaic.client.extensions.toCompressionConfig
import dev.catbit.mosaic.client.extensions.toResizeOptions
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import io.github.aryapreetam.cmpimgcompress.ImageCompressor
import io.github.aryapreetam.cmpimgcompress.ImageData
import io.github.aryapreetam.cmpimgcompress.ResizeOptions
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TakePictureEventRunner : EventRunner<TakePictureEventSchema> {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun EventRunningScope.runEvent(event: TakePictureEventSchema) {
        with(event) {
            withContext(Dispatchers.IO) {
                try {
                    val rawBytes = get<CameraManager>().takePicture()

                    if (rawBytes == null) {
                        onTrigger(EventTriggers.onFailure())
                        return@withContext
                    }

                    val finalBytes = compression?.toCompressionConfig()?.let { compressionConfig ->
                        ImageCompressor.compress(
                            input = ImageData(rawBytes, "image/png"),
                            config = compressionConfig,
                            resize = resize?.toResizeOptions() ?: ResizeOptions()
                        ).bytes
                    } ?: rawBytes

                    val outputData = when (outputType) {
                        TakePictureEventSchema.OutputType.ArrayOfBytes -> finalBytes
                        TakePictureEventSchema.OutputType.Base64 -> Base64.encode(finalBytes)
                    }

                    onTrigger(EventTriggers.onSuccess(), outputData)
                } catch (e: Throwable) {
                    onTrigger(EventTriggers.onFailure(), e)
                }
            }
        }
    }
}
