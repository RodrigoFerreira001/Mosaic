package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.get_image_from_galery

import dev.catbit.mosaic.client.extensions.sniffImageMimeType
import dev.catbit.mosaic.client.extensions.toCompressionConfig
import dev.catbit.mosaic.client.extensions.toResizeOptions
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import io.github.aryapreetam.cmpimgcompress.ImageCompressor
import io.github.aryapreetam.cmpimgcompress.ImageData
import io.github.aryapreetam.cmpimgcompress.ResizeOptions
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GetImageFromGalleryEventRunner : EventRunner<GetImageFromGalleryEventSchema> {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun EventRunningScope.runEvent(event: GetImageFromGalleryEventSchema) {
        with(event) {
            withContext(Dispatchers.IO) {
                try {
                    val pickedFile = FileKit.openFilePicker(
                        type = FileKitType.Image,
                        mode = FileKitMode.Single
                    )

                    if (pickedFile == null) {
                        onTrigger(EventTriggers.onFailure())
                        return@withContext
                    }

                    val rawBytes = pickedFile.readBytes()

                    val finalBytes = compression?.toCompressionConfig()?.let { compressionConfig ->
                        ImageCompressor.compress(
                            input = ImageData(rawBytes, sniffImageMimeType(rawBytes)),
                            config = compressionConfig,
                            resize = resize?.toResizeOptions() ?: ResizeOptions()
                        ).bytes
                    } ?: rawBytes

                    val outputData = when (outputType) {
                        GetImageFromGalleryEventSchema.OutputType.ArrayOfBytes -> finalBytes
                        GetImageFromGalleryEventSchema.OutputType.Base64 -> Base64.encode(finalBytes)
                    }

                    onTrigger(EventTriggers.onSuccess(), outputData)
                } catch (e: Throwable) {
                    onTrigger(EventTriggers.onFailure(), e)
                }
            }
        }
    }
}
