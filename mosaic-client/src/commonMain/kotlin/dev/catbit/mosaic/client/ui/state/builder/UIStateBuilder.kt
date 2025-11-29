package dev.catbit.mosaic.client.ui.state.builder

import dev.catbit.mosaic.client.ui.state.UIState

abstract class UIStateBuilder<out T : UIState> {

    protected var lastState: @UnsafeVariance T? = null
    val state: T
        get() = if (shouldRebuild()) {
            build().also { lastState = it }
        } else {
            lastState ?: build().also { lastState = it }
        }

    abstract fun shouldRebuild(): Boolean
    protected abstract fun build(): T

    protected fun shouldRebuildWithLastState(
        block: (T) -> Boolean
    ): Boolean = lastState?.let { block(it) } ?: true
}