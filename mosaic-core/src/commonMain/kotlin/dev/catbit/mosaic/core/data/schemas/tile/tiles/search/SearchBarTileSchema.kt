package dev.catbit.mosaic.core.data.schemas.tile.tiles.search

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryClearedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a search field: a Material `Surface` (extra-large shape, high surface
 * container color) wrapping a single-line text field showing [query], with [placeholder] when
 * empty. The keyboard's IME action is always "Search".
 *
 * **Icons:** [leadingIcon] is an arbitrary tile rendered in the leading slot. The trailing slot
 * is shared: while [query] is empty it shows [trailingIcon] (if any); as soon as [query] has
 * text it cross-fades to a built-in `clear` icon button, so a custom trailing icon is never
 * visible at the same time as the clear button.
 *
 * **Query state:** typing dispatches a local `SearchBarTileEvents.OnQueryChanged` and pressing
 * clear dispatches `OnQueryCleared`; the holder applies both to its own state, so the text
 * survives without a round trip to the server.
 *
 * **Triggers dispatched:**
 * - `OnQueryChangedEventTrigger` — on every keystroke, with the new text as the event's incoming
 *   data. Pressing clear also fires it, with an empty string.
 * - `OnQueryClearedEventTrigger` — when the clear button is pressed, right before the
 *   query-changed trigger above.
 * - `OnSearchEventTrigger` — when the IME "Search" action is pressed, with the current [query]
 *   as the event's incoming data.
 *
 * **Notes:** this is only the input field — it has no expanded state and shows no suggestion
 * list. Render results yourself, e.g. by pairing it with a `LazyColumn` whose
 * `filterChildrenByTerm` is driven by the query.
 */
@Immutable
@Triggers(
    [
        OnQueryChangedEventTrigger::class,
        OnQueryClearedEventTrigger::class,
        OnSearchEventTrigger::class,
    ]
)
@Serializable
@SerialName("SearchBar")
data class SearchBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("query") val query: String = "",
    @SerialName("placeholder") val placeholder: String? = null,
    @SerialName("leadingIcon") val leadingIcon: TileSchema? = null,
    @SerialName("trailingIcon") val trailingIcon: TileSchema? = null
) : TileSchema
