package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

/**
 * Contract every Tile — built-in or custom — implements to turn its own [TileSchema] subtype [T] into
 * its matching live [TileHolder] subtype [H]. Registered against [T]'s class via a `TileDefinition`,
 * and invoked exclusively through [BuilderScope.buildTileHolder] (and, transitively, through
 * `List<TileSchema>?.buildTileHolders()`) — nothing else in the framework constructs a `TileHolder`
 * directly.
 *
 * A stateless `object` is the norm (every built-in tile's `HolderBuilder` is one) — the *instance*
 * state lives entirely on the `H` it produces, not on the builder itself.
 */
interface TileHolderBuilder<out T: TileSchema, H : TileHolder<@UnsafeVariance T>> {
    /**
     * Builds a fresh [H] from [tileModel].
     *
     * @receiver [BuilderScope], used to recursively build this tile's own `events`/`tiles` (via
     * `buildEventHolders()`/`buildTileHolders()`) into holders of their own, and to resolve any
     * dependency the holder's construction needs.
     * @param tileModel the schema to build a holder for.
     * @return the newly built holder.
     */
    fun BuilderScope.build(tileModel: @UnsafeVariance T): H
}