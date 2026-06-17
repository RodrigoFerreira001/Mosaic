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

@Suppress("UNCHECKED_CAST")
fun <T> CompositionLocal<T>.current(): T =
    (BuildContext.get()[this]?.provide() as? T) ?: defaultValue()

fun <T : GenericBuilderScope<*, *>> T.CompositionLocalProvider(
    vararg providedValues: Pair<CompositionLocal<*>, ValueProvider<*>>,
    content: T.() -> Unit
) {
    BuildContext.with(BuildContext.get() + providedValues.toMap()) {
        this.content()
    }
}
