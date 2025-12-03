package dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style

import dev.catbit.mosaic.client.ui.foundation.state.tile.style.SizeUIState
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.SizeUIState.Behavior
import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

class SizeUIStateProducer(
    val width: Behavior.Horizontal,
    val height: Behavior.Vertical
) : UIStateProducer<SizeUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        width != it.width
                || height != it.height
    }

    override fun produce() = SizeUIState(
        width = width,
        height = height
    )
}