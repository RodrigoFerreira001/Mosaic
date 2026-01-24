package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

interface ScreenBehaviorsHolder {
    fun refresh()
    fun broadcastData(data: BroadcastData)
}