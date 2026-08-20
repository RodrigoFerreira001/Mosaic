package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

/**
 * Mutation surface a screen's `TilesManager` exposes for editing its own live tile tree by id — the
 * collaborator behind [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope.tilesEditor],
 * and in turn behind the server DSL's `AddTiles`, `RemoveTiles`, `ReplaceTiles`, `WipeTiles`,
 * `UpdateTiles`, `CheckIfTileContainsChildren` and `GetTileChildrenCount` events.
 *
 * Every mutating method returns `Result<Unit>` rather than throwing directly, and rather than
 * returning nothing: a call addressed to an id that doesn't currently exist on this screen fails the
 * `Result` (wrapping a `TileNotFoundException`) instead of crashing the event chain that issued it,
 * letting the runner that called it decide how to react (typically by firing its own `onFailure`).
 * Every method that succeeds also triggers a state update, causing the affected part of the tree to
 * recompose.
 */
interface TilesEditor {

    /**
     * Inserts [tileSchema] as a new top-level child of the screen's own root tile.
     *
     * @param tileSchema the tile to insert.
     * @param where where among the current root children to insert it. Defaults to the end.
     */
    fun addTile(
        tileSchema: TileSchema,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    /**
     * Same as [addTile], for several tiles inserted together at the same position, preserving
     * [tileSchemas]' own order.
     *
     * @param tileSchemas the tiles to insert.
     * @param where where among the current root children to insert them. Defaults to the end.
     */
    fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    /**
     * Inserts [tileSchema] as a new child of the container tile identified by [groupingTileId],
     * wherever that container currently is in the tree.
     *
     * @param tileSchema the tile to insert.
     * @param groupingTileId id of the container tile to insert into.
     * @param where where among that container's current children to insert it. Defaults to the end.
     */
    fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    /**
     * Same as the [groupingTileId] overload of [addTile], for several tiles inserted together.
     *
     * @param tileSchemas the tiles to insert, in order.
     * @param groupingTileId id of the container tile to insert into.
     * @param where where among that container's current children to insert them. Defaults to the end.
     */
    fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    /**
     * Removes the top-level root tile whose id equals [tileId].
     *
     * @param tileId id of the tile to remove.
     */
    fun removeTile(
        tileId: String,
    ): Result<Unit>

    /**
     * Removes the child whose id equals [tileId] from the container tile identified by
     * [groupingTileId].
     *
     * @param tileId id of the child to remove.
     * @param groupingTileId id of the container tile to remove it from.
     */
    fun removeTile(
        tileId: String,
        groupingTileId: String,
    ): Result<Unit>

    /**
     * Removes every top-level root tile whose id is in [tileIds].
     *
     * @param tileIds ids of the tiles to remove.
     */
    fun removeTiles(
        tileIds: List<String>,
    ): Result<Unit>

    /**
     * Removes every child whose id is in [tileIds] from the container tile identified by
     * [groupingTileId].
     *
     * @param tileIds ids of the children to remove.
     * @param groupingTileId id of the container tile to remove them from.
     */
    fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String,
    ): Result<Unit>

    /**
     * Replaces every top-level root tile with [tileSchemas] — the current root children are wiped
     * first, then [tileSchemas] is added in full.
     *
     * @param tileSchemas the new set of root tiles.
     */
    fun replaceTiles(
        tileSchemas: List<TileSchema>,
    ): Result<Unit>

    /**
     * Replaces every child of the container tile identified by [groupingTileId] with [tileSchemas] —
     * that container's current children are wiped first, then [tileSchemas] is added in full.
     *
     * @param tileSchemas the new set of children.
     * @param groupingTileId id of the container tile whose children are being replaced.
     */
    fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
    ): Result<Unit>

    /**
     * Removes every child of the container tile identified by [groupingTileId], leaving it empty.
     *
     * @param groupingTileId id of the container tile to empty.
     */
    fun wipeTiles(
        groupingTileId: String,
    ): Result<Unit>

    /**
     * Applies [updateData] as a shallow JSON-patch merge onto the tile identified by [tileId] — see
     * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder.update] for exactly
     * how the merge (and the special handling of a `"style"` patch) works. The lookup also searches
     * inside event holders' own nested tiles, not just the plain tile tree.
     *
     * @param tileId id of the tile to patch.
     * @param updateData the patch to merge onto that tile's current fields.
     */
    fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>,
    ): Result<Unit>

    /**
     * Whether every id in [childrenIds] is currently a direct child of the container tile identified
     * by [groupingTileId] — the mechanism behind `CheckIfTileContainsChildren`. Unlike the other
     * methods on this interface, this doesn't fail when [groupingTileId] isn't found; it simply
     * returns `false`.
     *
     * @param groupingTileId id of the container tile to check.
     * @param childrenIds ids that must all be present among that container's current children.
     * @return `true` if [groupingTileId] is found and every id in [childrenIds] is one of its
     * current direct children.
     */
    fun checkIfTileHasChildren(
        groupingTileId: String,
        childrenIds: List<String>
    ): Boolean

    /**
     * Number of direct children currently under the container tile identified by [groupingTileId] —
     * the mechanism behind `GetTileChildrenCount`.
     *
     * @param groupingTileId id of the container tile to count children of.
     * @return the current child count, or `null` if [groupingTileId] isn't found, or if it's found
     * but isn't a container tile at all.
     */
    fun getTileChildrenCount(
        groupingTileId: String
    ): Int?
}