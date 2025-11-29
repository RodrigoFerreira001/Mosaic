package dev.catbit.mosaic.client.ui.state.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.state.UIState
import dev.catbit.mosaic.client.ui.state.style.SizeUIState.Behavior
import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder
import dev.catbit.mosaic.client.ui.state.builder.producer.UIStateBuilderProducer
import dev.catbit.mosaic.client.ui.state.builder.producer.UIStateBuilderProducerScope
import dev.catbit.mosaic.core.data.tile.style.SizeModel

@Stable
data class SizeUIState(
    val width: Behavior.Horizontal,
    val height: Behavior.Vertical
) : UIState {
    sealed interface Behavior {

        sealed interface Horizontal : Behavior {

            data object Fill : Horizontal
            data object Wrap : Horizontal
            data class Fixed(val value: Dp) : Horizontal
            data class Weight(val value: Float) : Horizontal
            data class Span(val value: Int) : Horizontal
        }

        sealed interface Vertical : Behavior {

            data object Fill : Vertical
            data object Wrap : Vertical
            data class Fixed(val value: Dp) : Vertical
            data class Weight(val value: Float) : Vertical
        }
    }
}

class SizeUIStateBuilder(
    val width: Behavior.Horizontal,
    val height: Behavior.Vertical
) : UIStateBuilder<SizeUIState>() {

    override fun shouldRebuild() = shouldRebuildWithLastState {
        width != it.width
                || height != it.height
    }

    override fun build() = SizeUIState(
        width = width,
        height = height
    )
}

class SizeUIStateBuilderProducer : UIStateBuilderProducer<SizeModel, SizeUIStateBuilder> {

    override fun canProduce(data: Any) = data is SizeModel

    override fun UIStateBuilderProducerScope.produce(data: SizeModel) = SizeUIStateBuilder(
        width = data.width.mapTo(),
        height = data.height.mapTo(),
    )
}