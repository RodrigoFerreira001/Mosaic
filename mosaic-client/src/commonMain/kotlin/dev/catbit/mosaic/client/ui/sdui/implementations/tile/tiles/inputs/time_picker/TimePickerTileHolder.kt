package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.time_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class TimePickerTileHolder(
    override val id: String,
    override var tile: TimePickerTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TimePickerTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is TimePickerTileEvents.OnTimeConfirmed -> {
                tile = tile.copy(
                    selectedTime = event.time,
                    expanded = false
                )
            }

            is TimePickerTileEvents.OnTimePickerToggle -> {
                tile = tile.copy(expanded = !tile.expanded)
            }

            is TimePickerTileEvents.OnTimePickerDismissRequest -> {
                tile = tile.copy(expanded = false)
            }
        }
    }

    override fun produceValueWithKey(key: String) = tile.selectedTime?.let { mapOf(key to it) }
}
