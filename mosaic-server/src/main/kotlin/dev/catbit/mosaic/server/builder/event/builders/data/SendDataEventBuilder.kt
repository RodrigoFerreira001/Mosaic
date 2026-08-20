package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SendDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dataKey: String,
    private val data: AnySerializable?
) : EventSchemaBuilder<SendDataEventSchema>() {

    override fun build() = SendDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dataKey = dataKey,
        data = data
    )
}

/**
 * Posts a value into the client's `DataMailer` under [dataKey], where a later
 * `CheckForReceivedData` can pick it up — the mailer is a one-shot channel, so this is how a
 * value is handed off to another screen. Sends [data] when it is non-null, otherwise
 * `incomingData`. Dispatches `onSuccess` (no data) after the value is posted; `onFailure` (no
 * data, error logged) when both [data] and `incomingData` are `null`, so there is nothing to send.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param dataKey Mailer key the value is posted under, read by a matching `CheckForReceivedData`.
 * @param data Literal value to send. Defaults to none (sends `incomingData` instead).
 */
fun EventSchemaBuilderScope.SendData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dataKey: String,
    data: AnySerializable? = null
) {
    addBuilder(
        SendDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dataKey = dataKey,
            data = data
        )
    )
}
