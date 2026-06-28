package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.open_file_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema

object OpenFilePickerEventHolderBuilder : EventHolderBuilder<OpenFilePickerEventSchema, OpenFilePickerEventHolder> {

    override fun BuilderScope.build(
        eventSchema: OpenFilePickerEventSchema
    ) = with(eventSchema) {
        OpenFilePickerEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
