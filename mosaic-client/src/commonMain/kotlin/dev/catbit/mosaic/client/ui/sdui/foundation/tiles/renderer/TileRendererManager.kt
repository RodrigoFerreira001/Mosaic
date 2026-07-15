package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass
import org.koin.compose.koinInject

@Stable
class TileRendererManager(
    private val tileRenderers: Map<KClass<out TileSchema>, TileRenderer<*>>
) {

    @Composable
    fun Render(
        tileSchema: TileSchema,
        onEvent: (UIEvent) -> Unit
    ) {
        tileRenderers[tileSchema::class]?.let { renderer ->
            if (!tileSchema.isGone()) {
                @Suppress("UNCHECKED_CAST")
                with(renderer as TileRenderer<TileSchema>) {
                    TileRenderingScope(
                        tileId = tileSchema.id,
                        events = tileSchema.events,
                        onEvent = onEvent
                    ).Render(tileSchema)
                }
            }
        } ?: run {
            koinInject<MosaicLogger>().error("TileRendererManager: Couldn't find a renderer for $tileSchema")
        }
    }
}