package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer
import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

class ButtonTileUIStateProducer(
    private var text: String,
    private var loading: Boolean,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val updater: UIStateProducerUpdater
) : TileUIStateProducer<ButtonTileUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState { lastState ->
        text != lastState.text
                || loading != lastState.loading
                || visibility != lastState.visibility
    }

    override fun produce() = ButtonTileUIState(
        text = text,
        id = id,
        style = style.state,
        visibility = visibility,
        loading = loading
    )

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueIfPresent<String>(::text.name) { text = it }
            valueIfPresent<Boolean>(::loading.name) { loading = it }
            valueIfPresent<TileUIState.Visibility>(::visibility.name) { visibility = it }
            valueIfPresent<Map<String, Any?>>(::style.name) { style.update(it) }
        }
    }
}