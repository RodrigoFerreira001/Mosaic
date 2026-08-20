package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

/**
 * Contract every Tile — built-in or custom — implements to turn its own [TileSchema] subtype [T]
 * into Compose UI. Registered against [T]'s class via a `TileDefinition`, and reached exclusively
 * through [TileRendererManager.Render] — nothing else in the framework invokes a `TileRenderer`
 * directly.
 *
 * A stateless `object` is the norm (every built-in tile renderer is one) — any state a rendered tile
 * needs to track lives on its matching `TileHolder`, not here.
 */
@Immutable
interface TileRenderer<T : TileSchema> {

    /**
     * Renders [tileSchema] as Compose UI.
     *
     * @receiver [TileRenderingScope], carrying this tile's own id/events and every way to react to an
     * interaction — apply local state ([TileRenderingScope.dispatchEvent]), notify a tile group
     * ([TileRenderingScope.dispatchGroupEvent]), run this tile's own declared events
     * ([TileRenderingScope.triggerEvent]), or render children
     * ([TileRenderingScope.RenderChild]/[TileRenderingScope.RenderChildren]).
     * @param tileSchema the current schema snapshot to render — the same value [TileRenderingScope]
     * was built from, passed again as a parameter so implementations don't need to destructure it out
     * of the receiver.
     */
    @Stable
    @Composable
    fun TileRenderingScope.Render(
        tileSchema: T
    )
}