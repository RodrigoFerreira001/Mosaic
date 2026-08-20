package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

/**
 * Resolves an [EventSchema]'s concrete class to its registered [EventHolderBuilder] and actually
 * builds it — the single entry point every `EventHolder`, built-in or custom, is constructed
 * through, reached via [BuilderScope.buildEventHolder].
 *
 * One instance is built app-wide (by `MosaicModules`), from the merge of every built-in
 * `EventDefinition` and whatever custom ones were passed via
 * `MosaicDependencyInjectionConfig.eventDefinitions`.
 *
 * @param builders every registered [EventHolderBuilder], keyed by the [EventSchema] subclass it
 * builds.
 */
class EventHolderBuilderManager(
    private val builders: Map<KClass<out EventSchema>, EventHolderBuilder<*, *>>
) {
    /**
     * Builds a holder for [eventSchema] by looking up the builder registered for its concrete class.
     *
     * @receiver [BuilderScope] forwarded into the resolved builder.
     * @param eventSchema the schema to build a holder for.
     * @return the newly built holder.
     * @throws IllegalArgumentException if no builder is registered for [eventSchema]'s class — the
     * schema's own class was never included in `MosaicDependencyInjectionConfig.eventDefinitions`.
     * Unlike a missing `EventRunner`/`TileRenderer` (which logs and continues), a missing
     * `EventHolderBuilder` throws, since there's no way to build the tree at all without it.
     */
    fun BuilderScope.build(eventSchema: EventSchema) = builders[eventSchema::class]?.let { builder ->
        with(builder) { build(eventSchema) }
    } ?: throw IllegalArgumentException("Couldn't find a builder for $eventSchema")
}