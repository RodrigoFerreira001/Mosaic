package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UploadFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String?,
    private val method: HttpMethod,
    private val headers: Map<String, String>?,
    private val contentType: String?,
) : EventSchemaBuilder<UploadFileEventSchema>() {

    override fun build() = UploadFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        headers = headers,
        contentType = contentType,
    )
}

fun EventSchemaBuilderScope.UploadFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String? = null,
    method: HttpMethod = HttpMethod.PUT,
    headers: Map<String, String>? = null,
    contentType: String? = null,
) {
    addBuilder(
        UploadFileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            headers = headers,
            contentType = contentType,
        )
    )
}
