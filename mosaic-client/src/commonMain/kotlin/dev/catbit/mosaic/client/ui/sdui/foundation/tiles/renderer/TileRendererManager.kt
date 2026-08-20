package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass
import org.koin.compose.koinInject

/**
 * Resolves a [TileSchema]'s concrete class to its registered [TileRenderer] and actually renders it —
 * the single entry point every tile in a screen's tree goes through, whether it's the root tile
 * itself or a child reached via [TileRenderingScope.RenderChild]/`RenderChildren` (both of which
 * delegate back into this same [Render] through `LocalTileRendererManager`).
 *
 * One instance is built app-wide (by `MosaicModules`), from the merge of every built-in
 * `TileDefinition` and whatever custom ones were passed via
 * `MosaicDependencyInjectionConfig.tileDefinitions` — so a custom tile renders through exactly the
 * same path as a built-in one.
 *
 * @param tileRenderers every registered [TileRenderer], keyed by the [TileSchema] subclass it
 * renders.
 */
@Stable
class TileRendererManager(
    private val tileRenderers: Map<KClass<out TileSchema>, TileRenderer<*>>
) {

    /**
     * Renders [tileSchema] by looking up the [TileRenderer] registered for its concrete class and
     * invoking its `Render`, inside a fresh [TileRenderingScope] built from [tileSchema]'s own `id`
     * and `events` plus the given [onEvent] sink.
     *
     * A tile whose `visibility` is `GONE` (`tileSchema.isGone()`) is skipped entirely — nothing is
     * composed for it at all, not even an invisible placeholder, which is the difference between
     * `GONE` and `INVISIBLE` (the latter is left to each renderer's own use of
     * `Modifier.visible(isVisible())`, and still gets composed here). If no [TileRenderer] is
     * registered for [tileSchema]'s class at all — the schema's own class was never included in
     * `MosaicDependencyInjectionConfig.tileDefinitions` — nothing is rendered and an error is logged
     * via [MosaicLogger] instead of throwing.
     *
     * @param tileSchema the tile to render.
     * @param onEvent sink every interaction with this tile (and its children, if any) funnels into.
     */
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