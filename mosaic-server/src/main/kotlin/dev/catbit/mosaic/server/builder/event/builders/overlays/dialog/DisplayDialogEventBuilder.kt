package dev.catbit.mosaic.server.builder.event.builders.overlays.dialog

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class DisplayDialogEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val dialogId: String,
    private val isCancellable: Boolean,
    private val usePlatformDefaultWidth: Boolean
) : EventSchemaBuilder<DisplayDialogEventSchema>() {

    override fun build() = DisplayDialogEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        dialogId = dialogId,
        isCancellable = isCancellable,
        usePlatformDefaultWidth = usePlatformDefaultWidth
    )
}

/**
 * Shows a dialog built from [tiles], registered under [dialogId] so a later `DismissDialog` can
 * close it. [isCancellable] decides whether the user can dismiss it with the back gesture or a
 * scrim tap, and [usePlatformDefaultWidth] whether the dialog keeps the platform's default width
 * or sizes itself from its content. Does not consume `incomingData`. Dispatches `onSuccess` (no
 * data) when the dialog was added; `onFailure` (carrying the thrown exception) when it could not
 * be added, typically because [dialogId] is already in use.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param dialogId Id the dialog is registered under, matched by a later `DismissDialog`.
 * @param isCancellable Whether the user can dismiss the dialog with the back gesture or a scrim tap. Defaults to true.
 * @param usePlatformDefaultWidth Whether the dialog keeps the platform's default width instead of sizing from its content. Defaults to false.
 * @param tiles Tile tree rendered as the dialog's content.
 */
fun EventSchemaBuilderScope.DisplayDialog(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dialogId: String,
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
            dialogId = dialogId,
            isCancellable = isCancellable,
            usePlatformDefaultWidth = usePlatformDefaultWidth
        )
    )
}
