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

/**
 * Shows a modal bottom sheet built from [tiles], registered under [modalBottomSheetId] so a
 * later `DismissModalBottomSheet` can close it — unlike the plain bottom sheet, this one dims
 * and blocks the content behind it. [isCancellable] decides whether the user can dismiss it by
 * gesture or scrim tap, [fill] whether it takes the full height, and [allowsPartialExpansion]
 * whether it stops at a half-expanded state before reaching full height. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the sheet was added; `onFailure`
 * (carrying the thrown exception) when it could not be added, typically because
 * [modalBottomSheetId] is already in use; `onDisplay` once, when the sheet actually enters
 * composition on screen.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`, `onDisplay`).
 * @param modalBottomSheetId Id the sheet is registered under, matched by a later `DismissModalBottomSheet`.
 * @param isCancellable Whether the user can dismiss the sheet by gesture or scrim tap. Defaults to true.
 * @param fill Whether the sheet takes the full available height. Defaults to false.
 * @param allowsPartialExpansion Whether the sheet stops at a half-expanded state before reaching full height. Defaults to false.
 * @param tiles Tile tree rendered as the sheet's content.
 */
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
