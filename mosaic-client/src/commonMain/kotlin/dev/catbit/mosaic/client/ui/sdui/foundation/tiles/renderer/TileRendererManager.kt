package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass

class TileRendererManager(
    private val tileRenderers: Map<KClass<out TileSchema>, TileRenderer<*>>
) {

    // TODO, de alguma forma, encapsular aqui coisas padrões, como style, Visibility.GONE e afins
    @Composable
    fun Render(
        tileSchema: TileSchema,
        onEvent: (UIEvent) -> Unit
    ) {
        tileRenderers[tileSchema::class]?.let { renderer ->
            if (!tileSchema.isGone()) {
                with(renderer) {

                    DisposableEffect(tileSchema) {

                        // trigger display event

                        onDispose {
                            // trigger dispose event
                        }
                    }

                    TileRenderingScope(
                        tileId = tileSchema.id,
                        events = tileSchema.events,
                        onEvent = onEvent
                    ).Render(tileSchema)
                }
            }
        } ?: run {
            println("Couldn't find a renderer for $tileSchema") // TODO Usar logger
        }
    }
}