package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

/**
 * Ties one [EventSchema] subtype to everything `mosaic-client` needs to execute it — the unit passed
 * in `MosaicDependencyInjectionConfig.eventDefinitions` to register a custom event, and the shape
 * every built-in event's own `XEventDefinition` object follows. `MosaicModules` uses [eventSchemaClass]
 * to build the framework's `MosaicSerializer` registration, [eventRunner] to build the
 * `EventRunnerManager` lookup table, and [eventHolderBuilder] to build the `EventHolderBuilderManager`
 * lookup table — one `Definition` feeds all three, so registering a custom event is exactly this one
 * object, nothing more.
 */
interface EventDefinition <Schema: EventSchema> {
    /** The schema class this definition is for — the key everything else is looked up by. */
    val eventSchemaClass: KClass<Schema>
    /** Executes events of this schema type. */
    val eventRunner: EventRunner<Schema>
    /** Builds the live `EventHolder` for events of this schema type. */
    val eventHolderBuilder: EventHolderBuilder<Schema, out EventHolder<Schema>>
}