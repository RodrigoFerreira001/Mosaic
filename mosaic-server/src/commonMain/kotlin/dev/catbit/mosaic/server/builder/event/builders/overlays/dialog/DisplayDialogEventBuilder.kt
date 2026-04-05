package dev.catbit.mosaic.server.builder.event.builders.overlays.dialog

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class DisplayDialogEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val isCancellable: Boolean,
    private val usePlatformDefaultWidth: Boolean
) : EventSchemaBuilder<DisplayDialogEventSchema>() {

    override fun build() = DisplayDialogEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        isCancellable = isCancellable,
        usePlatformDefaultWidth = usePlatformDefaultWidth
    )
}

fun EventSchemaBuilderScope.DisplayDialog(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    isCancellable: Boolean = true,
    usePlatformDefaultWidth: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        DisplayDialogEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tiles = tiles,
            isCancellable = isCancellable,
            usePlatformDefaultWidth = usePlatformDefaultWidth
        )
    )
}
