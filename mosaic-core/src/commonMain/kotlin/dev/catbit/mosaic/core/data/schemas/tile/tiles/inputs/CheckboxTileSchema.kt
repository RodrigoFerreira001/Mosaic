package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 checkbox whose checked state is fully server-controlled via [checked].
 *
 * **Updatable fields (via UpdateTiles):** `checked`, `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnCheckEventTrigger] — fired when the user taps the checkbox and the new state is checked
 *   (`true`). Fired before [OnCheckChangedEventTrigger].
 * - [OnUncheckEventTrigger] — fired when the user taps the checkbox and the new state is
 *   unchecked (`false`). Fired before [OnCheckChangedEventTrigger].
 * - [OnCheckChangedEventTrigger] — fired on every toggle, regardless of direction, immediately
 *   after the directional trigger. A local [CheckboxTileEvents.OnCheckChanged] is also
 *   dispatched so other tiles on the screen can observe the state change.
 *
 * **Notes:** The [checked] field is the source of truth for the visual state — the checkbox
 * does not maintain its own internal toggle state. After the user interacts, the three triggers
 * fire in rapid succession but the checkbox will visually snap back to the server-provided
 * [checked] value until the server pushes an UpdateTiles payload with the new value. This means
 * the tile is always controlled, not uncontrolled. [enabled] prevents interaction when false.
 */
@Triggers(
    [
        OnUncheckEventTrigger::class,
        OnCheckEventTrigger::class,
        OnCheckChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("Checkbox")
data class CheckboxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("checked") val checked: Boolean,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema
