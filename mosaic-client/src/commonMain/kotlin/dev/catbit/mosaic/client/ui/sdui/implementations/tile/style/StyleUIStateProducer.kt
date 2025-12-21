package dev.catbit.mosaic.client.ui.sdui.implementations.tile.style

import androidx.compose.foundation.layout.PaddingValues
import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.extensions.valueOrNullIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.models.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater

class StyleUIStateProducer(
    private var size: SizeUIState,
    private var margin: PaddingValues?,
    private var padding: PaddingValues?,
    private var background: ColorUIModel?,
    private var border: BorderUIState?,
    private var clip: ClipUIState?,
    private var windowInsets: WindowInsetsUIModel?,
    override val updater: UIStateProducerUpdater
) : UIStateProducer<StyleUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState {
        size != it.size
                || margin != it.margin
                || padding != it.padding
                || background != it.background
                || border != it.border
                || clip != it.clip
                || windowInsets != it.windowInsets
    }

    override fun produce() = StyleUIState(
        size = size,
        margin = margin,
        padding = padding,
        background = background,
        border = border,
        clip = clip,
        windowInsets = windowInsets,
    )

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueIfPresent<SizeUIState>(::size.name) { this@StyleUIStateProducer.size = it }
            valueOrNullIfPresent<PaddingValues>(::margin.name) { margin = it }
            valueOrNullIfPresent<PaddingValues>(::padding.name) { padding = it }
            valueOrNullIfPresent<ColorUIModel>(::background.name) { background = it }
            valueOrNullIfPresent<BorderUIState>(::border.name) { border = it }
            valueOrNullIfPresent<ClipUIState>(::clip.name) { clip = it }
            valueOrNullIfPresent<WindowInsetsUIModel>(::windowInsets.name) { windowInsets = it }
        }
    }
}