package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Downloads [url] without touching the filesystem, keeping the content in memory. [method],
 * [headers] and [body] shape the request.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnStartEventTrigger` — before the download begins.
 * - `OnDownloadProgressEventTrigger` — repeatedly while downloading, with the progress passed as
 *   incomingData.
 * - `OnDownloadFinishEventTrigger` then `OnSuccessEventTrigger` — when the download completed,
 *   both carrying the total byte count as incomingData.
 * - `OnDownloadFailureEventTrigger` then `OnFailureEventTrigger` — when the download failed, both
 *   carrying the `Throwable`; the error is logged.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnDownloadProgressEventTrigger::class,
        OnDownloadFinishEventTrigger::class,
        OnDownloadFailureEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("DownloadFileToMemory")
data class DownloadFileToMemoryEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : EventSchema
