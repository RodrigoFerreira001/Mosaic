package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 switch (toggle) whose on/off state is fully server-controlled via
 * [checked].
 *
 * **Updatable fields (via UpdateTiles):** `checked`, `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnCheckEventTrigger] — fired when the user toggles the switch to the on position (`true`).
 *   Fired before [OnCheckChangedEventTrigger].
 * - [OnUncheckEventTrigger] — fired when the user toggles the switch to the off position
 *   (`false`). Fired before [OnCheckChangedEventTrigger].
 * - [OnCheckChangedEventTrigger] — fired on every toggle, regardless of direction, immediately
 *   after the directional trigger. A local [SwitchTileEvents.OnCheckChanged] is also dispatched
 *   so other tiles on the screen can observe the state change.
 *
 * **Notes:** Identical trigger semantics to [CheckboxTileSchema] — [checked] is server-owned
 * and the switch is a controlled component. After interaction, the visual state snaps back to
 * the server-provided [checked] value until an UpdateTiles payload arrives. [enabled] prevents
 * interaction when false. The renderer is structurally identical to [CheckboxTileRenderer]
 * with the composable replaced by [Switch].
 */
@Immutable
@Triggers(
    [
        OnUncheckEventTrigger::class,
        OnCheckEventTrigger::class,
        OnCheckChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("Switch")
data class SwitchTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("checked") val checked: Boolean,
    @SerialName("enabled") val enabled: Boolean
) : TileSchema
