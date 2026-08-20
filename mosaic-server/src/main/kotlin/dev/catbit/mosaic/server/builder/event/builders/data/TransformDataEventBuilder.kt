package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TransformDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val template: AnySerializable
) : EventSchemaBuilder<TransformDataEventSchema>() {

    override fun build() = TransformDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        template = template
    )
}

/**
 * Reshapes `incomingData` into a new value by applying [template] through the client's
 * `TemplateProcessor`, letting a payload be rewritten mid-chain without a round trip. `template`
 * placeholders follow `<|path.to.key|>` (dot-notation into `incomingData`) and `<||>` (the whole
 * `incomingData`, type preserved); a template mixing a placeholder with literal text is coerced
 * to a string, while a single bare placeholder keeps its native type. `incomingData` is required
 * as the input the template is applied to. Dispatches `onSuccess` (carrying the transformed
 * value) when the template applies cleanly; `onFailure` (carrying the thrown `Throwable`, error
 * logged) when applying it throws.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param template Literal template value (string, map, list, etc) whose placeholders are resolved against `incomingData`.
 */
fun EventSchemaBuilderScope.TransformData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    template: AnySerializable
) {
    addBuilder(
        TransformDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            template = template
        )
    )
}

/**
 * Overload of `TransformData` that builds the template as a nested tile-event-style block
 * ([eventTemplate]) rather than a literal value — useful when the template itself needs to be
 * assembled from Kotlin data structures via the event DSL. Behaves otherwise exactly like the
 * literal-[template] overload.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param eventTemplate Block building the template value; a single declared value is unwrapped, several are kept as a list. Defaults to empty.
 */
fun EventSchemaBuilderScope.TransformData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    eventTemplate: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        TransformDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            template = run {
                val template = EventSchemaBuilderScope()
                    .apply(eventTemplate)
                    .build()

                if (template.size == 1) template.first() else template
            }
        )
    )
}
