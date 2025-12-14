package dev.catbit.mosaic.client.ui.foundation.state.producer

import dev.catbit.mosaic.client.ui.foundation.state.UIState
import dev.catbit.mosaic.client.ui.foundation.state.producer.updater.UIStateProducerUpdater

abstract class UIStateProducer<out T : UIState> {

    protected abstract val updater: UIStateProducerUpdater
    abstract fun update(updateData: Map<String, Any?>)

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
}