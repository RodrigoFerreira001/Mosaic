package dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class DisplayBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val bottomSheetId: String,
    private val isCancellable: Boolean,
    private val fill: Boolean,
    private val allowsPartialExpansion: Boolean
) : EventSchemaBuilder<DisplayBottomSheetEventSchema>() {

    override fun build() = DisplayBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        bottomSheetId = bottomSheetId,
        isCancellable = isCancellable,
        fill = fill,
        allowsPartialExpansion = allowsPartialExpansion
    )
}

fun EventSchemaBuilderScope.DisplayBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    bottomSheetId: String = randomId(),
    isCancellable: Boolean = true,
    fill: Boolean = false,
    allowsPartialExpansion: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        DisplayBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tiles = tiles,
            bottomSheetId = bottomSheetId,
            isCancellable = isCancellable,
            fill = fill,
            allowsPartialExpansion = allowsPartialExpansion
        )
    )
}
