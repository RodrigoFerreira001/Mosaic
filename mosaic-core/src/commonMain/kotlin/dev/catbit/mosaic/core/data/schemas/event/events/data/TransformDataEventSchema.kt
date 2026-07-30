package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Reshapes `incomingData` by applying a [template] structure, substituting placeholders of the
 * form `<|path.to.value|>` or `</path.to.value/>` with values resolved from `incomingData`. The
 * result preserves the structural shape of [template] (map, list, or scalar) with placeholders
 * replaced.
 *
 * **incomingData consumed:** Used as the data source for all placeholder lookups. Dot-notation
 * paths (e.g. `<|user.address.city|>`) and array index notation (e.g. `<|items[0].name|>`) are
 * supported. A path that does not exist in `incomingData` throws an exception.
 *
 * **Two placeholder delimiters, same path syntax:**
 * - `<|path|>` (pipe) — when it is the entire template string, the resolved value keeps its
 *   native runtime type (e.g. `Int`, `Boolean`, `Map`).
 * - `</path/>` (slash) — always coerces the resolved value to `String` via `.toString()`, even
 *   when it is the entire template string. `<//>` (empty path) converts the whole `incomingData`
 *   value to its string form. Use this whenever the destination expects a `String` but the
 *   source value isn't naturally one (e.g. feeding a numeric countdown tick into a tile's `text`
 *   field via `UpdateTiles`' `mappedIncomingTileUpdateData`).
 * - Both behave identically when embedded in surrounding text ("mixed content") — literal text
 *   around a placeholder already forces stringification regardless of delimiter.
 *
 * **Triggers fired:**
 * - [onSuccess(data)] – When the template is applied without error. `data` is the fully resolved
 *   output value, which mirrors the structure of [template] with all placeholders replaced.
 * - [onFailure(data)] – When `TemplateProcessor.applyTemplate` throws. `data` is the `Throwable`.
 *
 * **Failure scenarios:**
 * - A placeholder path segment references a key that does not exist in the map at that level
 *   (throws [NoSuchElementException]).
 * - A path segment expects a Map but the current value is not a Map (throws
 *   [IllegalArgumentException]).
 * - A path segment uses an array index but the current value is not a List, or the index is out
 *   of bounds (throws [IllegalArgumentException] or [IndexOutOfBoundsException]).
 * - A mixed-content string (a string containing a placeholder alongside literal text) where the
 *   placeholder resolves to null (throws [IllegalArgumentException]).
 * - An invalid array index string (non-integer between `[` and `]`) throws [IllegalArgumentException].
 *
 * **Notes:**
 * - If the entire [template] string is a single `<|path|>` placeholder, the resolved value
 *   retains its native runtime type (e.g. Int, Boolean, Map). A `</path/>` placeholder always
 *   coerces to String, even as the entire template string. If a placeholder of either kind is
 *   embedded in surrounding text, the resolved value is coerced to String via `.toString()`.
 * - [template] may itself be a Map or List; transformation is applied recursively to every leaf.
 * - Non-string, non-Map, non-List template values (e.g. numbers, booleans) are returned as-is
 *   without placeholder substitution.
 * - This event is synchronous — it does not dispatch to a background dispatcher.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("TransformData")
data class TransformDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("template") val template: AnySerializable
) : EventSchema