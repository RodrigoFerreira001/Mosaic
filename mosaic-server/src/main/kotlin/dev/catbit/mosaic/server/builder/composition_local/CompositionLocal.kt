package dev.catbit.mosaic.server.builder.composition_local

import dev.catbit.mosaic.server.builder.GenericBuilderScope
import kotlin.reflect.KClass

/**
 * A build-time equivalent of Compose's `CompositionLocal`: a typed slot that DSL code can read
 * ([current]) without it being threaded explicitly through every builder call, and that a scope
 * of the tree can override for its descendants via [CompositionLocalProvider]. Used internally by
 * builders that need ambient state (e.g. the current lazy-item/row/column scope) during tile
 * composition.
 *
 * @param type Runtime type of the held value, used to key it in [BuildContext].
 * @param defaultValue Value returned by [current] when nothing has provided an override.
 */
data class CompositionLocal<T>(
    val type: KClass<*>,
    val defaultValue: () -> T
) {
    /** Pairs this local with [value], ready to be passed to [CompositionLocalProvider]. */
    infix fun provides(value: T) = this to ValueProvider(value)
}

/** Holds a value provided for a [CompositionLocal] inside a [CompositionLocalProvider] block. */
data class ValueProvider<T>(
    private val value: T
) {
    /** Returns the held value. */
    fun provide(): T = value
}

/** Declares a new [CompositionLocal] of type [T], falling back to [defaultValue] when unset. */
inline fun <reified T> compositionLocalOf(
    noinline defaultValue: () -> T
) = CompositionLocal(T::class, defaultValue)

/** Reads the value currently provided for this [CompositionLocal], or its [CompositionLocal.defaultValue] if none was provided. */
@Suppress("UNCHECKED_CAST")
fun <T> CompositionLocal<T>.current(): T =
    (BuildContext.get()[this]?.provide() as? T) ?: defaultValue()

/**
 * Runs [content] with [providedValues] overriding their respective [CompositionLocal]s for the
 * duration of the block — descendants built inside [content] see the new values via
 * [CompositionLocal.current]; once the block returns, the previous values are restored.
 *
 * @param providedValues Local-to-value overrides, built with [CompositionLocal.provides].
 * @param content Builder code run with the overrides active.
 */
fun <T : GenericBuilderScope<*, *>> T.CompositionLocalProvider(
    vararg providedValues: Pair<CompositionLocal<*>, ValueProvider<*>>,
    content: T.() -> Unit
) {
    BuildContext.with(BuildContext.get() + providedValues.toMap()) {
        this.content()
    }
}
