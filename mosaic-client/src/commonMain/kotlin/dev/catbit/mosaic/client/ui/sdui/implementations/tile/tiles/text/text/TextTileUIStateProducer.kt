package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer
import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

class TextTileUIStateProducer(
    private var text: String,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val updater: UIStateProducerUpdater
) : TileUIStateProducer<TextTileUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState { lastState ->
        text != lastState.text
                || visibility != lastState.visibility
                || style.shouldProduce()
    }

    override fun produce() = TextTileUIState(
        text = text,
        id = id,
        style = style.state,
        visibility = visibility
    )

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueIfPresent<String>(::text.name) { text = it }
            valueIfPresent<TileUIState.Visibility>(::visibility.name) { visibility = it }
            valueIfPresent<Map<String, Any?>>(::style.name) { style.update(it) }
        }
    }
}