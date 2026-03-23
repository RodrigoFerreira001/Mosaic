package dev.catbit.mosaic.client.ui.sdui.foundation.models

sealed interface LazyColumnRenderingScope {
    data object Defined: LazyColumnRenderingScope
    data object Undefined : LazyColumnRenderingScope

    fun isUndefined() = this is Undefined
}