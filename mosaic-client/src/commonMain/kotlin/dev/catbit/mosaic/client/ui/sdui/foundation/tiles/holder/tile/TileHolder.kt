package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer

/**
 * Live, mutable counterpart of a [TileSchema] instance in the rendered tree — one holder per tile,
 * built by a [TileHolderBuilder] and owned by the screen's `TilesManager` for as long as that tile
 * stays composed.
 *
 * Where a [TileSchema] is an immutable `data class` snapshot, a `TileHolder` is where the framework
 * actually keeps track of *this specific tile instance*: it owns the mutable [tile] copy that local
 * interactions patch directly (see [onTileEvent]), the child [EventHolder]s that let `TriggerEvent`/
 * `UpdateEvents` find an event by id anywhere under this tile, the child `TileHolder`s for a container
 * tile, and the dirty flag that decides whether [get] needs to re-derive the schema via
 * [getTileSchema] or can return the cached [tile] as-is. Every built-in tile subclasses this — see
 * `CheckboxTileHolder` for the simplest complete example (local state plus a value exposed to
 * `GetData`).
 */
abstract class TileHolder<T : TileSchema> {
    /** Id of this tile — how [TilesEditor]/[TilesEventDispatcher]/`GetData`'s `Tile` data source
     * address it by id, and what an ancestor's [getTileHolder]/[getEventHolder] search for. */
    abstract val id: String

    /** The current schema snapshot backing this holder. Subclasses widen this to `override var tile`
     * in their constructor; local interactions (see [onTileEvent]) mutate it directly via `tile =
     * tile.copy(...)`, and [update] replaces it wholesale after applying a JSON patch. */
    protected abstract var tile: T

    /** This tile's own declared events, already built into holders — non-nullable, since every tile
     * has an `events` list even when empty. What [getEventHolder]/[getEventsByTrigger] search through. */
    protected abstract val events: MutableList<EventHolder<*>>

    /** This tile's children, already built into holders — `null` for a leaf tile with no children,
     * a real (possibly empty) mutable list for a container tile. What [addChild]/[removeChild]/
     * [getTileHolder] operate on. */
    protected abstract val tiles: MutableList<TileHolder<*>>?

    private var isDirtyInternal: Boolean = false

    /**
     * Marks this holder as needing its schema re-derived the next time [get] is called, instead of
     * returning the cached [tile] as-is. Every mutating operation on a holder (a child added/removed,
     * [update] applied, [onTileEvent]/[onTileGroupEvent] handled) calls this immediately after.
     */
    fun markAsDirty() {
        isDirtyInternal = true
    }

    /**
     * Whether this holder — or any descendant tile or event holder — has been [markAsDirty]'d since
     * it was last read via [get]. Dirtiness propagates upward: a change deep in the tree marks every
     * ancestor dirty too, the next time each one is asked.
     *
     * @return `true` if this holder itself, any child in [tiles], or any child in [events] is dirty.
     */
    open fun isDirty(): Boolean = isDirtyInternal
            || tiles?.any { it.isDirty() } == true
            || events.any { it.isDirty() }

    /**
     * Searches this holder's own subtree (this tile, then depth-first through [tiles]) for the
     * `TileHolder` whose [id] equals [tileId].
     *
     * @param tileId the id to search for.
     * @param includeEventsOnSearch when `true` and the id isn't found among [tiles], also searches
     * inside [events] — needed because a tile can live nested under an event's own `tiles` (e.g. one
     * added via `AddTiles`, which is itself reachable through an `EventHolder`). Most callers leave
     * this `false`; `TilesManager.updateTile`/`checkIfTileHasChildren`/`getTileChildrenCount` pass
     * `true`.
     * @return the matching `TileHolder`, or `null` if [tileId] isn't found in this subtree.
     */
    open fun getTileHolder(
        tileId: String,
        includeEventsOnSearch: Boolean = false
    ): TileHolder<*>? =
        if (tileId == id) this
        else tiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: if (includeEventsOnSearch) events.firstNotNullOfOrNull { it.getTileHolder(tileId) } else null

    /**
     * Collects every `TileHolder` in this subtree (this one, then recursively through [tiles]) whose
     * [handlesGroupEvent] returns `true` for [event] — the traversal behind
     * `TileRenderingScope.dispatchGroupEvent`, which is how `RadioButton`'s mutual exclusion reaches
     * every radio button sharing a `groupId` regardless of where each one sits in the tree.
     *
     * @param event the group event to match tiles against.
     * @return every matching `TileHolder` in this subtree, in tree order.
     */
    open fun getTileHoldersByGroupEvent(
        event: TileGroupEvent
    ): List<TileHolder<*>> = mutableListOf<TileHolder<*>>().apply {
        if (handlesGroupEvent(event)) add(this@TileHolder)
        tiles?.flatMap { it.getTileHoldersByGroupEvent(event) }?.let(::addAll)
    }

    /**
     * Whether this holder should react to [event] when it's broadcast via
     * `TileRenderingScope.dispatchGroupEvent`. The default implementation never matches anything —
     * override it (typically with a type check against your own `TileGroupEvent` subtype, optionally
     * comparing a group-identifying field) on a `TileHolder` that participates in a group concern.
     *
     * @param event the group event to test.
     * @return `true` if this holder should receive [event] via [onTileGroupEvent].
     */
    open fun handlesGroupEvent(event: TileGroupEvent): Boolean = false

    /**
     * Searches this tile's own [events] (then, recursively, [tiles]' own events) for the
     * `EventHolder` whose id equals [eventId] — the lookup behind `TriggerEvent`/`UpdateEvents`.
     *
     * @param eventId the id to search for.
     * @return the matching `EventHolder`, or `null` if not found in this subtree.
     */
    open fun getEventHolder(eventId: String): EventHolder<*>? =
        events.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    /**
     * Collects every [EventSchema] in this subtree — this tile's own [events] first, then every
     * descendant tile's — whose `trigger` equals [eventTrigger]. Used by `EventManager.triggerEvents`
     * by way of `TilesEventHolder.getEventsByTrigger`, the mechanism behind screen-level triggers
     * like `onDisplay()` reaching every matching event anywhere in the tree, not just top-level ones.
     *
     * @param eventTrigger the trigger to match against.
     * @return every matching [EventSchema] in this subtree, own events before descendants'.
     */
    open fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema>? = events
        .filter { it.trigger == eventTrigger }
        .map { it.get() }
        .plus(tiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())

    /**
     * Re-derives a fresh [T] snapshot from this holder's current mutable state — [tile] plus
     * whatever [events]/[tiles] currently hold, each read via their own `get()`. Called by [get]
     * only when [isDirty] is `true`; every concrete `TileHolder` implements this by returning
     * `tile.copy(events = events.map { it.get() }, tiles = ...)`.
     *
     * @return the current schema snapshot for this tile, including its current children/events.
     */
    protected abstract fun getTileSchema(): T

    /**
     * Returns this tile's current [T] snapshot — the schema that gets serialized back to the client's
     * UI state and, ultimately, the network. Re-derives via [getTileSchema] and clears the dirty flag
     * when [isDirty] is `true`; otherwise returns the cached [tile] unchanged, avoiding a full subtree
     * walk on every read.
     *
     * @return the current [T] for this tile.
     */
    fun get(): T = if (isDirty()) {
        getTileSchema().apply {
            tile = this
            isDirtyInternal = false
        }
    } else tile

    /**
     * Applies [updateData] as a shallow JSON-patch merge onto this tile's current [tile], the
     * mechanism behind `UpdateTiles`. `style` is handled as its own nested merge (a patch under the
     * `"style"` key is merged onto the current `style` object specifically, rather than replacing it
     * wholesale) — every other top-level key in [updateData] simply overwrites the matching key on
     * the encoded tile object. The merged JSON is then decoded back into a fresh [T] instance via the
     * framework's [dev.catbit.mosaic.core.serialization.MosaicSerializer], which is why an update
     * that would leave the tile in an undecodable shape (e.g. a required field turned into the wrong
     * JSON type) fails and is logged rather than partially applied.
     *
     * After decoding, if the tile's own `events` list changed as a result of the patch, [events] is
     * rebuilt from the new list via [BuilderScope.buildEventHolders] — an update that doesn't touch
     * `events` leaves the existing `EventHolder`s (and any state they carry) untouched.
     *
     * @receiver [UpdateScope], which carries the [dev.catbit.mosaic.core.serialization.MosaicSerializer]
     * and [BuilderScope] this merge/rebuild needs.
     * @param updateData the patch to merge onto this tile's current fields.
     */
    @OptIn(InternalSerializationApi::class)
    fun UpdateScope.update(updateData: Map<String, Any?>) {
        runSafely(
            onError = { builderScope.logError(tag = "TileHolder.update", throwable = it) }
        ) {

            val tileUpdateObject = updateData
                .filterKeys { it != "style" }
                .toJsonElement()
                .jsonObject

            val styleUpdateObject = updateData["style"]
                ?.toJsonElement()
                ?.jsonObject

            val tileObject = serializer.encodeTileToJsonElement(tile).jsonObject
            val styleObject = tile.style.toJsonElement().jsonObject

            val updatedStyleObject = styleUpdateObject?.let {
                buildJsonObject {
                    styleObject.forEach { (key, element) -> put(key, element) }
                    styleUpdateObject.forEach { (key, element) -> put(key, element) }
                }
            }

            val updatedObject = buildJsonObject {
                tileObject.forEach { (key, element) -> put(key, element) }
                tileUpdateObject.forEach { (key, element) -> put(key, element) }
                updatedStyleObject?.let { put("style", updatedStyleObject) }
            }

            tile = serializer.decodeFromJsonElement(
                deserializer = tile::class.serializer(),
                element = updatedObject
            )

            val currentEvents = events.map { it.get() }

            if (tile.events != currentEvents) {
                events.apply {
                    clear()
                    with(builderScope) {
                        tile.events?.buildEventHolders()
                    }?.let {
                        events.addAll(it)
                    }
                }
            }
        }
    }

    /**
     * Applies [event] to this tile's local, in-memory state — the *local* half of a stateful tile's
     * interaction, invoked synchronously via `TileRenderingScope.dispatchEvent`. The default
     * implementation does nothing; override it on a `TileHolder` whose tile carries local state
     * (checked/selected/text/etc.), pattern-matching on your own `TileEvent` subtype and mutating
     * [tile] directly (`tile = tile.copy(...)`) — see `CheckboxTileHolder.onTileEvent` for the
     * canonical example. This runs on the UI thread and completes before recomposition, which is why
     * it always visually settles before whatever remote `EventTrigger` fired alongside it (via
     * `TileRenderingScope.triggerEvent`) has a chance to run.
     *
     * @receiver [TileEventScope], which lets an override recursively build new tile/event holders
     * (via `buildTileHolder`/`buildEventHolders`) if applying the event needs to construct new ones.
     * @param event the local mutation to apply.
     */
    open fun TileEventScope.onTileEvent(event: TileEvent) = Unit

    /**
     * Applies [event] to this tile's local state as part of a group-wide broadcast — invoked only on
     * holders whose [handlesGroupEvent] returned `true` for the same [event], via
     * `TileRenderingScope.dispatchGroupEvent`. The default implementation does nothing; override it
     * alongside [handlesGroupEvent] on a `TileHolder` that participates in a mutual-exclusion-style
     * group (the way `RadioButton` clears every other radio button sharing its `groupId`).
     *
     * @receiver [TileEventScope], same purpose as in [onTileEvent].
     * @param event the group event being applied.
     */
    open fun TileEventScope.onTileGroupEvent(event: TileGroupEvent) = Unit

    /**
     * Inserts [child] into this tile's [tiles] at the position resolved by [where] — a no-op if this
     * holder has no children list at all (`tiles == null`, i.e. it's a leaf tile). The mechanism
     * behind `AddTiles` targeting this tile as `groupingTileId`. Does **not** itself mark this holder
     * dirty or trigger a state update — callers (`TilesManager.addTile`/`addTiles`) do that
     * explicitly right after.
     *
     * @param child the already-built holder to insert.
     * @param where where in the current children to insert it. Defaults to the end.
     */
    fun addChild(
        child: TileHolder<*>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles?.add(
            element = child,
            index = where.toIndex(tiles.orEmpty())
        )
    }

    /**
     * Same as [addChild], for several children inserted together at the same position, preserving
     * [children]'s own order.
     *
     * @param children the already-built holders to insert.
     * @param where where in the current children to insert them. Defaults to the end.
     */
    fun addChildren(
        children: List<TileHolder<*>>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles?.addAll(
            elements = children,
            index = where.toIndex(tiles.orEmpty())
        )
    }

    /**
     * Removes the child in [tiles] whose id equals [id] — a no-op if this holder has no children
     * list, or if no child carries that id. The mechanism behind `RemoveTiles` targeting a single id
     * under this tile as `groupingTileId`.
     *
     * @param id id of the child to remove.
     */
    fun removeChild(id: String) {
        tiles?.removeAll { it.id == id }
    }

    /**
     * Same as [removeChild], for every child whose id is in [ids].
     *
     * @param ids ids of the children to remove.
     */
    fun removeChildren(ids: List<String>) {
        tiles?.removeAll { it.id in ids }
    }

    /**
     * Removes every child from [tiles] — a no-op if this holder has no children list. The mechanism
     * behind `WipeTiles`, and the first step of `ReplaceTiles` before the new children are added.
     */
    fun wipeChildren() {
        tiles?.clear()
    }

    /**
     * Whether every id in [childrenIds] is currently present among this tile's direct [tiles] — the
     * mechanism behind `CheckIfTileContainsChildren`. An empty [tiles] (or a holder with no children
     * list at all) only satisfies this for an empty [childrenIds].
     *
     * @param childrenIds ids that must all be present among the current children.
     * @return `true` if every id in [childrenIds] is a current direct child's id.
     */
    fun hasChildren(
        childrenIds: List<String>
    ) = tiles?.all { it.id in childrenIds } == true

    /**
     * Number of direct children currently in [tiles] — the mechanism behind
     * `GetTileChildrenCount`.
     *
     * @return the current child count, or `null` if this holder has no children list at all (a leaf
     * tile, as opposed to a container tile with zero children).
     */
    fun getChildrenCount() = tiles?.size

    /**
     * Exposes a value this tile currently holds, addressed by [key] — the mechanism behind the `Tile`
     * data source (`tile(tileId, dataKey)` in the server DSL), read by `GetData`/`EvaluateData`. The
     * default implementation exposes nothing (`null`); override it on a `TileHolder` whose tile has a
     * value worth reading this way (see `CheckboxTileHolder`, which exposes `mapOf(key to
     * tile.checked)` — `key` is ignored there since a checkbox only ever has one value to expose, but
     * a tile with several distinct values would branch on it).
     *
     * @param key the data key requested — meaning is entirely up to this tile's own implementation.
     * @return a single-entry map pairing [key] with the requested value, or `null` if this tile has
     * nothing to expose (or doesn't recognize [key]).
     */
    open fun produceValueWithKey(key: String): Map<String, Any>? = null
}