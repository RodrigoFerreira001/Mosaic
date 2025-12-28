package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.extensions.valueOrNullIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducerScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer

class TextFieldTileUIStateProducer(
    private var value: String,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val updater: UIStateProducerUpdater,
) : TileUIStateProducer<TextFieldTileUIState>() {

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueOrNullIfPresent<String>(::value.name) { value = it.orEmpty() }
            valueIfPresent<TileUIState.Visibility>(::visibility.name) { visibility = it }
            valueIfPresent<Map<String, Any?>>(::style.name) { style.update(it) }
        }
    }

    override fun shouldProduce() = shouldProduceWithLastState { state ->
        state.value != value
                || style.shouldProduce()
    }

    override fun produce() = TextFieldTileUIState(
        id = id,
        style = style.state,
        visibility = visibility,
        value = value,
    )

    override fun TileUIStateProducerScope.onEvent(event: TileEvent) {
        when (event) {
            is TextFieldTileEvents.OnTextChange -> {
                value = event.newValue
                updateState()
            }
        }
    }
}