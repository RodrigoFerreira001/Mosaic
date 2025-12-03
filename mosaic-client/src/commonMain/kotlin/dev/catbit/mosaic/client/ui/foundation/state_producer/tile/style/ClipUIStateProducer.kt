package dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style

import androidx.compose.ui.graphics.Shape
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.ClipUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

class ClipUIStateProducer(
    var shape: Shape
) : UIStateProducer<ClipUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        shape != it.shape
    }

    override fun produce() = ClipUIState(
        shape = shape
    )
}