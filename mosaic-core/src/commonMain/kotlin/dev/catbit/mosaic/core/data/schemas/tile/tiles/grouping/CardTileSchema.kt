package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 card hosting [tiles] in a `ColumnScope`. [kind] picks the composable:
 * [Kind.DEFAULT] → `Card`, [Kind.ELEVATED] → `ElevatedCard`, [Kind.OUTLINED] → `OutlinedCard`.
 *
 * **Child scope:** the card publishes its `ColumnScope` as a CompositionLocal (and clears the
 * lazy-item scope), so children are stacked vertically and can use column scope modifiers.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the card is tapped.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
    ]
)
@Serializable
@SerialName("Card")
data class CardTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("kind") val kind: Kind,
) : TileSchema {

    enum class Kind {
        DEFAULT, ELEVATED, OUTLINED
    }
}