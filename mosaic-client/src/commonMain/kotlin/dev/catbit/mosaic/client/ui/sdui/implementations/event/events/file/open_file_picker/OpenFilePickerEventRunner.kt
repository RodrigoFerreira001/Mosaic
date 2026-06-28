package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.open_file_picker

import dev.catbit.mosaic.client.extensions.toFileKitMode
import dev.catbit.mosaic.client.extensions.toFileKitType
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OpenFilePickerEventRunner : EventRunner<OpenFilePickerEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: OpenFilePickerEventSchema) {
        withContext(Dispatchers.IO) {
            with(event) {
                try {
                    val pickedFile = FileKit.openFilePicker(
                        type = fileType.toFileKitType(),
                        mode = pickMode.toFileKitMode()
                    )

                    if (pickedFile != null) {
                        onTrigger(EventTriggers.onStart())
                        val data = pickedFile.readBytes()
                        onTrigger(EventTriggers.onSuccess(), data)
                    } else {
                        onTrigger(EventTriggers.onFailure())
                    }
                } catch (e: Throwable) {
                    onTrigger(EventTriggers.onFailure(), e)
                }
            }
        }
    }
}
