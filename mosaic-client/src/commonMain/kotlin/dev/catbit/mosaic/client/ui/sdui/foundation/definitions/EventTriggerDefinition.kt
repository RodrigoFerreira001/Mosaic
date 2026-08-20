package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlin.reflect.KClass

/**
 * Registers a custom [EventTrigger] subtype's class with the framework's `MosaicSerializer` — the
 * unit passed in `MosaicDependencyInjectionConfig.eventTriggerDefinition`. Unlike [TileDefinition]/
 * [EventDefinition], there's no renderer/runner/holder-builder to also register here — a trigger has
 * no behavior of its own, it's only ever compared for equality (see the `mosaic` skill's
 * event-chaining mechanism), so serialization registration is all a custom trigger type needs.
 */
interface EventTriggerDefinition<T : EventTrigger> {
    /** The trigger class this definition is for. */
    val eventTriggerClass: KClass<T>
}