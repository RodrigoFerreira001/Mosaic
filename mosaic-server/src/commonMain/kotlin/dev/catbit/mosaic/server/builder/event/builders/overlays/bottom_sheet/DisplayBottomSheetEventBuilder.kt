package dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class DisplayBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val isCancellable: Boolean,
    private val fill: Boolean
) : EventSchemaBuilder<DisplayBottomSheetEventSchema> {

    override fun build() = DisplayBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        isCancellable = isCancellable,
        fill = fill
    )
}

fun EventSchemaBuilderScope.DisplayBottomSheet(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    isCancellable: Boolean = true,
    fill: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        DisplayBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tiles = tiles,
            isCancellable = isCancellable,
            fill = fill
        )
    )
}
