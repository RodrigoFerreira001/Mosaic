package dev.catbit.mosaic.server.builder.composition_local

object BuildContext {
    private val current = ThreadLocal.withInitial<Map<CompositionLocal<*>, ValueProvider<*>>> { emptyMap() }

    fun get(): Map<CompositionLocal<*>, ValueProvider<*>> = current.get()

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
