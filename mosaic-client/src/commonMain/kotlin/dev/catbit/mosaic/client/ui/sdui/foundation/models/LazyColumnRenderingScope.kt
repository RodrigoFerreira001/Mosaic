package dev.catbit.mosaic.client.ui.sdui.foundation.models

/**
 * Two-state marker (`Defined`/`Undefined`) with an `isUndefined()` check. Not referenced anywhere
 * else in `mosaic-client` at the time of writing — no `LazyColumn` renderer or holder constructs or
 * reads either case.
 */
sealed interface LazyColumnRenderingScope {
    data object Defined: LazyColumnRenderingScope
    data object Undefined : LazyColumnRenderingScope

    fun isUndefined() = this is Undefined
}