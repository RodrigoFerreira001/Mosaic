package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.extensions.withNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <reified R> MutableStateFlow<*>.updateAs(
    block: R.() -> R
) {
    @Suppress("UNCHECKED_CAST")
    (this as? MutableStateFlow<R>)?.let {
        withNotNull(value) {
            update { block() }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <R> MutableStateFlow<*>.like() = value as R