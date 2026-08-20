package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

/**
 * Surface that lets an event read a value a specific tile currently produces, addressed by tile id
 * plus a tile-defined key — the collaborator behind
 * [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope.tilesValueProducer], and in
 * turn behind the server DSL's `Tile` data source (`tile(tileId, dataKey)`) used by
 * `GetData`/`EvaluateData`.
 */
interface TilesValueProducer {
    /**
     * Looks up the `TileHolder` whose id equals [tileId] and asks it for the value it exposes under
     * [key], via that holder's own `produceValueWithKey` override.
     *
     * @param tileId id of the tile to read from.
     * @param key the data key requested — meaning is entirely up to the target tile's own
     * implementation (e.g. `"text"` for a `TextField`, or an implicit single key like `Checkbox`'s
     * `checked`).
     * @return a single-entry map pairing [key] with the requested value, or `null` if no tile carries
     * [tileId], or that tile exposes nothing for [key].
     */
    fun getValueWithKey(
        tileId: String,
        key: String
    ): Map<String, Any>?
}