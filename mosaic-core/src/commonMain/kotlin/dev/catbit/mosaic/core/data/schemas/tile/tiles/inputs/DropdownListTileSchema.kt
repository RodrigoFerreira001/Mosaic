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
 * Renders a Material 3 `ExposedDropdownMenuBox`: a read-only anchor field showing the label of
 * the currently selected option, plus a dropdown listing every entry in [options]. [kind] picks
 * the anchor composable — [Kind.FILLED] → `TextField`, [Kind.OUTLINED] → `OutlinedTextField` —
 * each with the matching `ExposedDropdownMenuDefaults` colors and the standard expand/collapse
 * trailing icon. [enabled] and [supportingText] are forwarded, and [state] set to [State.ERROR]
 * switches the field into Material's error styling.
 *
 * **Selection:** the anchor displays the [SelectOption.label] of the option whose
 * [SelectOption.id] equals [selectedOptionId]. Tapping an item dispatches a local
 * `DropdownListTileEvents.OnItemSelected`, and the holder stores the new [selectedOptionId] and
 * closes the menu — no round trip to the server. Toggling the anchor dispatches
 * `OnDropdownListToggle` (holder flips [expanded]) and dismissing dispatches
 * `OnDropdownListDismissRequest` (holder closes).
 *
 * **Triggers dispatched:**
 * - `OnDropdownListOpenEventTrigger` — when the anchor is tapped while closed.
 * - `OnDropdownListCloseEventTrigger` — whenever the menu closes: when the anchor is tapped
 *   while open, when the menu is dismissed by tapping outside, and right after an item is
 *   picked.
 * - `OnDropdownListItemSelectedEventTrigger` — when an item is picked, right before the close
 *   trigger above. The trigger carries the selected [SelectOption.id], so events can be wired
 *   per option.
 *
 * **Value production:** the holder exposes the current [selectedOptionId] under a caller-chosen
 * key, so `GetData` / `EvaluateData` events can read this dropdown by its [id].
 *
 * **Notes:** when [selectedOptionId] matches none of the [options] the anchor renders empty; the
 * menu itself still lists every option, so the user can recover by picking one.
 */
@Immutable
@Triggers(
    [
        OnDropdownListOpenEventTrigger::class,
        OnDropdownListCloseEventTrigger::class,
        OnDropdownListItemSelectedEventTrigger::class,
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
