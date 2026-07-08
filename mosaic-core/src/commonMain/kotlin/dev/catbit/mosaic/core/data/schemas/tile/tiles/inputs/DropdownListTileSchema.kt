package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListItemSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 `ExposedDropdownMenuBox` with a list of selectable options, in either a
 * filled or outlined visual style controlled by [kind].
 *
 * **Form tile:** [produceValueWithKey] always returns `mapOf(key to selectedOptionId)`. There is
 * no nullable/placeholder concept — [selectedOptionId] is always a valid option id. To represent
 * a "please select" state, include a dedicated option in [options] and pass its id as the initial
 * [selectedOptionId].
 *
 * **Server contract:** the server always sends [expanded] as `false`; the client owns the
 * expanded/collapsed state. The server builder validates that [selectedOptionId] is present in
 * [options] at build time.
 *
 * **Updatable fields (via UpdateTiles):** `selectedOptionId`, `enabled`, `options`, `kind`,
 * `supportingText`, `state`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnDropdownListItemSelectedEventTrigger] — fired when the user taps an option in the menu;
 *   carries the selected option `id` as incomingData.
 * - [OnDropdownListOpenEventTrigger] — fired when the dropdown menu opens (either by user toggle
 *   or after a dismiss-then-open sequence).
 * - [OnDropdownListCloseEventTrigger] — fired when the dropdown menu closes (user toggle or
 *   outside-dismiss via `onDismissRequest`).
 *
 * **Client state management:**
 * - `OnDropdownListToggle` TileEvent — toggles [expanded].
 * - `OnDropdownListDismissRequest` TileEvent — collapses [expanded] to `false`.
 * - `OnItemSelected` TileEvent — updates [selectedOptionId] and collapses [expanded] to `false`.
 */
@Immutable
@Triggers(
    [
        OnDropdownListItemSelectedEventTrigger::class,
        OnDropdownListOpenEventTrigger::class,
        OnDropdownListCloseEventTrigger::class,
    ]
)
@Serializable
@SerialName("DropdownList")
data class DropdownListTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("expanded") val expanded: Boolean,
    @SerialName("options") val options: SerializableImmutableList<SelectOption>,
    @SerialName("selectedOptionId") val selectedOptionId: String,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("kind") val kind: Kind,
    @SerialName("supportingText") val supportingText: String?,
    @SerialName("state") val state: State,
) : TileSchema {

    @Serializable
    data class SelectOption(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String,
    )

    enum class Kind {
        FILLED, OUTLINED
    }

    enum class State {
        NORMAL, ERROR
    }
}
