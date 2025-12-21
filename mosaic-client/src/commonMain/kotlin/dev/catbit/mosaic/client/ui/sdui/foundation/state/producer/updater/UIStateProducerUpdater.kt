package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater

fun interface UIStateProducerUpdater {
    fun getUpdatedData(updateData: Map<String, Any?>): Map<String, Any?>
}