package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema

/**
 * Contract every Event — built-in or custom — implements to turn its own [EventSchema] subtype [T]
 * into its matching live [EventHolder] subtype [E]. Registered against [T]'s class via an
 * `EventDefinition`, and invoked exclusively through [BuilderScope.buildEventHolder] (and,
 * transitively, through `List<EventSchema>?.buildEventHolders()`) — nothing else in the framework
 * constructs an `EventHolder` directly.
 *
 * A stateless `object` is the norm (every built-in event's `HolderBuilder` is one).
 */
interface EventHolderBuilder<out T: EventSchema, E : EventHolder<@UnsafeVariance T>> {
    /**
     * Builds a fresh [E] from [eventSchema].
     *
     * @receiver [BuilderScope], used to recursively build this event's own `events` (via
     * `buildEventHolders()`) into holders of their own.
     * @param eventSchema the schema to build a holder for.
     * @return the newly built holder.
     */
    fun BuilderScope.build(eventSchema: @UnsafeVariance T): E
}
