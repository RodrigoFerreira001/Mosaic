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
 * Renders a bare Material 3 `RadioButton` reflecting [selected]. [enabled] is forwarded to the
 * composable. The tile draws only the radio circle — no label is rendered, so pair it with a
 * `SimpleText` inside a `Row` when a caption is needed.
 *
 * **Grouping:** tapping the button dispatches a `RadioButtonTileGroupEvents.OnRadioSelected`
 * group event carrying this tile's [id] and [groupId]. Every radio holder whose [groupId]
 * matches reacts by setting its own `selected` to `id == selectedTileId`, so mutual exclusion
 * is handled entirely on the client — no server round trip and no `UpdateTiles` needed to
 * clear the previously selected button. Radios with different [groupId] values are unaffected.
 *
 * **Triggers dispatched:**
 * - `OnSelectEventTrigger` — fired on the radio button that becomes selected. Tapping a radio
 *   that is already selected fires nothing; the radios that become deselected fire nothing
 *   either, since only the tapped tile dispatches triggers.
 *
 * **Value production:** the holder exposes the current [selected] boolean under a caller-chosen
 * key, so `GetData` / `EvaluateData` events can read this radio button by its [id].
 */
@Immutable
@Triggers([OnSelectEventTrigger::class])
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
