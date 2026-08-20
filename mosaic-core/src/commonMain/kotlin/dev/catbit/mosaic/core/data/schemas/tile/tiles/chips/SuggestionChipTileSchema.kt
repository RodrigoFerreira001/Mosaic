package dev.catbit.mosaic.core.data.schemas.tile.tiles.chips

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 suggestion chip displaying [text] as its label, with an optional
 * leading [icon]. [variant] selects the composable: [Variant.DEFAULT] → `SuggestionChip`
 * (outlined), [Variant.ELEVATED] → `ElevatedSuggestionChip`. [enabled] is forwarded to the
 * underlying composable.
 *
 * **Triggers dispatched:**
 * - `OnClickEventTrigger` — fired when the chip is tapped.
 *
 * **Notes:** the chip is stateless — it has no selected state, and [icon] is decorative only.
 */
@Immutable
@Triggers([OnClickEventTrigger::class])
@Serializable
@SerialName("SuggestionChip")
data class SuggestionChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: IconSchema? = null,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("variant") val variant: Variant = Variant.DEFAULT,
) : TileSchema {

    enum class Variant { DEFAULT, ELEVATED }
}
