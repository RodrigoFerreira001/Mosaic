package dev.catbit.mosaic.core.data.schemas.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * The wire contract every Event implements — the shared shape [MosaicSerializer] resolves
 * polymorphically by `@SerialName`, decoded into a concrete subtype (one `data class` per built-in
 * event, plus whatever third-party ones an app registers).
 *
 * An event is never invoked directly by anything holding it — it's always reached by matching
 * [trigger] against a value fired by whatever ran before it (see the `mosaic` skill's event-chaining
 * mechanism for the matching itself), then executed by the `EventRunner` registered for its concrete
 * class.
 */
@Immutable
interface EventSchema {
    /** Unique identifier — how `TriggerEvent`/`UpdateEvents` address this event by id. Defaults to a
     * random id when omitted from the DSL. */
    val id: String

    /** The condition that must occur for this event to run — matched by structural equality
     * (`data class`/`data object` equality) against whatever trigger value fired, never by any kind
     * of subscription or listener registry. */
    val trigger: EventTrigger

    /** This event's own children — each declaring, via its own [trigger], which of *this* event's
     * outgoing triggers runs it. `null` when this event declares no children. */
    val events: SerializableImmutableList<EventSchema>?
}