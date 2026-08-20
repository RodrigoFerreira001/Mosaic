package dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissModalBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val modalBottomSheetId: String
) : EventSchemaBuilder<DismissModalBottomSheetEventSchema>() {

    override fun build() = DismissModalBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        modalBottomSheetId = modalBottomSheetId
    )
}

/**
 * Closes the modal bottom sheet registered under [modalBottomSheetId]. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the sheet was dismissed; `onFailure`
 * (carrying the thrown exception) when no modal bottom sheet is showing under
 * [modalBottomSheetId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param modalBottomSheetId Id of the `DisplayModalBottomSheet` sheet to close.
 */
fun EventSchemaBuilderScope.DismissModalBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    modalBottomSheetId: String
) {
    addBuilder(
        DismissModalBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            modalBottomSheetId = modalBottomSheetId
        )
    )
}
