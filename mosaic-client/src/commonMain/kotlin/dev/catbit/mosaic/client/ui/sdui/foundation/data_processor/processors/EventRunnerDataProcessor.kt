package dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.processors

import dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.DataProcessor
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

object EventRunnerDataProcessor : DataProcessor {
    override val id: String = "EVENT_RUNNER"

    override fun EventRunningScope.proccess(data: Any) {

        val jsonElement = data.toJsonElement()
        val serializer = get<MosaicSerializer>()

        runSafely {
            when (jsonElement) {
                is JsonArray -> {
                    jsonElement
                        .filterIsInstance<JsonObject>()
                        .map { serializer.decodeEventFromJsonElement(it) }
                        .forEach { runEventInline(it) }

                }

                is JsonObject -> {
                    runEventInline(serializer.decodeEventFromJsonElement(jsonElement))
                }

                else -> Unit
            }
        }
    }
}