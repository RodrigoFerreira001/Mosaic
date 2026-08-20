package dev.catbit.mosaic.client.ui.sdui.foundation.models

/**
 * Two-state marker (`Defined`/`Undefined`) with an `isUndefined()` check — the `LazyRow` counterpart
 * of [LazyColumnRenderingScope]. Not referenced anywhere else in `mosaic-client` at the time of
 * writing — no `LazyRow` renderer or holder constructs or reads either case.
 */
sealed interface LazyRowRenderingScope {
    data object Defined : LazyRowRenderingScope
    object Undefined : LazyRowRenderingScope

    fun isUndefined() = this is Undefined
}