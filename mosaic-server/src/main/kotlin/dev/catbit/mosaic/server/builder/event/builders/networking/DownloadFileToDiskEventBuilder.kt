package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToDiskEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DownloadFileToDiskEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?,
    private val targetFileName: String
) : EventSchemaBuilder<DownloadFileToDiskEventSchema>() {

    override fun build() = DownloadFileToDiskEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        body = body,
        headers = headers,
        targetFileName = targetFileName
    )
}

fun EventSchemaBuilderScope.DownloadFileToDisk(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    targetFileName: String
) {
    addBuilder(
        DownloadFileToDiskEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            body = body,
            headers = headers,
            targetFileName = targetFileName
        )
    )
}
