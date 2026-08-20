package dev.catbit.mosaic.server.builder.event.builders.overlays.dialog

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissDialogEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dialogId: String,
) : EventSchemaBuilder<DismissDialogEventSchema>() {

    override fun build() = DismissDialogEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dialogId = dialogId
    )
}

/**
 * Closes the dialog registered under [dialogId]. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) when the dialog was dismissed; `onFailure` (carrying the thrown
 * exception) when no dialog is showing under [dialogId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param dialogId Id of the `DisplayDialog` dialog to close.
 */
fun EventSchemaBuilderScope.DismissDialog(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dialogId: String,
) {
    addBuilder(
        DismissDialogEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dialogId = dialogId
        )
    )
}
