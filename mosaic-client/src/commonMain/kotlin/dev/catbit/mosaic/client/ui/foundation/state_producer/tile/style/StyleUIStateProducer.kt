package dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style

import androidx.compose.foundation.layout.PaddingValues
import dev.catbit.mosaic.client.ui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.foundation.models.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.StyleUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

class StyleUIStateProducer(
    val size: SizeUIStateProducer,
    var margin: PaddingValues?,
    var padding: PaddingValues?,
    var background: ColorUIModel?,
    val border: BorderUIStateProducer?,
    val clip: ClipUIStateProducer?,
    var windowInsets: WindowInsetsUIModel?,
) : UIStateProducer<StyleUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        size.shouldProduce()
                || margin != it.margin
                || padding != it.padding
                || background != it.background
                || border?.shouldProduce() == true
                || clip?.shouldProduce() == true
                || windowInsets != it.windowInsets
    }

    override fun produce() = StyleUIState(
        size = size.state,
        margin = margin,
        padding = padding,
        background = background,
        border = border?.state,
        clip = clip?.state,
        windowInsets = windowInsets,
    )
}