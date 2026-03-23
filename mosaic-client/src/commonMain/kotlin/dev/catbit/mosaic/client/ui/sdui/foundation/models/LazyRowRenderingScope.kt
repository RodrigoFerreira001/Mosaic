package dev.catbit.mosaic.client.ui.sdui.foundation.models

sealed interface LazyRowRenderingScope {
    data object Defined : LazyRowRenderingScope
    object Undefined : LazyRowRenderingScope

    fun isUndefined() = this is Undefined
}