package dev.catbit.mosaic.client.ui.sdui.foundation.data_processor

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope

interface DataProcessor {
    val id: String
    fun EventRunningScope.proccess(data: Any)
}