package dev.catbit.mosaic.server.builder.composition_local

/**
 * A build-time "CompositionLocal for `mosaic-server`" — a `ThreadLocal`-backed ambient value store,
 * modeled directly on Jetpack Compose's own `CompositionLocal` mechanism, letting a value set by an
 * outer block of the DSL (via [CompositionLocalProvider][dev.catbit.mosaic.server.builder.composition_local.CompositionLocalProvider])
 * reach a builder several levels deep without every intermediate function threading it through as an
 * explicit parameter.
 *
 * Not read/written directly by DSL authors — [dev.catbit.mosaic.server.builder.composition_local.CompositionLocal.current]
 * reads through [get], `CompositionLocalProvider` overrides via [with], and
 * [dev.catbit.mosaic.server.builder.GenericBuilderScope] snapshots/restores a builder's own values
 * around its [dev.catbit.mosaic.server.builder.GenericBuilder.build] call — that snapshot/restore
 * dance (not just a plain "current ambient value") is what keeps a builder's ambient state correct
 * even though `build()` may run later, and at a different call-stack depth, than the DSL call that
 * registered it.
 *
 * `snapshotLocals()`-style reads must happen before a builder is handed to `addBuilder()`, never
 * inside the lambda passed to it — by the time that lambda runs, the `CompositionLocalProvider` block
 * that set the value may have already restored the previous one.
 */
object BuildContext {
    private val current = ThreadLocal.withInitial<Map<CompositionLocal<*>, ValueProvider<*>>> { emptyMap() }

    /** The ambient [CompositionLocal]-to-value map currently active on this thread. */
    fun get(): Map<CompositionLocal<*>, ValueProvider<*>> = current.get()

    /**
     * Runs [block] with [locals] as the ambient map, restoring whatever was active before once
     * [block] returns (or throws) — the primitive behind both `CompositionLocalProvider` (temporarily
     * overriding values for nested DSL calls) and [dev.catbit.mosaic.server.builder.GenericBuilderScope.build]
     * (restoring each builder's own captured snapshot for the duration of its `build()` call).
     *
     * @param locals the ambient map to make active for the duration of [block].
     * @param block the code to run with [locals] active.
     * @return whatever [block] returns.
     */
    internal fun <R> with(
        locals: Map<CompositionLocal<*>, ValueProvider<*>>,
        block: () -> R
    ): R {
        val previous = current.get()
        current.set(locals)
        return try {
            block()
        } finally {
            current.set(previous)
        }
    }
}
