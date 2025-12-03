package dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style

import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.RadiusUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

class RadiusUIStateProducer(
    private var topStart: Dp,
    private var topEnd: Dp,
    private var bottomStart: Dp,
    private var bottomEnd: Dp
) : UIStateProducer<RadiusUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        topStart != it.topStart
                || topEnd != it.topEnd
                || bottomStart != it.bottomStart
                || bottomEnd != it.bottomEnd
    }

    override fun produce() = RadiusUIState(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd,
    )
}