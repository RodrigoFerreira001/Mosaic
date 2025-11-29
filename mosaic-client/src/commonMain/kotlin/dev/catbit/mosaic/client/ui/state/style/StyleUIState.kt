package dev.catbit.mosaic.client.ui.state.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.model.ColorUIModel
import dev.catbit.mosaic.client.ui.model.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.state.UIState
import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder
import dev.catbit.mosaic.client.ui.state.builder.producer.UIStateBuilderProducer
import dev.catbit.mosaic.client.ui.state.builder.producer.UIStateBuilderProducerScope
import dev.catbit.mosaic.core.data.tile.style.StyleModel

@Stable
data class StyleUIState(
    val size: SizeUIState,
    val margin: PaddingValues? = null,
    val padding: PaddingValues? = null,
    val background: ColorUIModel? = null,
    val border: BorderUIState? = null,
    val clip: ClipUIState? = null,
    val windowInsets: WindowInsetsUIModel? = null
) : UIState

class StyleUIStateBuilder(
    val size: SizeUIStateBuilder,
    var margin: PaddingValues?,
    var padding: PaddingValues?,
    var background: ColorUIModel?,
    val border: BorderUIStateBuilder?,
    val clip: ClipUIStateBuilder?,
    var windowInsets: WindowInsetsUIModel?,
) : UIStateBuilder<StyleUIState>() {

    override fun shouldRebuild() = shouldRebuildWithLastState {
        size.shouldRebuild()
                || margin != it.margin
                || padding != it.padding
                || background != it.background
                || border?.shouldRebuild() == true
                || clip?.shouldRebuild() == true
                || windowInsets != it.windowInsets
    }

    override fun build() = StyleUIState(
        size = size.state,
        margin = margin,
        padding = padding,
        background = background,
        border = border?.state,
        clip = clip?.state,
        windowInsets = windowInsets,
    )
}

class StyleUIStateBuilderProducer : UIStateBuilderProducer<StyleModel, StyleUIStateBuilder> {

    override fun canProduce(data: Any) = data is StyleModel

    override fun UIStateBuilderProducerScope.produce(data: StyleModel) = with(data) {
        StyleUIStateBuilder(
            size = produceBuilder(size),
            margin = margin?.mapTo(),
            padding = padding?.mapTo(),
            background = background?.mapTo(),
            border = border?.let { produceBuilder(it) },
            clip = clip?.let { produceBuilder(it) },
            windowInsets = windowInsets?.mapTo(),
        )
    }
}













