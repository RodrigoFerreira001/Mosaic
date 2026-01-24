package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlin.reflect.KClass

class TileRendererManager(
    private val tileRenderers: Map<KClass<out TileModel>, TileRenderer<*>>
) {

    // TODO, de alguma forma, encapsular aqui coisas padrões, como style, Visibility.GONE e afins
    @Composable
    fun Render(
        tileModel: TileModel,
        onEvent: (UIEvent) -> Unit
    ) {
        tileRenderers[tileModel::class]?.let { renderer ->
            with(renderer) {
                TileRenderingScope(
                    tileId = tileModel.id,
                    events = tileModel.events,
                    onEvent = onEvent
                ).Render(tileModel)
            }
        } ?: run {
            println("Couldn't find a renderer for $tileModel") // TODO Usar logger
        }
    }
}