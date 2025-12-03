package dev.catbit.mosaic.client.ui.foundation.state_producer

import dev.catbit.mosaic.client.ui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.foundation.state.UIState

abstract class UIStateProducer<out T : UIState> {

    protected var lastState: @UnsafeVariance T? = null
    val state: T
        get() = if (shouldProduce()) {
            produce().also { lastState = it }
        } else {
            lastState ?: produce().also { lastState = it }
        }

    abstract fun shouldProduce(): Boolean
    protected abstract fun produce(): T

    protected fun shouldProduceWithLastState(
        block: (T) -> Boolean
    ): Boolean = lastState?.let { block(it) } ?: true

    // Update
    // Events
}