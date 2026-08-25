package dev.catbit.mosaic.client.ui.sdui.foundation.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class OverlayDisplayCallbackHolder {

    private val callbacks = mutableMapOf<String, suspend () -> Unit>()

    fun register(overlayId: String, callback: suspend () -> Unit) {
        callbacks[overlayId] = callback
    }

    fun fire(overlayId: String) {
        callbacks.remove(overlayId)?.let { callback ->
            CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).launch {
                callback()
            }
        }
    }

    fun cancel(overlayId: String) {
        callbacks.remove(overlayId)
    }
}
