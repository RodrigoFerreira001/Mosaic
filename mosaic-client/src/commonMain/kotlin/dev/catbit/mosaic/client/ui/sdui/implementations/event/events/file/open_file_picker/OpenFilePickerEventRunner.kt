package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.open_file_picker

import dev.catbit.mosaic.client.extensions.asChunkedFlow
import dev.catbit.mosaic.client.extensions.toFileKitMode
import dev.catbit.mosaic.client.extensions.toFileKitType
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.decodeAsJsonMap
import dev.catbit.mosaic.core.data.schemas.event.events.file.FileOutputType
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object OpenFilePickerEventRunner : EventRunner<OpenFilePickerEventSchema> {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun EventRunningScope.runEvent(event: OpenFilePickerEventSchema) {
        with(event) {
            try {
                val pickedFile = FileKit.openFilePicker(
                    type = fileType.toFileKitType(),
                    mode = pickMode.toFileKitMode()
                )

                if (pickedFile == null) {
                    onTrigger(EventTriggers.onFailure())
                    return
                }

                when (outputType) {
                    FileOutputType.PlatformFile -> onTrigger(EventTriggers.onSuccess(), pickedFile)
                    FileOutputType.ArrayOfBytes -> onTrigger(EventTriggers.onSuccess(), pickedFile.readBytes())
                    FileOutputType.FlowOfBytes -> onTrigger(EventTriggers.onSuccess(), pickedFile.asChunkedFlow())
                    FileOutputType.MapObject ->
                        runCatching { pickedFile.readBytes().decodeAsJsonMap() }
                            .onSuccess { map -> onTrigger(EventTriggers.onSuccess(), map) }
                            .onFailure { onTrigger(EventTriggers.onFailure(), it) }
                    FileOutputType.Base64 ->
                        onTrigger(EventTriggers.onSuccess(), Base64.Default.encode(pickedFile.readBytes()))
                }
            } catch (e: Throwable) {
                onTrigger(EventTriggers.onFailure(), e)
            }
        }
    }
}
