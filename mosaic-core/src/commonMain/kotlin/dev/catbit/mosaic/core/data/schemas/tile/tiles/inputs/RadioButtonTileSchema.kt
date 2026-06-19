package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 radio button whose selected state is fully server-controlled via
 * [selected]. Multiple radio buttons sharing the same [groupId] form a logical selection group.
 *
 * **Updatable fields (via UpdateTiles):** `selected`, `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnSelectEventTrigger] — fired when the user taps this radio button. Carries no incomingData.
 *
 * **Notes:** On tap, in addition to firing [OnSelectEventTrigger], the renderer dispatches a
 * group-level [RadioButtonTileGroupEvents.OnRadioSelected] event carrying both this tile's [id]
 * and the [groupId]. This group event allows other radio buttons in the same group to deselect
 * themselves without a server round-trip. The [selected] field is server-owned — the radio
 * button is a controlled component and will not stay visually selected until the server sends
 * an UpdateTiles payload. [enabled] prevents interaction when false.
 */
@Immutable
@Triggers(
    [
        OnSelectEventTrigger::class,
    ]
)
@Serializable
@SerialName("RadioButton")
data class RadioButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("selected") val selected: Boolean,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("groupId") val groupId: String
) : TileSchema
