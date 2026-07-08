package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.date_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class DatePickerTileHolder(
    override val id: String,
    override var tile: DatePickerTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<DatePickerTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is DatePickerTileEvents.OnDateConfirmed -> {
                tile = tile.copy(
                    selectedDate = event.date,
                    expanded = false
                )
            }

            is DatePickerTileEvents.OnDatePickerToggle -> {
                tile = tile.copy(expanded = !tile.expanded)
            }

            is DatePickerTileEvents.OnDatePickerDismissRequest -> {
                tile = tile.copy(expanded = false)
            }
        }
    }

    override fun produceValueWithKey(key: String) = tile.selectedDate?.let { mapOf(key to it) }
}
