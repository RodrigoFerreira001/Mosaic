package dev.catbit.mosaic.server.builder.composition_local

import dev.catbit.mosaic.server.builder.GenericBuilderScope
import kotlin.reflect.KClass

data class CompositionLocal<T>(
    val type: KClass<*>,
    val defaultValue: () -> T
) {
    infix fun provides(value: T) = this to ValueProvider(value)
}

data class ValueProvider<T>(
    private val value: T
) {
    fun provide(): T = value
}

inline fun <reified T> compositionLocalOf(
    noinline defaultValue: () -> T
) = CompositionLocal(T::class, defaultValue)

fun <T : GenericBuilderScope<*, *>, V> T.CompositionLocalProvider(
    vararg providedValues: Pair<CompositionLocal<V>, ValueProvider<V>>,
    content: T.() -> Unit
) {
    val snapshot = snapshotLocals()
    pushLocals(providedValues.toMap())
    content()
    restoreLocals(snapshot)
}