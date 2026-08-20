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

/**
 * Shows a non-modal bottom sheet built from [tiles], registered under [bottomSheetId] so a
 * later `DismissBottomSheet` can close it — unlike the modal variant, it does not dim or block
 * the content behind it. [isCancellable] decides whether the user can dismiss it by gesture,
 * [fill] whether it takes the full height, and [allowsPartialExpansion] whether it stops at a
 * half-expanded state before reaching full height. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) when the sheet was added; `onFailure` (carrying the thrown exception)
 * when it could not be added, typically because [bottomSheetId] is already in use.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param bottomSheetId Id the sheet is registered under, matched by a later `DismissBottomSheet`.
 * @param isCancellable Whether the user can dismiss the sheet by gesture. Defaults to true.
 * @param fill Whether the sheet takes the full available height. Defaults to false.
 * @param allowsPartialExpansion Whether the sheet stops at a half-expanded state before reaching full height. Defaults to false.
 * @param tiles Tile tree rendered as the sheet's content.
 */
fun EventSchemaBuilderScope.DisplayBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    bottomSheetId: String,
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
