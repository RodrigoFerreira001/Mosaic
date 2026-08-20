package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Live, mutable counterpart of an [EventSchema] instance registered somewhere in a screen's tile
 * tree — one holder per event, built by an [EventHolderBuilder]. Mirrors
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder]'s shape, but note the
 * key difference: an `EventHolder` is used **only for lookup and patching by id** (`TriggerEvent`,
 * `UpdateEvents`) — it never runs anything itself. Execution always goes through the matching
 * `EventRunner` directly, dispatched by `EventManager`, entirely independent of whether a holder
 * exists for that event.
 */
abstract class EventHolder<T : EventSchema> {
    /** Id of this event — how `TriggerEvent`/`UpdateEvents` and [getEventHolder]/[getTileHolder]
     * address it by id. */
    abstract val id: String

    /** The current schema snapshot backing this holder. Subclasses widen this to `override var
     * event` in their constructor; [update] replaces it wholesale after applying a JSON patch. */
    protected abstract var event: T

    /** This event's own trigger condition — not itself mutated by [update]'s JSON-patch merge in
     * practice (an update targeting `trigger` would be unusual), but part of the schema `get()`
     * returns. */
    abstract val trigger: EventTrigger

    /** This event's own child events, already built into holders — `null` if it declares none. What
     * [getEventHolder] searches through. */
    protected abstract val events: List<EventHolder<*>>?

    /** Child tiles nested under this event (e.g. ones added via `AddTiles` inside this event's own
     * chain), already built into holders — `null` if there are none. What [getTileHolder] searches
     * through. */
    protected abstract val tiles: List<TileHolder<*>>?

    private var isDirtyInternal: Boolean = false

    /** Marks this holder as needing its schema re-derived the next time [get] is called. Called
     * after [update] applies a patch. */
    fun markAsDirty() {
        isDirtyInternal = true
    }

    /**
     * Whether this holder — or any descendant tile or event holder — has been [markAsDirty]'d since
     * it was last read via [get].
     *
     * @return `true` if this holder itself, any child in [tiles], or any child in [events] is dirty.
     */
    open fun isDirty(): Boolean = isDirtyInternal
            || tiles?.any { it.isDirty() } == true
            || events?.any { it.isDirty() } == true

    /**
     * Searches this holder's own subtree (this event, then depth-first through [events] and
     * [tiles]) for the `EventHolder` whose [id] equals [eventId] — the lookup behind
     * `TriggerEvent`/`UpdateEvents`, when it recurses past the point where
     * `TilesManager.getEventHolder` handed off into a specific event's own nested structure.
     *
     * @param eventId the id to search for.
     * @return the matching `EventHolder`, or `null` if not found in this subtree.
     */
    fun getEventHolder(eventId: String): EventHolder<*>? =
        if (eventId == id) this
        else events?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    /**
     * Searches this event's own [tiles] (then, recursively, its own [events]) for the `TileHolder`
     * whose id equals [tileId] — the counterpart lookup to `TileHolder.getTileHolder`, letting a
     * search cross from the tile side into an event's own nested tiles and back.
     *
     * @param tileId the id to search for.
     * @return the matching `TileHolder`, or `null` if not found in this subtree.
     */
    fun getTileHolder(tileId: String): TileHolder<*>? =
        tiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, true) }
            ?: events?.firstNotNullOfOrNull { it.getTileHolder(tileId) }

    /**
     * Re-derives a fresh [T] snapshot from this holder's current mutable state. Called by [get]
     * only when [isDirty] is `true`; every concrete `EventHolder` implements this by returning
     * `event.copy(events = events?.map { it.get() })`.
     *
     * @return the current schema snapshot for this event, including its current children.
     */
    protected abstract fun getEventSchema(): T

    /**
     * Returns this event's current [T] snapshot. Re-derives via [getEventSchema] and clears the
     * dirty flag when [isDirty] is `true`; otherwise returns the cached [event] unchanged.
     *
     * @return the current [T] for this event.
     */
    fun get(): T = if (isDirty()) {
        getEventSchema().apply {
            event = this
            isDirtyInternal = false
        }
    } else event

    /**
     * Applies [updateData] as a shallow JSON-patch merge onto this event's current [event] — the
     * mechanism behind `UpdateEvents`, the event-level equivalent of
     * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder.update]. Unlike the
     * tile version, there's no special-cased nested field (no `style` equivalent) — every top-level
     * key in [updateData] simply overwrites the matching key on the encoded event object, and the
     * merged JSON is decoded back into a fresh [T] via the framework's
     * [dev.catbit.mosaic.core.serialization.MosaicSerializer].
     *
     * @receiver [UpdateScope], which carries the [dev.catbit.mosaic.core.serialization.MosaicSerializer]
     * this merge needs.
     * @param updateData the patch to merge onto this event's current fields.
     */
    @OptIn(InternalSerializationApi::class)
    fun UpdateScope.update(updateData: Map<String, Any?>) {

        runSafely(
            onError = { builderScope.logError(tag = "EventHolder.update", throwable = it) }
        ) {
            val updateObject = updateData.toJsonElement().jsonObject
            val eventObject = serializer.encodeEventToJsonElement(event).jsonObject

            val updatedObject = buildJsonObject {
                eventObject.forEach { (key, element) -> put(key, element) }
                updateObject.forEach { (key, element) -> put(key, element) }
            }

            event = serializer.decodeFromJsonElement(
                deserializer = event::class.serializer(),
                element = updatedObject
            )
        }
    }
}