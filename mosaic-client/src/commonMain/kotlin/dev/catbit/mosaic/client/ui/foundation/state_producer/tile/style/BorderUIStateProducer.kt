package dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style

import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.BorderUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

class BorderUIStateProducer(
    private var color: ColorUIModel,
    private var thickness: Dp,
    private val radius: RadiusUIStateProducer
) : UIStateProducer<BorderUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        color != it.color
                || thickness != it.thickness
                || radius.shouldProduce()
    }

    override fun produce() = BorderUIState(
        color = color,
        thickness = thickness,
        radius = radius.state,
    )
}