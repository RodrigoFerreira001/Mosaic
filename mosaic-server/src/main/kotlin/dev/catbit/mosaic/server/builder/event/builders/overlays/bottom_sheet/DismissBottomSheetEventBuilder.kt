package dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val bottomSheetId: String
) : EventSchemaBuilder<DismissBottomSheetEventSchema>() {

    override fun build() = DismissBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        bottomSheetId = bottomSheetId
    )
}

/**
 * Closes the bottom sheet registered under [bottomSheetId]. Does not consume `incomingData`.
 * Dispatches `onSuccess` (no data) when the sheet was dismissed; `onFailure` (carrying the
 * thrown exception) when no bottom sheet is showing under [bottomSheetId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param bottomSheetId Id of the `DisplayBottomSheet` sheet to close.
 */
fun EventSchemaBuilderScope.DismissBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    bottomSheetId: String
) {
    addBuilder(
        DismissBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            bottomSheetId = bottomSheetId
        )
    )
}
