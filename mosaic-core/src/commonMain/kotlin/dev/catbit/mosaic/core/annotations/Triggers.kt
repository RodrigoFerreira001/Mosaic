package dev.catbit.mosaic.core.annotations

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlin.reflect.KClass

/**
 * Documents, right on a [dev.catbit.mosaic.core.data.schemas.tile.TileSchema]/
 * [dev.catbit.mosaic.core.data.schemas.event.EventSchema] class itself, which [EventTrigger]s it can
 * fire — living documentation visible to a human (or another LLM) reading the schema's source,
 * without them having to also read its `TileRenderer`/`EventRunner` implementation in `mosaic-client`
 * to find out. `AnnotationRetention.SOURCE` means this is purely a source-level/tooling artifact —
 * it's stripped before compilation and nothing at runtime ever reads it; every built-in
 * tile/event declares it as a convention, and a custom one should too, for the same reason.
 *
 * @property triggers every [EventTrigger] subtype the annotated schema can fire.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Triggers(
    val triggers: Array<KClass<out EventTrigger>>
)
