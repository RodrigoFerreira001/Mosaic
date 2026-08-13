package dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class DisplayModalBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val modalBottomSheetId: String,
    private val isCancellable: Boolean,
    private val fill: Boolean,
    private val allowsPartialExpansion: Boolean
) : EventSchemaBuilder<DisplayModalBottomSheetEventSchema>() {

    override fun build() = DisplayModalBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        modalBottomSheetId = modalBottomSheetId,
        isCancellable = isCancellable,
        fill = fill,
        allowsPartialExpansion = allowsPartialExpansion
    )
}

fun EventSchemaBuilderScope.DisplayModalBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    modalBottomSheetId: String,
    isCancellable: Boolean = true,
    fill: Boolean = false,
    allowsPartialExpansion: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        DisplayModalBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tiles = tiles,
            modalBottomSheetId = modalBottomSheetId,
            isCancellable = isCancellable,
            fill = fill,
            allowsPartialExpansion = allowsPartialExpansion
        )
    )
}
