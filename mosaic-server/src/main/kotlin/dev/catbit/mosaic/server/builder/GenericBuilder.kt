package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider

/**
 * Base class every DSL builder extends — `TileSchemaBuilder<T>` and `EventSchemaBuilder<T>` both
 * derive from this, one instance per DSL call (`Column { }`, `SendNetworkRequest(...)`, etc.),
 * responsible for producing the concrete schema [T] that call compiles down to.
 *
 * @param T the schema type this builder produces.
 * @property compositionLocals the [CompositionLocal] snapshot captured by
 * [GenericBuilderScope.addBuilder] at the moment this builder was registered — not when [build] is
 * later called. [GenericBuilderScope.build] restores this exact snapshot (via [BuildContext.with])
 * around the [build] call, so a `CompositionLocalProvider` block's ambient values are visible to
 * every builder created inside it, regardless of how deeply nested the actual `build()` invocation
 * ends up being relative to that block.
 */
abstract class GenericBuilder<out T> {
    internal var compositionLocals = emptyMap<CompositionLocal<*>, ValueProvider<*>>()

    /** Produces this builder's concrete schema instance. Called once, by
     * [GenericBuilderScope.build], with [compositionLocals] active. */
    abstract fun build(): T
}
