package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.extensions.withNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Applies [block] to a `MutableStateFlow<*>`'s current value as if it were statically typed
 * `MutableStateFlow<R>` — for call sites holding a type-erased state flow (e.g. a sealed UI-state
 * flow narrowed to one specific subtype at a given point) that still want a type-safe `update {}`.
 * A no-op if the flow's current value isn't actually non-null (via [withNotNull]) — this doesn't
 * itself verify the value is really an instance of [R]; an unchecked cast that doesn't hold throws
 * inside [block].
 *
 * @param block transforms the current value of type [R] into its next value.
 */
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

/** Unchecked-casts this type-erased `MutableStateFlow<*>`'s current value to [R] — the read-only
 * counterpart of [updateAs], for a call site that only needs the current value narrowed, not to
 * update it. */
@Suppress("UNCHECKED_CAST")
fun <R> MutableStateFlow<*>.like() = value as R