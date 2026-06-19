package dev.catbit.mosaic.core.data.schemas.tile.tiles.search

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a custom search bar built on [BasicTextField] with a Material 3 surface styling.
 * The bar displays a search input field with an optional placeholder, an optional leading
 * icon tile, and a built-in animated clear button that appears when [query] is non-empty.
 * The [trailingIcon] field defined on the schema is not rendered by the current renderer
 * (the clear button is hardcoded instead).
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `query: String`, `placeholder: String?`,
 * `leadingIcon: TileSchema?`
 *
 * **Triggers dispatched:**
 * - `OnTextChangedEventTrigger` (aliased as `onQueryChanged`) — fired on every keystroke,
 *   with the new query string passed as trigger data. Also dispatches the internal
 *   `SearchBarTileEvents.OnQueryChanged` with the new value.
 * - `onQueryCleared` — fired when the user taps the clear button. Also dispatches the
 *   internal `SearchBarTileEvents.OnQueryCleared`.
 * - `onSearch` — fired when the user submits the search via the IME search action, with the
 *   current [query] as trigger data.
 *
 * **Notes:** The search bar is rendered at a fixed height of 56 dp. The IME action is set
 * to `ImeAction.Search`. Because the query value is server-driven, the server must update
 * [query] via UpdateTiles on each `onQueryChanged` trigger to keep the displayed text in
 * sync with user input.
 */
@Immutable
@Triggers(
    [
        OnTextChangedEventTrigger::class
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
